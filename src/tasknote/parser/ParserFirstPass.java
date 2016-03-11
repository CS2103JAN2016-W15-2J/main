package tasknote.parser;

import java.util.ArrayList;

public class ParserFirstPass {

	private String command;
	private ArrayList<String> parsedCommandWords;
	
	public ParserFirstPass(String command) {
		this.command = command;
		this.parsedCommandWords = new ArrayList<String>();
		
		rebuildWordsWithQuotes();
	}
	
	private void rebuildWordsWithQuotes() {
		
		boolean findingEndQuote = false;
		int stringLength = command.length();
		
		StringBuilder phraseBuilder = new StringBuilder();
		
		for (int i = 0; i < stringLength; i++) {
			
			char currentChar = this.command.charAt(i);
			if (findingEndQuote) {
				
				if (currentChar == '"') {
					if (!phraseBuilder.toString().trim().equals("")) {
						this.parsedCommandWords.add(phraseBuilder.toString());
						phraseBuilder.delete(0, phraseBuilder.length());
					}
					findingEndQuote = !findingEndQuote;
				} else {
					phraseBuilder.append(currentChar);
				}
			} else {
				
				if (currentChar == '"') {
					
					if (!phraseBuilder.toString().trim().equals("")) {
						this.parsedCommandWords.add(phraseBuilder.toString());
						phraseBuilder.delete(0, phraseBuilder.length());
					}
					findingEndQuote = !findingEndQuote;
				} else if (currentChar == ' ') {
					
					if (phraseBuilder.toString().trim().equals("")) {
						continue;
					} else {
						this.parsedCommandWords.add(phraseBuilder.toString());
						phraseBuilder.delete(0, phraseBuilder.length());
					}
				} else {
					phraseBuilder.append(currentChar);
				}
			}
		}
		
		if (!phraseBuilder.toString().trim().equals("")) {
			this.parsedCommandWords.add(phraseBuilder.toString());
		}
	}
	
	public ArrayList<String> getFirstPassParsedResult() {
		return this.parsedCommandWords;
	}
}
