//@@author A0129529W
package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import tasknote.shared.COMMAND_TYPE;

public class ParserCommandReturnTest {
	
	Parser testParser = new Parser();

	@Test
	public void testReturnAddCommand() {
		
		String userCommand = "add some task";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnAddCommandCaseInsensitive() {
		
		String userCommand = "AdD sOmE TaSK";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnSearchCommand() {
		
		String userCommand = "search stuff";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.SEARCH;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnEditCommand() {
		
		String userCommand = "edit 3 morestuff";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.UPDATE;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnDeleteCommand() {
		
		String userCommand = "delete 1 2 3";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.DELETE;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnExitCommand() {
		
		String userCommand = "exit";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.EXIT;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnInvalidtCommand() {
		
		String userCommand = "sgnsdnsgioapgo";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.INVALID;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnInvalidAddCommand() {
		
		// Add command requires at least one other parameter to act
		// as the new task name
		String userCommand = "add";
		this.testParser.setInputString(userCommand);
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.INVALID;
		COMMAND_TYPE actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
}
