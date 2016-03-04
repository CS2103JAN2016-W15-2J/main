package tasknote.logic;

import org.junit.Assert;

import java.util.ArrayList;

import org.junit.Test;

import tasknote.shared.TaskObject;

public class TaskNoteControlTest {
	
	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;
	
	@Test
	public void testAdd() {
		command = "add breakfast 5pm";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 1. breakfast 5pm";
		Assert.assertEquals(output, feedback);
		
		command = "add do work    at 9am";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 2. do work";
		Assert.assertEquals(output, feedback);
		
		command = "add Complete Assignment by 8pm";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 3. Complete Assignment";
		Assert.assertEquals(output, feedback);
		
		command = "add      wedding at New York";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 4. wedding";
		Assert.assertEquals(output, feedback);
		
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 5. Kids Marathon";
		Assert.assertEquals(output, feedback);
		
		//TODO: Parser
		command = "add ";
		feedback = tnc.executeCommand(command);
		output = "Add Failed";
		//Assert.assertEquals(output, feedback);
	}
	
	@Test
	public void testDelete() {
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 2 on Saturday";
		feedback = tnc.executeCommand(command);
		
		command = "delete 1";
		feedback = tnc.executeCommand(command);
		output = "Deleted 1 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "  delete    99";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);
		
		command = "  delete    1 3 99";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);

		//TODO: Parser Check for length list
		command = "  delete    ";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		//Assert.assertEquals(output, feedback);
		
		//TODO: Parser
		command = "  delete 1 2 3";
		feedback = tnc.executeCommand(command);
		output = "Deleted 3 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		//TODO: Parser
		command = "  delete 1, 3-5";
		feedback = tnc.executeCommand(command);
		output = "Deleted 4 task(s) Successfully";
		//Assert.assertEquals(output, feedback);
	}
	
	@Test
	/**
	 * @condition: Task File must be empty before executing this test
	 */
	public void testMarkAsComplete() {
		String taskName;
		ArrayList<TaskObject> list;
		TaskObject task;
		
		command = "add Buy Vegetables next tue";
		feedback = tnc.executeCommand(command);
		command = "add Complete Assignment by tonight 9pm";
		feedback = tnc.executeCommand(command);
		
		/*First test case*/
		command = "done 1";
		feedback = tnc.executeCommand(command);
		
		list = tnc.getDisplayList();
		task = list.get(0);
		//System.out.println(task);
		
		output = "Task \"%s\" has been marked as completed successfully";
		taskName = task.getTaskName();
		Assert.assertEquals(String.format(output, taskName), feedback);
		
		/*Second test case*/
		command = "done 200";
		output = "Mark as complete failed";
		feedback = tnc.executeCommand(command);
		Assert.assertEquals(output, feedback);
		
		/*Third test case*/
		command = "done 0";
		output = "Mark as complete failed";
		feedback = tnc.executeCommand(command);
		Assert.assertEquals(output, feedback);
		
		/*Fourth test case*/
		command = "done 1";
		feedback = tnc.executeCommand(command);

		list = tnc.getDisplayList();
		task = list.get(0);
		//System.out.println(task);
		
		output = "Task \"%s\" has been marked as completed successfully";
		taskName = task.getTaskName();
		Assert.assertEquals(String.format(output, taskName), feedback);
	}

}
