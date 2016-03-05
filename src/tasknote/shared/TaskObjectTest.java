package tasknote.shared;

import static org.junit.Assert.*;

import javax.management.InvalidAttributeValueException;

import org.junit.Test;

/*
 * Test the behaviour of task object.
 * 
 * @@author MunKeat
 */
public class TaskObjectTest {

    @Test
    public void setAndGetTaskName() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertTrue(testTaskObjectOne.getTaskName().equals("Task 1"));
        testTaskObjectOne.setTaskName("Modified Task 1");
        assertTrue(testTaskObjectOne.getTaskName().equals("Modified Task 1"));
        
        TaskObject testTaskObjectTwo = new TaskObject();
        assertTrue(testTaskObjectTwo.getTaskName() == null);
        testTaskObjectTwo.setTaskName("Modified Task 2");
        assertTrue(testTaskObjectTwo.getTaskName().equals("Modified Task 2"));
    }
    
    @Test
    public void setAndGetTaskDateYear() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertEquals(testTaskObjectOne.getDateYear(), testTaskObjectOne.DEFAULT_DATETIME_VALUE);
        
        testTaskObjectOne.setDateYear(2000);
        assertEquals(testTaskObjectOne.getDateYear(), 2000);
    }
    
    @Test
    public void setAndGetTaskDateMonth() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertEquals(testTaskObjectOne.getDateYear(), testTaskObjectOne.DEFAULT_DATETIME_VALUE);
        
        testTaskObjectOne.setDateMonth(1);
        assertEquals(testTaskObjectOne.getDateMonth(), 1);
    }
    
    @Test
    public void setAndGetTaskDateDay() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertEquals(testTaskObjectOne.getDateYear(), testTaskObjectOne.DEFAULT_DATETIME_VALUE);
        
        testTaskObjectOne.setDateMonth(4);
        assertEquals(testTaskObjectOne.getDateMonth(), 4);
    }
    
    @Test
    public void setAndGetTaskDateHour() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertEquals(testTaskObjectOne.getDateYear(), testTaskObjectOne.DEFAULT_DATETIME_VALUE);
        
        testTaskObjectOne.setDateMonth(10);
        assertEquals(testTaskObjectOne.getDateMonth(), 10);
    }
    
    @Test
    public void setAndGetTaskDateMinute() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        assertEquals(testTaskObjectOne.getDateYear(), testTaskObjectOne.DEFAULT_DATETIME_VALUE);
        
        testTaskObjectOne.setDateMonth(30);
        assertEquals(testTaskObjectOne.getDateMonth(), 30);
    }
    
    @Test
    public void getTaskFormattedDate() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        testTaskObjectOne.setDateYear(2000);
        testTaskObjectOne.setDateMonth(1);
        testTaskObjectOne.setDateDay(1);
        assertEquals(testTaskObjectOne.getFormattedDate(), "1 January 2000");
        
        testTaskObjectOne.setDateDay(2);
        assertEquals(testTaskObjectOne.getFormattedDate(), "2 January 2000");
        
        testTaskObjectOne.setDateMonth(2);
        assertEquals(testTaskObjectOne.getFormattedDate(), "2 February 2000");
        
        testTaskObjectOne.setDateYear(2010);
        assertEquals(testTaskObjectOne.getFormattedDate(), "2 February 2010");
    }
    
    @Test
    public void getTaskFormattedTime() {
        TaskObject testTaskObjectOne = new TaskObject("Task 1");
        testTaskObjectOne.setDateYear(2000);
        testTaskObjectOne.setDateMonth(1);
        testTaskObjectOne.setDateDay(1);
        testTaskObjectOne.setDateHour(10);
        testTaskObjectOne.setDateMinute(10);
        assertEquals(testTaskObjectOne.getFormattedTime(), "10:10am");

        testTaskObjectOne.setDateHour(20);
        assertEquals(testTaskObjectOne.getFormattedTime(), "08:10pm");
        
        testTaskObjectOne.setDateMinute(30);
        assertEquals(testTaskObjectOne.getFormattedTime(), "08:30pm");
    }
}
