package tasknote.parser;

public class KeywordValidator {

	private String keyword;
	private String parameter;
	
	private String rawKeyphrase;
	
	private final String KEYWORD_AT = "at";
	private final String KEYWORD_BY = "by";
	private final String KEYWORD_ON = "on";	
	
	public KeywordValidator(String rawKeyphrase) {
		this.rawKeyphrase = rawKeyphrase;
	}
	
	
}
