package tasknote.logic.JUnitTests;

import org.junit.Test;
import org.junit.Assert;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;
import tasknote.logic.TaskNote;
import tasknote.logic.TaskNoteControl;

import java.util.ArrayList;
import java.util.logging.Level;

public class TaskNoteTests {

	TaskNote note = new TaskNote();
	TaskNoteControl tnc = new TaskNoteControl();
	String feedback;
	String output;
	String command;

	@Test
	public void testDeleteTask() {
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
		Assert.assertEquals(output, feedback);

		// Case 2
		populateTasks();
		ids.add(200);
		feedback = note.deleteTask(ids);
		output = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		ids = new ArrayList<Integer>();
		ids.add(1);
		ids.add(2);
		feedback = note.deleteTask(ids);
		output = String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, 2);
		Assert.assertEquals(output, feedback);

		/* This is another boundary case for the ‘negative value’ partitions */
		ids = new ArrayList<Integer>();
		feedback = note.deleteTask(ids);
		output = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);
	}

	@Test
	public void testSearchTasks() {
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

	}

	@Test
	public void testUpdateTask() {
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

		// Case 4
		id = 1;
		feedback = note.updateTask(id, new TaskObject());
		output = Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */
		id = 1;
		feedback = note.updateTask(id, new TaskObject("New Task"));
		output = Constants.MESSAGE_UPDATE_SUCCESSFUL;
		Assert.assertEquals(output, feedback);
	}

	@Test
	public void testMarkCompleted() {
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
		feedback = note.markTaskAsCompleted(new TaskObject("Some New Task"));
		output = String.format(Constants.MESSAGE_DONE_UNSUCCESSFUL);
		Assert.assertEquals(output, feedback);

		/*
		 * The following is a test case that is part of the partition that is
		 * valid
		 */

		feedback = note.markTaskAsCompleted(new TaskObject("breakfast 10:00"));
		output = String.format(Constants.MESSAGE_DONE_SUCCESSFUL, "breakfast 10:00");
		Assert.assertEquals(output, feedback);

	}

	public void populateTasks() {
		tnc = new TaskNoteControl();
		command = "add breakfast 10:00";
		feedback = tnc.executeCommand(command);
		command = "add lunch at 14:00";
		feedback = tnc.executeCommand(command);
		command = "add dinner at Marriot by 21:00";
		feedback = tnc.executeCommand(command);
	}

}