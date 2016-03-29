package tasknote.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class ParserDeleteTest {
	
	@Test
	public void testDeleteRange() {
		
		String userCommand = "delete 1 - 3";
		
		ArrayList<Integer> expectedResult = new ArrayList<>();
		expectedResult.add(1);
		expectedResult.add(2);
		expectedResult.add(3);
		
		assertEquals(Parser.parseDelete(userCommand), expectedResult);
	}
}
