package tasknote.parser;

public class DateMessage {
	
	// Here are the constants that are specific to DateMessage
	private static final int DEFAULT_EXTRA_WORDS = 0;
	
	// Private fields
	private int day;
	private int month;
	private int year;
	private int extraWordsUsed;
	
	private String message;
	
	public DateMessage() {
		this.setDay(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
		this.setMonth(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
		this.setYear(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
		
		this.setExtraWordsUsed(DEFAULT_EXTRA_WORDS);
		
		this.setMessage(ParserConstants.MESSAGE_DATE_UNSURE);
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the extraWordsUsed
	 */
	public int getExtraWordsUsed() {
		return extraWordsUsed;
	}

	/**
	 * @param extraWordsUsed the extraWordsUsed to set
	 */
	public void setExtraWordsUsed(int extraWordsUsed) {
		this.extraWordsUsed = extraWordsUsed;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
