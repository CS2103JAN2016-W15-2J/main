package tasknote.parser;

import tasknote.logic.ShowInterval;
import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;
import tasknote.shared.Constants;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import com.sun.nio.sctp.PeerAddressChangeNotification;

public class Parser {

	// Private fields
	private ArrayList<String> allPhrases;
	private int listPointer;
	private COMMAND_TYPE commandType;
	private TaskObject objectForThisCommand;

	public Parser() {
		this.setAllPhrases(null);
		this.setListPointer(-1);
		this.setCommandType(COMMAND_TYPE.INVALID);
		this.setObjectForThisCommand(null);
	}

	private boolean isNotReady() {
		return this.getAllPhrases().equals(null) || this.getListPointer() == -1
				|| this.getCommandType().equals(COMMAND_TYPE.INVALID);
	}

	public void setInputString(String userCommand) {
		ParserFirstPass parserFirstPass = new ParserFirstPass(userCommand);
		this.setAllPhrases(parserFirstPass.getFirstPassParsedResult());
		this.setListPointer(0);
		this.setCommandType(matchCommandType());

		// Create a new TaskObject only if it makes sense
		if (this.getCommandType() == COMMAND_TYPE.ADD
				|| this.getCommandType() == COMMAND_TYPE.UPDATE) {
			this.setObjectForThisCommand(new TaskObject());
		}
	}

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
	public COMMAND_TYPE getCommandType() {
		return this.commandType;
	}

	private void setCommandType(COMMAND_TYPE commandType) {
		this.commandType = commandType;
	}

	// parseHelp should only be called if the COMMAND_TYPE was valid
	public COMMAND_TYPE parseHelp(boolean throwException) {

		// Make sure that the COMMAND_TYPE is accurate
		if (this.getCommandType() != COMMAND_TYPE.HELP) {
			throw new RuntimeException(
					"Wrong method used for non-help type input!");
		}

		ArrayList<String> allPhrase = this.getAllPhrases();
		int allPhraseCount = allPhrase.size();
		COMMAND_TYPE returnValue = COMMAND_TYPE.INVALID;

		// This handles the case where "help" is the only
		// input supplied by the user
		if (allPhraseCount == 1) {
			return COMMAND_TYPE.HELP;
		} else {
			int nextPhrasePointer = this.getListPointer() + 1;
			this.setListPointer(nextPhrasePointer);
		}

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		returnValue = matchCommandType();

		return returnValue;
	}

	// Fixed command structure: add taskname <on date> <by time> <notify time>
	// <at location>
	public TaskObject parseAdd(boolean throwException) {

		if (this.getCommandType() != COMMAND_TYPE.ADD) {
			throw new RuntimeException(
					"Wrong method used for non-add type input!");
		}

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();
		this.setListPointer(1);

		String switchString = ParserConstants.SWITCH_STRING_NAME;
		String oldSwitchString = ParserConstants.SWITCH_STRING_NAME;
		String taskType = TaskObject.TASK_TYPE_FLOATING;
		
		TaskObject taskObjectToBuild = this.getObjectForThisCommand();

		while (this.getListPointer() < phraseCount) {

			if (switchString.equals(ParserConstants.SWITCH_STRING_NAME)) {
				switchString = parseNameUntilSurrender();
			} else if (switchString
					.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)) {
				switchString = parseDateTimeUntilSurrender(ParserConstants.SWITCH_STRING_DATETIMESTART);
			} else if (switchString
					.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {
				switchString = parseDateTimeUntilSurrender(ParserConstants.SWITCH_STRING_DATETIMEEND);
			} else if (switchString
					.equals(ParserConstants.SWITCH_STRING_LOCATIONTIME)) {
				switchString = parseLocationTimeUntilSurrender();
			} else {
				this.setListPointer(this.getListPointer() + 1);
			}
		}
		
		if (taskObjectToBuild.getDateHour() > ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& taskObjectToBuild.getDateMinute() > ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			if (taskObjectToBuild.getDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
					|| taskObjectToBuild.getDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
					|| taskObjectToBuild.getDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
				GregorianCalendar todayOrTomorrow = new GregorianCalendar();
				
				int todayMinute = todayOrTomorrow.get(Calendar.HOUR_OF_DAY) * 60 + todayOrTomorrow.get(Calendar.MINUTE);
				int taskMinute = taskObjectToBuild.getDateHour() * 60 + taskObjectToBuild.getDateMinute();
				
				if (todayMinute > taskMinute) {
					DateParser tomorrowParser = new DateParser();
					todayOrTomorrow = tomorrowParser.rollByDays(todayOrTomorrow, 1);
				}
				
				taskObjectToBuild.setDateDay(todayOrTomorrow.get(Calendar.DAY_OF_MONTH));
				taskObjectToBuild.setDateMonth(todayOrTomorrow.get(Calendar.MONTH) + 1);
				taskObjectToBuild.setDateYear(todayOrTomorrow.get(Calendar.YEAR));
			}
		}

		return taskObjectToBuild;
	}

	private String parseNameUntilSurrender() {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();
		StringBuilder nameBuilder = new StringBuilder();
		String currentPhrase = allPhrases.get(listPointer);

		do {
			nameBuilder.append(currentPhrase);
			nameBuilder.append(Constants.STRING_CONSTANT_SPACE);
			listPointer++;
			
			if (listPointer < phraseCount) {
				currentPhrase = allPhrases.get(listPointer);
			}
		} while (listPointer < phraseCount 
				&& !ParserConstants.KEYWORD_SET_UNMODIFIABLE.contains(currentPhrase));

		// Clean up the trailing spaces in the name
		String taskObjectFinalName = nameBuilder.toString().trim();
		String taskObjectOldName = this.getObjectForThisCommand().getTaskName();

		if (!taskObjectOldName.equals(Constants.STRING_CONSTANT_EMPTY)) {
			taskObjectOldName = taskObjectOldName
					+ Constants.STRING_CONSTANT_SPACE;
		}

		this.setListPointer(listPointer);
		this.getObjectForThisCommand().setTaskName(
				taskObjectOldName + taskObjectFinalName);

		// For this specific case, remove keyword should be ignored
		// and name construction should continue
		if (this.getCommandType() == COMMAND_TYPE.ADD
				&& currentPhrase.equals(ParserConstants.KEYWORD_REMOVE)) {
			return parseNameUntilSurrender();
		} else {
			return decideNewSwitchString(currentPhrase);
		}

	}

	private String parseDateTimeUntilSurrender(String startOrEnd) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();
		String currentPhrase = allPhrases.get(listPointer);
		TaskObject objectToModify = this.getObjectForThisCommand();

		do {

			DateMessage potentialDateMessage = tryToParseDate();
			TimeMessage potentialTimeMessage = tryToParseTime();

			if (potentialDateMessage.getMessage().equals(
					ParserConstants.MESSAGE_DATE_SURE)) {

				if (startOrEnd
						.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
						&& objectToModify.getDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
						&& objectToModify.getDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
						&& objectToModify.getDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
					objectToModify.setDateDay(potentialDateMessage.getDay());
					objectToModify
							.setDateMonth(potentialDateMessage.getMonth());
					objectToModify.setDateYear(potentialDateMessage.getYear());
					objectToModify.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
				} else {
					objectToModify.setEndDateDay(potentialDateMessage.getDay());
					objectToModify.setEndDateMonth(potentialDateMessage
							.getMonth());
					objectToModify.setEndDateYear(potentialDateMessage
							.getYear());
					objectToModify.setTaskType(TaskObject.TASK_TYPE_EVENT);
				}

				listPointer = listPointer
						+ potentialDateMessage.getExtraWordsUsed() + 1;
				this.setListPointer(listPointer);
				
				if (listPointer < phraseCount) {
					currentPhrase = allPhrases.get(listPointer);
				}

			} else if (potentialTimeMessage.getMessage().equals(
					ParserConstants.MESSAGE_TIME_SURE)) {

				if (startOrEnd
						.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
						&& objectToModify.getDateHour() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
						&& objectToModify.getDateMinute() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
					objectToModify.setDateHour(potentialTimeMessage.getHour());
					objectToModify.setDateMinute(potentialTimeMessage
							.getMinute());
					objectToModify.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
				} else {
					objectToModify.setEndDateHour(potentialTimeMessage
							.getHour());
					objectToModify.setEndDateMinute(potentialTimeMessage
							.getMinute());

					objectToModify.setTaskType(TaskObject.TASK_TYPE_EVENT);
				}

				listPointer = listPointer + 1;
				this.setListPointer(listPointer);
				
				if (listPointer < phraseCount) {
					currentPhrase = allPhrases.get(listPointer);
				}

			} else {

				listPointer = listPointer + 1;
				this.setListPointer(listPointer);
				
				if (listPointer < phraseCount) {
					currentPhrase = allPhrases.get(listPointer);
				}

				return decideNewSwitchString(currentPhrase);
			}
		} while (listPointer < phraseCount
				&& !ParserConstants.KEYWORD_SET_UNMODIFIABLE.contains(currentPhrase));
		return decideNewSwitchString(currentPhrase);
	}

	private String parseLocationTimeUntilSurrender() {
		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();
		StringBuilder locationBuilder = new StringBuilder();
		String currentPhrase = allPhrases.get(listPointer);
		TaskObject objectToModify = this.getObjectForThisCommand();

		do {

			TimeMessage potentialTimeMessage = tryToParseTime();

			if (potentialTimeMessage.getMessage().equals(
					ParserConstants.MESSAGE_TIME_SURE)) {

				if (objectToModify.getDateHour() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
						&& objectToModify.getDateMinute() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
					objectToModify.setDateHour(potentialTimeMessage.getHour());
					objectToModify.setDateMinute(potentialTimeMessage
							.getMinute());
					objectToModify.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
				} else {
					objectToModify.setEndDateHour(potentialTimeMessage
							.getHour());
					objectToModify.setEndDateMinute(potentialTimeMessage
							.getMinute());
					objectToModify.setTaskType(TaskObject.TASK_TYPE_EVENT);
				}
			} else {
				currentPhrase = allPhrases.get(listPointer);
				locationBuilder.append(currentPhrase);
				locationBuilder.append(Constants.STRING_CONSTANT_SPACE);
			}
			
			listPointer = listPointer + 1;
			this.setListPointer(listPointer);
			
			if (listPointer < phraseCount) {
				currentPhrase = allPhrases.get(listPointer);
			}
		
		} while (listPointer < phraseCount
				&& !ParserConstants.KEYWORD_SET_UNMODIFIABLE.contains(currentPhrase));
		
		// Trim the location results of any trailing whitespace
		String finalLocation = locationBuilder.toString().trim();
		String oldLocation = objectToModify.getLocation();
		
		if (!oldLocation.equals(Constants.STRING_CONSTANT_EMPTY)) {
			oldLocation = oldLocation + Constants.STRING_CONSTANT_SPACE;
		}
		
		objectToModify.setLocation(oldLocation + finalLocation);
		
		return decideNewSwitchString(currentPhrase);
	}

	private String decideNewSwitchString(String keyword) {

		String returnValue = "name";

		switch (keyword) {

		case ParserConstants.KEYWORD_AT:
			returnValue = "locationtime";
			break;

		case ParserConstants.KEYWORD_BY:
		case ParserConstants.KEYWORD_ON:
		case ParserConstants.KEYWORD_FROM:
			returnValue = "datetimestart";
			break;

		case ParserConstants.KEYWORD_TO:
			returnValue = "datetimeend";
			break;

		case ParserConstants.KEYWORD_REMOVE:
			returnValue = "remove";
			break;

		case ParserConstants.KEYWORD_NOTIFY:
			returnValue = "notify";
			break;
		}
		
		// If you are deciding on a new switchString, that means
		// this current keyword would be foregone
		int listPointer = this.getListPointer();
		this.setListPointer(listPointer + 1);

		return returnValue;
	}

	public TaskObject parseUpdate(TaskObject reallyOldTaskObject, boolean throwException) {
		return null;
	}

	public ArrayList<Integer> parseDelete(boolean throwException) {
		// TODO Auto-generated method stub
		
		if (this.getCommandType() != COMMAND_TYPE.DELETE) {
			throw new RuntimeException("Wrong method used for non-add type input!");
		}

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();

		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = listPointer; i < phraseCount; i++) {

			String nextCommand = allPhrases.get(i).toLowerCase();

			if (nextCommand.equals("-") || nextCommand.equals("to")) {

				try {

					int endID = Integer.parseInt(allPhrases.get(i + 1)) - 1;
					int startID = list.get(list.size() - 1);

					startID++;
					i++;

					while (startID <= endID) {

						list.add(startID);
						startID++;
					}

				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Cannot delete non-numeric ID");
						System.out.println(e2);
						throw e2;
					} else {

						// Let it continue running, ignoring this bad value
						continue;
					}

				} catch (IndexOutOfBoundsException e) {

					if (throwException) {
						IndexOutOfBoundsException e2 = new IndexOutOfBoundsException(
								"No start of range ID was found");
						System.out.println(e2);
						throw e2;
					} else {

						// Let it continue running, ignoring this bad value
						continue;
					}
				}
			} else {

				try {

					int nextID = Integer.parseInt(nextCommand) - 1;

					list.add(nextID);

				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Cannot delete non-numeric ID");
						System.out.println(e2);
						throw e2;
					} else {
						// Let it continue running, ignoring this bad value
						continue;
					}
				}
			}
		}

		return list;
	}

	// Ahead of SuperParser
	public ArrayList<Integer> parseSearch(ArrayList<TaskObject> displayList, boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();
		int itemCount = displayList.size();
		int listPointer = this.getListPointer();

		HashSet<Integer> indicesToRemove = new HashSet<>();
		HashSet<Integer> indicesToReturn = new HashSet<>();

		for (int i = 0; i < itemCount; i++) {
			indicesToReturn.add(i);
		}

		String testKeyWord = allPhrases.get(listPointer).toLowerCase();
		boolean exactOnly = false;

		if (phraseCount > 2 && testKeyWord.equals("exact")) {
			exactOnly = true;
		}

		for (int i = listPointer; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i).toLowerCase();

			if (exactOnly && i == 1) {
				continue;
			}

			for (int j = 0; j < itemCount; j++) {
				String currentTaskName = displayList.get(j).getTaskName()
						.toLowerCase();

				if (!exactOnly) {

					if (!currentTaskName.contains(currentPhrase)) {
						indicesToRemove.add(j);
					}
				} else {

					String[] splitTaskName = currentTaskName
							.split(Constants.STRING_CONSTANT_SPACE);

					indicesToRemove.add(j);

					for (int k = 0; k < splitTaskName.length; k++) {
						if (currentPhrase.equals(splitTaskName[k])) {
							indicesToRemove.remove(j);
							break;
						}
					}
				}

			}

			indicesToReturn.removeAll(indicesToRemove);
		}

		ArrayList<Integer> listToReturn = new ArrayList<>();
		listToReturn.addAll(indicesToReturn);

		return listToReturn;
	}

	public ShowInterval parseShow(boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();
		int listPointer = this.getListPointer();

		for (int i = listPointer; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i).toLowerCase();

			if (currentPhrase.equals("today")) {
				return ShowInterval.TODAY;
			}

			if (currentPhrase.equals("tomorrow")) {
				return ShowInterval.TOMORROW;
			}

			if (currentPhrase.equals("week") || currentPhrase.equals("weeks")) {
				return ShowInterval.WEEK;
			}

			if (currentPhrase.equals("day") || currentPhrase.equals("days")) {
				return ShowInterval.DAY;
			}

			if (currentPhrase.equals("all")) {
				return ShowInterval.ALL;
			}
		}

		// Default behaviour - consider throwing exception
		return ShowInterval.ALL;
	}

	public int getInterval(boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();
		int listPointer = this.getListPointer();

		for (int i = listPointer; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i).toLowerCase();

			if (currentPhrase.equals(ParserConstants.INTERVAL_LONG_TODAY)
					|| currentPhrase.equals(ParserConstants.INTERVAL_LONG_TOMORROW)
					|| currentPhrase.equals(ParserConstants.INTERVAL_LONG_ALL)) {
				return -1;
			}

			if (currentPhrase.equals("next")) {

				int forwardCounter = i + 1;

				// Defensive check
				if (forwardCounter == phraseCount) {
					return -1;
				}

				currentPhrase = allPhrases.get(forwardCounter).toLowerCase();

				// Trivial case
				if (currentPhrase.equals("week") || currentPhrase.equals("day")) {
					return 1;
				}

				// Recursive case
				if (currentPhrase.equals("next")) {

					int nextCount = 2;
					forwardCounter++;

					while (forwardCounter < phraseCount) {

						currentPhrase = allPhrases.get(forwardCounter)
								.toLowerCase();

						if (currentPhrase.equals("next")) {
							nextCount++;
							forwardCounter++;
							continue;
						}

						if (currentPhrase.equals("week")
								|| currentPhrase.equals("day")) {
							return nextCount;
						}

						if (throwException) {
							// Consider changing exception type
							RuntimeException e = new RuntimeException(
									"Could not identify how far ahead to show. "
											+ "Use the following command for more details:\n help show");
							System.out.println(e);
							throw e;
						} else {

							// Default value
							return -1;
						}

					}

					if (throwException) {
						// Consider changing exception type
						RuntimeException e = new RuntimeException(
								"Could not identify how far ahead to show. "
										+ "Use the following command for more details:\n help show");
						System.out.println(e);
						throw e;
					} else {

						// Default value
						return -1;
					}
				}

				// Numeric case + all other cases
				try {

					int placeOfWeekKeyword = forwardCounter + 1;

					// Defensive check
					if (placeOfWeekKeyword == phraseCount) {
						return -1;
					}

					int returnValue = Integer.parseInt(currentPhrase);
					currentPhrase = allPhrases.get(placeOfWeekKeyword)
							.toLowerCase();

					if (returnValue <= 0) {
						NumberFormatException e = new NumberFormatException(
								"Unable to show negative weeks/days");
						System.out.println(e);
						throw e;
					}

					if (currentPhrase.equals("week")
							|| currentPhrase.equals("weeks")
							|| currentPhrase.equals("day")
							|| currentPhrase.equals("days")) {
						return returnValue;
					} else {
						if (throwException) {
							// Consider changing exception type
							RuntimeException e = new RuntimeException(
									"Could not identify how far ahead to show. "
											+ "Use the following command for more details:\n help show");
							System.out.println(e);
							throw e;
						} else {

							// Default value
							return -1;
						}
					}

				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Unable to show non-numerical weeks/days");
						System.out.println(e2);
						throw e2;
					} else {

						// Default value
						return -1;
					}

				} catch (RuntimeException e) {
					if (throwException) {
						// Consider changing exception type
						RuntimeException e2 = new RuntimeException(
								"Could not identify how far ahead to show. "
										+ "Use the following command for more details:\n help show");
						System.out.println(e2);
						throw e2;
					} else {

						// Default value
						return -1;
					}
				}
			}
		}

		if (throwException) {
			// Consider changing exception type
			RuntimeException e = new RuntimeException(
					"Could not identify how far ahead to show. "
							+ "Use the following command for more details:\n help show");
			System.out.println(e);
			throw e;
		} else {

			// Default value
			return -1;
		}
	}

	public static String parseFilePath(String userCommand,
			boolean throwException) {

		ParserFirstPass scanAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = scanAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		for (int i = 1; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i);

			try {

				Paths.get(currentPhrase);
				return currentPhrase;

			} catch (InvalidPathException | NullPointerException e) {
				continue;
			}
		}

		if (throwException) {

			RuntimeException e = new RuntimeException(
					"Could not find valid file path in command supplied. "
							+ "Consult the following for more details:\n help relocate");
			System.out.println(e);
			throw e;
		} else {

			// Default value
			return "";
		}
	}

	public static int getTaskId(String userCommand, boolean throwException) {

		String[] splitUserCommand = userCommand.trim().split(
				Constants.STRING_CONSTANT_SPACE);

		try {

			String givenID = splitUserCommand[1];

			int returnValue = Integer.parseInt(givenID) - 1;

			return returnValue;
		} catch (NumberFormatException e) {

			if (throwException) {

				NumberFormatException e2 = new NumberFormatException(
						"ID for editing task is not valid.");
				System.out.println(e2);
				throw e2;

			} else {
				return -1;
			}
		}
	}

	private DateMessage tryToParseDate() {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();
		String currentPhrase = allPhrases.get(listPointer);

		DateParser dateParser = new DateParser();
		dateParser.setAllPhrases(allPhrases);
		dateParser.setCurrentPhrase(currentPhrase);
		dateParser.setListPointer(listPointer);
		dateParser.setPhraseCount(phraseCount);

		DateMessage returnMessage = dateParser.tryToParseDate();

		return returnMessage;
	}

	private TimeMessage tryToParseTime() {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		int phraseCount = allPhrases.size();
		String currentPhrase = allPhrases.get(listPointer);

		TimeParser timeParser = new TimeParser();
		timeParser.setCurrentPhrase(currentPhrase);

		TimeMessage returnMessage = timeParser.tryToParseTime();

		return returnMessage;

	}

	protected static boolean isNumber(String stringToTest) {

		try {
			Integer.parseInt(stringToTest);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean hasValidInputLength(COMMAND_TYPE command) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int remainingPhrasesCount = allPhrases.size() - this.getListPointer();
		int expectedMinimumLength = ParserConstants
				.getMinimumCommandLength(command);

		return remainingPhrasesCount >= expectedMinimumLength;
	}

	// This method does not throw exceptions or check for userCommand length
	private COMMAND_TYPE matchCommandType() {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = this.getListPointer();
		String currentPhrase = allPhrases.get(listPointer).toLowerCase();

		COMMAND_TYPE returnValue = COMMAND_TYPE.INVALID;

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		if (currentPhrase.equals(ParserConstants.COMMAND_ADD)) {
			returnValue = COMMAND_TYPE.ADD;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_EDIT)) {
			return COMMAND_TYPE.UPDATE;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_DONE)) {
			return COMMAND_TYPE.DONE;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_REDO)) {
			return COMMAND_TYPE.REDO;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_UNDO)) {
			return COMMAND_TYPE.UNDO;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_SHOW)) {
			return COMMAND_TYPE.SHOW;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_RELOCATE)) {
			return COMMAND_TYPE.CHANGE_FILE_PATH;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_HELP)) {
			return COMMAND_TYPE.HELP;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_UNDONE)) {
			return COMMAND_TYPE.UNDONE;
		} else {
			return COMMAND_TYPE.INVALID;
		}

		if (!hasValidInputLength(returnValue)) {
			returnValue = COMMAND_TYPE.INVALID;
		}

		return returnValue;

	}

	/**
	 * @return the allPhrases
	 */
	public ArrayList<String> getAllPhrases() {
		return allPhrases;
	}

	/**
	 * @param allPhrases
	 *            the allPhrases to set
	 */
	public void setAllPhrases(ArrayList<String> allPhrases) {
		this.allPhrases = allPhrases;
	}

	/**
	 * @return the listPointer
	 */
	public int getListPointer() {
		return listPointer;
	}

	/**
	 * @param listPointer
	 *            the listPointer to set
	 */
	public void setListPointer(int listPointer) {
		this.listPointer = listPointer;
	}

	/**
	 * @return the objectForThisCommand
	 */
	public TaskObject getObjectForThisCommand() {
		return objectForThisCommand;
	}

	/**
	 * @param objectForThisCommand
	 *            the objectForThisCommand to set
	 */
	public void setObjectForThisCommand(TaskObject objectForThisCommand) {
		this.objectForThisCommand = objectForThisCommand;
	}
}
