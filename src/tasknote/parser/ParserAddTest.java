package tasknote.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class ParserAddTest {

	@Test
	public void testAddBasicFloatingTask() {
		
		String userCommand = "add longtaskname";
		
		TaskObject expectedResult = new TaskObject("longtaskname");
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void testAddBasicDeadline() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longdeadline by 23:59";
		
		TaskObject expectedResult = new TaskObject("longdeadline");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH));
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void testAddBasicEvent() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longevent on 1/2/2001 by 5:30";
		
		TaskObject expectedResult = new TaskObject("longevent");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(2001);
		expectedResult.setDateMonth(2);
		expectedResult.setDateDay(1);
		expectedResult.setDateHour(5);
		expectedResult.setDateMinute(30);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
	
	@Test
	public void testAddBadTaskWithDateWithoutTime() {

		// In this case, the TaskObject is still assigned
		// the date field, but the GUI will ignore it
		String userCommand = "add badtask on 2/3/2002";
		
		TaskObject expectedResult = new TaskObject("badtask");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(2002);
		expectedResult.setDateMonth(3);
		expectedResult.setDateDay(2);
		
		assertEquals(Parser.parseAdd(userCommand), expectedResult);
	}
}
