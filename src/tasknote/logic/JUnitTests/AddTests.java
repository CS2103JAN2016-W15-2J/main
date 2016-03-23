package tasknote.logic.JUnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class AddTests {

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
		output = "Added Successfully: 1. Complete Assignment";
		Assert.assertEquals(output, feedback);
		
		command = "add      wedding at New York";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 4. wedding";
		Assert.assertEquals(output, feedback);
		
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		output = "Added Successfully: 2. Kids Marathon";
		Assert.assertEquals(output, feedback);
	}

}
