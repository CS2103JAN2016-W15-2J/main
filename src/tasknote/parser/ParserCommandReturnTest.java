//@@author A0129529W
package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import tasknote.shared.CommandType;

public class ParserCommandReturnTest {
	
	Parser testParser = new Parser();

	@Test
	public void testReturnAddCommand() {
		
		String userCommand = "add some task";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.ADD;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnAddCommandCaseInsensitive() {
		
		String userCommand = "AdD sOmE TaSK";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.ADD;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnSearchCommand() {
		
		String userCommand = "search stuff";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.SEARCH;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnEditCommand() {
		
		String userCommand = "edit 3 morestuff";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.UPDATE;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnDeleteCommand() {
		
		String userCommand = "delete 1 2 3";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.DELETE;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnExitCommand() {
		
		String userCommand = "exit";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.EXIT;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnInvalidtCommand() {
		
		String userCommand = "sgnsdnsgioapgo";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.INVALID;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReturnInvalidAddCommand() {
		
		// Add command requires at least one other parameter to act
		// as the new task name
		String userCommand = "add";
		this.testParser.setInputString(userCommand);
		
		CommandType expectedResult = CommandType.INVALID;
		CommandType actualResult = this.testParser.getCommandType();
		
		assertEquals(expectedResult, actualResult);
	}
}
