package tasknote.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

public class StorageTest {
	private static final int BASE_DATE = 1;
	private static final int BASE_YEAR = 1980;
	private static final int RAND_RANGE_MINUTE = 60;
	private static final int RAND_RANGE_HOUR = 24;
	private static final int RAND_RANGE_DAY = 28;
	private static final int RAND_RANGE_MONTH = 12;
	private static final int RAND_RANGE_YEAR = 40;
	Storage storage;
	Random random;
	
	//local variables
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private ArrayList<TaskObject> tempArrayList1;
	private ArrayList<TaskObject> tempArrayList2;
	private TaskObject tempTaskObject;
	
	@Before
	public void setUp() throws Exception {
		//set up class
		storage = new Storage();
		random = new Random();
		
		//add a specific item
		tempArrayList1 = new ArrayList<TaskObject>();
		tempArrayList2 = new ArrayList<TaskObject>();
		try{
			tempTaskObject = InitializeTaskObject();
			tempArrayList1.add(tempTaskObject);
			tempArrayList2.add(tempTaskObject);
			tempArrayList2.add(tempTaskObject);
			tempArrayList2.add(tempTaskObject);
			tempArrayList2.add(tempTaskObject);
			storage = new Storage();
		}catch(Exception e){}
	}

	private TaskObject InitializeTaskObject() {
		TaskObject tempTaskObject = new TaskObject();
		year = random.nextInt(RAND_RANGE_YEAR) + BASE_YEAR;
		month = random.nextInt(RAND_RANGE_MONTH) + BASE_DATE;
		day = random.nextInt(RAND_RANGE_DAY) + BASE_DATE;
		hour = random.nextInt(RAND_RANGE_HOUR);
		minute = random.nextInt(RAND_RANGE_MINUTE);
		//tempTaskObject.setTaskObjectCalendar(new GregorianCalendar(year, month, day, hour, minute));
		tempTaskObject.setDateYear(year);
		tempTaskObject.setDateMonth(month);
		tempTaskObject.setDateDay(day);
		tempTaskObject.setDateHour(hour);
		tempTaskObject.setDateMinute(minute);
		return tempTaskObject;
	}
	
	@Test
	public final void test() throws IOException, TaskListIOException {
		//assertTrue(false);
		try{
			//case 1: test loadTasks() is able to load something
			//		  test cleanFile()
			storage.saveTasks(tempArrayList2);
			assertEquals(tempArrayList2.size(),4);
			storage.cleanFile();
			assertTrue(storage.loadTasks().isEmpty());
			
			//case 2: add empty ArrayList
			storage.saveTasks(new ArrayList<TaskObject>());
			assertTrue(storage.loadTasks().equals(new ArrayList<TaskObject>()));
			
			
			//case 3: add an ArrayList<TaskObject> with an item
			storage.saveTasks(tempArrayList1);
			System.out.println(tempTaskObject);
			System.out.println();
			System.out.println(storage.loadTasks().get(0));
			assertTrue(storage.loadTasks().equals(tempArrayList1));
			
			//case 4: add 4 items into storage and retrieval
			storage.saveTasks(tempArrayList2);

			
		}catch(IOException ioe){
			
		}catch(TaskListIOException tlioe){
			
		}
	}
}
