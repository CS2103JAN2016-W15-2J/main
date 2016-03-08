package tasknote.parser;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

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
	private static final String COMMAND_EXIT = "exit";
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
	private static final String REGEX_QUOTATION = "\"";

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
			
			//TODO invalid
			TaskObject newTaskObject = Parser.parseAdd(userCommandWord);
			
			
			return COMMAND_TYPE.ADD;
			
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
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
			
			if (current_action.equalsIgnoreCase("location")) {
				
				if (currentWord.equalsIgnoreCase(KEYWORD_BY) || currentWord.equalsIgnoreCase(KEYWORD_FROM) ||
					currentWord.equalsIgnoreCase(KEYWORD_TO) || currentWord.equalsIgnoreCase(KEYWORD_ON) ||
					i == userCommandDataLength) {
					
					location = temporaryPhrase.toString();
					temporaryPhrase.delete(0, temporaryPhrase.length());
					
					if (i != userCommandDataLength) {
						current_action = "name";
						i--;	
					}
					
				} else {
					temporaryPhrase.append(currentWord);
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
			} else if (current_action.startsWith(REGEX_QUOTATION)) {
				
				
			} else if (current_action.endsWith(REGEX_QUOTATION)) {
				
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
		
		newTaskObject.setLocation(location);
		
		return newTaskObject;
	}

	public static TaskObject getTaskObject(String userCommand) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static ArrayList<Integer> parseDelete(String userCommand) {
		// TODO Auto-generated method stub
		
		String[] splitCommand = userCommand.trim().split(REGEX_WHITESPACE);
		int idCount = splitCommand.length;
		
		ArrayList<Integer> list = new ArrayList<Integer>();
	
		for (int i = 1; i < idCount; i++) {
			
			String nextCommand = splitCommand[i];
			
			try {
				
				int nextID = Integer.parseInt(nextCommand) - 1;
				
				list.add(nextID);
				
			} catch (NumberFormatException e) {
				// No Exceptions handling supported yet
				//TODO
			}
		}
		
		return list;
	}
	
	public static ArrayList<Integer> parseSearch(String userCommand, ArrayList<TaskObject> displayList) {
		// TODO Auto-generated method stub
		
		// Magic number: Search is of length 7
		String searchString = userCommand.trim().substring(7, userCommand.length());
		
		Iterator taskObjectIterator = displayList.iterator();
		
		ArrayList<Integer> allSelectedTaskIDs = new ArrayList<>();
		
		int i = 0;
		
		while (taskObjectIterator.hasNext()) {
			
			TaskObject currentTaskObject = (TaskObject) taskObjectIterator.next();
			
			String currentTaskName = currentTaskObject.getTaskName();	
			
			if (currentTaskName.contains(searchString)) {
				
				System.out.println(currentTaskName);
				System.out.println(currentTaskObject.getTaskID());
				
				allSelectedTaskIDs.add(i);
			}
			
			i++;
		}
		
		return allSelectedTaskIDs;
	}
	
	public static TaskObject parseUpdate(String userCommand, TaskObject oldTaskObject) {
		// TODO Auto-generated method stub
		
		// Update <id> <update words>
		String[] splitUserCommand = userCommand.split(REGEX_WHITESPACE);
		
		int userCommandLength = splitUserCommand.length;
		
		for (int i = 2; i < userCommandLength; i++) {
			
			String currentWord = splitUserCommand[userCommandLength];
			
			if (currentWord.equalsIgnoreCase(KEYWORD_AT)) {
				oldTaskObject.setLocation("");
			} else if (currentWord.equalsIgnoreCase(KEYWORD_BY)) {
				//TODO
			} else if (currentWord.equalsIgnoreCase(KEYWORD_FROM)) {
				//TODO
			} else if (currentWord.equalsIgnoreCase(KEYWORD_TO)) {
				// TODO
			} else if (currentWord.equalsIgnoreCase(KEYWORD_ON)) {
				oldTaskObject.setDateHour(-1);
				oldTaskObject.setDateMinute(-1);
			}
		}
		
		TaskObject newTaskObject = Parser.parseAdd(userCommand);
		
		if (oldTaskObject.getLocation().isEmpty()) {
			oldTaskObject.setLocation(newTaskObject.getLocation());
		}
		if (oldTaskObject.getDateHour() == -1) {
			oldTaskObject.setDateHour(newTaskObject.getDateHour());
		}
		if (oldTaskObject.getDateMinute() == -1) {
			oldTaskObject.setDateMinute(newTaskObject.getDateMinute());
		}
		
		return oldTaskObject;
		
	}
	
	public static int getUpdateTaskId(String userCommand) {
		
		String[] splitUserCommand = userCommand.split(REGEX_WHITESPACE);
		
		try {
			
			String givenID = splitUserCommand[1];
			
			int returnValue = Integer.parseInt(givenID) - 1;
			
			return returnValue;
		} catch (NumberFormatException e) {
			
			return -1;
		}
		
	}

}
