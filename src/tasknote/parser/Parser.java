//@@author A0129529W
package tasknote.parser;

import tasknote.logic.ShowCategory;
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

	public void setInputString(String userCommand) {
		UserStringProcessor parserFirstPass = new UserStringProcessor(userCommand);
		this.setAllPhrases(parserFirstPass.getProcessedInput());
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
			throw new RuntimeException("Wrong method used for non-help type input!");
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

	public TaskObject parseAdd(boolean throwException) {

		if (this.getCommandType() != COMMAND_TYPE.ADD) {
			throw new RuntimeException("Wrong method used for non-add type input!");
		}

		TaskObject objectCreated = constructObject(false);
		return objectCreated;
	}

	public TaskObject parseUpdate(TaskObject reallyOldTaskObject, boolean throwException) {

		if (this.getCommandType() != COMMAND_TYPE.UPDATE) {
			throw new RuntimeException("Wrong method used for non-edit type input!");
		}

		TaskObject objectCreated = constructObject(true);
		objectCreated = overlayTaskObject(reallyOldTaskObject, objectCreated);

		return objectCreated;
	}

	private TaskObject constructObject(boolean isEdit) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();

		if (isEdit) {
			this.setListPointer(2);
		} else {
			this.setListPointer(1);
		}

		ForwardCheckerWithBacktracking forwardChecker = new ForwardCheckerWithBacktracking(
				allPhrases);
		String[] allMarks = forwardChecker.getAllPhraseTypes();

		StringBuilder nameBuilder = new StringBuilder();
		StringBuilder locationBuilder = new StringBuilder();
		String currentPhrase = Constants.STRING_CONSTANT_EMPTY;

		boolean toStartDate = true;
		boolean toStartTime = true;

		ArrayList<String> allStartDateInfo = new ArrayList<>();
		ArrayList<String> allEndDateInfo = new ArrayList<>();

		ArrayList<String> allStartTimeInfo = new ArrayList<>();
		ArrayList<String> allEndTimeInfo = new ArrayList<>();

		for (int i = this.getListPointer(); i < phraseCount; i++) {

			currentPhrase = allPhrases.get(i);

			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_NAME)) {
				nameBuilder.append(currentPhrase);
				nameBuilder.append(Constants.STRING_CONSTANT_SPACE);
				continue;
			}

			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_LOCATIONTIME)
					|| allMarks[i].equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
					|| allMarks[i].equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {

				// Only push to endDate or endTime if they are already filled
				if (allMarks[i].equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {
					if (!allStartDateInfo.isEmpty()) {
						toStartDate = false;
					}
					if (!allStartTimeInfo.isEmpty()) {
						toStartTime = false;
					}
				}
				continue;
			}

			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_DATE)) {
				if (toStartDate) {
					allStartDateInfo.add(currentPhrase);
				} else {
					allEndDateInfo.add(currentPhrase);
				}
				continue;
			}

			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_TIME)) {
				if (toStartTime) {
					allStartTimeInfo.add(currentPhrase);
				} else {
					allEndTimeInfo.add(currentPhrase);
				}
				continue;
			}

			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_LOCATION)) {
				locationBuilder.append(currentPhrase);
				locationBuilder.append(Constants.STRING_CONSTANT_SPACE);
				continue;
			}
		}

		DateMessage startDate = tryToParseDate(allStartDateInfo);
		DateMessage endDate = tryToParseDate(allEndDateInfo);

		TimeMessage startTime = tryToParseTime(allStartTimeInfo);
		TimeMessage endTime = tryToParseTime(allEndTimeInfo);

		String finalTaskName = nameBuilder.toString().trim();
		String finalLocation = locationBuilder.toString().trim();

		this.objectForThisCommand.setTaskName(finalTaskName);
		this.objectForThisCommand.setLocation(finalLocation);

		if (startTime.getHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& startTime.getMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			this.objectForThisCommand.setDateHour(startTime.getHour());
			this.objectForThisCommand.setDateMinute(startTime.getMinute());
			this.objectForThisCommand.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		}

		if (startDate.getDay() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& startDate.getMonth() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& startDate.getYear() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			this.objectForThisCommand.setDateDay(startDate.getDay());
			this.objectForThisCommand.setDateMonth(startDate.getMonth());
			this.objectForThisCommand.setDateYear(startDate.getYear());
			this.objectForThisCommand.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		} else {
			if (startTime.getHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
					&& startTime.getMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {

				GregorianCalendar today = new GregorianCalendar();
				int todayTime = today.get(Calendar.HOUR) * 60 + today.get(Calendar.MINUTE);
				int setTime = startTime.getHour() * 60 + startTime.getMinute();

				if (todayTime > setTime) {
					today = DateParser.rollByDays(today, 1);
				}

				this.objectForThisCommand.setDateDay(today.get(Calendar.DAY_OF_MONTH));
				this.objectForThisCommand.setDateMonth(today.get(Calendar.MONTH) + 1);
				this.objectForThisCommand.setDateYear(today.get(Calendar.YEAR));
			}
		}

		if (endTime.getHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& endTime.getMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			this.objectForThisCommand.setEndDateHour(endTime.getHour());
			this.objectForThisCommand.setEndDateMinute(endTime.getMinute());
			this.objectForThisCommand.setTaskType(TaskObject.TASK_TYPE_EVENT);
		}

		if (endDate.getDay() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& endDate.getMonth() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& endDate.getYear() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			this.objectForThisCommand.setEndDateDay(endDate.getDay());
			this.objectForThisCommand.setEndDateMonth(endDate.getMonth());
			this.objectForThisCommand.setEndDateYear(endDate.getYear());
			this.objectForThisCommand.setTaskType(TaskObject.TASK_TYPE_EVENT);
		} else {
			if (endTime.getHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
					&& endTime.getMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
				this.objectForThisCommand.setEndDateDay(this.objectForThisCommand.getDateDay());
				this.objectForThisCommand.setEndDateMonth(this.objectForThisCommand.getDateMonth());
				this.objectForThisCommand.setEndDateYear(this.objectForThisCommand.getDateYear());
			}
		}

		return this.objectForThisCommand;
	}

	private TaskObject overlayTaskObject(TaskObject oldTaskObject, TaskObject newTaskObject) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = 2;
		int phraseCount = allPhrases.size();
		ForwardCheckerWithBacktracking forwardChecker = new ForwardCheckerWithBacktracking(
				allPhrases);
		String[] allMarks = forwardChecker.getAllPhraseTypes();
		String currentPhrase = Constants.STRING_CONSTANT_EMPTY;

		for (int i = listPointer; i < phraseCount; i++) {
			currentPhrase = allPhrases.get(i);
			if (allMarks[i].equals(ParserConstants.SWITCH_STRING_REMOVE)) {
				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_REMOVE)) {
					continue;
				}

				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_TIME)) {
					oldTaskObject.setDateHour(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setEndDateHour(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setDateMinute(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setEndDateMinute(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					continue;
				}

				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_DATE)) {
					oldTaskObject.setDateDay(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setEndDateDay(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setDateMonth(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setEndDateMonth(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setDateYear(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					oldTaskObject.setEndDateYear(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
					continue;
				}

				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_NAME)) {
					oldTaskObject.setTaskName(Constants.STRING_CONSTANT_EMPTY);
					continue;
				}

				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_LOCATION)) {
					oldTaskObject.setLocation(Constants.STRING_CONSTANT_EMPTY);
					continue;
				}
			}
		}

		if (newTaskObject.getTaskName().equals(Constants.STRING_CONSTANT_EMPTY)) {
			newTaskObject.setTaskName(oldTaskObject.getTaskName());
		}

		if (newTaskObject.getLocation().equals(Constants.STRING_CONSTANT_EMPTY)) {
			newTaskObject.setLocation(oldTaskObject.getLocation());
		}

		if (newTaskObject.getDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setDateDay(oldTaskObject.getDateDay());
			newTaskObject.setDateMonth(oldTaskObject.getDateMonth());
			newTaskObject.setDateYear(oldTaskObject.getDateYear());
		}

		if (newTaskObject.getDateHour() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getDateMinute() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setDateHour(oldTaskObject.getDateHour());
			newTaskObject.setDateMinute(oldTaskObject.getDateMinute());
		}

		if (newTaskObject.getEndDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getEndDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getEndDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setEndDateDay(oldTaskObject.getEndDateDay());
			newTaskObject.setEndDateMonth(oldTaskObject.getEndDateMonth());
			newTaskObject.setEndDateYear(oldTaskObject.getEndDateYear());
		}

		if (newTaskObject.getEndDateHour() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getEndDateMinute() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setEndDateHour(oldTaskObject.getEndDateHour());
			newTaskObject.setEndDateMinute(oldTaskObject.getEndDateMinute());
		}

		// If date is *still* invalid and time exists
		if (newTaskObject.getDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {

			if (newTaskObject.getDateHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
					&& newTaskObject.getDateMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {

				GregorianCalendar today = new GregorianCalendar();
				int todayTime = today.get(Calendar.HOUR) * 60 + today.get(Calendar.MINUTE);
				int setTime = newTaskObject.getDateHour() * 60 + newTaskObject.getDateMinute();

				if (todayTime > setTime) {
					today = DateParser.rollByDays(today, 1);
				}

				newTaskObject.setDateDay(today.get(Calendar.DAY_OF_MONTH));
				newTaskObject.setDateMonth(today.get(Calendar.MONTH) + 1);
				newTaskObject.setDateYear(today.get(Calendar.YEAR));
			}
		}

		if (newTaskObject.getEndDateDay() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getEndDateMonth() == ParserConstants.DEFAULT_INVALID_INT_DATETIME
				|| newTaskObject.getEndDateYear() == ParserConstants.DEFAULT_INVALID_INT_DATETIME) {

			if (newTaskObject.getEndDateHour() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
					&& newTaskObject.getEndDateMinute() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
				newTaskObject.setEndDateDay(newTaskObject.getDateDay());
				newTaskObject.setEndDateMonth(newTaskObject.getDateMonth());
				newTaskObject.setEndDateYear(newTaskObject.getDateYear());
			}
		}
		
		if (newTaskObject.getDateDay() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& newTaskObject.getDateMonth() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& newTaskObject.getDateYear() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		}
		
		if (newTaskObject.getEndDateDay() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& newTaskObject.getEndDateMonth() != ParserConstants.DEFAULT_INVALID_INT_DATETIME
				&& newTaskObject.getEndDateYear() != ParserConstants.DEFAULT_INVALID_INT_DATETIME) {
			newTaskObject.setTaskType(TaskObject.TASK_TYPE_EVENT);
		}

		return newTaskObject;
	}

	public ArrayList<Integer> parseDelete(boolean throwException) {

		if (this.getCommandType() != COMMAND_TYPE.DELETE) {
			throw new RuntimeException("Wrong method used for non-add type input!");
		}

		ArrayList<String> allPhrases = this.getAllPhrases();
		int listPointer = 1;
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
				String currentTaskName = displayList.get(j).getTaskName().toLowerCase();

				if (!exactOnly) {

					if (!currentTaskName.contains(currentPhrase)) {
						indicesToRemove.add(j);
					}
				} else {

					String[] splitTaskName = currentTaskName.split(Constants.STRING_CONSTANT_SPACE);

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
		this.setListPointer(1);
		
		for (int i = this.getListPointer(); i < allPhrases.size(); i++) {

			String currentPhrase = allPhrases.get(i).toLowerCase();

			if (currentPhrase.equals("today")) {
				return ShowInterval.TODAY;
			}

			if (currentPhrase.equals("tomorrow")) {
				return ShowInterval.TOMORROW;
			}
			
			if (currentPhrase.equals("year") || currentPhrase.equals("years")) {
				return ShowInterval.YEAR;
			}
			
			if (currentPhrase.equals("month") || currentPhrase.equals("months")) {
				return ShowInterval.MONTH;
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
		if (throwException) {
			throw new RuntimeException("Could not understand what to show!");
		} else {
			return ShowInterval.ALL;
		}

	}

	public ShowCategory parseChangeCategory(boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();

		if (phraseCount >= 2) {

			String currentPhrase = allPhrases.get(1).toLowerCase();

			if (currentPhrase.equals("outstanding")) {
				return ShowCategory.OUTSTANDING;
			}

			if (currentPhrase.equals("overdue")) {
				return ShowCategory.OVERDUE;
			}

			if (currentPhrase.equals("completed")) {
				return ShowCategory.COMPLETED;
			}

			if (currentPhrase.equals("all")) {
				return ShowCategory.ALL;
			}
		}

		// Default behaviour - consider throwing exception
		if (throwException) {
			throw new RuntimeException("Could not understand what to show!");
		} else {
			return ShowCategory.ALL;
		}
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

						currentPhrase = allPhrases.get(forwardCounter).toLowerCase();

						if (currentPhrase.equals("next")) {
							nextCount++;
							forwardCounter++;
							continue;
						}

						if (currentPhrase.equals("week") || currentPhrase.equals("day")) {
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
					currentPhrase = allPhrases.get(placeOfWeekKeyword).toLowerCase();

					if (returnValue <= 0) {
						NumberFormatException e = new NumberFormatException(
								"Unable to show negative weeks/days");
						System.out.println(e);
						throw e;
					}

					if (currentPhrase.equals("week") || currentPhrase.equals("weeks")
							|| currentPhrase.equals("day") || currentPhrase.equals("days")) {
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
			RuntimeException e = new RuntimeException("Could not identify how far ahead to show. "
					+ "Use the following command for more details:\n help show");
			System.out.println(e);
			throw e;
		} else {

			// Default value
			return -1;
		}
	}

	public String parseFilePath(boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		int phraseCount = allPhrases.size();
		int listPointer = 1;

		for (int i = listPointer; i < phraseCount; i++) {

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
			return Constants.STRING_CONSTANT_EMPTY;
		}
	}

	public int getTaskId(boolean throwException) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		String currentPhrase = allPhrases.get(1);

		try {

			int returnValue = Integer.parseInt(currentPhrase) - 1;

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

	private DateMessage tryToParseDate(ArrayList<String> allDateInfo) {

		int listPointer = 0;
		int phraseCount = allDateInfo.size();

		if (phraseCount == 0) {
			return new DateMessage();
		}

		String currentPhrase = allDateInfo.get(listPointer);

		DateParser dateParser = new DateParser();
		dateParser.setAllPhrases(allDateInfo);
		dateParser.setCurrentPhrase(currentPhrase);
		dateParser.setListPointer(listPointer);
		dateParser.setPhraseCount(phraseCount);

		DateMessage returnMessage = dateParser.tryToParseDate();

		return returnMessage;
	}

	private TimeMessage tryToParseTime(ArrayList<String> allTimeInfo) {

		int listPointer = 0;
		int phraseCount = allTimeInfo.size();

		if (phraseCount == 0) {
			return new TimeMessage();
		}

		String currentPhrase = allTimeInfo.get(listPointer);

		TimeParser timeParser = new TimeParser();
		timeParser.setCurrentPhrase(currentPhrase);

		TimeMessage returnMessage = timeParser.tryToParseTime();

		return returnMessage;

	}

	protected boolean isNumber(String stringToTest) {

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
		int expectedMinimumLength = ParserConstants.getMinimumCommandLength(command);

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
		} else if (currentPhrase.equals(ParserConstants.COMMAND_CATEGORY)) {
			return COMMAND_TYPE.CHANGE_CATEGORY;
		} else {
			return COMMAND_TYPE.INVALID;
		}

		if (!hasValidInputLength(returnValue)) {
			returnValue = COMMAND_TYPE.INVALID;
		}

		return returnValue;

	}

	//@@author A0129529W-generated
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
