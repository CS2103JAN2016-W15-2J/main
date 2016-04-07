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
		return this.getAllPhrases().equals(UNINITIALIZED_ARRAYLIST) ||
				this.getCurrentPhrase().equals(UNITIALIZED_STRING) ||
				this.getListPointer() == UNINITIALIZED_INT ||
				this.getPhraseCount() == UNINITIALIZED_INT;
	}

	/**
	 * @return the allPhrases
	 */
	public ArrayList<String> getAllPhrases() {
		return allPhrases;
	}

	/**
	 * @param allPhrases the allPhrases to set
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
	 * @param listPointer the listPointer to set
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
	 * @param currentPhrase the currentPhrase to set
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
	 * @param phraseCount the phraseCount to set
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
	 * @param dateMessage the dateMessage to set
	 */
	public void setDateMessage(DateMessage dateMessage) {
		this.dateMessage = dateMessage;
	}
	
	
	public DateMessage tryToParseDate() {
		
		if (this.isNotReady()) {
			throw new RuntimeException("Need to set up DateParser first!");
		}
		
		DateMessage returnMessage = new DateMessage();
		
		ArrayList<String> allPhrases = this.getAllPhrases();
		String currentPhrase = this.getCurrentPhrase();
		int listPointer = this.getListPointer();
		int phraseCount = this.getPhraseCount();
		
		int extraWordsUsed = 0;
		

		if (currentPhrase.equals(ParserConstants.DATE_LONG_TODAY) ||
				currentPhrase.equals(ParserConstants.DATE_SHORT_TODAY)) {

			GregorianCalendar today = new GregorianCalendar();
			int todayDay = today.get(Calendar.DAY_OF_MONTH);
			int todayMonth = today.get(Calendar.MONTH) + 1;
			int todayYear = today.get(Calendar.YEAR);
			
			returnMessage.setDay(todayDay);
			returnMessage.setMonth(todayMonth);
			returnMessage.setYear(todayYear);
			returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

		} else if (currentPhrase.equals(ParserConstants.DATE_LONG_TOMORRROW) || 
				currentPhrase.equals(ParserConstants.DATE_SHORT_TOMORROW)) {

			GregorianCalendar tomorrow = new GregorianCalendar();
			tomorrow.roll(Calendar.DAY_OF_MONTH, 1);

			int tomorrowDay = tomorrow.get(Calendar.DAY_OF_MONTH);
			int tomorrowMonth = tomorrow.get(Calendar.MONTH) + 1;
			int tomorrowYear = tomorrow.get(Calendar.YEAR);

			if (tomorrowDay == 1) {
				tomorrow.roll(Calendar.MONTH, 1);
				tomorrowMonth = tomorrow.get(Calendar.MONTH) + 1;

				if (tomorrowMonth == 1) {
					tomorrow.roll(Calendar.YEAR, 1);
					tomorrowYear = tomorrow.get(Calendar.YEAR);
				}
			}

			returnMessage.setDay(tomorrowDay);
			returnMessage.setMonth(tomorrowMonth);
			returnMessage.setYear(tomorrowYear);
			returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

		} else if (currentPhrase.contains("/")) {
			String[] tempDayMonthYear = currentPhrase.split(ParserConstants.DATE_SEPARATOR_SLASH);
			
			int givenDay = Integer.parseInt(tempDayMonthYear[0]);
			int givenMonth = Integer.parseInt(tempDayMonthYear[1]);
			int givenYear = Integer.parseInt(tempDayMonthYear[2]);

			returnMessage.setDay(givenDay);
			returnMessage.setMonth(givenMonth);
			returnMessage.setYear(givenYear);
			returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);

		} else if (currentPhrase.contains("-")) {
			String[] tempDayMonthYear = currentPhrase.split(ParserConstants.DATE_SEPARATOR_DASH);
			
			int givenDay = Integer.parseInt(tempDayMonthYear[0]);
			int givenMonth = Integer.parseInt(tempDayMonthYear[1]);
			int givenYear = Integer.parseInt(tempDayMonthYear[2]);

			returnMessage.setDay(givenDay);
			returnMessage.setMonth(givenMonth);
			returnMessage.setYear(givenYear);
			returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);
		} else {

			String possibleDay = currentPhrase;

			// Trim stuff if required
			if (possibleDay.endsWith(ParserConstants.DATE_SUFFIX_ST) || 
					possibleDay.endsWith(ParserConstants.DATE_SUFFIX_ND)
					|| possibleDay.endsWith(ParserConstants.DATE_SUFFIX_RD) || 
					possibleDay.endsWith(ParserConstants.DATE_SUFFIX_TH)) {

				possibleDay = possibleDay
						.substring(0, possibleDay.length() - 2);

				returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);
			}


			boolean foundMonth = false;
			String possibleMonth = Constants.STRING_CONSTANT_EMPTY;

			if (listPointer + 1 < phraseCount) {
				possibleMonth = allPhrases.get(listPointer + 1).toLowerCase();
			}
			
			int monthValue = ParserConstants.getMonthFromString(possibleMonth);
			
			if (ParserConstants.isValidMonth(monthValue)) {
				foundMonth = true;
				extraWordsUsed++;
				
				returnMessage.setMonth(monthValue);
				returnMessage.setMessage(ParserConstants.MESSAGE_DATE_SURE);
			}


			if (!foundMonth) {
				GregorianCalendar today = new GregorianCalendar();
				
				returnMessage.setMonth(today.get(Calendar.MONTH) + 1);
			}

			String possibleYear = Constants.STRING_CONSTANT_EMPTY;

			if (foundMonth && (listPointer + 2) < phraseCount) {
				possibleYear = allPhrases.get(listPointer + 2);
			}

			try {
				int numericYear = Integer.parseInt(possibleYear);

				if (numericYear > 1900 && numericYear < 2100) {
					returnMessage.setYear(numericYear);
					extraWordsUsed++;
				} else {
					throw new NumberFormatException();
				}
			} catch (NumberFormatException e) {
				GregorianCalendar today = new GregorianCalendar();
				returnMessage.setYear(today.get(Calendar.YEAR));
			}
		}

		returnMessage.setExtraWordsUsed(extraWordsUsed);

		return returnMessage;
	}



}
