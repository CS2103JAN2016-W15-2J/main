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

public class UndoTests {
	
	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	ArrayList<String> contents;
	String command;
	String feedback;
	String output;

	@Test
	public void testUndo() {
		
		storeContents();
		
		
		//Test Case 1
		command = "add breakfast 5pm";
		tnc.executeCommand(command);
		
		command = "undo";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "The Last ADD Operation has been Undone Successfully";
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
		feedback = feedback.trim();
		output = "The Last ADD Operation has been Undone Successfully";
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
		feedback = feedback.trim();
		output = "The Last DELETE Operation has been Undone Successfully";
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
		feedback = feedback.trim();
		output = "The Last DELETE Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "The Last DELETE Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		obj = tnc.getDisplayList();
		print(obj);
		
		
		//Test Case 5
		command = "delete 1 3";
		tnc.executeCommand(command);
		
		command = "undo   ";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = "The Last DELETE Operation has been Undone Successfully";
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
		feedback = feedback.trim();
		output = "The Last UPDATE Operation has been Undone Successfully";
		Assert.assertEquals(output, feedback);
		
		obj = tnc.getDisplayList();
		print(obj);
		
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
	
	public void print(ArrayList<TaskObject> obj) {
		for(int i = 0; i < obj.size(); i++) {
			System.out.println(obj.get(i).getTaskName());
		}
		System.out.println();
	}

}
