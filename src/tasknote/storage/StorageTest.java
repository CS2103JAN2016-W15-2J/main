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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tasknote.shared.AddDuplicateAliasException;
import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

/**
 * this class does jUnit testing for the whole Storage Component
 *
 */
public class StorageTest {
	// various different paths
	private static final String PATH_NAME_DEFAULT = "C:\\blablabla.txt";
	private static final String PATH_NAME_INVALID = "C:/z2fhi";
	private static final String PATH_NAME_WITH_TEXT_FILE = "C:/markes.txt";
	private static final String PATH_NAME_WITHOUT_SLASH = "C:/.";
	private static final String PATH_NAME_RELATIVE = "./.././../snake.txt";
	private static final String PATH_NAME_TEXT_FILE_ONLY = "W152JGroup.txt";
	private static final String PATH_NAME_RELATIVE_ONLY = ".././../.././../../..";
	private static final String PATH_NAME_UNIQUE_FILE_NAME = "C:/uniqueName.txt";
	private static final String PATH_NAME_TEST_DELETE = "silly.txt";

	// To set up random dates
	private static final int BASE_MINUTE = 1;
	private static final int BASE_DATE = 1;
	private static final int BASE_YEAR = 2100;
	private static final int RAND_RANGE_MINUTE = 60;
	private static final int RAND_RANGE_HOUR = 24;
	private static final int RAND_RANGE_DAY = 28;
	private static final int RAND_RANGE_MONTH = 11;
	private static final int RAND_RANGE_YEAR = 40;

	// To set up a confirmed overdue time that is today
	private static final GregorianCalendar calendar = new GregorianCalendar();
	private static final int YEAR = calendar.get(Calendar.YEAR);
	private static final int MONTH = calendar.get(Calendar.MONTH) + BASE_DATE;
	private static final int DAY = calendar.get(Calendar.DATE);
	private static final int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
	private static final int MINUTE = calendar.get(Calendar.MINUTE) - BASE_MINUTE;

	// Magic Integers
	private static final int ARRAY_MAX_SIZE = 10;
	private static final int OVERDUE_TASK_SIZE = 1;

	// for setting up alias
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_CHANGE_PATH = "relocate";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DELETE = "delete";

	private static final String ALIAS_ADD_INCLUDE = "include";
	private static final String ALIAS_ADD_PUSH = "push";
	private static final String ALIAS_CHANGE_PATH_CD = "cd";
	private static final String ALIAS_EDIT_MODIFY = "modify";
	private static final String ALIAS_DELETE_REMOVE = "remove";

	private Storage storage;
	private PathManipulation pathManipulator;
	private Random random;

	// local variables
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private ArrayList<TaskObject> tempArrayList1;
	private ArrayList<TaskObject> tempArrayList2;
	private ArrayList<TaskObject> tempArrayList3;
	private TaskObject randomFutureTaskObject;
	private TaskObject overdueTaskObject;
	private int tempArrayList2Size;
	private int tempArrayList3Size;
	private HashMap<String, String> fullAlias;
	
	//local variables to set everything back in place
	String currentTextFilePath;
	ArrayList<TaskObject> currentTasks;
	HashMap<String,String> currentAlias;
	
	@Before
	public void setUp() throws Exception {
		// set up class
		storage = new Storage();
		pathManipulator = new PathManipulation();
		random = new Random();
		
		//backup the current items
		currentTextFilePath = storage.getCurrentAbsoluteTextFilePath();
		currentTasks = storage.loadTasks();
		currentAlias = storage.getAlias();

		tempArrayList2Size = random.nextInt(ARRAY_MAX_SIZE);
		tempArrayList3Size = random.nextInt(ARRAY_MAX_SIZE) + OVERDUE_TASK_SIZE;

		// add a specific item
		tempArrayList1 = new ArrayList<TaskObject>();
		tempArrayList2 = new ArrayList<TaskObject>();
		tempArrayList3 = new ArrayList<TaskObject>();
		fullAlias = new HashMap<String, String>();

		overdueTaskObject = initializeOverdueTaskObject();
		randomFutureTaskObject = initializeTaskObject();

		setUpTempArrayContents();
	}

	private void setUpTempArrayContents() {
		tempArrayList1.add(randomFutureTaskObject);

		for (int index = 0; index < tempArrayList2Size; ++index) {
			tempArrayList2.add(randomFutureTaskObject);
		}

		for (int index = 1; index < tempArrayList3Size; ++index) {
			tempArrayList3.add(randomFutureTaskObject);
		}
		tempArrayList3.add(overdueTaskObject);
	}

	private TaskObject initializeOverdueTaskObject() {
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

	/*
	 * TEST TEXT FILE MANIPULATION
	 */
	@Test
	public final void testLoadTasks() throws TaskListIOException, IOException {
		storage.saveTasks(tempArrayList3);
		assertEquals(tempArrayList3.size(), tempArrayList3Size);
	}

	@Test
	public final void testCleanup() throws IOException, TaskListIOException {
		storage.cleanTextFile();
		assertTrue(storage.loadTasks().isEmpty());
	}

	@Test
	public final void testAddingAnEmptyArrayList() throws TaskListIOException, IOException {
		ArrayList<TaskObject> emptyArrayList = new ArrayList<TaskObject>();
		storage.saveTasks(emptyArrayList);
		assertTrue(storage.loadTasks().equals(emptyArrayList));
	}

	@Test
	public final void testAddingRandomGeneratedArrayList() throws TaskListIOException, IOException {
		storage.saveTasks(tempArrayList1);
		assertTrue(storage.loadTasks().equals(tempArrayList1));
	}

	@Test
	public final void testAddNItems() throws TaskListIOException, IOException {
		storage.saveTasks(tempArrayList2);
		ArrayList<TaskObject> returnedTempArrayList = storage.loadTasks();
		assertTrue(returnedTempArrayList.equals(tempArrayList2));
	}

	@Test
	public final void testStorageDeadlineUpdater() throws TaskListIOException, IOException {
		// Firstly, check the previous taskObject is TASK_OUTSTANDING
		assertTrue(overdueTaskObject.getTaskStatus().equals(TaskObject.TaskStatus.TASK_OUTSTANDING));
		storage.saveTasks(tempArrayList3);
		ArrayList<TaskObject> returnedTempArrayList = storage.loadTasks();
		// Next, check that the arrayList has changed
		assertFalse(returnedTempArrayList.equals(tempArrayList3));
		TaskObject overduedTask = returnedTempArrayList.get(tempArrayList3Size - OVERDUE_TASK_SIZE);
		// Finally check if the returned overdued task is really set to
		// TASK_OVERDUE
		assertTrue(overduedTask.getTaskStatus().equals(TaskObject.TaskStatus.TASK_OVERDUE));
	}

	@Test
	public final void testUndoPathWhenNoHistory() throws InvalidPathException, IOException {
		while(storage.undoPath()){}
		assertFalse(storage.undoPath());
	}

	@Test
	public final void testChangeAbsolutePathWithoutSlash() throws IOException {
		assertTrue(storage.changePath(PATH_NAME_WITHOUT_SLASH));
		String fullPathNameWithoutSlash = storage.getCurrentAbsoluteTextFilePath();
		File firstFile = new File(fullPathNameWithoutSlash);
		assertTrue(firstFile.exists());
	}

	@Test
	public final void testChangeAbsolutePathWithSlash() throws IOException {
		assertTrue(storage.changePath(PATH_NAME_WITHOUT_SLASH));
		String fullPathNameWithoutSlash = storage.getCurrentAbsoluteTextFilePath();
		File tempFile = new File(fullPathNameWithoutSlash);
		assertTrue(tempFile.exists());
	}

	@Test
	public final void testChangeAbsolutePathWithDifferentFile() throws IOException, TaskListIOException {
		storage.saveTasks(tempArrayList2);
		assertTrue(storage.changePath(PATH_NAME_WITH_TEXT_FILE));
		String fullPathNameWithTextFile = storage.getCurrentAbsoluteTextFilePath();
		File tempFile = new File(fullPathNameWithTextFile);
		assertTrue(tempFile.exists());
		ArrayList<TaskObject> tasks = storage.loadTasks();

		assertTrue(tempArrayList2.equals(tasks));
	}

	@Test
	public final void testPreviousFileIsDeleted() throws IOException {
		File previousFile = new File(storage.getCurrentAbsoluteTextFilePath());
		assertTrue(storage.changePath(PATH_NAME_TEST_DELETE));
		assertFalse(previousFile.exists());
	}

	@Test
	public final void testChangeToInvalidPath() throws IOException {
		assertTrue(storage.changePath(PATH_NAME_WITH_TEXT_FILE));
		String fullPathNameWithTextFile = storage.getCurrentAbsoluteTextFilePath();
		assertFalse(storage.changePath(PATH_NAME_INVALID));
		String path = storage.getCurrentAbsoluteTextFilePath();
		File tempFile = new File(path);
		assertTrue(path.equals(fullPathNameWithTextFile));
		assertTrue(tempFile.exists());
	}

	@Test
	public final void testUndoPath() throws IOException {
		storage.changePath(PATH_NAME_WITHOUT_SLASH);
		String fullPathNameWithoutSlash = storage.getCurrentAbsoluteTextFilePath();
		storage.changePath(PATH_NAME_INVALID);
		assertTrue(storage.undoPath());
		String path = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(path.equals(fullPathNameWithoutSlash));
	}

	@Test
	public final void testRedoPath() throws IOException {
		storage.changePath(PATH_NAME_WITH_TEXT_FILE);
		storage.changePath(PATH_NAME_WITHOUT_SLASH);
		String fullPathNameWithoutSlash = storage.getCurrentAbsoluteTextFilePath();
		storage.undoPath();
		assertTrue(storage.redoPath());
		String path = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(path.equals(fullPathNameWithoutSlash));
	}
	
	@Test
	public final void testRedoPathWhenNoFuture() throws InvalidPathException, IOException{
		while(storage.redoPath()){}
		assertFalse(storage.redoPath());
	}
	
	//Test if storage undo and does a valid change PATH does redo PATH remains
	@Test
	public final void testStorageUndoAndRedo() throws IOException{
		storage.changePath(PATH_NAME_WITHOUT_SLASH);
		storage.changePath(PATH_NAME_WITH_TEXT_FILE);
		storage.undoPath();
		storage.changePath(PATH_NAME_UNIQUE_FILE_NAME);
		String newFilePath = storage.getCurrentAbsoluteTextFilePath();
		assertFalse(storage.redoPath()); // if it exist would be the path with
		String path = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(path.equals(newFilePath));
	}
	
	@Test
	public final void testChangePathWithRelativePath() throws IOException{
		assertTrue(storage.changePath(PATH_NAME_RELATIVE));
		String absolutePath = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(pathManipulator.isAbsolutePath(absolutePath));
	}
	
	//This test test changing of path with relative path and path with only textfile name
	//then it will check that it has indeed change the fileName of the path
	@Test
	public final void changePathWithOnlyFileName() throws IOException{
		storage.changePath(PATH_NAME_RELATIVE);
		String absolutePath = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(storage.changePath(PATH_NAME_TEXT_FILE_ONLY));
		String newTextFilePath = storage.getCurrentAbsoluteTextFilePath();
		assertTrue(newTextFilePath.contains(PATH_NAME_TEXT_FILE_ONLY));
		assertFalse(absolutePath.equals(newTextFilePath));
	}
	
	@Test
	public final void changePathWithMultipleLocalPaths() throws IOException{
		assertTrue(storage.changePath(PATH_NAME_RELATIVE_ONLY));
		String relativePath = storage.getCurrentAbsoluteTextFilePath();
		File tempFile = new File(relativePath);
		assertTrue(tempFile.exists());
	}
	
	@Test
	public final void testUndoCommandWhenNothingThere() throws IOException{
		while(storage.undoAlias()){}
		assertFalse(storage.undoAlias());
	}
	
	@Test
	public final void testCleanAliasFile() throws IOException, AddDuplicateAliasException{
		storage.cleanAliasFile();
		storage.addAlias(COMMAND_ADD, ALIAS_ADD_INCLUDE);
		storage.cleanAliasFile();
		HashMap<String, String> emptyAlias = storage.getAlias();
		assertTrue(emptyAlias.isEmpty());
	}
	
	@Test
	public final void testAddAliasFile() throws AddDuplicateAliasException, IOException{
		storage.cleanAliasFile();
		storage.addAlias(COMMAND_ADD, ALIAS_ADD_INCLUDE);
		assertTrue(storage.getAlias(ALIAS_ADD_INCLUDE).equals(COMMAND_ADD));
	}
	
	@Test
	public final void testDuplicateAliasAdded() throws IOException, AddDuplicateAliasException{
		boolean isAddExceptionThrown = false;
		try{
			storage.cleanAliasFile();
			storage.addAlias(COMMAND_CHANGE_PATH, ALIAS_ADD_INCLUDE);
			storage.addAlias(COMMAND_DELETE, ALIAS_ADD_INCLUDE);
		}catch(AddDuplicateAliasException e){
			isAddExceptionThrown = true;
		}
		assertTrue(isAddExceptionThrown);
	}
	
	@Test
	public final void testSaveAlias() throws IOException{
		storage.cleanAliasFile();
		fullAlias = new HashMap<String,String>();
		fullAlias.put(ALIAS_ADD_INCLUDE, COMMAND_ADD);
		fullAlias.put(ALIAS_CHANGE_PATH_CD, COMMAND_CHANGE_PATH);
		fullAlias.put(ALIAS_ADD_PUSH, COMMAND_ADD);
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		fullAlias.put(ALIAS_DELETE_REMOVE, COMMAND_DELETE);

		storage.saveAlias(fullAlias);
		assertTrue(storage.getAlias().equals(fullAlias));
	}
	
	@Test
	public final void testRemoveAnAlias() throws IOException{
		storage.cleanAliasFile();
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		storage.removeAlias(ALIAS_EDIT_MODIFY);
		HashMap<String, String> aliasRemovedContent = storage.getAlias();
		assertFalse(aliasRemovedContent.containsKey(ALIAS_EDIT_MODIFY));
	}
	
	@Test
	public final void testUndoAlias() throws IOException{
		storage.cleanAliasFile();
		fullAlias = new HashMap<String,String>();
		fullAlias.put(ALIAS_ADD_INCLUDE, COMMAND_ADD);
		fullAlias.put(ALIAS_CHANGE_PATH_CD, COMMAND_CHANGE_PATH);
		fullAlias.put(ALIAS_ADD_PUSH, COMMAND_ADD);
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		fullAlias.put(ALIAS_DELETE_REMOVE, COMMAND_DELETE);
		storage.saveAlias(fullAlias);
		storage.removeAlias(ALIAS_EDIT_MODIFY);	
		assertTrue(storage.undoAlias());
		HashMap<String, String> undoedAlias = storage.getAlias();
		assertTrue(undoedAlias.equals(fullAlias));
	}
	
	@Test
	public final void testRedoAlias() throws IOException{
		storage.cleanAliasFile();
		fullAlias = new HashMap<String,String>();
		fullAlias.put(ALIAS_ADD_INCLUDE, COMMAND_ADD);
		fullAlias.put(ALIAS_CHANGE_PATH_CD, COMMAND_CHANGE_PATH);
		fullAlias.put(ALIAS_ADD_PUSH, COMMAND_ADD);
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		fullAlias.put(ALIAS_DELETE_REMOVE, COMMAND_DELETE);
		storage.saveAlias(fullAlias);
		storage.removeAlias(ALIAS_EDIT_MODIFY);	
		HashMap<String, String> aliasRemovedContent = storage.getAlias();
		storage.undoAlias();
		assertTrue(storage.redoAlias());
		HashMap<String, String> redoedAlias = storage.getAlias();
		assertTrue(redoedAlias.equals(aliasRemovedContent));
	}
	
	@Test
	public final void testRedoCommandWhenThereIsNot() throws IOException{
		storage.cleanAliasFile();
		fullAlias = new HashMap<String,String>();
		fullAlias.put(ALIAS_ADD_INCLUDE, COMMAND_ADD);
		fullAlias.put(ALIAS_CHANGE_PATH_CD, COMMAND_CHANGE_PATH);
		fullAlias.put(ALIAS_ADD_PUSH, COMMAND_ADD);
		fullAlias.put(ALIAS_EDIT_MODIFY, COMMAND_EDIT);
		fullAlias.put(ALIAS_DELETE_REMOVE, COMMAND_DELETE);
		storage.saveAlias(fullAlias);
		storage.removeAlias(ALIAS_EDIT_MODIFY);
		HashMap<String, String> aliasRemovedContent = storage.getAlias();
		storage.redoAlias();
		HashMap<String, String> redoedAlias = storage.getAlias();
		assertTrue(redoedAlias.equals(aliasRemovedContent));
	}
	
	@After
	public void placeAllContentsBackToOriginal() throws IOException, TaskListIOException{
		storage.changePath(currentTextFilePath);
		storage.saveTasks(currentTasks);
		storage.saveAlias(currentAlias);
	}
}
