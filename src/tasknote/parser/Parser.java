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
		ArrayList<String> allPhrases = firstPassCommand
				.getFirstPassParsedResult();

		COMMAND_TYPE returnValue = COMMAND_TYPE.INVALID;

		try {

			// This will throw an exception if the ArrayList is empty
			String userCommandWord = allPhrases.get(0).toLowerCase();

			// Finally, we check to see which COMMAND_TYPE
			// matches the command given by the user
			// If none of the COMMAND_TYPE matches, the
			// INVALID COMMAND_TYPE is returned instead
			returnValue = matchCommandTypeNoLengthCheck(userCommandWord);
			
			// Finally, we check if the given user command
			// has the minimum required length to make the command
			// a valid one
			if (!hasValidInputLength(returnValue, allPhrases)) {
				returnValue = COMMAND_TYPE.INVALID;
			}

			return returnValue;

		} catch (IndexOutOfBoundsException e) {

			if (throwException) {
				IndexOutOfBoundsException exceptionToThrow = new IndexOutOfBoundsException(
						"Command given was not recognised. "
								+ "Consult the following command for assistance:\n help");

				System.out.println(exceptionToThrow);
				throw exceptionToThrow;
			} else {
				return COMMAND_TYPE.INVALID;
			}
		}
	}

	// parseHelp should only be called if the COMMAND_TYPE was valid
	public static COMMAND_TYPE parseHelp(String userCommand,
			boolean throwException) {

		ParserFirstPass firstPassCommand = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrase = firstPassCommand
				.getFirstPassParsedResult();

		int allPhraseCount = allPhrase.size();
		String userCommandWord = "";
		COMMAND_TYPE returnValue = COMMAND_TYPE.INVALID;
		
		// This handles the case where "help" is the only
		// input supplied by the user
		if (allPhraseCount == 1) {
			return COMMAND_TYPE.HELP;
		}

		// If not, get the words that comes after "help"
		if (allPhraseCount > 1) {
			userCommandWord = allPhrase.get(1).toLowerCase();
		}

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		returnValue = matchCommandTypeNoLengthCheck(userCommandWord);
		
		return returnValue;
	}

	// Fixed command structure: add taskname <on date> <by time> <notify time>
	// <at location>
	public static TaskObject parseAdd(String userCommand, boolean throwException) {

		ParserFirstPass parseAllWords = new ParserFirstPass(userCommand);
		ArrayList<String> allPhrases = parseAllWords.getFirstPassParsedResult();

		int phraseCount = allPhrases.size();

		String switchString = "name";
		String oldSwitchString = "name";
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

		int endDateDay = -1;
		int endDateMonth = -1;
		int endDateYear = -1;
		int endDateHour = -1;
		int endDateMinute = -1;

		boolean toStartDateTime = true;

		for (int i = 2; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i);
			String lowerPhrase = currentPhrase.toLowerCase();

			if (lowerPhrase.equals(ParserConstants.KEYWORD_ON)
					|| lowerPhrase.equals(ParserConstants.KEYWORD_BY)) {
				switchString = "datetime";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_NOTIFY)) {
				switchString = "notify";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_AT)) {
				switchString = "locationtime";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_FROM)) {
				switchString = "datetime";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_TO)
					&& (dateHour >= 0 && dateMinute >= 0)) {
				switchString = "datetime";
				toStartDateTime = false;
				continue;
			}

			if (switchString.equals("name")) {
				name.append(Constants.STRING_CONSTANT_SPACE);
				name.append(currentPhrase);
				continue;
			}

			if (switchString.equals("time") || switchString.equals("date")) {
				switchString = oldSwitchString;
			}

			if (switchString.equals("datetime")) {

				DateMessage maybeDayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				if (maybeDayMonthYear.getMessage().equals("maybeNotDate")) {
					switchString = "time";
					oldSwitchString = "datetime";
				} else {
					switchString = "date";
					oldSwitchString = "datetime";
				}
			}

			if (switchString.equals("locationtime")) {

				String[] maybeHourMinute = tryToParseTime(currentPhrase);

				if (maybeHourMinute[3].equals("maybeNotTime")) {

					if (!Parser.isNumber(currentPhrase)) {
						switchString = "location";
					} else if (i + 1 < phraseCount) {
						String nextLowerPhrase = allPhrases.get(i + 1)
								.toLowerCase();

						if (Parser.isNumber(currentPhrase)
								&& (nextLowerPhrase
										.equals(ParserConstants.KEYWORD_ON)
										|| nextLowerPhrase
												.equals(ParserConstants.KEYWORD_AT)
										|| nextLowerPhrase
												.equals(ParserConstants.KEYWORD_BY)
										|| nextLowerPhrase
												.equals(ParserConstants.KEYWORD_FROM) || nextLowerPhrase
											.equals(ParserConstants.KEYWORD_TO))) {
							switchString = "time";
						} else {
							switchString = "location";
						}
					} else {
						switchString = "time";
					}
				} else {
					switchString = "time";
				}
			}

			if (switchString.equals("date")) {

				DateMessage dayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				int extraWordsUsed = dayMonthYear.getExtraWordsUsed();
				i = i + extraWordsUsed;

				try {
					int holderDay = dayMonthYear.getDay();
					int holderMonth = dayMonthYear.getMonth();
					int holderYear = dayMonthYear.getYear();

					if (holderDay < 0 || holderDay > 31 || holderMonth < 0
							|| holderMonth > 12) {

						holderDay = -1;
						holderMonth = -1;
						holderYear = -1;

						if (throwException) {
							NumberFormatException e = new NumberFormatException(
									"Invalid date or month given. "
											+ "Consult the following command for assistance:\n help add");
							System.out.println(e);
							throw e;
						} else {

							if (toStartDateTime) {
								dateYear = holderYear;
								dateMonth = holderMonth;
								dateDay = holderDay;
							} else {
								endDateYear = holderYear;
								endDateMonth = holderMonth;
								endDateDay = holderDay;
							}

							switchString = "name";
							continue;
						}
					}

					if (toStartDateTime) {
						dateYear = holderYear;
						dateMonth = holderMonth;
						dateDay = holderDay;

						taskType = "deadline";
					} else {
						endDateYear = holderYear;
						endDateMonth = holderMonth;
						endDateDay = holderDay;

						taskType = "event";
					}

					continue;
				} catch (NumberFormatException e) {

					if (throwException) {
						NumberFormatException e2 = new NumberFormatException(
								"Could not parse date value given. "
										+ "Consult the following command for assistance:\n help add");
						System.out.println(e2);
						throw e2;
					} else {

						if (toStartDateTime) {
							dateYear = -1;
							dateMonth = -1;
							dateDay = -1;
						} else {
							endDateYear = -1;
							endDateMonth = -1;
							endDateDay = -1;
						}

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

						if (toStartDateTime) {
							dateYear = -1;
							dateMonth = -1;
							dateDay = -1;
						} else {
							endDateYear = -1;
							endDateMonth = -1;
							endDateDay = -1;
						}

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

					int holderHour = Integer.parseInt(hourMinute[0]);
					int holderMinute = Integer.parseInt(hourMinute[1]);

					// A delayed check prevents passing weird cases like
					// -1pm
					if (holderHour >= 0) {
						holderHour = holderHour + extraHours;
					}

					if (holderHour > 24 || holderHour < 0 || holderMinute > 60
							|| holderMinute < 0) {

						holderHour = -1;
						holderMinute = -1;

						if (throwException) {
							NumberFormatException e2 = new NumberFormatException(
									"Invalid time value"
											+ " given. Consult the following command"
											+ " for assistance:\n help add");
							System.out.println(e2);
							throw e2;
						} else {

							if (toStartDateTime) {
								dateHour = -1;
								dateMinute = -1;
							} else {
								endDateHour = -1;
								endDateMinute = -1;
								duration = 0;
							}
							switchString = "name";
							continue;
						}
					}

					if (toStartDateTime) {
						dateHour = holderHour;
						dateMinute = holderMinute;
						taskType = "deadline";
					} else {
						endDateHour = holderHour;
						endDateMinute = holderMinute;
						duration = 60 * (endDateHour - dateHour)
								+ (endDateMinute - dateMinute);
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
				location.append(Constants.STRING_CONSTANT_SPACE);
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

		if (endDateHour > 0 || endDateMinute > 0) {
			if (endDateDay == -1 || endDateMonth == -1 || endDateYear == -1) {
				endDateDay = dateDay;
				endDateMonth = dateMonth;
				endDateYear = dateYear;
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
			taskObjectToBuild.setEndDateYear(endDateYear);
			taskObjectToBuild.setEndDateMonth(endDateMonth);
			taskObjectToBuild.setEndDateDay(endDateDay);
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
		String taskType = reallyOldTaskObject.getTaskType();
		TaskObject.TASK_STATUS taskStatus = reallyOldTaskObject.getTaskStatus();

		boolean alteringName = false;
		boolean alteringLocation = false;

		boolean toStartDateTime = true;

		String switchString = "name";

		for (int i = 2; i < phraseCount; i++) {

			String currentPhrase = allPhrases.get(i);
			String lowerPhrase = currentPhrase.toLowerCase();

			if (lowerPhrase.equals(ParserConstants.KEYWORD_ON)
					|| lowerPhrase.equals(ParserConstants.KEYWORD_BY)) {
				switchString = "datetime";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_NOTIFY)) {
				switchString = "notify";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_AT)) {
				switchString = "locationtime";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_FROM)) {
				switchString = "timerangestart";
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_TO)
					&& (dateHour >= 0 && dateMinute >= 0)) {
				switchString = "timerangeend";
				toStartDateTime = false;
				continue;
			} else if (lowerPhrase.equals(ParserConstants.KEYWORD_REMOVE)
					|| lowerPhrase.equals("rm")) {
				switchString = "remove";
				continue;
			}

			if (switchString.equals("remove")) {

				if (lowerPhrase.equals("time")) {
					dateHour = -1;
					dateMinute = -1;

					if (dateYear == -1 && dateMonth == -1 && dateDay == -1) {
						taskType = "floating";
					}
					continue;
				} else if (lowerPhrase.equals("date")) {
					dateYear = -1;
					dateMonth = -1;
					dateDay = -1;

					if (dateHour == -1 && dateMinute == -1) {
						taskType = "floating";
					}
					continue;
				} else if (lowerPhrase.equals("location")) {
					location.delete(0, location.length());
					continue;
				} else {
					switchString = "name";
					i--;
					continue;
				}
			}

			if (switchString.equals("name")) {

				if (!alteringName) {
					name.delete(0, name.length());
					alteringName = true;
				} else {
					name.append(Constants.STRING_CONSTANT_SPACE);
				}
				name.append(currentPhrase);
				continue;
			}

			if (switchString.equals("datetime")) {

				DateMessage maybeDayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				if (maybeDayMonthYear.getMessage().equals("maybeNotDate")) {
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

						if (nextLowerPhrase.equals(ParserConstants.KEYWORD_ON)
								|| nextLowerPhrase
										.equals(ParserConstants.KEYWORD_AT)
								|| nextLowerPhrase
										.equals(ParserConstants.KEYWORD_BY)
								|| nextLowerPhrase
										.equals(ParserConstants.KEYWORD_FROM)
								|| nextLowerPhrase
										.equals(ParserConstants.KEYWORD_TO)) {
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

				DateMessage dayMonthYear = tryToParseDate(allPhrases,
						currentPhrase, i, phraseCount);

				try {
					dateDay = dayMonthYear.getDay();
					dateMonth = dayMonthYear.getMonth();
					dateYear = dayMonthYear.getYear();
					int extraWordsUsed = dayMonthYear.getExtraWordsUsed();

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
					location.append(Constants.STRING_CONSTANT_SPACE);
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

	private static DateMessage tryToParseDate(ArrayList<String> allPhrases,
			String currentPhrase, int i, int phraseCount) {
		
		DateParser dateParser = new DateParser();
		dateParser.setAllPhrases(allPhrases);
		dateParser.setCurrentPhrase(currentPhrase);
		dateParser.setListPointer(i);
		dateParser.setPhraseCount(phraseCount);
		
		DateMessage returnMessage = dateParser.tryToParseDate();
		
		return returnMessage;
	}

	private static String[] tryToParseTime(String currentPhrase) {

		String[] hourMinute = new String[4];
		hourMinute[0] = ParserConstants.DEFAULT_INVALID_STRING_DATETIME;
		hourMinute[1] = ParserConstants.DEFAULT_INVALID_STRING_DATETIME;
		hourMinute[2] = "0";
		hourMinute[3] = "maybeNotTime";

		int extraHours = Constants.ZERO_TIME_INTERVAL;

		if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON) || 
				currentPhrase.contains(ParserConstants.TIME_SEPARATOR_DOT)) {

			if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON)) {
				String[] tempHourMinute = currentPhrase.split(ParserConstants.TIME_SEPARATOR_COLON);
				hourMinute[0] = tempHourMinute[0];
				hourMinute[1] = tempHourMinute[1];
				hourMinute[3] = "isTime";
			} else {
				String[] tempHourMinute = currentPhrase.split(ParserConstants.TIME_SEPARATOR_ESCAPED_DOT);
				hourMinute[0] = tempHourMinute[0];
				hourMinute[1] = tempHourMinute[1];
				hourMinute[3] = "isTime";
			}

			// Trim as required
			if (hourMinute[1].endsWith(ParserConstants.HOUR_MOD_AM)) {
				hourMinute[1] = hourMinute[1].substring(0,
						hourMinute[1].length() - 2);

				if (Parser.isNumber(hourMinute[1])) {
					hourMinute[3] = "isTime";
				}
			} else if (hourMinute[1].endsWith(ParserConstants.HOUR_MOD_PM)) {

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

				if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
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

				if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_AM)) {
					hourMinute[0] = currentPhrase.substring(0, phraseSize - 2);
					hourMinute[1] = "0";

					if (Parser.isNumber(hourMinute[0])) {
						hourMinute[3] = "isTime";
					}
				} else if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
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

	private static boolean hasValidInputLength(COMMAND_TYPE command,
			ArrayList<String> allPhrases) {

		int userCommandLength = allPhrases.size();
		int expectedMinimumLength = ParserConstants
				.getMinimumCommandLength(command);

		return userCommandLength >= expectedMinimumLength;
	}

	// This method does not throw exceptions or check for userCommand length
	private static COMMAND_TYPE matchCommandTypeNoLengthCheck(String commandWord) {

		COMMAND_TYPE returnValue = COMMAND_TYPE.INVALID;

		// Finally, we check to see which COMMAND_TYPE
		// matches the command given by the user
		// If none of the COMMAND_TYPE matches, the
		// INVALID COMMAND_TYPE is returned instead
		if (commandWord.equals(ParserConstants.COMMAND_ADD)) {
			returnValue = COMMAND_TYPE.ADD;
		} else if (commandWord.equals(ParserConstants.COMMAND_EDIT)) {
			return COMMAND_TYPE.UPDATE;
		} else if (commandWord.equals(ParserConstants.COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (commandWord.equals(ParserConstants.COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else if (commandWord.equals(ParserConstants.COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
		} else if (commandWord.equals(ParserConstants.COMMAND_DONE)) {
			return COMMAND_TYPE.DONE;
		} else if (commandWord.equals(ParserConstants.COMMAND_REDO)) {
			return COMMAND_TYPE.REDO;
		} else if (commandWord.equals(ParserConstants.COMMAND_UNDO)) {
			return COMMAND_TYPE.UNDO;
		} else if (commandWord.equals(ParserConstants.COMMAND_SHOW)) {
			return COMMAND_TYPE.SHOW;
		} else if (commandWord.equals(ParserConstants.COMMAND_RELOCATE)) {
			return COMMAND_TYPE.CHANGE_FILE_PATH;
		} else if (commandWord.equals(ParserConstants.COMMAND_HELP)) {
			return COMMAND_TYPE.HELP;
		} else if (commandWord.equals(ParserConstants.COMMAND_UNDONE)) {
			return COMMAND_TYPE.UNDONE;
		} else {
			return COMMAND_TYPE.INVALID;
		}

		return returnValue;

	}
}
