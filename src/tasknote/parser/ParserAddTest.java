package tasknote.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class ParserAddTest {
	
	Parser testParser = new Parser();

	@Test
	public void testAddBasicFloatingTask() {
		
		String userCommand = "add longtaskname";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longtaskname");
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadline() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longdeadline by 23:59";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longdeadline");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);
		
		expectedResult.setTaskType("deadline");
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineBadHourBadMinute() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add baddeadlinehourminute by 76:90";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("baddeadlinehourminute");
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineBadHour() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add baddeadlinehour by 76:31";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("baddeadlinehour");
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineBadMinute() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add baddeadlineminute by 15:99";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("baddeadlineminute");
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineWithDate() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longerdeadline on 1/2/2001 by 5:30";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longerdeadline");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(2001);
		expectedResult.setDateMonth(2);
		expectedResult.setDateDay(1);
		expectedResult.setDateHour(5);
		expectedResult.setDateMinute(30);
		
		expectedResult.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineBadDay() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longerdeadlinebadday on 45/2/2001 by 23:59";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longerdeadlinebadday");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);
		
		expectedResult.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBasicDeadlineBadMonth() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longerdeadlinebadmonth on 15/22/2001 by 23:59";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longerdeadlinebadmonth");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);
		
		expectedResult.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
	}
	
	@Test
	public void testAddBasicDeadlineBadDayBadMonth() {
		
		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longerdeadlinebaddaybadmonth on 49/22/2001 by 23:59";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("longerdeadlinebaddaybadmonth");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);
		
		expectedResult.setTaskType("deadline");
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testAddBadTaskWithDateWithoutTime() {

		// In this case, the TaskObject is still assigned
		// the date field, but the GUI will ignore it
		String userCommand = "add badtask on 2/3/2002";
		this.testParser.setInputString(userCommand);
		
		TaskObject expectedResult = new TaskObject("badtask");
		
		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(2002);
		expectedResult.setDateMonth(3);
		expectedResult.setDateDay(2);
		
		TaskObject actualResult = this.testParser.parseAdd(false);
		
		assertEquals(expectedResult, actualResult);
	}
}
