package tasknote.parser;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class Parser {

	// Here are the valid commands accepted by
	// the program
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_SEARCH = "search";

	// Here are the valid keywords accepted by
	// the program
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_AT = "at";
	private static final String KEYWORD_ON = "on";
	private static final String KEYWORD_FROM = "from";
	private static final String KEYWORD_TO = "to";

	// Here are the regex that are used by the
	// parser for parsing all user commands
	private static final String REGEX_WHITESPACE = " ";

	/**
	 * This method accepts an entire String passed from the user through his
	 * command line, and returns the matching COMMAND_TYPE
	 * 
	 * @param userCommand
	 *            This refers to the entire String that is passed by the user
	 *            through the command line
	 * 
	 * @return A COMMAND_TYPE enum value is returned. All the COMMAND_TYPE enums
	 *         can be found in the tasknote.shared package
	 * 
	 *         In the event where the command supplied by the user, does not
	 *         match any of the valid COMMAND_TYPES, the INVALID COMMAND_TYPE
	 *         value is returned instead
	 */
	public static COMMAND_TYPE getCommandType(String userCommand) {

		// Extract the first word from the userCommand
		// We do this by trimming whitespaces first
		// to prevent any possible interference with
		// the regex split
		String trimmedUserCommand = userCommand.trim();

		// Next, we split the userCommand by a whitespace
		// regex to split the entire string into words
		// The first word must be the user command
		String[] userCommandWords = trimmedUserCommand.split(REGEX_WHITESPACE);
		String userCommandWord = userCommandWords[0];

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		if (userCommandWord.equalsIgnoreCase(COMMAND_ADD)) {
			return COMMAND_TYPE.ADD;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * This method returns a TaskObject that encapsulates the task to be added.
	 * The userCommand is read and parsed such that relevant information is
	 * differentiated using keywords and added into the TaskObject
	 * 
	 * @param userCommand
	 *            This refers to the entire String that is passed by the user
	 *            through the command line
	 * 
	 * @return A TaskObject that contains all the information in the userCommand
	 *         and stored in the correct data field by identifying keywords to
	 *         differentiate the data's intended location
	 */
	public static TaskObject parseAdd(String userCommand) {

		// Extract the first word from the userCommand
		// We do this by trimming whitespaces first
		// to prevent any possible interference with
		// the regex split
		String trimmedUserCommand = userCommand.trim();

		// Next, we split the userCommand by a whitespace
		// regex to split the entire string into words
		String[] userCommandWords = trimmedUserCommand.split(REGEX_WHITESPACE);
		
		// Since the first word must be the user command
		// then our main concern should be with all the
		// other words excluding the first one
		// We initialise all the possible data types that
		// would be relevant for creating the TaskObject
		StringBuilder taskName = new StringBuilder();
		
		int dateDay = -1;
		int dateMonth = -1;
		int dateYear = -1;
		int dateHour = -1;
		int dateMinute = -1;
		
		int duration = -1;
		
		String location = "";
		
		long notifyTime = -1;
		boolean isNotified = false;
		
		String taskColor = "";
		String taskType = "";
		
		boolean isMarkedDone = false;
		
		// We obtain the length of the userCommandWords
		// array to identify the amount of words we
		// need to iterate through to obtain the relevant
		// data to fill the TaskObject
		int userCommandDataLength = userCommandWords.length - 1;
		
		// Finally, we construct the TaskObject from all
		// the data fields that have been set
		
		String current_action = "name";
		StringBuilder temporaryPhrase = new StringBuilder();
		
		for (int i = 0; i < userCommandDataLength; i++) {
			
			String currentWord = userCommandWords[i + 1];
			
			if (current_action.equalsIgnoreCase("time point")) {
				
				int currentWordLength = currentWord.length();
				
				if (currentWordLength > 4) {
					taskName.append(temporaryPhrase);
					temporaryPhrase.delete(0, temporaryPhrase.length());
					current_action = "name";
				} else if (currentWordLength > 2) {
					
					String lastTwoCharacters = currentWord.substring(currentWordLength - 2, 
																	 currentWordLength);
					
					char secondLastCharacter = lastTwoCharacters.charAt(0);
					char lastCharacter = lastTwoCharacters.charAt(1);
					
					if (lastTwoCharacters.equalsIgnoreCase("pm")) {
						
						String expectedTime = currentWord.substring(0,  currentWordLength - 2);
						
						try {
							dateHour = Integer.parseInt(expectedTime) + 12;
							
						} catch (NumberFormatException e) {
							taskName.append(temporaryPhrase);
							current_action = "name";
						} finally {
							temporaryPhrase.delete(0, temporaryPhrase.length());
						}
						
					} else if (lastTwoCharacters.equalsIgnoreCase("am")) {
						
						String expectedTime = currentWord.substring(0,  currentWordLength - 2);
						
						try {
							dateHour = Integer.parseInt(expectedTime);
							
						} catch (NumberFormatException e) {
							taskName.append(temporaryPhrase);
							current_action = "name";
						} finally {
							temporaryPhrase.delete(0, temporaryPhrase.length());
						}
						
					} else if (secondLastCharacter >= '0' && secondLastCharacter <= '5' && 
							   lastCharacter >= '0' && lastCharacter <= '9') {
						try {
							dateHour = Integer.parseInt(currentWord.substring(0, currentWordLength - 2));
							dateMinute = Integer.parseInt(currentWord.substring(currentWordLength - 2, 
									 											currentWordLength));
						} catch (NumberFormatException e) {
							taskName.append(temporaryPhrase);
							current_action = "name";
						} finally {
							temporaryPhrase.delete(0, temporaryPhrase.length());
						}
						
					} else {
						taskName.append(temporaryPhrase);
						temporaryPhrase.delete(0, temporaryPhrase.length());
						current_action = "name";
					}
					
				} else {
					
					try {
						dateHour = Integer.parseInt(currentWord);
						
					} catch (NumberFormatException e) {
						taskName.append(temporaryPhrase);
						current_action = "name";
					} finally {
						temporaryPhrase.delete(0, temporaryPhrase.length());
					}
					
				}
				
			}
			
			if (currentWord.equalsIgnoreCase("by")) {
				
				if (current_action.equalsIgnoreCase("name")) {
					taskName.append(temporaryPhrase);
					temporaryPhrase.delete(0, temporaryPhrase.length());
				}
				
				temporaryPhrase.append(currentWord);
				temporaryPhrase.append(" ");
				current_action = "time point";
			} else if (currentWord.equalsIgnoreCase("at")) {
				
				if (current_action.equalsIgnoreCase("name")) {
					taskName.append(temporaryPhrase);
					temporaryPhrase.delete(0, temporaryPhrase.length());
				}
				
				temporaryPhrase.append(currentWord);
				temporaryPhrase.append(" ");
				current_action = "location";
			} else if (currentWord.equalsIgnoreCase("on")) {
				
				if (current_action.equalsIgnoreCase("name")) {
					taskName.append(temporaryPhrase);
					temporaryPhrase.delete(0, temporaryPhrase.length());
				}
				
				temporaryPhrase.append(currentWord);
				temporaryPhrase.append(" ");
				current_action = "date";
			} else if (currentWord.equalsIgnoreCase("in")) {
				
				if (current_action.equalsIgnoreCase("name")) {
					taskName.append(temporaryPhrase);
					temporaryPhrase.delete(0, temporaryPhrase.length());
				}
				
				temporaryPhrase.append(currentWord);
				temporaryPhrase.append(" ");
				current_action = "countdown";
			} else {
				
				if (current_action.equalsIgnoreCase("name")) {
					taskName.append(currentWord);
					taskName.append(" ");
				}
			}
		}
		
		TaskObject newTaskObject = new TaskObject(taskName.toString().trim());
		newTaskObject.setDateHour(dateHour);
		newTaskObject.setDateMinute(dateMinute);
		
		return newTaskObject;
	}

	public static ArrayList<Integer> deletedObjects(String userCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	public static TaskObject getTaskObject(String userCommand) {
		// TODO Auto-generated method stub
		return null;
	}

}
