package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import tasknote.shared.COMMAND_TYPE;

public class ParserTest {
	
	@Test
	public void getCommandTypeBasic() {
		
		String userCommand = "add another task at 3pm";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void getCommandTypeLeadingSpaces() {
		
		String userCommand = "    add yet    another      task at        9pm";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void getCommandTypeTrailingSpaces() {
		
		String userCommand = "add special    commands      lol  at 10pm          ";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.ADD;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void getCommandTypeAllSpaces() {
		
		String userCommand = "     delete       thisverylongstuff             ";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.DELETE;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
	
	@Test
	public void getCommandTypeLeadingSpacesAllCases() {
		
		String userCommand = "    DeLEte stuff";
		
		COMMAND_TYPE expectedResult = COMMAND_TYPE.DELETE;
		
		assertEquals(Parser.getCommandType(userCommand), expectedResult);
	}
}
