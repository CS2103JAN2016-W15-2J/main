//@@author A0129529W
package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class ParserFirstPassTest {

	@Test
	public void basicWordTest() {

		String userCommand = "add task1 by 5pm";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("add");
		expected.add("task1");
		expected.add("by");
		expected.add("5pm");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}

	@Test
	public void testWithExtraSpaces() {

		String userCommand = "  delete   task   object by 9       ";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("delete");
		expected.add("task");
		expected.add("object");
		expected.add("by");
		expected.add("9");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}
	
	@Test
	public void basicTestWithQuotes() {

		String userCommand = "add \"another task for tomorrow\" by 5pm";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("add");
		expected.add("another task for tomorrow");
		expected.add("by");
		expected.add("5pm");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}
	
	@Test
	public void testQuotesWithSpaces() {

		String userCommand = "add \"  another    task     again for tomorrow   \" by 5pm";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("add");
		expected.add("  another    task     again for tomorrow   ");
		expected.add("by");
		expected.add("5pm");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}
	
	@Test
	public void basicMultiQuoteTest() {

		String userCommand = "add \"here at here\" at \"there by there\"";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("add");
		expected.add("here at here");
		expected.add("at");
		expected.add("there by there");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}
	
	@Test
	public void multiQuoteWithSpacesTest() {

		String userCommand = "add   \" here   at here \" at \"   there  by there    \"  ";
		ParserFirstPass firstPassUserCommand = new ParserFirstPass(userCommand);

		ArrayList<String> expected = new ArrayList<>();
		expected.add("add");
		expected.add(" here   at here ");
		expected.add("at");
		expected.add("   there  by there    ");

		ArrayList<String> actual = firstPassUserCommand.getFirstPassParsedResult();

		assertEquals(expected, actual);
	}
}
