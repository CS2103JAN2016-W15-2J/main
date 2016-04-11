/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class AddTests {

	TaskNoteControl tnc;
	ArrayList<TaskObject> obj;
	ArrayList<String> contents;
	String command;
	String feedback;
	String output;
	String details;
	
	@Test
	public void testAdd() {
		
		storeContents();
		
		tnc = new TaskNoteControl();
		
		command = "add breakfast 5pm";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		String name = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "breakfast 5pm");
		output = output.concat(name);
		System.out.println(feedback);
		System.out.println("X" + output);
		Assert.assertEquals(output, feedback);
		
		command = "add do work    at 9am";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 2, "do work");
		details = details.concat(String.format(Constants.STRING_TASK_DATE, getDate(9, 0)));
		details = details.concat(String.format(Constants.STRING_TASK_TIME, formatTime(9, 00, "AM")));
		output = output.concat(details);
		System.out.println(feedback);
		System.out.println(output);
		Assert.assertEquals(output, feedback);
		
		command = "add Complete Assignment by 8pm";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 2, "Complete Assignment");
		details = details.concat(String.format(Constants.STRING_TASK_DATE, getDate(20, 0)));
		details = details.concat(String.format(Constants.STRING_TASK_TIME, formatTime(8, 00, "PM")));
		output = output.concat(details);
		Assert.assertEquals(output, feedback);
		
		command = "add      wedding at New York";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 2, "wedding");
		details = details.concat(String.format(Constants.STRING_TASK_LOCATION, "New York"));
		output = output.concat(details);
		//System.out.println(feedback);
		//System.out.println(output);
		Assert.assertEquals(output, feedback);
		
		
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "Kids Marathon");
		output = output.concat(details);
		//System.out.println(output);
		//System.out.println(feedback);
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
	
	private static String getDate(int hour, int min){
		
		LocalDateTime now = LocalDateTime.now();
		if(now.getHour() >= hour && now.getMinute() > min){
			now = now.plusDays(1);
		}
		int currentDay = now.getDayOfMonth();
		String month = now.getMonth().name();
		month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
		int currentYear = now.getYear();
		String date = "%d %s %d";
		date = String.format(date, currentDay, month, currentYear);
		return date;
	}
	
	private static String getTime(){
		LocalDateTime now = LocalDateTime.now();
		int hour = now.getHour();
		int minute = now.getMinute();
		String amPm;
		if(hour >= 12) {
			amPm = "am";
		} else {
			amPm = "pm";
		}
		String time = "%d:%d%s";
		time = String.format(time, hour, minute, amPm);
		return time;
	}
	
	private static String formatTime(int hour, int minute, String period) {
		String time = "%02d:%02d%s";
		time = String.format(time, hour, minute, period);
		return time;
	}
	

}
