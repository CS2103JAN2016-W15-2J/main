package tasknote.parser;

public class TimeMessage {
	
	// These are the constants that are applicable for TimeMessage
	private static final int DEFAULT_EXTRA_HOURS = 0;
	
	// Private fields
	private int hour;
	private int minute;
	private int extraHours;
	
	private String message;
	
	public TimeMessage() {
		this.setHour(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
		this.setMinute(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
		
		this.setExtraHours(DEFAULT_EXTRA_HOURS);
		
		this.setMessage(ParserConstants.MESSAGE_TIME_UNSURE);
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * @return the extraHours
	 */
	public int getExtraHours() {
		return extraHours;
	}

	/**
	 * @param extraHours the extraHours to set
	 */
	public void setExtraHours(int extraHours) {
		this.extraHours = extraHours;
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
