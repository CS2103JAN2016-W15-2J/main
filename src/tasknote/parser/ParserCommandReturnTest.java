package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import tasknote.shared.COMMAND_TYPE;

public class ParserCommandReturnTest {

	@Test
	public void testReturnAddCommand() {
		
		String userCommand = "add some task";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnSearchCommand() {
		
		String userCommand = "search stuff";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.SEARCH;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnEditCommand() {
		
		String userCommand = "edit 3 morestuff";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.UPDATE;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnDeleteCommand() {
		
		String userCommand = "delete 1 2 3";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.DELETE;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnExitCommand() {
		
		String userCommand = "exit";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.EXIT;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnInvalidtCommand() {
		
		String userCommand = "sgnsdnsgioapgo";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.INVALID;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void testReturnInvalidAddCommand() {
		
		String userCommand = "add";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.INVALID;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
}
