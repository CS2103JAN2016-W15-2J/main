package tasknote.logic.JUnitTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;

public class DeleteTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;
	
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
		
		command = "  delete 1 ";
		feedback = tnc.executeCommand(command);
		output = "Deleted 1 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "  delete 1, 3-5";
		feedback = tnc.executeCommand(command);
		output = "Deleted 4 task(s) Successfully";
		//Assert.assertEquals(output, feedback);
		
		/*The following are boundary cases for the ‘negative value’ partitions */
		command = "  delete    99";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);
		
		command = "  delete    1 3 99";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);

		command = "  delete    ";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);
		
		command = "  delete 1 2 3";
		feedback = tnc.executeCommand(command);
		output = "Deletion Failed";
		Assert.assertEquals(output, feedback);
		
		
	}

}
