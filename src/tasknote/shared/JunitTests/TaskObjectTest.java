//@@author A0129561A
package tasknote.shared.JunitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tasknote.shared.Constants;
import tasknote.shared.TaskObject;
import tasknote.shared.TaskObject.TaskStatus;

public class TaskObjectTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void setAndGetTaskName() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertTrue(testTaskObjectOne.getTaskName().equals("Task 1"));
        testTaskObjectOne.setTaskName("Modified Task 1");
        assertEquals("Modified Task 1", testTaskObjectOne.getTaskName());

        TaskObject testTaskObjectTwo = new TaskObject();
        assertTrue(testTaskObjectTwo.getTaskName().isEmpty());
        testTaskObjectTwo.setTaskName("Modified Task 2");
        assertEquals("Modified Task 2", testTaskObjectTwo.getTaskName());
    }

    @Test
    public void setAndGetTaskDateYear() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals(testTaskObject.DEFAULT_DATETIME_VALUE, testTaskObject.getDateYear());

        testTaskObject.setDateYear(2000);
        assertEquals(2000, testTaskObject.getDateYear());
    }

    @Test
    public void setAndGetTaskDateMonth() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals(testTaskObject.DEFAULT_DATETIME_VALUE, testTaskObject.getDateYear());

        testTaskObject.setDateMonth(1);
        assertEquals(1, testTaskObject.getDateMonth());
    }

    @Test
    public void setAndGetTaskDateDay() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals(testTaskObject.DEFAULT_DATETIME_VALUE, testTaskObject.getDateYear());

        testTaskObject.setDateMonth(4);
        assertEquals(4, testTaskObject.getDateMonth());
    }

    @Test
    public void setAndGetTaskDateHour() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals(testTaskObject.DEFAULT_DATETIME_VALUE, testTaskObject.getDateYear());

        testTaskObject.setDateMonth(10);
        assertEquals(10, testTaskObject.getDateMonth());
    }

    @Test
    public void setAndGetTaskDateMinute() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals(testTaskObject.DEFAULT_DATETIME_VALUE, testTaskObject.getDateYear());

        testTaskObject.setDateMonth(30);
        assertEquals(30, testTaskObject.getDateMonth());
    }

    @Test
    public void getTaskFormattedDate() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals("", testTaskObject.getFormattedDate());

        testTaskObject.setDateYear(2000);
        testTaskObject.setDateMonth(1);
        testTaskObject.setDateDay(1);
        assertEquals("1 January 2000", testTaskObject.getFormattedDate());

        testTaskObject.setDateDay(2);
        assertEquals("2 January 2000", testTaskObject.getFormattedDate());

        testTaskObject.setDateMonth(2);
        assertEquals("2 February 2000", testTaskObject.getFormattedDate());

        testTaskObject.setDateYear(2010);
        assertEquals("2 February 2010", testTaskObject.getFormattedDate());

        exception.expect(IndexOutOfBoundsException.class);
        testTaskObject.setDateMonth(13);
        testTaskObject.getFormattedDate();
    }

    @Test
    public void getTaskFormattedEndDate() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals("", testTaskObject.getFormattedEndDate());

        testTaskObject.setEndDateYear(2000);
        testTaskObject.setEndDateMonth(1);
        testTaskObject.setEndDateDay(1);
        assertEquals("1 January 2000", testTaskObject.getFormattedEndDate());

        testTaskObject.setEndDateDay(2);
        assertEquals("2 January 2000", testTaskObject.getFormattedEndDate());

        testTaskObject.setEndDateMonth(2);
        assertEquals("2 February 2000", testTaskObject.getFormattedEndDate());

        testTaskObject.setEndDateYear(2010);
        assertEquals("2 February 2010", testTaskObject.getFormattedEndDate());

        exception.expect(IndexOutOfBoundsException.class);
        testTaskObject.setEndDateMonth(13);
        testTaskObject.getFormattedEndDate();
    }

    @Test
    public void getTaskFormattedTime() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals("", testTaskObject.getFormattedTime());

        testTaskObject.setDateYear(2000);
        testTaskObject.setDateMonth(1);
        testTaskObject.setDateDay(1);
        testTaskObject.setDateHour(10);
        testTaskObject.setDateMinute(10);
        assertEquals("10:10AM", testTaskObject.getFormattedTime());

        testTaskObject.setDateHour(12);
        assertEquals("12:10PM", testTaskObject.getFormattedTime());

        testTaskObject.setDateHour(20);
        testTaskObject.setDateMinute(30);
        assertEquals("08:30PM", testTaskObject.getFormattedTime());
    }

    @Test
    public void getTaskFormattedEndTime() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertEquals("", testTaskObject.getFormattedEndTime());

        testTaskObject.setEndDateYear(2000);
        testTaskObject.setEndDateMonth(1);
        testTaskObject.setEndDateDay(1);
        testTaskObject.setEndDateHour(10);
        testTaskObject.setEndDateMinute(10);
        assertEquals("10:10AM", testTaskObject.getFormattedEndTime());

        testTaskObject.setEndDateHour(12);
        assertEquals("12:10PM", testTaskObject.getFormattedEndTime());

        testTaskObject.setEndDateHour(20);
        testTaskObject.setEndDateMinute(30);
        assertEquals("08:30PM", testTaskObject.getFormattedEndTime());
    }

    @Test
    public void testSetTaskStatus() {
        TaskObject testTaskObjectSetStatusWithEnum = new TaskObject("Task 1");
        TaskObject testTaskObjectSetStatusWithString = new TaskObject("Task 2");
        assertEquals(TaskStatus.TASK_DEFAULT, testTaskObjectSetStatusWithEnum.getTaskStatus());
        assertTrue(
                testTaskObjectSetStatusWithString.getTaskStatus() == testTaskObjectSetStatusWithEnum.getTaskStatus());

        testTaskObjectSetStatusWithEnum.setTaskStatus(TaskStatus.TASK_OUTSTANDING);
        testTaskObjectSetStatusWithString.setTaskStatus(Constants.STRING_TASKSTATUS_OUTSTANDING);
        assertTrue(
                testTaskObjectSetStatusWithString.getTaskStatus() == testTaskObjectSetStatusWithEnum.getTaskStatus());
        testTaskObjectSetStatusWithEnum.setTaskStatus(TaskStatus.TASK_OVERDUE);
        testTaskObjectSetStatusWithString.setTaskStatus(Constants.STRING_TASKSTATUS_OVERDUE);
        assertTrue(
                testTaskObjectSetStatusWithString.getTaskStatus() == testTaskObjectSetStatusWithEnum.getTaskStatus());
        testTaskObjectSetStatusWithEnum.setTaskStatus(TaskStatus.TASK_COMPLETED);
        testTaskObjectSetStatusWithString.setTaskStatus(Constants.STRING_TASKSTATUS_COMPLETED);
        assertTrue(
                testTaskObjectSetStatusWithString.getTaskStatus() == testTaskObjectSetStatusWithEnum.getTaskStatus());
        testTaskObjectSetStatusWithEnum.setTaskStatus(TaskStatus.TASK_INVALID_STORAGE);
        testTaskObjectSetStatusWithString.setTaskStatus(Constants.STRING_TASKSTATUS_INVALID_STORAGE);
        assertTrue(
                testTaskObjectSetStatusWithString.getTaskStatus() == testTaskObjectSetStatusWithEnum.getTaskStatus());
    }

    @Test
    public void testDeepClone() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        testTaskObject.setDateYear(2000);
        testTaskObject.setDateMonth(1);
        testTaskObject.setDateDay(1);
        testTaskObject.setDateHour(10);
        testTaskObject.setDateMinute(10);

        TaskObject cloneOfTestTaskObject = new TaskObject("Task 2");
        cloneOfTestTaskObject.deepCopy(testTaskObject);

        assertEquals(testTaskObject, cloneOfTestTaskObject);
    }

    @Test
    public void testTaskTypeSet() {
        TaskObject testTaskObject = new TaskObject("Task 1");
        assertTrue(testTaskObject.isTaskTypeSet());
        assertEquals(testTaskObject.getTaskType(), TaskObject.TASK_TYPE_FLOATING);

        testTaskObject.setDateYear(2000);
        testTaskObject.setDateMonth(1);
        testTaskObject.setDateDay(1);
        testTaskObject.setDateHour(10);
        testTaskObject.setDateMinute(10);
        assertTrue(testTaskObject.isTaskTypeSet());
        assertEquals(testTaskObject.getTaskType(), TaskObject.TASK_TYPE_DEADLINE);

        testTaskObject.setEndDateYear(2000);
        testTaskObject.setEndDateMonth(1);
        testTaskObject.setEndDateDay(1);
        testTaskObject.setEndDateHour(10);
        testTaskObject.setEndDateMinute(11);
        testTaskObject.setDuration(1);
        assertTrue(testTaskObject.isTaskTypeSet());
        assertEquals(testTaskObject.getTaskType(), TaskObject.TASK_TYPE_EVENT);

        testTaskObject.setDuration(-1);
        assertFalse(testTaskObject.isTaskTypeSet());
    }
}
