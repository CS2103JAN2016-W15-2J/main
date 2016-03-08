package tasknote.parser;

public class ActionExtractor {
	
	// Here are the valid commands accepted by
	// the program
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_RENAME = "rename";
	private static final String COMMAND_DONE = "done";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_INVALID = "invalid";
	
	private static final String[] COMMANDS = {COMMAND_ADD, COMMAND_EDIT, COMMAND_RENAME,
											  COMMAND_DONE, COMMAND_DELETE, COMMAND_UNDO,
											  COMMAND_SEARCH};
	
	// Here are the valid keywords accepted by
	// the program
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_AT = "at";
	private static final String KEYWORD_ON = "on";
	private static final String KEYWORD_FROM = "from";
	private static final String KEYWORD_TO = "to";
	
	private static final String[] KEYWORDS = {KEYWORD_BY, KEYWORD_AT, KEYWORD_ON,
											  KEYWORD_FROM, KEYWORD_TO};
	
	// Here are the regex that are used by the
	// parser for parsing all user commands
	private static final String REGEX_WHITESPACE = " ";
	
	private String rawUserCommand;
	private StringBuilder rawUserCommandBuilder;
	
	private String action;
	private String actionArguments;
	
	private String remainingString;
	

	public ActionExtractor(String rawUserCommand) {
		this.rawUserCommand = rawUserCommand.trim();
		this.rawUserCommandBuilder = new StringBuilder(this.rawUserCommand);
	}
	
	private String determineAction() {
		
		int numberOfValidCommands = ActionExtractor.COMMANDS.length;
		boolean hasFoundCommand = false;
		
		for (int i = 0; i < numberOfValidCommands && !hasFoundCommand; i++) {
			if (this.rawUserCommand.startsWith(ActionExtractor.COMMANDS[i])) {
				this.action = ActionExtractor.COMMANDS[i];
				hasFoundCommand = true;
			}
		}
		
		if (!hasFoundCommand) {
			this.action = ActionExtractor.COMMAND_INVALID;
		}
		
		return this.action;
	}
	
	private String removeAction() {
		
		if (this.rawUserCommand.startsWith(this.action)) {
			
			int lengthOfCommand = this.action.length();
			int lengthOfRawCommand = this.rawUserCommand.length();
			
			this.rawUserCommand = this.rawUserCommand.substring(lengthOfCommand, 
																lengthOfRawCommand);
			
			this.rawUserCommandBuilder.subSequence(lengthOfCommand, lengthOfRawCommand);
		}
		
		return this.rawUserCommand;
	}
	
	private String getEarliestKeyword() {
		
		String[] splitUserCommand = this.rawUserCommand.split(REGEX_WHITESPACE);
		int numberOfWordsInCommand = splitUserCommand.length;
		int numberOfKeywords = ActionExtractor.KEYWORDS.length;
		
		boolean foundKeyword = false;
		String keyword = "";
		
		for (int i = 0; i < numberOfWordsInCommand && !foundKeyword; i++) {
			
			for (int j = 0; j < numberOfKeywords && !foundKeyword; j++) {
				
				if (splitUserCommand[i].equalsIgnoreCase(ActionExtractor.KEYWORDS[j])) {
					keyword = ActionExtractor.KEYWORDS[j];
					foundKeyword = true;
				}
			}
		}
		
		return keyword;
	}
	
	private String cutCommandsAtKeyword(String keyword) {
		
		
		return "";
	}
}

