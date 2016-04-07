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

	/**
	 * @return the currentPhrase
	 */
	public String getCurrentPhrase() {
		return currentPhrase;
	}

	/**
	 * @param currentPhrase the currentPhrase to set
	 */
	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}

}
