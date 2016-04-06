package tasknote.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;

// Utility class
// This class does no processing, only returns constants
public class ParserConstants {
	
	// Here are the valid commands accepted by
	// the program
	protected static final String COMMAND_ADD = "add";
	protected static final String COMMAND_EDIT = "edit";
	protected static final String COMMAND_DONE = "done";
	protected static final String COMMAND_DELETE = "delete";
	protected static final String COMMAND_UNDO = "undo";
	protected static final String COMMAND_REDO = "redo";
	protected static final String COMMAND_EXIT = "exit";
	protected static final String COMMAND_SEARCH = "search";
	protected static final String COMMAND_SHOW = "show";
	protected static final String COMMAND_RELOCATE = "relocate";
	protected static final String COMMAND_HELP = "help";
	protected static final String COMMAND_UNDONE = "undone";

	// Here are the valid keywords accepted by
	// the program
	protected static final String KEYWORD_BY = "by";
	protected static final String KEYWORD_AT = "at";
	protected static final String KEYWORD_ON = "on";
	protected static final String KEYWORD_FROM = "from";
	protected static final String KEYWORD_TO = "to";
	protected static final String KEYWORD_IN = "in";
	protected static final String KEYWORD_NOTIFY = "notify";
	protected static final String KEYWORD_REMOVE = "remove";
	
	// Here are the valid month words accepted by Parser
	protected static final String MONTH_LONG_JAN = "january";
	protected static final String MONTH_LONG_FEB = "february";
	protected static final String MONTH_LONG_MAR = "march";
	protected static final String MONTH_LONG_APR = "april";
	protected static final String MONTH_LONG_MAY = "may";
	protected static final String MONTH_LONG_JUN = "june";
	protected static final String MONTH_LONG_JUL = "july";
	protected static final String MONTH_LONG_AUG = "august";
	protected static final String MONTH_LONG_SEP = "september";
	protected static final String MONTH_LONG_OCT = "october";
	protected static final String MONTH_LONG_NOV = "november";
	protected static final String MONTH_LONG_DEC = "december";
	
	protected static final String MONTH_SHORT_JAN = "jan";
	protected static final String MONTH_SHORT_FEB = "feb";
	protected static final String MONTH_SHORT_MAR = "mar";
	protected static final String MONTH_SHORT_APR = "apr";
	protected static final String MONTH_SHORT_MAY = "may";
	protected static final String MONTH_SHORT_JUN = "jun";
	protected static final String MONTH_SHORT_JUL = "jul";
	protected static final String MONTH_SHORT_AUG = "aug";
	protected static final String MONTH_SHORT_SEP = "sep";
	protected static final String MONTH_SHORT_OCT = "oct";
	protected static final String MONTH_SHORT_NOV = "nov";
	protected static final String MONTH_SHORT_DEC = "dec";
	
	protected static final String[] MONTHS_ALL = {MONTH_LONG_JAN, MONTH_LONG_FEB, 
		MONTH_LONG_MAR, MONTH_LONG_APR, MONTH_LONG_MAY, MONTH_LONG_JUN, MONTH_LONG_JUL,
		MONTH_LONG_AUG, MONTH_LONG_SEP, MONTH_LONG_OCT, MONTH_LONG_NOV, MONTH_LONG_DEC,
		MONTH_SHORT_JAN, MONTH_SHORT_FEB, MONTH_SHORT_MAR, MONTH_SHORT_APR, 
		MONTH_SHORT_MAY, MONTH_SHORT_JUN, MONTH_SHORT_JUL, MONTH_SHORT_AUG,
		MONTH_SHORT_SEP, MONTH_SHORT_OCT, MONTH_SHORT_NOV, MONTH_SHORT_DEC};
	
	protected static final HashSet<String> MONTH_SET = new HashSet<>(Arrays.asList(MONTHS_ALL));
	protected static final Set<String> MONTH_SET_UNMODIFIABLE = 
			Collections.unmodifiableSet(MONTH_SET);
	
	// Here are the int constants for date and time
	protected static final int MONTH_MIN = 1;
	protected static final int MONTH_MAX = 12;
	
	protected static final int DAY_MIN = 1;
	protected static final int DAY_MAX = 31;
	
	protected static final int HOUR_MIN = 0;
	protected static final int HOUR_MAX = 23;
	
	protected static final int MINUTE_MIN = 0;
	protected static final int MINUTE_MAX = 59;
	
	
	// Here are all the minimum length of the expected user command
	protected static final int MINIMUM_ADD = 2;
	protected static final int MINIMUM_EDIT = 3;
	protected static final int MINIMUM_DONE = 2;
	protected static final int MINIMUM_DELETE = 2;
	protected static final int MINIMUM_UNDO = 1;
	protected static final int MINIMUM_REDO = 1;
	protected static final int MINIMUM_EXIT = 1;
	protected static final int MINIMUM_SEARCH = 2;
	protected static final int MINIMUM_SHOW = 2;
	protected static final int MINIMUM_RELOCATE = 2;
	protected static final int MINIMUM_HELP = 1;
	protected static final int MINIMUM_UNDONE = 2;
	protected static final int MINIMUM_INVALID = 0;

	protected static int getMinimumCommandLength(COMMAND_TYPE command) {
		
		int returnValue = MINIMUM_INVALID;
		
		switch (command) {
			case ADD:
				returnValue = MINIMUM_ADD;
				break;
			case UPDATE:
				returnValue = MINIMUM_EDIT;
				break;
			case DONE:
				returnValue = MINIMUM_DONE;
				break;
			case DELETE:
				returnValue = MINIMUM_DELETE;
				break;
			case UNDO:
				returnValue = MINIMUM_UNDO;
				break;
			case REDO:
				returnValue = MINIMUM_REDO;
				break;
			case EXIT:
				returnValue = MINIMUM_EXIT;
				break;
			case SEARCH:
				returnValue = MINIMUM_SEARCH;
				break;
			case SHOW:
				returnValue = MINIMUM_SHOW;
				break;
			case CHANGE_FILE_PATH:
				returnValue = MINIMUM_RELOCATE;
				break;
			case HELP:
				returnValue = MINIMUM_HELP;
				break;
			case UNDONE:
				returnValue = MINIMUM_UNDONE;
				break;
				
			// default includes the case for which COMMAND_TYPE is INVALID
			default:
				returnValue = MINIMUM_INVALID;
				break;
		}
		
		return returnValue;
	}
	
	protected static int getMonthFromString(String lowercaseWord) {
		
		int returnValue = Constants.INVALID_VALUE_CONSTANT;
		
		// Do check only if it is a valid month
		if (MONTH_SET_UNMODIFIABLE.contains(lowercaseWord)) {
			
			for (int i = 0; i < MONTHS_ALL.length; i++) {
				if (lowercaseWord.equals(MONTHS_ALL[i])) {
					
					// Case 1: Match MONTH_LONG => i from 0 to 11
					// Case 2: Match MONTH_SHORT => i from 12 to 23 
					// => (Modulo 12) from 0 to 11
					// Need to add 1 to make month value from 1 to 12
					returnValue = (i % MONTH_MAX) + 1;
					break;
				}
			}
		}
		
		return returnValue;
	}
	
	protected static boolean isValidMonth(int monthValue) {
		return (monthValue >= MONTH_MIN) && (monthValue <= MONTH_MAX);
	}
	
	protected static boolean isValidDay(int dayValue) {
		return (dayValue >= DAY_MIN) && (dayValue <= DAY_MAX);
	}
	
	protected static boolean isValidHour(int hourValue) {
		return (hourValue >= HOUR_MIN) && (hourValue <= HOUR_MAX);
	}
	
	protected static boolean isValidMinute(int minuteValue) {
		return (minuteValue >= MINUTE_MIN) && (minuteValue <= MINUTE_MAX);
	}
}
