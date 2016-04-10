/** @@author A0108371L */
package tasknote.logic.JUnitTests;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import tasknote.logic.TaskNoteControl;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class AddTests {

	TaskNoteControl tnc = new TaskNoteControl();
	ArrayList<TaskObject> obj;
	String command;
	String feedback;
	String output;
	String details;
	
	@Test
	public void testAdd() {
		
		command = "add breakfast 5pm";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		String name = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "breakfast 5pm");
		output = output.concat(name);
		Assert.assertEquals(output, feedback);
		
		command = "add do work    at 9am";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 2, "do work");
		details = details.concat(String.format(Constants.STRING_TASK_DATE, getDate()));
		details = details.concat(String.format(Constants.STRING_TASK_TIME, formatTime(9, 00, "AM")));
		output = output.concat(details);
		Assert.assertEquals(output, feedback);
		
		command = "add Complete Assignment by 8pm";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 3, "Complete Assignment");
		details = details.concat(String.format(Constants.STRING_TASK_DATE, getDate()));
		details = details.concat(String.format(Constants.STRING_TASK_TIME, formatTime(8, 00, "PM")));
		output = output.concat(details);
		Assert.assertEquals(output, feedback);
		
		command = "add      wedding at New York";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 2, "wedding");
		details = details.concat(String.format(Constants.STRING_TASK_LOCATION, " New York"));
		output = output.concat(details);
		Assert.assertEquals(output, feedback);
		
		/*
		command = "add Kids Marathon on Saturday";
		feedback = tnc.executeCommand(command);
		feedback = feedback.trim();
		output = Constants.MESSAGE_ADD_SUCCESSFUL;
		details = String.format(Constants.STRING_TASK_NAME_INDEX, 1, "Kids Marathon");
		output = output.concat(details);
		//System.out.println(output);
		//System.out.println(feedback);
		Assert.assertEquals(output, feedback);
		*/
	}
	
	private static String getDate(){
		LocalDateTime now = LocalDateTime.now();
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
