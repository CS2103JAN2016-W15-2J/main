//@@author A0129529W
package tasknote.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ForwardCheckerWithBacktracking {
	
	// This contain the constant that is only useful for ForwardCheckerWithBacktracking
	protected static String SPECIAL_SKIP_TAG = "skip";

	// This are the private date fields used by this class
	private ArrayList<String> allPhrases;
	private int listPointer;
	private String[] allPhraseTypes;
	private String commandType;

	// The default constructor is not supported here
	// Constructor requires the entire processed user input for a meaningful check
	// The forward checker checks immediately upon object construction
	public ForwardCheckerWithBacktracking(ArrayList<String> allPhrases) {
		this.allPhrases = allPhrases;
		this.listPointer = 0;
		this.allPhraseTypes = new String[allPhrases.size()];

		approximateForwardCheck();
	}

	// This returns a String array containing all the marks assigned
	// to each phrase in the user input, mapped index to index
	public String[] getAllPhraseTypes() {
		return this.allPhraseTypes;
	}

	// This method performs the forward checking, followed by the backtracking
	// to mark all the user input as accurately as possible
	private void approximateForwardCheck() {

		String currentPhrase = this.allPhrases.get(listPointer).toLowerCase();
		int phraseCount = allPhrases.size();

		if (currentPhrase.equals(ParserConstants.COMMAND_ADD)) {
			this.commandType = ParserConstants.COMMAND_ADD;
			this.listPointer = 1;
			this.allPhraseTypes[0] = SPECIAL_SKIP_TAG;
		} else if (currentPhrase.equals(ParserConstants.COMMAND_EDIT)) {
			this.commandType = ParserConstants.COMMAND_EDIT;
			this.listPointer = 2;
			this.allPhraseTypes[0] = SPECIAL_SKIP_TAG;
			this.allPhraseTypes[1] = SPECIAL_SKIP_TAG;
		} else {
			throw new RuntimeException(
					"Forward checking is not necessary for non-add or non-edit commands");
		}

		String currentSwitchString = ParserConstants.SWITCH_STRING_NAME;

		// Forward Checking
		for (int currentPointer = listPointer; currentPointer < phraseCount; currentPointer++) {

			currentPhrase = this.allPhrases.get(currentPointer).toLowerCase();
			
			if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_REMOVE)) {
				if (currentPhrase.equals(ParserConstants.SWITCH_STRING_DATE)
						|| currentPhrase.equals(ParserConstants.SWITCH_STRING_TIME)
						|| currentPhrase.equals(ParserConstants.SWITCH_STRING_NAME)
						|| currentPhrase.equals(ParserConstants.SWITCH_STRING_LOCATION)) {
					this.allPhraseTypes[currentPointer] = currentSwitchString;
					continue;
				} else {
					currentSwitchString = ParserConstants.decideNewSwitchString(currentPhrase);
					this.allPhraseTypes[currentPointer] = currentSwitchString;
					continue;
				}
			}

			if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_NAME)) {
				currentSwitchString = ParserConstants.decideNewSwitchString(currentPhrase);
				this.allPhraseTypes[currentPointer] = currentSwitchString;
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)) {
				
				currentSwitchString = markDateTime(currentPhrase, currentSwitchString);
				
				// A special consideration is used here
				// In this case, if it currentPhrase is a raw number that passes both date and time
				// marking, and it is the last phrase in all the phrases, then we give precedence
				// to time over date
				if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
						&& currentPointer + 1 >= phraseCount) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_TIME;
				} else {
					this.allPhraseTypes[currentPointer] = currentSwitchString;
				}
				
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {
				
				currentSwitchString = markDateTime(currentPhrase, currentSwitchString);
				
				// A special consideration is used here
				// In this case, if it currentPhrase is a raw number that passes both date and time
				// marking, and it is the last phrase in all the phrases, then we give precedence
				// to time over date
				if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)
						&& currentPointer + 1 >= phraseCount) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_TIME;
				} else {
					this.allPhraseTypes[currentPointer] = currentSwitchString;
				}
				
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_DATE)) {
				currentSwitchString = markDate(currentPhrase, true);
				this.allPhraseTypes[currentPointer] = currentSwitchString;
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_TIME)) {
				currentSwitchString = markTime(currentPhrase, true);
				this.allPhraseTypes[currentPointer] = currentSwitchString;
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_LOCATIONTIME)) {
				currentSwitchString = markLocationTime(currentPhrase);
				this.allPhraseTypes[currentPointer] = currentSwitchString;
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_LOCATION)) {
				currentSwitchString = markLocation(currentPhrase);
				this.allPhraseTypes[currentPointer] = currentSwitchString;
			} else if (currentSwitchString.equals(ParserConstants.SWITCH_STRING_REMOVE)){
				if (this.commandType.equals(ParserConstants.COMMAND_ADD)) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_NAME;
				} else {
					this.allPhraseTypes[currentPointer] = 
							ParserConstants.SWITCH_STRING_REMOVE;
				}
			} else {
				this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_NAME;
			}
		}
		
		String lastSeen = ParserConstants.SWITCH_STRING_NAME;
		String currentSeen = ParserConstants.SWITCH_STRING_NAME;
		
		// Backtracking
		for (int currentPointer = phraseCount - 1; currentPointer >= 0; currentPointer--) {
			
			if (currentPointer == (phraseCount - 1)) {
				lastSeen = this.allPhraseTypes[currentPointer];
				currentSeen = this.allPhraseTypes[currentPointer];
				continue;
			}
			
			lastSeen = this.allPhraseTypes[currentPointer + 1];
			currentSeen = this.allPhraseTypes[currentPointer];
			currentPhrase = this.allPhrases.get(currentPointer);
			
			// Current seen is by, on, from, or to
			if (currentSeen.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
					|| currentSeen.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {
				
				// Keyword was not used as a keyword
				if (lastSeen.equals(ParserConstants.SWITCH_STRING_NAME)) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_NAME;
				}
				
				// In the event where we have returned to the by/on/from/to keyword
				// and we have not encountered a date but have seen an ambiguous datetime value
				// we give precedence to time
				if (currentPhrase.equals(ParserConstants.KEYWORD_BY)
						|| currentPhrase.equals(ParserConstants.KEYWORD_ON)
						|| currentPhrase.equals(ParserConstants.KEYWORD_FROM)
						|| currentPhrase.equals(ParserConstants.KEYWORD_TO)) {
					
					if (lastSeen.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)
							|| lastSeen.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)) {
						this.allPhraseTypes[currentPointer + 1] = ParserConstants.SWITCH_STRING_TIME;
					}
				}
			}
			
			// A whole bunch of Strings at the end, preceded by an at
			if (currentSeen.equals(ParserConstants.SWITCH_STRING_LOCATIONTIME)
					|| currentSeen.equals(ParserConstants.SWITCH_STRING_LOCATION)) {
				
				if (lastSeen.equals(ParserConstants.SWITCH_STRING_NAME)) {
					
					// Iteratively convert all "name" to "location";
					for (int shadowPointer = currentPointer + 1; shadowPointer < phraseCount; shadowPointer++) {
						
						if (this.allPhraseTypes[shadowPointer].equals(ParserConstants.SWITCH_STRING_NAME)) {
							this.allPhraseTypes[shadowPointer] = ParserConstants.SWITCH_STRING_LOCATION;
						} else {
							break;
						}
					}
				}
			}
			
			// Removes all redundant locationtime switchstrings
			if (currentSeen.equals(ParserConstants.SWITCH_STRING_LOCATIONTIME)) {
				
				if (lastSeen.equals(ParserConstants.SWITCH_STRING_LOCATION) 
						&& !currentPhrase.equals(ParserConstants.KEYWORD_AT)) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_LOCATION;
				}
				
				// Date time inversion
				// At keywords means that time must precede date
				if (lastSeen.equals(ParserConstants.SWITCH_STRING_DATE)) {
					this.allPhraseTypes[currentPointer] = ParserConstants.SWITCH_STRING_TIME;
				}
			}
		}
	}

	// This method is used when the by/on keyword was most frequently encountered,
	// and the program has not discerned whether it is a date or time yet
	private String markDateTime(String currentPhrase, String globalSwitchString) {

		String dateMark = markDate(currentPhrase, true);
		String timeMark = markTime(currentPhrase, true);

		if (dateMark.equals(ParserConstants.SWITCH_STRING_DATE)
				&& timeMark.equals(ParserConstants.SWITCH_STRING_TIME)) {
			
			// In the case of a raw number, try to guess that it is time if the value
			// is particularly small
			try {
				int guessTimeOrYear = Integer.parseInt(currentPhrase);
				int goodYearCutOff = 2000;
				
				if (guessTimeOrYear < goodYearCutOff) {
					return ParserConstants.SWITCH_STRING_TIME;
				} else {
					return globalSwitchString;
				}
			} catch (NumberFormatException e) {
				return globalSwitchString;
			}
			
		}

		if (dateMark.equals(ParserConstants.SWITCH_STRING_DATE)) {
			return ParserConstants.SWITCH_STRING_DATE;
		}

		if (timeMark.equals(ParserConstants.SWITCH_STRING_TIME)) {
			return ParserConstants.SWITCH_STRING_TIME;
		}
		
		if (dateMark.equals(ParserConstants.SWITCH_STRING_DATETIMEEND)
				&& globalSwitchString.equals(ParserConstants.SWITCH_STRING_DATETIMESTART)) {
			return ParserConstants.SWITCH_STRING_DATETIMEEND;
		}

		return ParserConstants.SWITCH_STRING_NAME;

	}

	// This method is used when the at keyword was most frequently encountered,
	// and the program has not discerned whether it is a location or time yet
	private String markLocationTime(String currentPhrase) {

		String locationMark = markLocation(currentPhrase);
		String timeMark = markTime(currentPhrase, true);

		if (locationMark.equals(ParserConstants.SWITCH_STRING_DATE)) {
			return ParserConstants.SWITCH_STRING_DATE;
		}

		if (timeMark.equals(ParserConstants.SWITCH_STRING_TIME)) {
			
			// Paranoid double-checking
			if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_AM)
					|| currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)
					|| currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON)
					|| currentPhrase.contains(ParserConstants.TIME_SEPARATOR_DOT)) {
				return ParserConstants.SWITCH_STRING_TIME;
			} else {
				return ParserConstants.SWITCH_STRING_LOCATIONTIME;
			}
		}

		return ParserConstants.SWITCH_STRING_LOCATION;
	}

	// This method is used when the last keyword found was definitely a date
	private String markDate(String currentPhrase, boolean firstCheck) {

		String defaultSwitchString = ParserConstants.SWITCH_STRING_NAME;

		if (firstCheck) {
			defaultSwitchString = markTime(currentPhrase, false);

			if (!defaultSwitchString.equals(ParserConstants.SWITCH_STRING_TIME)) {
				defaultSwitchString = ParserConstants.SWITCH_STRING_NAME;
			}
		}

		String keywordCheck = ParserConstants.decideNewSwitchString(currentPhrase);

		if (!keywordCheck.equals(ParserConstants.SWITCH_STRING_NAME)) {
			return keywordCheck;
		}
		
		if (ParserConstants.DAY_SET_UNMODIFIABLE.contains(currentPhrase)) {
			return ParserConstants.SWITCH_STRING_DATE;
		}

		if (currentPhrase.equals(ParserConstants.DATE_LONG_TOMORRROW)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TOMORROW)
				|| currentPhrase.equals(ParserConstants.DATE_LONG_TODAY)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TODAY)) {
			return ParserConstants.SWITCH_STRING_DATE;
		}
		
		if (currentPhrase.contains(ParserConstants.DATE_SEPARATOR_SLASH)
				|| currentPhrase.contains(ParserConstants.DATE_SEPARATOR_DASH)) {
			
			String[] temporaryDateHolder;
			
			if (currentPhrase.contains(ParserConstants.DATE_SEPARATOR_SLASH)) {
				temporaryDateHolder = currentPhrase.split(ParserConstants.DATE_SEPARATOR_SLASH);
			} else {
				temporaryDateHolder = currentPhrase.split(ParserConstants.DATE_SEPARATOR_DASH);
			}
			
			GregorianCalendar today = new GregorianCalendar();
			
			String potentialDay = temporaryDateHolder[0];
			String potentialMonth = Integer.toString(today.get(Calendar.MONTH) + 1);
			String potentialYear = Integer.toString(today.get(Calendar.YEAR));
			
			if (temporaryDateHolder.length > 1) {
				potentialMonth = temporaryDateHolder[1];
			}
			
			if (temporaryDateHolder.length > 2) {
				potentialYear = temporaryDateHolder[2];
			}
			
			try {
				int potentialDayNumeric = Integer.parseInt(potentialDay);
				int potentialMonthNumeric = Integer.parseInt(potentialMonth);
				Integer.parseInt(potentialYear);
				
				if (!ParserConstants.isValidDay(potentialDayNumeric)
						|| !ParserConstants.isValidMonth(potentialMonthNumeric)) {
					throw new NumberFormatException();
				}
				
				return ParserConstants.SWITCH_STRING_DATE;
			} catch (NumberFormatException e) {
				return ParserConstants.SWITCH_STRING_NAME;
			}
		}

		if (currentPhrase.endsWith(ParserConstants.DATE_SUFFIX_ST)
				|| currentPhrase.endsWith(ParserConstants.DATE_SUFFIX_ND)
				|| currentPhrase.endsWith(ParserConstants.DATE_SUFFIX_RD)
				|| currentPhrase.endsWith(ParserConstants.DATE_SUFFIX_TH)) {
			currentPhrase = currentPhrase.substring(0, currentPhrase.length() - 2);

			try {
				int potentialDay = Integer.parseInt(currentPhrase);
				
				if (!ParserConstants.isValidDay(potentialDay)) {
					throw new NumberFormatException();
				}
				
				return ParserConstants.SWITCH_STRING_DATE;
			} catch (NumberFormatException e) {
				return ParserConstants.SWITCH_STRING_NAME;
			}
		}

		if (ParserConstants.getMonthFromString(currentPhrase) > 0) {
			return ParserConstants.SWITCH_STRING_DATE;
		}

		// If it contains neither date suffix, nor time suffix, and is not a
		// month then it is either a number (day, year) or String
		try {
			Integer.parseInt(currentPhrase);
			return ParserConstants.SWITCH_STRING_DATE;
		} catch (NumberFormatException e) {
			return defaultSwitchString;
		}
	}

	// This method is used when the last keyword found was definitely a time
	private String markTime(String currentPhrase, boolean firstCheck) {

		String defaultSwitchString = ParserConstants.SWITCH_STRING_NAME;

		if (firstCheck) {
			defaultSwitchString = markDate(currentPhrase, false);

			if (!defaultSwitchString.equals(ParserConstants.SWITCH_STRING_DATE)) {
				defaultSwitchString = ParserConstants.SWITCH_STRING_NAME;
			}
		}

		String keywordCheck = ParserConstants.decideNewSwitchString(currentPhrase);

		if (!keywordCheck.equals(ParserConstants.SWITCH_STRING_NAME)) {
			return keywordCheck;
		}

		if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_AM)
				|| currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
			currentPhrase = currentPhrase.substring(0, currentPhrase.length() - 2);
		}

		String potentialHour;
		String potentialMinute;

		if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON)) {
			String[] potentialHourMinute = currentPhrase
					.split(ParserConstants.TIME_SEPARATOR_COLON);
			potentialHour = potentialHourMinute[0];
			potentialMinute = potentialHourMinute[1];
		} else if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_DOT)) {
			String[] potentialHourMinute = currentPhrase
					.split(ParserConstants.TIME_SEPARATOR_ESCAPED_DOT);
			potentialHour = potentialHourMinute[0];
			potentialMinute = potentialHourMinute[1];
		} else if (currentPhrase.length() > 2) {
			potentialHour = currentPhrase.substring(0, currentPhrase.length() - 2);
			potentialMinute = currentPhrase.substring(currentPhrase.length() - 2,
					currentPhrase.length());
		} else {

			// In this case, split will not split anything, if given a
			// separator
			// that the currentPhrase does not contain
			potentialHour = currentPhrase;
			potentialMinute = ParserConstants.DEFAULT_VALID_STRING_TIME;
		}

		try {

			int potentialHourNumeric = Integer.parseInt(potentialHour);
			int potentialMinuteNumeric = ParserConstants.DEFAULT_VALID_INT_TIME;

			if (!potentialMinute.equals(ParserConstants.DEFAULT_VALID_STRING_TIME)) {
				potentialMinuteNumeric = Integer.parseInt(potentialMinute);
			}

			if (!ParserConstants.isValidHour(potentialHourNumeric)
					|| !ParserConstants.isValidMinute(potentialMinuteNumeric)) {
				throw new NumberFormatException();
			}

			return ParserConstants.SWITCH_STRING_TIME;
		} catch (NumberFormatException e) {
			return defaultSwitchString;
		}

	}

	// This method name is a little deceptive
	// Checks to ensure that today and tomorrow (and their variants) are not
	// part
	// of the currentPhrase
	// Also checks if currentPhrase does not contain a keyword
	private String markLocation(String currentPhrase) {

		String keywordCheck = ParserConstants.decideNewSwitchString(currentPhrase);

		if (!keywordCheck.equals(ParserConstants.SWITCH_STRING_NAME)) {
			return keywordCheck;
		}

		if (currentPhrase.equals(ParserConstants.DATE_LONG_TODAY)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TODAY)
				|| currentPhrase.equals(ParserConstants.DATE_LONG_TOMORRROW)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TOMORROW)) {
			return ParserConstants.SWITCH_STRING_DATE;
		} else {
			return ParserConstants.SWITCH_STRING_LOCATION;
		}
	}

	public void setListPointer(int listPointer) {
		this.listPointer = listPointer;
	}

}
