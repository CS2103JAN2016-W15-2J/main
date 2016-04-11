/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.logic.TaskNote;
import tasknote.shared.TaskObject;
import tasknote.shared.Constants;

public class DoneTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;
	
	@Test
	// @condition: Task File must be empty before executing this test
	public void testMarkAsComplete() {
		
		resetTaskContents();
		
		String taskName;
		ArrayList<TaskObject> list;
		TaskObject task;
		
		command = "add Buy Vegetables next tue";
		feedback = tnc.executeCommand(command);
		command = "add Complete Assignment by tonight 9pm";
		feedback = tnc.executeCommand(command);
		
		//First test case
		command = "done 1";
		feedback = tnc.executeCommand(command);
		
		list = tnc.getDisplayList();
		task = list.get(0);
		
		output = "Task \"%s\" has been marked as completed Successfully";
		taskName = task.getTaskName();
		feedback = feedback.trim();
		Assert.assertEquals(String.format(output, taskName), feedback);
		
		
		//Second test case
		command = "done 200";
		output = "Mark as complete failed\n\n";
		output = output.concat("Invalid task ID specified: 200.\n");
		output = output.concat("Please Specify a valid TaskID to mark Task as Completed.");
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		Assert.assertEquals(output, feedback);
		
		
		//Third test case
		command = "done 0";
		output = "Mark as complete failed\n\n";
		output = output.concat("Invalid task ID specified: 0.\n");
		output = output.concat("Please Specify a valid TaskID to mark Task as Completed.");
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		Assert.assertEquals(output, feedback);
		
		
		//Fourth test case
		command = "done 1";
		feedback = tnc.executeCommand(command);

		list = tnc.getDisplayList();
		task = list.get(0);
		//System.out.println(task);
		
		output = "Task 1 is already marked as completed.\n";
		output = output.concat("To mark task as incomplete, use the UNDONE command.");
		taskName = task.getTaskName();
		feedback = feedback.trim();
		Assert.assertEquals(String.format(output, taskName), feedback);
		
		tnc.executeCommand("undone 1");
	}
	
	private void resetTaskContents() {
		Path taskContentsPath = Paths.get("taskContents.txt");
		ArrayList<String> resetList = new ArrayList<>();
		
		try {
			Files.write(taskContentsPath, resetList, Charset.forName("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
