package tasknote.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

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
	
	@Test
	public void parseAddNameOnly() {
		
		String userCommand = "add this task for me";
		
		TaskObject expectedResult = new TaskObject("this task for me");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithSingleDigitTime() {
		
		String userCommand = "add do CE2 by 5";
		
		TaskObject expectedResult = new TaskObject("do CE2");
		expectedResult.setDateHour(5);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithDoubleDigitTime() {
		
		String userCommand = "add do CE2 by 11";
		
		TaskObject expectedResult = new TaskObject("do CE2");
		expectedResult.setDateHour(11);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithTimeAndPM() {
		
		String userCommand = "add do CE2 by 5pm";
		
		TaskObject expectedResult = new TaskObject("do CE2");
		expectedResult.setDateHour(17);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithTimeAndAM() {
		
		String userCommand = "add do CE2 by 5am";
		
		TaskObject expectedResult = new TaskObject("do CE2");
		expectedResult.setDateHour(5);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWith24hrTime() {
		
		String userCommand = "add do CE2 by 1846";
		
		TaskObject expectedResult = new TaskObject("do CE2");
		expectedResult.setDateHour(18);
		expectedResult.setDateMinute(46);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithByNotTIme1Char() {
		
		String userCommand = "add do CE2 by a fluke";
		
		TaskObject expectedResult = 
				new TaskObject("do CE2 by a fluke");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithByNotTIme3Char() {
		
		String userCommand = "add do CE2 by the street";
		
		TaskObject expectedResult = new TaskObject("do CE2 by the street");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithByNotTIme7Char() {
		
		String userCommand = "add do CE2 by playing a prank";
		
		TaskObject expectedResult = 
				new TaskObject("do CE2 by playing a prank");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void parseAddWithMultipleBy() {
		
		String userCommand = "add do CE2 by by by by LOL";
		
		TaskObject expectedResult = 
				new TaskObject("do CE2 by by by by LOL");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
}
