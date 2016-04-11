/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;
import tasknote.shared.Constants;

public class DeleteTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;
	
	@Test
	public void testDelete() {
		
		resetTaskContents();
		
		addCommands();
		
		command = "delete 1";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deleted 1 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "  delete 1 ";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deleted 1 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "  delete 1 3 - 5";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deleted 4 task(s) Successfully";
		Assert.assertEquals(output, feedback);
		
		/*The following are boundary cases for the ‘negative value’ partitions */
		command = "  delete    99";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deletion Failed\n\n";
		output = output.concat(Constants.WARNING_INVALID_DELETE_INDEX);
		System.out.println(feedback);
		System.out.println(output);
		Assert.assertEquals(output, feedback);
		
		command = "  delete    1 3 99";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deletion Failed\n\n";
		output = output.concat(Constants.WARNING_INVALID_DELETE_INDEX);
		Assert.assertEquals(output, feedback);

		command = "  delete    ";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deletion Failed\n\n";
		output = output.concat(Constants.WARNING_EMPTY_DELETEID_LIST);
		Assert.assertEquals(output, feedback);
		
		command = "  delete 1 2 3";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "Deletion Failed\n\n";
		output = output.concat(Constants.WARNING_INVALID_DELETE_INDEX);
		Assert.assertEquals(output, feedback);
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
	
	public static void addCommands(){
		String command;
		String feedback;
		TaskNoteControl tnc = new TaskNoteControl();
		
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 2 on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 3 on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 4 on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 5 on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 6 on Saturday";
		feedback = tnc.executeCommand(command);
		command = "add Kids Marathon 7 on Saturday";
		feedback = tnc.executeCommand(command);
	}

}
