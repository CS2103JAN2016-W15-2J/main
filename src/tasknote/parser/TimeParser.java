//@@author A0129529W
package tasknote.parser;

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

	//@@author A0129529W-generated
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

	//@@author A0129529W
	public TimeMessage tryToParseTime() {

		if (this.isNotReady()) {
			throw new RuntimeException("Need to set up TimeParser first!");
		}

		TimeMessage returnMessage = new TimeMessage();
		int givenHour = ParserConstants.DEFAULT_INVALID_INT_DATETIME;
		int givenMinute = ParserConstants.DEFAULT_INVALID_INT_DATETIME;
		boolean hadAMPM = false;

		String currentPhrase = this.getCurrentPhrase();
		int extraHours = 0;

		// Check if pm or am exists at the end of the currentPhrase first
		if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
			extraHours = 12;
		}

		if (currentPhrase.endsWith(ParserConstants.HOUR_MOD_AM)
				|| currentPhrase.endsWith(ParserConstants.HOUR_MOD_PM)) {
			hadAMPM = true;
			currentPhrase = currentPhrase.substring(0,
					currentPhrase.length() - 2);
			returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
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
				givenHour = Integer.parseInt(tempHourMinute[0]) + extraHours;
				givenMinute = Integer.parseInt(tempHourMinute[1]);

				returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
			} catch (NumberFormatException e) {
				return returnMessage;
			}
		} else {
			
			if (currentPhrase.length() >= 3) {

				String hourString = currentPhrase.substring(0,
						currentPhrase.length() - 2);
				String minuteString = currentPhrase.substring(
						currentPhrase.length() - 2, currentPhrase.length());

				try {
					givenHour = Integer.parseInt(hourString) + extraHours;
					givenMinute = Integer.parseInt(minuteString);

					returnMessage.setMessage(ParserConstants.MESSAGE_TIME_SURE);
				} catch (NumberFormatException e) {
					return returnMessage;
				}
			}

			if (currentPhrase.length() < 3) {

				try {
					givenHour = Integer.parseInt(currentPhrase) + extraHours;
					givenMinute = 0;

				} catch (NumberFormatException e) {
					return returnMessage;

				}
			}

		}

		// This check inverts 12pm (which becomes 12 + 12 = 24)
		// back into the normal 12 value, and 12am (which becomes 12 + 0 = 12)
		// back into the normal 0 value
		if (givenHour == 24 && hadAMPM) {
			givenHour = 12;
		} else if (givenHour == 12 && hadAMPM) {
			givenHour = 0;
		}

		if (!ParserConstants.isValidMinute(givenMinute)
				|| !ParserConstants.isValidHour(givenHour)) {
			returnMessage.setHour(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
			returnMessage
					.setMinute(ParserConstants.DEFAULT_INVALID_INT_DATETIME);
			returnMessage.setMessage(ParserConstants.MESSAGE_TIME_UNSURE);
		} else {
			returnMessage.setHour(givenHour);
			returnMessage.setMinute(givenMinute);
		}

		return returnMessage;

	}

}
