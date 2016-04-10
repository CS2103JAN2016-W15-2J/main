//@@author A0129529
package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class ParserDeleteTest {
	
	Parser testParser = new Parser();
	
	@Test
	public void testDeleteRange() {
		
		String userCommand = "delete 1 - 3";
		this.testParser.setInputString(userCommand);
		
		ArrayList<Integer> expectedResult = new ArrayList<>();
		expectedResult.add(0);
		expectedResult.add(1);
		expectedResult.add(2);
		
		ArrayList<Integer> actualResult = this.testParser.parseDelete(false);
		
		assertEquals(expectedResult, actualResult);
	}
}
