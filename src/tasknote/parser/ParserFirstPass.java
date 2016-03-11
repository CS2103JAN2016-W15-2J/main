package tasknote.parser;

import java.util.ArrayList;

public class ParserFirstPass {

	private String[] commandWordsBuffer;
	private ArrayList<String> parsedCommandWords;
	
	public ParserFirstPass(String command) {
		this.commandWordsBuffer = command.split(" ");
	}
	
	public void rebuildWordsWithQuotes() {
		
		
	}
}
