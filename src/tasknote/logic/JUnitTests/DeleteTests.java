/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.TaskObject;
import tasknote.shared.Constants;

public class DeleteTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	ArrayList<String> contents;
	String command;
	String feedback;
	String output;
	
	@Test
	public void testDelete() {
		
		storeContents();
		
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
		
		fillContents();
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
