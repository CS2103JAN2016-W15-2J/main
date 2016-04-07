package tasknote.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import tasknote.shared.Constants;

public class DateParser {

	// Static constants that are applicable for DateParser
	private static final ArrayList<String> UNINITIALIZED_ARRAYLIST = null;
	private static final String UNITIALIZED_STRING = null;
	private static final DateMessage UNINITIALIZED_DATEMESSAGE = null;
	private static final int UNINITIALIZED_INT = -1;

	// Private fields
	private ArrayList<String> allPhrases;
	private int listPointer;

	private String currentPhrase;
	private int phraseCount;

	private DateMessage dateMessage;

	public DateParser() {
		this.setAllPhrases(UNINITIALIZED_ARRAYLIST);
		this.setListPointer(UNINITIALIZED_INT);

		this.setCurrentPhrase(UNITIALIZED_STRING);
		this.setPhraseCount(UNINITIALIZED_INT);

		this.setDateMessage(UNINITIALIZED_DATEMESSAGE);
	}

	public boolean isNotReady() {
		return this.getAllPhrases().equals(UNINITIALIZED_ARRAYLIST)
				|| this.getCurrentPhrase().equals(UNITIALIZED_STRING)
				|| this.getListPointer() == UNINITIALIZED_INT
				|| this.getPhraseCount() == UNINITIALIZED_INT;
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
	 * @return the currentPhrase
	 */
	public String getCurrentPhrase() {
		return currentPhrase;
	}

	/**
	 * @param currentPhrase
	 *            the currentPhrase to set
	 */
	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}

	/**
	 * @return the phraseCount
	 */
	public int getPhraseCount() {
		return phraseCount;
	}

	/**
	 * @param phraseCount
	 *            the phraseCount to set
	 */
	public void setPhraseCount(int phraseCount) {
		this.phraseCount = phraseCount;
	}

	/**
	 * @return the dateMessage
	 */
	public DateMessage getDateMessage() {
		return dateMessage;
	}

	/**
	 * @param dateMessage
	 *            the dateMessage to set
	 */
	public void setDateMessage(DateMessage dateMessage) {
		this.dateMessage = dateMessage;
	}

	public DateMessage tryToParseDate() {

		if (this.isNotReady()) {
			throw new RuntimeException("Need to set up DateParser first!");
		}

		// Begin trying to parse as date, through the chain of responsibilities
		DateMessage returnMessage = new DateMessage();
		returnMessage = tryToParseAsToday(returnMessage);

		return returnMessage;
	}

	// Chain of responsibility => (start) => Today => Tomorrow
	private DateMessage tryToParseAsToday(DateMessage passedMessage) {

		String currentPhrase = this.getCurrentPhrase();

		if (currentPhrase.equals(ParserConstants.DATE_LONG_TODAY)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TODAY)) {

			// If the word found is "today" or "tdy", then parse as today's date
			GregorianCalendar today = new GregorianCalendar();
			int todayDay = today.get(Calendar.DAY_OF_MONTH);
			int todayMonth = today.get(Calendar.MONTH) + 1;
			int todayYear = today.get(Calendar.YEAR);

			passedMessage.setDay(todayDay);
			passedMessage.setMonth(todayMonth);
			passedMessage.setYear(todayYear);
			passedMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

			return passedMessage;
		} else {

			return tryToParseAsTomorrow(passedMessage);
		}
	}

	// Chain of responsibility => Today => Tomorrow => SlashDot
	private DateMessage tryToParseAsTomorrow(DateMessage passedMessage) {

		String currentPhrase = this.getCurrentPhrase();

		if (currentPhrase.equals(ParserConstants.DATE_LONG_TOMORRROW)
				|| currentPhrase.equals(ParserConstants.DATE_SHORT_TOMORROW)) {

			// If the word found is "tomorrow" or "tmr", then parse as
			// tomorrow's date
			// Note: Rolling of Calendar is necessary
			GregorianCalendar tomorrow = new GregorianCalendar();
			tomorrow = this.rollByDays(tomorrow, 1);

			passedMessage.setDay(tomorrow.get(Calendar.DAY_OF_MONTH));
			passedMessage.setMonth(tomorrow.get(Calendar.MONTH));
			passedMessage.setYear(tomorrow.get(Calendar.YEAR));
			passedMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

			return passedMessage;
		} else {

			return tryToParseAsSlashDot(passedMessage);
		}
	}

	// Chain of responsibility => Tomorrow => SlashDot => Written
	private DateMessage tryToParseAsSlashDot(DateMessage passedMessage) {

		String currentPhrase = this.getCurrentPhrase();

		int givenDay = ParserConstants.DEFAULT_INVALID_INT_DATETIME;
		int givenMonth = ParserConstants.DEFAULT_INVALID_INT_DATETIME;
		int givenYear = ParserConstants.DEFAULT_INVALID_INT_DATETIME;

		String[] temporaryDateHolder;

		if (currentPhrase.contains(ParserConstants.DATE_SEPARATOR_SLASH)) {
			temporaryDateHolder = currentPhrase
					.split(ParserConstants.DATE_SEPARATOR_SLASH);
		} else if (currentPhrase.contains(ParserConstants.DATE_SEPARATOR_DASH)) {
			temporaryDateHolder = currentPhrase
					.split(ParserConstants.DATE_SEPARATOR_DASH);
		} else {
			return tryToParseAsWritten(passedMessage);
		}

		try {
			givenDay = Integer.parseInt(temporaryDateHolder[0]);
			givenMonth = Integer.parseInt(temporaryDateHolder[1]);
			givenYear = Integer.parseInt(temporaryDateHolder[2]);

			passedMessage.setDay(givenDay);
			passedMessage.setMonth(givenMonth);
			passedMessage.setYear(givenYear);
			passedMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

			return passedMessage;
		} catch (NumberFormatException e) {

			return tryToParseAsWritten(passedMessage);
		}
	}

	// Chain of responsibility => SlashDot => Written => (end)
	private DateMessage tryToParseAsWritten(DateMessage passedMessage) {

		ArrayList<String> allPhrases = this.getAllPhrases();
		String currentPhrase = this.getCurrentPhrase();
		int listPointer = this.getListPointer();
		int phraseCount = this.getPhraseCount();

		String possibleDay = currentPhrase;
		int extraWordsUsed = 0;

		// Try to process day first
		// If not day, we are done
		boolean hasDayValue = tryToProcessDay(passedMessage, possibleDay);

		if (!hasDayValue) {
			return passedMessage;
		}
		
		// We advance the listPointer to get the next word
		// So that we can try to process the month, if given
		listPointer = listPointer + 1;

		String possibleMonth = Constants.STRING_CONSTANT_EMPTY;

		// After day of month, is there another word after this
		// to specify the month
		if (listPointer < phraseCount) {
			possibleMonth = allPhrases.get(listPointer).toLowerCase();
		}

		// Try to process month next
		// If not month, we fill it with today's month
		boolean hasMonthValue = tryToProcessMonth(passedMessage, possibleMonth);

		String possibleYear = Constants.STRING_CONSTANT_EMPTY;
		extraWordsUsed = passedMessage.getExtraWordsUsed();
		
		// We advance the listPointer once more to get the next word
		// So that we can try to process the year, if given
		listPointer = listPointer + 1;

		// Looking for year only makes sense if the month
		if (hasMonthValue && listPointer < phraseCount) {
			possibleYear = allPhrases.get(listPointer);
		}

		try {
			int numericYear = Integer.parseInt(possibleYear);

			passedMessage.setYear(numericYear);
			extraWordsUsed++;
		} catch (NumberFormatException e) {
			GregorianCalendar today = new GregorianCalendar();
			passedMessage.setYear(today.get(Calendar.YEAR));
		}

		passedMessage.setExtraWordsUsed(extraWordsUsed);

		return passedMessage;
	}

	private boolean tryToProcessDay(DateMessage passedMessage,
			String possibleDay) {

		// Trim stuff if required
		if (possibleDay.endsWith(ParserConstants.DATE_SUFFIX_ST)
				|| possibleDay.endsWith(ParserConstants.DATE_SUFFIX_ND)
				|| possibleDay.endsWith(ParserConstants.DATE_SUFFIX_RD)
				|| possibleDay.endsWith(ParserConstants.DATE_SUFFIX_TH)) {

			possibleDay = possibleDay.substring(0, possibleDay.length() - 2);
		}

		try {
			int numericDay = Integer.parseInt(possibleDay);
			passedMessage.setDay(numericDay);
			passedMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean tryToProcessMonth(DateMessage passedMessage, String possibleMonth) {
		
		int extraWordsUsed = passedMessage.getExtraWordsUsed();
		
		int monthValue = ParserConstants.getMonthFromString(possibleMonth);

		if (ParserConstants.isValidMonth(monthValue)) {
			extraWordsUsed++;

			// If we found a word after the number that is a valid month
			// then we know that this must be a date value
			passedMessage.setMonth(monthValue);
			passedMessage.setExtraWordsUsed(extraWordsUsed);
			passedMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);
		} else {
			GregorianCalendar today = new GregorianCalendar();
			passedMessage.setMonth(today.get(Calendar.MONTH) + 1);
		}
		
		return true;
	}

	private GregorianCalendar rollByDays(GregorianCalendar initialCalendar,
			int numberOfDaysToRoll) {

		// Maximum values for current calendar
		int numberOfDaysThisYear = initialCalendar
				.getActualMaximum(Calendar.DAY_OF_YEAR);

		// Roll years first
		while (numberOfDaysToRoll >= numberOfDaysThisYear) {
			numberOfDaysToRoll = numberOfDaysToRoll - numberOfDaysThisYear;
			initialCalendar.roll(Calendar.YEAR, 1);

			numberOfDaysThisYear = initialCalendar
					.getActualMaximum(Calendar.DAY_OF_YEAR);
		}

		int numberOfDaysThisMonth = initialCalendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);

		while (numberOfDaysToRoll >= numberOfDaysThisMonth) {
			numberOfDaysToRoll = numberOfDaysToRoll - numberOfDaysThisMonth;
			initialCalendar.roll(Calendar.MONTH, 1);

			// Check if month roll resulted in year roll
			if (initialCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {
				initialCalendar.roll(Calendar.YEAR, 1);
			}

			numberOfDaysThisMonth = initialCalendar
					.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		while (numberOfDaysToRoll > 0) {
			numberOfDaysToRoll = numberOfDaysToRoll - 1;
			initialCalendar.roll(Calendar.DAY_OF_MONTH, 1);

			if (initialCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
				initialCalendar.roll(Calendar.MONTH, 1);

				if (initialCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {
					initialCalendar.roll(Calendar.YEAR, 1);
				}
			}
		}

		return initialCalendar;
	}

}
