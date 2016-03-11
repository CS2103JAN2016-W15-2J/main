package tasknote.parser;

public class ParsedWord {

	public enum WORDTYPE {
		KEYWORD, DATA
	};
	
	private WORDTYPE wordType;
	private String parsedWord;
	
	public ParsedWord(String parsedWord, WORDTYPE wordType) {
		this.wordType = wordType;
		this.parsedWord = parsedWord;
	}
}
