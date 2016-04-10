//@@author A0126172M
package tasknote.storage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tasknote.shared.AddDuplicateAliasException;
import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

/**
 * this class does jUnit testing for the whole Storage Component
 *
 */
public class StorageTest {
	//various different paths
	private static final String PATH_NAME_DEFAULT = "C:\\NUS\\CS2103T\\main\\taskContents.txt";
	private static final String PATH_NAME_INVALID = "C:/hello";
	private static final String PATH_NAME_WITH_TEXT_FILE = "C:/NUS/hello.txt";
	private static final String PATH_NAME_WITHOUT_SLASH = "C:/NUS/CS2103T/main";
	private static final String PATH_NAME_RELATIVE = "./.././../CS2101";
	private static final String PATH_NAME_TEXT_FILE_ONLY = "W152JGroup.txt";
	private static final String PATH_NAME_RELATIVE_ONLY = ".././../.././../../..";
	
	//To set up random dates
	private static final int BASE_MINUTE = 1;
	private static final int BASE_DATE = 1;
	private static final int BASE_YEAR = 2017;
	private static final int RAND_RANGE_MINUTE = 60;
	private static final int RAND_RANGE_HOUR = 24;
	private static final int RAND_RANGE_DAY = 28;
	private static final int RAND_RANGE_MONTH = 11;
	private static final int RAND_RANGE_YEAR = 40;
	
	//To set up a confirmed overdue time that is today
	private static final GregorianCalendar calendar = new GregorianCalendar();
	private static final int YEAR = calendar.get(Calendar.YEAR);
	private static final int MONTH = calendar.get(Calendar.MONTH) + BASE_DATE;
	private static final int DAY = calendar.get(Calendar.DATE);
	private static final int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
	private static final int MINUTE = calendar.get(Calendar.MINUTE) - BASE_MINUTE;
	
	//Magic Integers
	private static final int ARRAY_MAX_SIZE = 10;
	private static final int OVERDUE_TASK_SIZE = 1;
	
	//for setting up alias
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_CHANGE_PATH = "relocate";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DELETE = "delete";
	
	private static final String ALIAS_ADD_INCLUDE = "include";
	private static final String ALIAS_ADD_PUSH = "push";
	private static final String ALIAS_CHANGE_PATH_CD = "cd";
	private static final String ALIAS_EDIT_MODIFY = "modify";
	private static final String ALIAS_DELETE_REMOVE = "remove";
	
	Storage storage;
	FileManipulation fileManipulator;
	PathManipulation pathManipulator;
	Random random;
	
	//local variables
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private ArrayList<TaskObject> tempArrayList1;
	private ArrayList<TaskObject> tempArrayList2;
	private ArrayList<TaskObject> tempArrayList3;
	private ArrayList<TaskObject> returnedTempArrayList;
	private TaskObject randomFutureTaskObject;
	private TaskObject overdueTaskObject;
	private int tempArrayList2Size;
	private int tempArrayList3Size;
	private HashMap<String,String> fullAlias;
	
	@Before
	public void setUp() throws Exception {
		//set up class
		storage = new Storage();
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
		random = new Random();
		
		tempArrayList2Size = random.nextInt(ARRAY_MAX_SIZE);
		tempArrayList3Size = random.nextInt(ARRAY_MAX_SIZE) + OVERDUE_TASK_SIZE;
		
		//add a specific item
		tempArrayList1 = new ArrayList<TaskObject>();
		tempArrayList2 = new ArrayList<TaskObject>();
		tempArrayList3 = new ArrayList<TaskObject>();
		fullAlias = new HashMap<String,String>();
		
		overdueTaskObject = initializeOverdueTaskObject();
		randomFutureTaskObject = initializeTaskObject();

		setUpTempArrayContents();
	}

	private void setUpTempArrayContents() {
		tempArrayList1.add(randomFutureTaskObject);
		
		for(int index = 0; index < tempArrayList2Size; ++index){
			tempArrayList2.add(randomFutureTaskObject);
		}
		
		for(int index = 1; index < tempArrayList3Size; ++index){
			tempArrayList3.add(randomFutureTaskObject);
		}
		tempArrayList3.add(overdueTaskObject);
	}
	
	private TaskObject initializeOverdueTaskObject(){
		TaskObject tempTaskObject = new TaskObject();
		tempTaskObject.setDateYear(YEAR);
		tempTaskObject.setDateMonth(MONTH);
		tempTaskObject.setDateDay(DAY);
		tempTaskObject.setDateHour(HOUR);
		tempTaskObject.setDateMinute(MINUTE);
		tempTaskObject.setTaskType(TaskObject.TASK_TYPE_DEADLINE);
		return tempTaskObject;
	}

	private TaskObject initializeTaskObject() {
		TaskObject tempTaskObject = new TaskObject();
		year = random.nextInt(RAND_RANGE_YEAR) + BASE_YEAR;
		month = random.nextInt(RAND_RANGE_MONTH) + BASE_DATE;
		day = random.nextInt(RAND_RANGE_DAY) + BASE_DATE;
		hour = random.nextInt(RAND_RANGE_HOUR);
		minute = random.nextInt(RAND_RANGE_MINUTE);
		tempTaskObject.setDateYear(year);
		tempTaskObject.setDateMonth(month);
		tempTaskObject.setDateDay(day);
		tempTaskObject.setDateHour(hour);
		tempTaskObject.setDateMinute(minute);
		return tempTaskObject;
	}
	

	@Test
	public final void test() throws IOException, TaskListIOException, InvalidPathException {
		try{
			testReadAndWriteTaskObjects();
			testPathManipulation();
			testAliasManipulation();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(TaskListIOException tlioe){
			tlioe.printStackTrace();
		}
	}
	
	private void testReadAndWriteTaskObjects() throws TaskListIOException, IOException {
		//case 1: test loadTasks() is able to load something
		storage.saveTasks(tempArrayList3);
		assertEquals(tempArrayList3.size(),tempArrayList3Size);
		
		//case 2: test cleanFile()
		storage.cleanTextFile();
		assertTrue(storage.loadTasks().isEmpty());
		
		//case 2: test if add empty ArrayList will return the same list
		ArrayList<TaskObject> emptyArrayList = new ArrayList<TaskObject>();
		storage.saveTasks(emptyArrayList);
		assertTrue(storage.loadTasks().equals(emptyArrayList));
		
		//case 3: test if add a random generated ArrayList<TaskObject> will return the same arrayList
		storage.saveTasks(tempArrayList1);
		assertTrue(storage.loadTasks().equals(tempArrayList1));
		
		//case 4: test if add n items into storage and retrieval equals same list
		storage.saveTasks(tempArrayList2);
		returnedTempArrayList = storage.loadTasks();
		assertTrue(returnedTempArrayList.equals(tempArrayList2));
		
		//case 5: test if StorageDeadlineUpdater successfully updated the task if overdued
		
		//Firstly, check the previous taskObject is TASK_OUTSTANDING
		assertTrue(overdueTaskObject.getTaskStatus().equals(TaskObject.TaskStatus.TASK_OUTSTANDING));
		storage.saveTasks(tempArrayList3);
		returnedTempArrayList = storage.loadTasks();
		//Next, check that the arrayList has changed
		assertFalse(returnedTempArrayList.equals(tempArrayList3));
		TaskObject overduedTask = returnedTempArrayList.get(tempArrayList3Size-OVERDUE_TASK_SIZE);
		//Finally check if the returned overdued task is really set to TASK_OVERDUE
		assertTrue(overduedTask.getTaskStatus().equals(TaskObject.TaskStatus.TASK_OVERDUE));
	}

	private void testPathManipulation() throws IOException, TaskListIOException {
		//case 1: undo PATH where there is no history
		assertFalse(storage.undoPath());
		
		//case 2: change absolute PATH with a correct PATH but without slash
		assertTrue(storage.changePath(PATH_NAME_WITHOUT_SLASH));
		String fullPathNameWithoutSlash = fileManipulator.readFullPathFromPathFile();
		File firstFile = new File(fullPathNameWithoutSlash);
		assertTrue(firstFile.exists());
		
		//case 3: change absolute PATH with a different file
		//also test that the previous file is deleted
		//additional test that the items inside the storage is still there
		assertTrue(storage.changePath(PATH_NAME_WITH_TEXT_FILE));
		String fullPathNameWithTextFile = fileManipulator.readFullPathFromPathFile();
		File tempFile = new File(fullPathNameWithTextFile);
		assertTrue(tempFile.exists());
		assertFalse(firstFile.exists());
		ArrayList<TaskObject> tasks = storage.loadTasks();
		assertTrue(returnedTempArrayList.equals(tasks));
		
		//case 4: change PATH to an invalid PATH
		//also check that the current path is still retained at the previous path
		assertFalse(storage.changePath(PATH_NAME_INVALID));
		String path = fileManipulator.readFullPathFromPathFile();
		assertTrue(path.equals(fullPathNameWithTextFile));
		assertTrue(tempFile.exists());
		
		//case 5: undo PATH
		assertTrue(storage.undoPath());
		path = fileManipulator.readFullPathFromPathFile();
		assertTrue(path.equals(fullPathNameWithoutSlash));

		//case 6: redo PATH
		assertTrue(storage.redoPath());
		path = fileManipulator.readFullPathFromPathFile();
		assertTrue(path.equals(fullPathNameWithTextFile));

		//case 7: redo PATH when there is no future history
		assertFalse(storage.redoPath());
		path = fileManipulator.readFullPathFromPathFile();
		assertTrue(path.equals(fullPathNameWithTextFile));
		
		//case 8: test if storage undo and does a valid change PATH does the redo PATH remains
		//test with previous passed tests
		storage.undoPath(); //expect to the path without slash
		storage.changePath(PATH_NAME_WITHOUT_SLASH);
		assertFalse(storage.redoPath()); //if it exist would be the path with text file
		path = fileManipulator.readFullPathFromPathFile();
		assertTrue(path.equals(fullPathNameWithoutSlash));
		
		//case 8: change PATH to original PATH
		assertTrue(storage.changePath(PATH_NAME_DEFAULT));
		String fullPathNameDefault = fileManipulator.readFullPathFromPathFile();
		tempFile = new File(fullPathNameDefault);
		assertTrue(tempFile.exists());
		
		//case 9: change PATH with relative PATH
		assertTrue(storage.changePath(PATH_NAME_RELATIVE));
		String absolutePath = fileManipulator.readFullPathFromPathFile();
		assertTrue(pathManipulator.isAbsolutePath(absolutePath));
		
		//case 10: change PATH with ONLY fileName
		assertTrue(storage.changePath(PATH_NAME_TEXT_FILE_ONLY));
		String newTextFilePath = fileManipulator.readFullPathFromPathFile();
		assertTrue(fileManipulator.getTextFileName().equals(PATH_NAME_TEXT_FILE_ONLY));
		assertFalse(absolutePath.equals(newTextFilePath));
		
		//case 11: change PATH with multiple local PATHS
		assertTrue(storage.changePath(PATH_NAME_RELATIVE_ONLY));
		String relativePath = fileManipulator.readFullPathFromPathFile();
		tempFile = new File(relativePath);
		assertTrue(tempFile.exists());
	}
	
	private void testAliasManipulation() throws IOException {
		boolean isAddExceptionThrown = false;
		try {
			//case 1: test undo command when there isn't any
			storage.undoAlias();
			storage.undoAlias();
			assertFalse(storage.undoAlias());
			
			//case 2: clean the alias file and check that it gets a valid empty HasMap
			storage.cleanAliasFile();
			HashMap<String,String> emptyAlias = storage.getAlias();
			assertTrue(emptyAlias.isEmpty());
			
			//case 3: test addAlias
			storage.addAlias(COMMAND_ADD, ALIAS_ADD_INCLUDE);
			assertTrue(storage.getAlias(ALIAS_ADD_INCLUDE).equals(COMMAND_ADD));
			
			//case 4: test duplicate alias added (Only tested outside try-catch block)
			storage.addAlias(COMMAND_CHANGE_PATH, ALIAS_ADD_INCLUDE);	
		} catch (AddDuplicateAliasException e) {
			isAddExceptionThrown = true;
		}	
		assertTrue(isAddExceptionThrown);

		//case 5: fillup fullAlias and test saveAlias
		fullAlias.put(ALIAS_ADD_INCLUDE,COMMAND_ADD);
		fullAlias.put(ALIAS_CHANGE_PATH_CD, COMMAND_CHANGE_PATH);
		fullAlias.put(ALIAS_ADD_PUSH, COMMAND_ADD);
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		fullAlias.put(ALIAS_DELETE_REMOVE, COMMAND_DELETE);
			
		storage.saveAlias(fullAlias);
		assertTrue(storage.getAlias().equals(fullAlias));
		
		//case 6: test remove an alias
		storage.removeAlias(ALIAS_EDIT_MODIFY);
		HashMap<String,String> aliasRemovedContent = storage.getAlias();
		assertFalse(aliasRemovedContent.containsKey(ALIAS_EDIT_MODIFY));
		
		//case 7: test undo command for alias
		assertTrue(storage.undoAlias());
		HashMap<String,String> undoedAlias = storage.getAlias();
		assertTrue(undoedAlias.equals(fullAlias));
		
		//case 8: test redo command for alias
		assertTrue(storage.redoAlias());
		HashMap<String,String> redoedAlias = storage.getAlias();
		assertTrue(redoedAlias.equals(aliasRemovedContent));
		
		//case 9: test redo command when there isn't any
		assertFalse(storage.redoAlias());
		redoedAlias = storage.getAlias();
		assertTrue(redoedAlias.equals(aliasRemovedContent));
	
	}
}
