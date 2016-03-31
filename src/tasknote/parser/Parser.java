package tasknote.parser;

import tasknote.logic.ShowInterval;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class Parser {

	// Here are the valid commands accepted by
	// the program
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_SHOW = "show";
	private static final String COMMAND_RELOCATE = "relocate";
	private static final String COMMAND_HELP = "help";

	// Here are the valid keywords accepted by
	// the program
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_AT = "at";
	private static final String KEYWORD_ON = "on";
	private static final String KEYWORD_FROM = "from";
	private static final String KEYWORD_TO = "to";
	private static final String KEYWORD_IN = "in";
	private static final String KEYWORD_NOTIFY = "notify";

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
	public static COMMAND_TYPE getCommandType(String userCommand,
			boolean throwException) {

		ParserFirstPass firstPassCommand = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrase = firstPassCommand
				.getFirstPassParsedResult();

		int allPhraseCount = allPhrase.size();
		String userCommandWord = "";

		if (allPhraseCount > 0) {
			userCommandWord = allPhrase.get(0);
		}

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		if (userCommandWord.equalsIgnoreCase(COMMAND_ADD)) {

			if (allPhraseCount == 1) {
				return COMMAND_TYPE.INVALID;
			} else {
				return COMMAND_TYPE.ADD;
			}

		} else if (userCommandWord.equalsIgnoreCase(COMMAND_EDIT)) {
			return COMMAND_TYPE.UPDATE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DONE)) {
			return COMMAND_TYPE.DONE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_REDO)) {
			return COMMAND_TYPE.REDO;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_UNDO)) {
			return COMMAND_TYPE.UNDO;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SHOW)) {
			return COMMAND_TYPE.SHOW;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_RELOCATE)) {
			return COMMAND_TYPE.CHANGE_FILE_PATH;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_HELP)) {
			return COMMAND_TYPE.HELP;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	public static COMMAND_TYPE parseHelp(String userCommand,
			boolean throwException) {

		ParserFirstPass firstPassCommand = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrase = firstPassCommand
				.getFirstPassParsedResult();

		int allPhraseCount = allPhrase.size();
		String userCommandWord = "";

		if (allPhraseCount > 1) {
			userCommandWord = allPhrase.get(1);
		}

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		if (userCommandWord.equalsIgnoreCase(COMMAND_ADD)) {

			if (allPhraseCount == 1) {
				return COMMAND_TYPE.INVALID;
			} else {
				return COMMAND_TYPE.ADD;
			}

		} else if (userCommandWord.equalsIgnoreCase(COMMAND_EDIT)) {
			return COMMAND_TYPE.UPDATE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_DONE)) {
			return COMMAND_TYPE.DONE;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_REDO)) {
			return COMMAND_TYPE.REDO;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_UNDO)) {
			return COMMAND_TYPE.UNDO;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_SHOW)) {
			return COMMAND_TYPE.SHOW;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_RELOCATE)) {
			return COMMAND_TYPE.CHANGE_FILE_PATH;
		} else if (userCommandWord.equalsIgnoreCase(COMMAND_HELP)) {
			return COMMAND_TYPE.HELP;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	// Fixed command structure: add taskname <on date> <by time> <notify time>
	// <at location>
	public static TaskObject parseAdd(String userCommand, boolean throwException) {

		ParserFirstPass parseAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = parseAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		String switchString = "name";
		String taskType = "floating";

		if (phraseCount == 2) {
			return new TaskObject(allPhrases.get(1));
		}

		StringBuilder name = new StringBuilder(allPhrases.get(1));
		StringBuilder location = new StringBuilder();
		int dateDay = -1;
		int dateMonth = -1;
		int dateYear = -1;
		int dateHour = -1;
		int dateMinute = -1;
		int hourBefore = 0;
		int duration = 0;

		int endDateHour = -1;
		int endDateMinute = -1;

		for (int i = 2; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i);
			String lowerPhrase = currentPhrase.toLowerCase();

			if (lowerPhrase.equals(KEYWORD_ON)
					|| lowerPhrase.equals(KEYWORD_BY)) {
				switchString = "datetime";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_NOTIFY)) {
				switchString = "notify";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_AT)) {
				switchString = "locationtime";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_FROM)) {
				switchString = "timerangestart";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_TO)
					&& (dateHour >= 0 && dateMinute >= 0)) {
				switchString = "timerangeend";
				continue;
			}

			if (switchString.equals("name")) {
				name.append(REGEX_WHITESPACE);
				name.append(currentPhrase);
				continue;
			}

			if (switchString.equals("datetime")) {

				String[] maybeDayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				if (maybeDayMonthYear[4].equals("maybeNotDate")) {
					switchString = "time";
				} else {
					switchString = "date";
				}
			}

			if (switchString.equals("locationtime")) {

				String[] maybeHourMinute = tryToParseTime(currentPhrase);

				if (maybeHourMinute[3].equals("maybeNotTime")) {

					if (i + 1 < phraseCount) {
						String nextLowerPhrase = allPhrases.get(i + 1)
								.toLowerCase();

						if (nextLowerPhrase.equals(KEYWORD_ON)
								|| nextLowerPhrase.equals(KEYWORD_AT)
								|| nextLowerPhrase.equals(KEYWORD_BY)
								|| nextLowerPhrase.equals(KEYWORD_FROM)
								|| nextLowerPhrase.equals(KEYWORD_TO)) {
							switchString = "time";
						} else {
							switchString = "location";
						}
					} else {
						switchString = "location";
					}
				} else {
					switchString = "time";
				}
			}

			if (switchString.equals("date")) {

				String[] dayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				int extraWordsUsed = Integer.parseInt(dayMonthYear[3]);
				i = i + extraWordsUsed;

				try {
					dateDay = Integer.parseInt(dayMonthYear[0]);
					dateMonth = Integer.parseInt(dayMonthYear[1]);
					dateYear = Integer.parseInt(dayMonthYear[2]);

					if (dateDay < 0 || dateDay > 31 || dateMonth < 0
							|| dateMonth > 12) {

						dateDay = -1;
						dateMonth = -1;
						dateYear = -1;

						if (throwException) {
							NumberFormatException e = new NumberFormatException(
									"Invalid date or month given. "
											+ "Consult the following command for assistance:\n help add");
							System.out.println(e);
							throw e;
						} else {
							switchString = "name";
							continue;
						}
					}

					taskType = "deadline";
					continue;
				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse date value given. "
										+ "Consult the following command for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						dateDay = -1;
						dateMonth = -1;
						dateYear = -1;
						switchString = "name";
						continue;
					}

				} catch (ArrayIndexOutOfBoundsException e) {

					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse "
										+ "date value given. Consult the following "
										+ "command for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						dateDay = -1;
						dateMonth = -1;
						dateYear = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("time")
					|| switchString.equals("timerangestart")) {

				String[] hourMinute = tryToParseTime(currentPhrase);

				try {

					int extraHours = 0;

					// For 12am
					if (hourMinute[0].equals("12") && hourMinute[2].equals("0")) {
						hourMinute[0] = "0";
					}

					// For pm
					if ((!hourMinute[0].equals("12"))
							&& hourMinute[2].equals("12")) {
						extraHours = 12;
					}

					dateHour = Integer.parseInt(hourMinute[0]);
					dateMinute = Integer.parseInt(hourMinute[1]);

					// A delayed check prevents passing weird cases like
					// -1pm
					if (dateHour >= 0) {
						dateHour = dateHour + extraHours;
					}

					if (dateHour > 24 || dateHour < 0 || dateMinute > 60
							|| dateMinute < 0) {

						dateHour = -1;
						dateMinute = -1;

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Invalid time value"
											+ " given. Consult the following command"
											+ " for assistance:\n help add");
							System.out.println(e2);
							throw e2;
						} else {
							dateHour = -1;
							dateMinute = -1;
							switchString = "name";
							continue;
						}
					}

					if (switchString.equals("time")) {
						taskType = "deadline";
					} else if (switchString.equals("timerangestart")) {
						taskType = "event";
					}
					continue;

				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse time value"
										+ " given. Consult the following command"
										+ " for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						dateHour = -1;
						dateMinute = -1;
						switchString = "name";
						continue;
					}

				} catch (ArrayIndexOutOfBoundsException e) {

					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse time value"
										+ " given. Consult the following command"
										+ " for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						dateHour = -1;
						dateMinute = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equalsIgnoreCase("timerangeend")) {

				String[] hourMinute = tryToParseTime(currentPhrase);

				try {

					int extraHours = 0;

					// For 12am
					if (hourMinute[0].equals("12") && hourMinute[2].equals("0")) {
						hourMinute[0] = "0";
					}

					// For pm
					if ((!hourMinute[0].equals("12"))
							&& hourMinute[2].equals("12")) {
						extraHours = 12;
					}

					endDateHour = Integer.parseInt(hourMinute[0]);
					endDateMinute = Integer.parseInt(hourMinute[1]);

					// A delayed check prevents passing weird cases like
					// -1pm
					if (endDateHour >= 0) {
						endDateHour = endDateHour + extraHours;
					}

					if (endDateHour > 24 || endDateHour < 0
							|| endDateMinute > 60 || endDateMinute < 0) {
						endDateHour = -1;
						endDateMinute = -1;

						if (throwException) {

							NumberFormatException e2 = new NumberFormatException(
									"Invalid end time value"
											+ " given. Consult the following command"
											+ " for assistance:\n help add");
							System.out.println(e2);
							throw e2;
						} else {
							endDateHour = -1;
							endDateMinute = -1;
							switchString = "name";
							continue;
						}
					}

					duration = 60 * (endDateHour - dateHour)
							+ (endDateMinute - dateMinute);
					continue;

				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse end time value"
										+ " given. Consult the following command"
										+ " for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						endDateHour = -1;
						endDateMinute = -1;
						switchString = "name";
						continue;
					}

				} catch (ArrayIndexOutOfBoundsException e) {

					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse end"
										+ " time value given. Consult the following command"
										+ " for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						endDateHour = -1;
						endDateMinute = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("notify")) {

				try {
					hourBefore = Integer.parseInt(currentPhrase);

					if (hourBefore < 0) {
						hourBefore = 0;

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Could not parse notify value"
											+ " given. Consult the following command"
											+ " for assistance:\n help add");
							System.out.println(e2);
							throw e2;
						} else {
							switchString = "name";
							continue;
						}
					}
					continue;
				} catch (NumberFormatException e) {
					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse notify value"
										+ " given. Consult the following command"
										+ " for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("location")) {
				location.append(REGEX_WHITESPACE);
				location.append(currentPhrase);
				continue;
			}
		}

		if (dateHour > 0 || dateMinute > 0) {
			if (dateDay == -1 || dateMonth == -1 || dateYear == -1) {
				GregorianCalendar todayCalendar = new GregorianCalendar();
				int todayHour = todayCalendar.get(Calendar.HOUR_OF_DAY);
				int todayMinute = todayCalendar.get(Calendar.MINUTE);

				if (todayHour > dateHour
						|| (todayHour == dateHour && todayMinute > dateMinute)) {
					todayCalendar.roll(Calendar.DAY_OF_MONTH, 1);

					if (todayCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
						todayCalendar.roll(Calendar.MONTH, 1);

						if (todayCalendar.get(Calendar.MONTH) == 1) {
							todayCalendar.roll(Calendar.YEAR, 1);
						}
					}
				}

				dateDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
				dateMonth = todayCalendar.get(Calendar.MONTH) + 1;
				dateYear = todayCalendar.get(Calendar.YEAR);
			}
		}

		// Set name
		TaskObject taskObjectToBuild = new TaskObject(name.toString());

		// Set date
		taskObjectToBuild.setDateYear(dateYear);
		taskObjectToBuild.setDateMonth(dateMonth);
		taskObjectToBuild.setDateDay(dateDay);

		// Set time
		taskObjectToBuild.setDateHour(dateHour);
		taskObjectToBuild.setDateMinute(dateMinute);
		taskObjectToBuild.setNotifyTime(hourBefore);

		// Set duration
		taskObjectToBuild.setDuration(duration);

		// set end datetime
		if (duration > 0) {
			taskObjectToBuild.setEndDateYear(dateYear);
			taskObjectToBuild.setEndDateMonth(dateMonth);
			taskObjectToBuild.setEndDateDay(dateDay);
			taskObjectToBuild.setEndDateHour(endDateHour);
			taskObjectToBuild.setEndDateMinute(endDateMinute);
		}

		// Set location
		taskObjectToBuild.setLocation(location.toString());

		// Set taskType
		taskObjectToBuild.setTaskType(taskType);

		return taskObjectToBuild;
	}

	public static TaskObject parseUpdate(String userCommand,
			TaskObject reallyOldTaskObject, boolean throwException) {

		ParserFirstPass parseAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = parseAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		StringBuilder name = new StringBuilder(
				reallyOldTaskObject.getTaskName());
		StringBuilder location = new StringBuilder(
				reallyOldTaskObject.getLocation());
		int dateDay = reallyOldTaskObject.getDateDay();
		int dateMonth = reallyOldTaskObject.getDateMonth();
		int dateYear = reallyOldTaskObject.getDateYear();
		int dateHour = reallyOldTaskObject.getDateHour();
		int dateMinute = reallyOldTaskObject.getDateMinute();
		int hourBefore = reallyOldTaskObject.getNotifyTime();
		int duration = reallyOldTaskObject.getDuration();
		int endDateHour = reallyOldTaskObject.getEndDateHour();
		int endDateMinute = reallyOldTaskObject.getEndDateMinute();
		TaskObject.TASK_STATUS taskStatus = reallyOldTaskObject.getTaskStatus();

		boolean alteringName = false;
		boolean alteringLocation = false;

		String switchString = "name";
		String taskType = reallyOldTaskObject.getTaskType();

		for (int i = 2; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i);
			String lowerPhrase = currentPhrase.toLowerCase();

			if (lowerPhrase.equals(KEYWORD_ON)
					|| lowerPhrase.equals(KEYWORD_BY)) {
				switchString = "datetime";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_NOTIFY)) {
				switchString = "notify";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_AT)) {
				switchString = "locationtime";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_FROM)) {
				switchString = "timerangestart";
				continue;
			} else if (lowerPhrase.equals(KEYWORD_TO)
					&& (dateHour >= 0 && dateMinute >= 0)) {
				switchString = "timerangeend";
				continue;
			}

			if (switchString.equals("name")) {
				
				if (!alteringName) {
					name.delete(0, name.length());
					alteringName = true;
				}
				
				name.append(REGEX_WHITESPACE);
				name.append(currentPhrase);
				continue;
			}

			if (switchString.equals("datetime")) {

				String[] maybeDayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				if (maybeDayMonthYear[4].equals("maybeNotDate")) {
					switchString = "time";
				} else {
					switchString = "date";
				}
			}

			if (switchString.equals("locationtime")) {

				String[] maybeHourMinute = tryToParseTime(currentPhrase);

				if (maybeHourMinute[3].equals("maybeNotTime")) {

					if (i + 1 < phraseCount) {
						String nextLowerPhrase = allPhrases.get(i + 1)
								.toLowerCase();

						if (nextLowerPhrase.equals(KEYWORD_ON)
								|| nextLowerPhrase.equals(KEYWORD_AT)
								|| nextLowerPhrase.equals(KEYWORD_BY)
								|| nextLowerPhrase.equals(KEYWORD_FROM)
								|| nextLowerPhrase.equals(KEYWORD_TO)) {
							switchString = "time";
						} else {
							switchString = "location";
						}
					} else {
						switchString = "location";
					}
				} else {
					switchString = "time";
				}
			}

			if (switchString.equals("date")) {

				String[] dayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				try {
					dateDay = Integer.parseInt(dayMonthYear[0]);
					dateMonth = Integer.parseInt(dayMonthYear[1]);
					dateYear = Integer.parseInt(dayMonthYear[2]);
					int extraWordsUsed = Integer.parseInt(dayMonthYear[3]);

					i = i + extraWordsUsed;

					if (dateDay < 0 || dateDay > 31 || dateMonth < 0
							|| dateMonth > 31) {

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Could not parse date value given. "
											+ "Consult the following command for assistance:\n help edit");
							System.out.println(e2);
							throw e2;
						} else {
							dateDay = -1;
							dateMonth = -1;
							dateYear = -1;
							switchString = "name";
							continue;
						}
					}

					taskType = "deadline";
					continue;
				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse date value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						dateDay = -1;
						dateMonth = -1;
						dateYear = -1;
						switchString = "name";
						continue;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse date value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						dateDay = -1;
						dateMonth = -1;
						dateYear = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("time")
					|| switchString.equals("timerangestart")) {

				String[] hourMinute = tryToParseTime(currentPhrase);

				try {

					int extraHours = 0;

					// For 12am
					if (hourMinute[0].equals("12") && hourMinute[2].equals("0")) {
						hourMinute[0] = "0";
					}

					// For pm
					if ((!hourMinute[0].equals("12"))
							&& hourMinute[2].equals("12")) {
						extraHours = 12;
					}

					dateHour = Integer.parseInt(hourMinute[0]);
					dateMinute = Integer.parseInt(hourMinute[1]);

					// A delayed check prevents passing weird cases like
					// -1pm
					if (dateHour >= 0) {
						dateHour = dateHour + extraHours;
					}

					if (dateHour < 0 || dateHour > 24 || dateMinute < 0
							|| dateMinute > 60) {

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Could not parse time value given. "
											+ "Consult the following command for assistance:\n help edit");
							System.out.println(e2);
							throw e2;
						} else {
							dateHour = -1;
							dateMinute = -1;
							switchString = "name";
							continue;
						}
					}

					if (switchString.equals("time")) {
						taskType = "deadline";
					} else if (switchString.equals("timerangestart")) {
						taskType = "event";
					}
					continue;
				} catch (NumberFormatException e) {
					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse time value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						dateHour = -1;
						dateMinute = -1;
						switchString = "name";
						continue;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse time value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						dateHour = -1;
						dateMinute = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("timerangeend")) {

				String[] hourMinute = tryToParseTime(currentPhrase);

				try {

					int extraHours = 0;

					// For 12am
					if (hourMinute[0].equals("12") && hourMinute[2].equals("0")) {
						hourMinute[0] = "0";
					}

					// For pm
					if ((!hourMinute[0].equals("12"))
							&& hourMinute[2].equals("12")) {
						extraHours = 12;
					}

					endDateHour = Integer.parseInt(hourMinute[0]);
					endDateMinute = Integer.parseInt(hourMinute[1]);

					// A delayed check prevents passing weird cases like
					// -1pm
					if (endDateHour >= 0) {
						endDateHour = endDateHour + extraHours;
					}

					if (endDateHour < 0 || endDateMinute > 24
							|| endDateMinute < 0 || endDateMinute > 60) {

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Could not parse end time value given. "
											+ "Consult the following command for assistance:\n help edit");
							System.out.println(e2);
							throw e2;
						} else {
							endDateHour = -1;
							endDateMinute = -1;
							switchString = "name";
							continue;
						}
					}

					duration = 60 * (endDateHour - dateHour)
							+ (endDateMinute - dateMinute);
					continue;
				} catch (NumberFormatException e) {
					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse end time value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						endDateHour = -1;
						endDateMinute = -1;
						switchString = "name";
						continue;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (throwException) {
						ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
								"Could not parse end time value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						endDateHour = -1;
						endDateMinute = -1;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("notify")) {

				try {
					hourBefore = Integer.parseInt(currentPhrase);

					if (hourBefore < 0) {

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Could not parse notify value given. "
											+ "Consult the following command for assistance:\n help edit");
							System.out.println(e2);
							throw e2;
						} else {
							hourBefore = 0;
							switchString = "name";
							continue;
						}
					}
					continue;
				} catch (NumberFormatException e) {
					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse notify value given. "
										+ "Consult the following command for assistance:\n help edit");
						System.out.println(e2);
						throw e2;
					} else {
						hourBefore = 0;
						switchString = "name";
						continue;
					}
				}
			}

			if (switchString.equals("location")) {
				if (!alteringLocation) {
					alteringLocation = true;
					location.delete(0, location.length());
					location.append(currentPhrase);
				} else {
					location.append(REGEX_WHITESPACE);
					location.append(currentPhrase);
				}
				continue;
			}
		}

		if (dateHour > 0 || dateMinute > 0) {
			if (dateDay == -1 || dateMonth == -1 || dateYear == -1) {
				GregorianCalendar todayCalendar = new GregorianCalendar();
				int todayHour = todayCalendar.get(Calendar.HOUR_OF_DAY);
				int todayMinute = todayCalendar.get(Calendar.MINUTE);

				if (todayHour > dateHour
						|| (todayHour == dateHour && todayMinute > dateMinute)) {
					todayCalendar.roll(Calendar.DAY_OF_MONTH, 1);

					if (todayCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
						todayCalendar.roll(Calendar.MONTH, 1);

						if (todayCalendar.get(Calendar.MONTH) == 1) {
							todayCalendar.roll(Calendar.YEAR, 1);
						}
					}
				}

				dateDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
				dateMonth = todayCalendar.get(Calendar.MONTH) + 1;
				dateYear = todayCalendar.get(Calendar.YEAR);
			}
		}

		// Set name
		TaskObject taskObjectToBuild = new TaskObject(name.toString());

		// Set date
		taskObjectToBuild.setDateYear(dateYear);
		taskObjectToBuild.setDateMonth(dateMonth);
		taskObjectToBuild.setDateDay(dateDay);

		// Set time
		taskObjectToBuild.setDateHour(dateHour);
		taskObjectToBuild.setDateMinute(dateMinute);
		taskObjectToBuild.setNotifyTime(hourBefore);

		// Set duration
		taskObjectToBuild.setDuration(duration);

		// Set end datetime
		if (duration > 0) {
			taskObjectToBuild.setEndDateYear(dateYear);
			taskObjectToBuild.setEndDateMonth(dateMonth);
			taskObjectToBuild.setEndDateDay(dateDay);
			taskObjectToBuild.setEndDateHour(endDateHour);
			taskObjectToBuild.setEndDateMinute(endDateMinute);
		}

		// Set location
		taskObjectToBuild.setLocation(location.toString());

		// Set taskType
		taskObjectToBuild.setTaskType(taskType);

		// Set taskStatus
		taskObjectToBuild.setTaskStatus(taskStatus);

		return taskObjectToBuild;
	}

	public static ArrayList<Integer> parseDelete(String userCommand,
			boolean throwException) {
		// TODO Auto-generated method stub

		ParserFirstPass parseAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = parseAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i = 1; i < phraseCount; i++) {

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
	public static ArrayList<Integer> parseSearch(String userCommand,
			ArrayList<TaskObject> displayList, boolean throwException) {

		ParserFirstPass scanAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = scanAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();
		int itemCount = displayList.size();

		HashSet<Integer> indicesToRemove = new HashSet<>();
		HashSet<Integer> indicesToReturn = new HashSet<>();

		for (int i = 0; i < itemCount; i++) {
			indicesToReturn.add(i);
		}
		
		String testKeyWord = allPhrases.get(1).toLowerCase();
		boolean exactOnly = false;
		
		if (phraseCount > 2 && testKeyWord.equals("exact")) {
			exactOnly = true;
		}

		for (int i = 1; i < phraseCount; i++) {

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
					
					String[] splitTaskName = currentTaskName.split(REGEX_WHITESPACE);
					
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

	public static ShowInterval parseShow(String userCommand,
			boolean throwException) {

		ParserFirstPass scanAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = scanAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		for (int i = 1; i < phraseCount; i++) {

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

	public static int getInterval(String userCommand, boolean throwException) {

		ParserFirstPass scanAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = scanAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		for (int i = 1; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i).toLowerCase();

			if (currentPhrase.equals("today")
					|| currentPhrase.equals("tomorrow")
					|| currentPhrase.equals("all")) {
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

	public static int getUpdateTaskId(String userCommand, boolean throwException) {

		String[] splitUserCommand = userCommand.trim().split(REGEX_WHITESPACE);

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

	private static String[] tryToParseDate(ArrayList<String> allPhrases,
			String currentPhrase, int i, int phraseCount) {

		String[] dayMonthYear = new String[5];
		dayMonthYear[0] = "-1";
		dayMonthYear[1] = "-1";
		dayMonthYear[2] = "-1";
		dayMonthYear[3] = "0";
		dayMonthYear[4] = "maybeNotDate";

		int extraWordsUsed = 0;

		if (currentPhrase.equals("today") || currentPhrase.equals("tdy")) {

			GregorianCalendar today = new GregorianCalendar();
			int todayDate = today.get(Calendar.DAY_OF_MONTH);
			int todayMonth = today.get(Calendar.MONTH) + 1;
			int todayYear = today.get(Calendar.YEAR);

			dayMonthYear[0] = Integer.toString(todayDate);
			dayMonthYear[1] = Integer.toString(todayMonth);
			dayMonthYear[2] = Integer.toString(todayYear);
			dayMonthYear[4] = "isDate";

		} else if (currentPhrase.equals("tomorrow")
				|| currentPhrase.equals("tmr")) {

			GregorianCalendar tomorrow = new GregorianCalendar();
			tomorrow.roll(Calendar.DAY_OF_MONTH, 1);

			int tomorrowDate = tomorrow.get(Calendar.DAY_OF_MONTH);
			int tomorrowMonth = tomorrow.get(Calendar.MONTH) + 1;
			int tomorrowYear = tomorrow.get(Calendar.YEAR);

			if (tomorrowDate == 1) {
				tomorrow.roll(Calendar.MONTH, 1);
				tomorrowMonth = tomorrow.get(Calendar.MONTH) + 1;

				if (tomorrowMonth == 1) {
					tomorrow.roll(Calendar.YEAR, 1);
					tomorrowYear = tomorrow.get(Calendar.YEAR);
				}
			}

			dayMonthYear[0] = Integer.toString(tomorrowDate);
			dayMonthYear[1] = Integer.toString(tomorrowMonth);
			dayMonthYear[2] = Integer.toString(tomorrowYear);
			dayMonthYear[4] = "isDate";

		} else if (currentPhrase.contains("/")) {
			String[] tempDayMonthYear = currentPhrase.split("/");
			dayMonthYear[0] = tempDayMonthYear[0];
			dayMonthYear[1] = tempDayMonthYear[1];
			dayMonthYear[2] = tempDayMonthYear[2];
			dayMonthYear[4] = "isDate";
		} else if (currentPhrase.contains("-")) {
			String[] tempDayMonthYear = currentPhrase.split("-");
			dayMonthYear[0] = tempDayMonthYear[0];
			dayMonthYear[1] = tempDayMonthYear[1];
			dayMonthYear[2] = tempDayMonthYear[2];
			dayMonthYear[4] = "isDate";
		} else {

			String possibleDay = currentPhrase;

			// Trim stuff if required
			if (possibleDay.endsWith("st") || possibleDay.endsWith("nd")
					|| possibleDay.endsWith("rd") || possibleDay.endsWith("th")) {

				possibleDay = possibleDay
						.substring(0, possibleDay.length() - 2);

				dayMonthYear[4] = "isDate";
			}

			dayMonthYear[0] = possibleDay;

			boolean foundMonth = false;
			String possibleMonth = "";

			if (i + 1 < phraseCount) {
				possibleMonth = allPhrases.get(i + 1).toLowerCase();
			}

			String[] possibleMonthValues = { "jan", "january", "feb",
					"february", "mar", "march", "apr", "april", "may", "may",
					"jun", "june", "jul", "july", "aug", "august", "oct",
					"october", "nov", "november", "dec", "december" };

			for (int j = 0; j < possibleMonthValues.length; j++) {

				if (possibleMonth.equals(possibleMonthValues[j])) {
					foundMonth = true;
					dayMonthYear[4] = "isDate";
					int numericMonth = 1 + (j / 2);
					dayMonthYear[1] = Integer.toString(numericMonth);
					extraWordsUsed++;
					break;
				}
			}

			if (!foundMonth) {
				GregorianCalendar today = new GregorianCalendar();
				dayMonthYear[1] = Integer
						.toString(today.get(Calendar.MONTH) + 1);
			}

			String possibleYear = "";

			if (foundMonth && (i + 2) < phraseCount) {
				possibleYear = allPhrases.get(i + 2);
			}

			try {
				int numericYear = Integer.parseInt(possibleYear);
				dayMonthYear[2] = Integer.toString(numericYear);
				extraWordsUsed++;
			} catch (NumberFormatException e) {
				GregorianCalendar today = new GregorianCalendar();
				dayMonthYear[2] = Integer.toString(today.get(Calendar.YEAR));
			}
		}

		dayMonthYear[3] = Integer.toString(extraWordsUsed);

		return dayMonthYear;
	}

	private static String[] tryToParseTime(String currentPhrase) {

		String[] hourMinute = new String[4];
		hourMinute[0] = "-1";
		hourMinute[1] = "-1";
		hourMinute[2] = "0";
		hourMinute[3] = "maybeNotTime";

		int extraHours = 0;

		if (currentPhrase.contains(":") || currentPhrase.contains(".")) {

			if (currentPhrase.contains(":")) {
				String[] tempHourMinute = currentPhrase.split(":");
				hourMinute[0] = tempHourMinute[0];
				hourMinute[1] = tempHourMinute[1];
				hourMinute[3] = "isTime";
			} else {
				String[] tempHourMinute = currentPhrase.split("\\.");
				hourMinute[0] = tempHourMinute[0];
				hourMinute[1] = tempHourMinute[1];
				hourMinute[3] = "isTime";
			}

			// Trim as required
			if (hourMinute[1].endsWith("am")) {
				hourMinute[1] = hourMinute[1].substring(0,
						hourMinute[1].length() - 2);

				if (Parser.isNumber(hourMinute[1])) {
					hourMinute[3] = "isTime";
				}
			} else if (hourMinute[1].endsWith("pm")) {

				extraHours = 12;
				hourMinute[1] = hourMinute[1].substring(0,
						hourMinute[1].length() - 2);

				if (Parser.isNumber(hourMinute[1])) {
					hourMinute[3] = "isTime";
				}
			}
		} else {

			// Count character by character from the back
			int phraseSize = currentPhrase.length();

			// Five length and above case
			// Must contain an am/pm suffix
			// Trim and run it with shortest cases
			if (phraseSize >= 5) {

				if (currentPhrase.endsWith("pm")) {
					extraHours = 12;
				}
				currentPhrase = currentPhrase.substring(0,
						currentPhrase.length() - 2);
				phraseSize = phraseSize - 2;
			}

			// Triple and quad length case
			// Might have either single/double digit + am/pm
			// Or triple/quad digit 24hr time format
			if (phraseSize >= 3) {

				if (currentPhrase.endsWith("am")) {
					hourMinute[0] = currentPhrase.substring(0, phraseSize - 2);
					hourMinute[1] = "0";

					if (Parser.isNumber(hourMinute[0])) {
						hourMinute[3] = "isTime";
					}
				} else if (currentPhrase.endsWith("pm")) {
					extraHours = 12;
					hourMinute[0] = currentPhrase.substring(0, phraseSize - 2);
					hourMinute[1] = "0";

					if (Parser.isNumber(hourMinute[0])) {
						hourMinute[3] = "isTime";
					}
				} else {
					hourMinute[0] = currentPhrase.substring(0, phraseSize - 2);
					hourMinute[1] = currentPhrase.substring(phraseSize - 2,
							phraseSize);

					if (Parser.isNumber(hourMinute[0])
							&& Parser.isNumber(hourMinute[1])) {
						hourMinute[3] = "isTime";
					}
				}
			}

			// Single and double length case
			// e.g. 3, 11, 09
			// Impossible: 1pm, 145
			if (phraseSize < 3) {
				hourMinute[0] = currentPhrase;
				hourMinute[1] = "0";
			}
		}

		hourMinute[2] = Integer.toString(extraHours);

		return hourMinute;
	}

	private static boolean isNumber(String stringToTest) {

		try {
			Integer.parseInt(stringToTest);

			return true;
		} catch (NumberFormatException e) {

			return false;
		}
	}
}
