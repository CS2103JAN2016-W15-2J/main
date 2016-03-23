package tasknote.logic.JUnitTests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class UndoTests {
	
	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;

	@Test
	public void testUndo() {
		
		//Test Case 1
		command = "add breakfast 5pm";
		tnc.executeCommand(command);
		
		command = "undo";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		
		//Test Case 2
		command = "add breakfast 5pm";
		tnc.executeCommand(command);
		command = "add breakfast 6pm";
		tnc.executeCommand(command);
		command = "add breakfast 7pm";
		tnc.executeCommand(command);
		
		//obj = tnc.getDisplayList();
		//print(obj);
		
		command = "undo";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		//obj = tnc.getDisplayList();
		//print(obj);
		
		
		//Test Case 3
		command = "add breakfast 7pm";
		tnc.executeCommand(command);
		command = "add breakfast 8pm";
		tnc.executeCommand(command);
		obj = tnc.getDisplayList();
		print(obj);

		command = "delete 3";
		tnc.executeCommand(command);
		obj = tnc.getDisplayList();
		print(obj);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		obj = tnc.getDisplayList();
		print(obj);
		
		
		//Test Case 4
		command = "delete 3";
		tnc.executeCommand(command);
		command = "delete 1";
		tnc.executeCommand(command);
		command = "delete 2";
		tnc.executeCommand(command);

		obj = tnc.getDisplayList();
		print(obj);
		
		command = "      undo   ";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		obj = tnc.getDisplayList();
		print(obj);
		
		
		//Test Case 5
		command = "delete 1 3";
		tnc.executeCommand(command);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		obj = tnc.getDisplayList();
		print(obj);
		
		
		//Test Case 6
		command = "edit 1 brunch 5.15pm";
		tnc.executeCommand(command);
		
		obj = tnc.getDisplayList();
		print(obj);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		output = "The Last Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		obj = tnc.getDisplayList();
		print(obj);
		
	}
	
	public void print(ArrayList<TaskObject> obj) {
		for(int i = 0; i < obj.size(); i++) {
			System.out.println(obj.get(i).getTaskName());
		}
		System.out.println();
	}

}