/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import org.junit.Test;
import org.junit.Assert;

import tasknote.shared.Constants;
import tasknote.shared.TaskObject;
import tasknote.logic.TaskNote;
import tasknote.logic.TaskNoteControl;
import tasknote.logic.ShowInterval;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskNoteTests {

	TaskNote note = new TaskNote();
	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<String> contents;
	String feedback;
	String output;
	String command;
	
	
	@Test
	public void testAddTask() {
		
		storeContents();
		
		/*
		 * The following is a boundary case for the ‘negative value’ partition
		 */
		
		//feedback = note.addTask(null);
		//output = Constants.MESSAGE_ADD_UNSUCCESSFUL;
		//Assert.assertEquals(output, feedback);
		
		

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		note = new TaskNote();
		feedback = note.addTask(new TaskObject("New Task Object"));
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		String name = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "New Task Object");
		output = output.concat(name);
		//System.out.println(feedback);
		//System.out.println(output);
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}
	
	@Test
	public void testDeleteTask() {
		
		storeContents();
		
		ArrayList<Integer> ids = new ArrayList<Integer>();

		/*
		 * The following are boundary cases for the ‘negative value’ partitions
		 */

		// Case 1
		ids.add(-1);
		ids.add(-9);
		ids.add(-200);
		feedback = note.deleteTask(ids);
		output = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		output = output.concat("\n\nInvalid Deletion Index found. Please enter valid task IDs to delete tasks.");
		//System.out.println(feedback);
		//System.out.println(output);
		Assert.assertEquals(output, feedback);

		// Case 2
		populateTasks();
		ids.add(200);
		feedback = note.deleteTask(ids);
		output = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		output = output.concat("\n\nInvalid Deletion Index found. Please enter valid task IDs to delete tasks.");
		//System.out.println(feedback);
		//System.out.println(output);
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		populateTasks();
		ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		feedback = note.deleteTask(ids);
		feedback = feedback.trim();
		output = String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, 2);
		//System.out.println(feedback);
		//System.out.println(output);
		Assert.assertEquals(output, feedback);

		/* This is another boundary case for the ‘negative value’ partitions */
		ids = new ArrayList<Integer>();
		feedback = note.deleteTask(ids);
		output = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		output = output.concat("\n\n• No Delete IDs specified to Delete Tasks.\n• Please specify Task ID(s) to delete corresponding Task(s).");
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}

	@Test
	public void testSearchTasks() {
		
		storeContents();
		
		
		populateTasks();
		ArrayList<Integer> ids = new ArrayList<Integer>();

		/*
		 * The following are boundary cases for the ‘negative value’ partitions
		 */

		// Case 1
		ids.add(-1);
		ids.add(-2);
		feedback = note.searchTasks(ids);
		output = Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		// Case 2
		ids = new ArrayList<Integer>();
		feedback = note.searchTasks(ids);
		output = Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		feedback = note.searchTasks(ids);
		output = String.format(Constants.MESSAGE_SEARCH_SUCCESSFUL, 2);
		Assert.assertEquals(output, feedback);
		
		fillContents();

	}

	@Test
	public void testUpdateTask() {
		
		storeContents();
		
		populateTasks();

		/*
		 * The following are boundary cases for the ‘negative value’ partitions
		 */

		// Case 1
		int id = -1;
		feedback = note.updateTask(id, null);
		output = Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		// Case 2
		id = 0;
		feedback = note.updateTask(id, null);
		output = Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		// Case 3
		id = 1;
		feedback = note.updateTask(id, null);
		output = Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		id = 1;
		feedback = note.updateTask(id, new TaskObject("New Task"));
		feedback = feedback.trim();
		output = Constants.MESSAGE_UPDATE_SUCCESSFUL;
		String name = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "New Task");
		output = output.concat(name);
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}

	@Test
	public void testMarkCompleted() {
		
		storeContents();
		
		
		/*
		 * The following are boundary cases for the ‘negative value’ partitions
		 */

		/*
		 * Note: Comment out "taskNote.loadTasks();" in TaskNote Control class
		 * in Class Constructor before testing
		 * 
		 **/

		// Case 1
		populateTasks();
		feedback = note.setTaskCompletionStatus(new TaskObject("Some New Task"), true);
		output = String.format(Constants.MESSAGE_DONE_UNSUCCESSFUL);
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */

		feedback = note.setTaskCompletionStatus(new TaskObject("breakfast 10:00"), true);
		output = String.format(Constants.MESSAGE_DONE_SUCCESSFUL, "breakfast 10:00");
		Assert.assertEquals(output, feedback);
		
		fillContents();

	}
	
	@Test
	public void testUndoCommand() {
		
		storeContents();
		
		note.addTask(new TaskObject("New Task Object"));
		feedback = note.undoLastCommand();
		output = String.format(Constants.MESSAGE_UNDO_SUCCESSFUL, "ADD");
		Assert.assertEquals(output, feedback);
		
		feedback = note.undoLastCommand();
		output = Constants.MESSAGE_UNDO_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);
		
		note.markTaskAsComplete(0);
		feedback = note.undoLastCommand();
		output = String.format(Constants.MESSAGE_UNDO_SUCCESSFUL, "DONE");
		Assert.assertEquals(output, feedback);
		
		note.changeFilePath("/Users/Girish/");
		feedback = note.undoLastCommand();
		output = String.format(Constants.MESSAGE_UNDO_SUCCESSFUL, "CHANGE_FILE_PATH");
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}
	
	@Test
	public void testRedoCommand() {
		
		storeContents();
		
		note.addTask(new TaskObject("New Task Object"));
		note.undoLastCommand();
		feedback = note.redoLastUndoCommand();
		output = String.format(Constants.MESSAGE_REDO_SUCCESSFUL, "DELETE");
		Assert.assertEquals(output, feedback);
		
		feedback = note.redoLastUndoCommand();
		output = Constants.MESSAGE_REDO_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);
		
		note.markTaskAsComplete(0);
		note.undoLastCommand();
		feedback = note.redoLastUndoCommand();
		output = String.format(Constants.MESSAGE_REDO_SUCCESSFUL, "DONE");
		Assert.assertEquals(output, feedback);
		
		
		note.changeFilePath("/Users/Girish/");
		note.undoLastCommand();
		feedback = note.redoLastUndoCommand();
		output = String.format(Constants.MESSAGE_REDO_SUCCESSFUL, "CHANGE_FILE_PATH");
		Assert.assertEquals(output, feedback);
		
		
		fillContents();
	}
	
	@Test
	public void testMarkTaskAsComplete() {
		
		storeContents();
		
		//note = new TaskNote();
		
		populateTasks();
		feedback = note.markTaskAsComplete(0);
		output = String.format(Constants.MESSAGE_DONE_SUCCESSFUL, "breakfast 10:00");
		Assert.assertEquals(output, feedback);
		
		feedback = note.markTaskAsComplete(0);
		output = String.format(Constants.WARNING_EXECUTE_DONE_TASK_COMPLETED, 1);
		Assert.assertEquals(output, feedback);
		
		feedback = note.markTaskAsComplete(99);
		output = Constants.MESSAGE_DONE_UNSUCCESSFUL.concat(Constants.STRING_CONSTANT_NEWLINE);
		output = output.concat(String.format(Constants.WARNING_EXECUTE_DONE_INVALID_ID, 100));
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}
	
	@Test
	public void testMarkTaskAsIncomplete() {
		
		storeContents();
		
		populateTasks();
		note.markTaskAsComplete(0);
		feedback = note.markTaskAsIncomplete(0);
		output = String.format(Constants.MESSAGE_UNDONE_SUCCESSFUL, "breakfast 10:00");
		Assert.assertEquals(output, feedback);
		
		feedback = note.markTaskAsIncomplete(0);
		output = String.format(Constants.WARNING_EXECUTE_DONE_TASK_INCOMPLETE, 1);
		Assert.assertEquals(output, feedback);
		
		feedback = note.markTaskAsIncomplete(99);
		output = Constants.MESSAGE_UNDONE_UNSUCCESSFUL.concat(Constants.STRING_CONSTANT_NEWLINE);
		output = output.concat(String.format(Constants.WARNING_EXECUTE_DONE_INVALID_ID, 100));
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}
	
	@Test
	public void testChangeFilePath() {
		
		storeContents();
		
		feedback = note.changeFilePath("");
		output = String.format(Constants.MESSAGE_CHANGE_PATH_UNSUCCESSFUL, "");
		Assert.assertEquals(output, feedback);
		
		feedback = note.changeFilePath(null);
		output = String.format(Constants.MESSAGE_CHANGE_PATH_UNSUCCESSFUL, null);
		Assert.assertEquals(output, feedback);
		
		feedback = note.changeFilePath("/Users/Girish/");
		output = String.format(Constants.MESSAGE_CHANGE_PATH_SUCCESSFUL, "/Users/Girish/");
		Assert.assertEquals(output, feedback);
		
		feedback = note.changeFilePath("/Users/Girish92/");
		output = String.format(Constants.MESSAGE_CHANGE_PATH_UNSUCCESSFUL, "/Users/Girish92/");
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}
	
	@Test
	public void testShowTasks() {
		
		storeContents();
		
		populateTasks();
		
		feedback = note.showTasks(ShowInterval.TODAY, -1);
		output = feedback = String.format(Constants.MESSAGE_SHOW_SUCCESSFUL_DEADLINE_INTERVAL, 0, 
				0, ShowInterval.TODAY);
		System.out.println(feedback);
		System.out.println(output);
		Assert.assertEquals(output, feedback);
		
		feedback = note.showTasks(null, -1);
		output = String.format(Constants.MESSAGE_SHOW_UNSUCCESSFUL, ShowInterval.ALL);
		Assert.assertEquals(output, feedback);
		
		
		feedback = note.showTasks(ShowInterval.ALL, -1);
		output = Constants.MESSAGE_SHOW_SUCCESSFUL_ALL;
		Assert.assertEquals(output, feedback);
		
		fillContents();
	}

	public void printTasks(ArrayList<TaskObject> displayList){
		System.out.println("DISPLAY LIST SIZE = " + displayList.size());
		for(int i = 0; i < displayList.size(); i++) {
			System.out.println(displayList.get(i).getTaskName());
		}
	}
	public void populateTasks() {
		note = new TaskNote();
		feedback = note.addTask(new TaskObject("breakfast 10:00"));
		feedback = note.addTask(new TaskObject("lunch 14:00"));
		feedback = note.addTask(new TaskObject("dinner 20:00"));
		note.refreshDisplay(note.getTaskList());
	}
	
	private void storeContents() {
		try{
			String path = getFilePath();
			contents = copyFileContents(path);
			resetTaskContents(new ArrayList<String>());
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	private void fillContents() {
		try{
			resetTaskContents(contents);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	private String getFilePath() {
		String defaultPath = new String();
		try{
			Path correctPath = Paths.get("pathContents.txt");
			List<String> pathInList = Files.readAllLines(correctPath);
			for(int i  = 0; i < pathInList.size(); i++) {
				System.out.println(pathInList.get(i));
			}
			defaultPath = pathInList.get(0); // Get first line
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultPath;
	}
	
	private ArrayList<String> copyFileContents(String path) {
		ArrayList<String> resetList = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File(path));
			while (s.hasNextLine()){
			    resetList.add(s.nextLine());
			}
			s.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resetList;
	}
	
	private void resetTaskContents(ArrayList<String> list) throws IOException {
		Path correctPath = Paths.get("pathContents.txt");
		List<String> pathInList = Files.readAllLines(correctPath);
		String defaultPath = pathInList.get(0); // Get first line
		//System.out.println(defaultPath);
		Path intendedPath = Paths.get(defaultPath);
		//System.out.println(intendedPath.toString());
		
		try {
			Files.write(intendedPath, list, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}