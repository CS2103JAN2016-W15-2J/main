package tasknote.parser;

import tasknote.shared.Constants;

public class TimeParser {

	// Here are the constants that are specific to TimeParser
	private static final String UNINITIALIZED_STRING = null;

	// Private fields
	private String currentPhrase;

	public TimeParser() {
		this.setCurrentPhrase(UNINITIALIZED_STRING);
	}

	public boolean isNotReady() {
		return this.getCurrentPhrase().equals(UNINITIALIZED_STRING);
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

	public TimeMessage tryToParseTime() {

		if (this.isNotReady()) {
			throw new RuntimeException("Need to set up TimeParser first!");
		}

		TimeMessage returnMessage = new TimeMessage();

		String currentPhrase = this.getCurrentPhrase();
		int extraHours = 0;

		// Check if pm or am exists at the end of the currentPhrase first
		if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
			extraHours = 12;
		}

		if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_AM)
				|| currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
			currentPhrase = currentPhrase.substring(0,
					currentPhrase.length() - 2);
		}

		// Try as colon separated or dash separated
		if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON)
				|| currentPhrase.contains(ParserConstants.TIME_SEPARATOR_DOT)) {

			String[] tempHourMinute;

			if (currentPhrase.contains(ParserConstants.TIME_SEPARATOR_COLON)) {
				tempHourMinute = currentPhrase
						.split(ParserConstants.TIME_SEPARATOR_COLON);
			} else {
				tempHourMinute = currentPhrase
						.split(ParserConstants.TIME_SEPARATOR_ESCAPED_DOT);
			}

			try {
				int givenHour = Integer.parseInt(tempHourMinute[0])
						+ extraHours;
				int givenMinute = Integer.parseInt(tempHourMinute[1]);

				returnMessage.setHour(givenHour);
				returnMessage.setMinute(givenMinute);
				returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
			} catch (NumberFormatException e) {
				return returnMessage;
			}
		}

		if (currentPhrase.length() >= 3) {

			String hourString = currentPhrase.substring(0,
					currentPhrase.length() - 2);
			String minuteString = currentPhrase.substring(
					currentPhrase.length() - 2, currentPhrase.length());

			try {
				int givenHour = Integer.parseInt(hourString) + extraHours;
				int givenMinute = Integer.parseInt(minuteString);

				returnMessage.setHour(givenHour);
				returnMessage.setMinute(givenMinute);
				returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
			} catch (NumberFormatException e) {
				return returnMessage;
			}
		}

		if (currentPhrase.length() < 3) {

			try {
				int givenHour = Integer.parseInt(currentPhrase) + extraHours;
				int givenMinute = 0;

				returnMessage.setHour(givenHour);
				returnMessage.setMinute(givenMinute);
				returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
			} catch (NumberFormatException e) {
				return returnMessage;

			}
		}
		
		return returnMessage;
		
		
	}

}
