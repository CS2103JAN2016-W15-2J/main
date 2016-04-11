/** @@author A0108371L */
package tasknote.logic.JUnitTests;

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

public class DoneTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	ArrayList<String> contents;
	String command;
	String feedback;
	String output;
	
	@Test
	// @condition: Task File must be empty before executing this test
	public void testMarkAsComplete() {
		
		storeContents();
		
		String taskName;
		ArrayList<TaskObject> list;
		TaskObject task;
		
		command = "add Buy Vegetables next tue";
		feedback = tnc.executeCommand(command);
		command = "add Complete Assignment by tonight 9pm";
		feedback = tnc.executeCommand(command);
		
		
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
		
		fillContents();
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
