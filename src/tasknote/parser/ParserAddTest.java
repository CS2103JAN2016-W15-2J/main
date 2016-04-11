//@@author A0129529W
package tasknote.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

		TaskObject expectedResult = new TaskObject("longdeadline");

		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(today.get(Calendar.DAY_OF_MONTH));
		expectedResult.setDateHour(23);
		expectedResult.setDateMinute(59);

		expectedResult.setTaskType("deadline");

		// Test all keywords allowed by Parser
		String[] validKeywords = { "by", "on", "at" };

		for (int i = 0; i < validKeywords.length; i++) {

			// This should generally prevent cases where the add
			// function automatically rolls the date to the next day
			// for the purpose of checking
			String userCommand = "add longdeadline " + validKeywords[i] + " 23:59";
			this.testParser.setInputString(userCommand);

			TaskObject actualResult = this.testParser.parseAdd(false);

			assertEquals(expectedResult, actualResult);
		}
	}

	@Test
	public void testAddBasicDeadlineBadHourBadMinute() {

		String[] validKeywords = { "by", "on", "at" };

		for (int i = 0; i < validKeywords.length; i++) {

			String userCommand = "add baddeadlinehourminute " + validKeywords[i] + " 76:90";
			this.testParser.setInputString(userCommand);

			TaskObject expectedResult = new TaskObject();

			// For this case, "at" keyword would treat 76:90 as location
			// since it is not a valid time
			if (validKeywords[i].equals("at")) {
				expectedResult.setTaskName("baddeadlinehourminute");
				expectedResult.setLocation("76:90");
			} else {
				expectedResult.setTaskName("baddeadlinehourminute " + validKeywords[i] + " 76:90");
			}

			TaskObject actualResult = this.testParser.parseAdd(false);

			assertEquals(expectedResult, actualResult);
		}
	}

	@Test
	public void testAddBasicDeadlineBadHour() {

		String userCommand = "add baddeadlinehour by 76:31";
		this.testParser.setInputString(userCommand);

		TaskObject expectedResult = new TaskObject("baddeadlinehour by 76:31");
		TaskObject actualResult = this.testParser.parseAdd(false);

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testAddBasicDeadlineBadMinute() {

		String userCommand = "add baddeadlineminute by 15:99";
		this.testParser.setInputString(userCommand);

		TaskObject expectedResult = new TaskObject("baddeadlineminute by 15:99");
		TaskObject actualResult = this.testParser.parseAdd(false);

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testAddBasicDeadlineWithDate() {

		String userCommand = "add longerdeadline on 1/2/2001 by 5:30";
		this.testParser.setInputString(userCommand);

		TaskObject expectedResult = new TaskObject("longerdeadline");

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

		TaskObject expectedResult = new TaskObject("longerdeadlinebadday on 45/2/2001");

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

		TaskObject expectedResult = new TaskObject("longerdeadlinebadmonth on 15/22/2001");

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
	public void testAddBasicDeadlineBadDayBadMonth() {

		// This should generally prevent cases where the add
		// function automatically rolls the date to the next day
		// for the purpose of checking
		String userCommand = "add longerdeadlinebaddaybadmonth on 49/22/2001 by 23:59";
		this.testParser.setInputString(userCommand);

		TaskObject expectedResult = new TaskObject("longerdeadlinebaddaybadmonth on 49/22/2001");

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

		String userCommand = "add badtask on 2/3/2002";
		this.testParser.setInputString(userCommand);

		TaskObject expectedResult = new TaskObject("badtask");

		expectedResult.setDateYear(2002);
		expectedResult.setDateMonth(3);
		expectedResult.setDateDay(2);

		TaskObject actualResult = this.testParser.parseAdd(false);

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testAddDeadlineJoinDateTime() {

		TaskObject expectedResult = new TaskObject("joineddatetimetask");

		GregorianCalendar today = new GregorianCalendar();
		expectedResult.setDateYear(today.get(Calendar.YEAR));
		expectedResult.setDateMonth(today.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(15);

		expectedResult.setDateHour(21);
		expectedResult.setDateMinute(0);

		String[] validKeywords = { "by", "on", "at" };

		for (int i = 0; i < validKeywords.length; i++) {
			String userCommand = "add joineddatetimetask by 9pm 15th";
			this.testParser.setInputString(userCommand);

			TaskObject actualResult = this.testParser.parseAdd(false);

			assertEquals(expectedResult, actualResult);
		}
	}

	@Test
	public void testSpecialMcdonaldsThatGCalCannotHandle() {

		TaskObject expectedResult = new TaskObject("breakfast with mk");

		GregorianCalendar tomorrow = new GregorianCalendar();

		tomorrow = DateParser.rollByDays(tomorrow, 1);

		expectedResult.setDateYear(tomorrow.get(Calendar.YEAR));
		expectedResult.setDateMonth(tomorrow.get(Calendar.MONTH) + 1);
		expectedResult.setDateDay(tomorrow.get(Calendar.DAY_OF_MONTH));

		expectedResult.setDateHour(10);
		expectedResult.setDateMinute(0);

		expectedResult.setLocation("8 redhill st, mcd");

		String[] validKeywords = { "by", "on", "at" };

		for (int i = 0; i < validKeywords.length; i++) {
			String userCommand = "add breakfast with mk at 8 redhill st, mcd " + validKeywords[i]
					+ " 10am tomorrow";
			this.testParser.setInputString(userCommand);

			TaskObject actualResult = this.testParser.parseAdd(false);

			assertEquals(expectedResult, actualResult);
		}
	}
}
