/** @@author A0108371L */
package tasknote.logic;

import tasknote.logic.Commands.Command;
import tasknote.logic.Commands.AddCommand;
import tasknote.logic.Commands.DeleteCommand;
import tasknote.logic.Commands.ChangeCategoryCommand;
import tasknote.logic.Commands.ChangeFilePathCommand;
import tasknote.logic.Commands.HelpCommand;
import tasknote.logic.Commands.SearchCommand;
import tasknote.logic.Commands.UpdateCommand;
import tasknote.logic.Commands.DoneCommand;
import tasknote.logic.Commands.UndoneCommand;
import tasknote.logic.Commands.UndoCommand;
import tasknote.logic.Commands.RedoCommand;
import tasknote.logic.Commands.ShowCommand;
import tasknote.logic.ShowCategory;
import tasknote.logic.ShowInterval;
import tasknote.parser.Parser;
import tasknote.shared.TaskObject;
import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;

import java.util.ArrayList;

/**
 * This class is used to interact directly with the UI to process and execute
 * User Commands. 
 * The Command object will be used to create a new instance of
 * the corresponding Command Class. 
 * This Class interacts with the Parser Component, which parses the User Command 
 * and returns relevant data. 
 * Once each command is executed, the task list will be updated with the relevant 
 * task objects to be displayed to the User. Each method in this class that executes
 * a command returns a feedback String to the caller.
 *
 * @author Murali Girish Narayanan
 */

public class TaskNoteControl {

	private static TaskNote taskNote;
	private static Command command;
	private static Parser parser;

	public TaskNoteControl() {
		taskNote = new TaskNote();
		parser = new Parser();
		taskNote.loadTasks();
	}
	
	/**
	 * This operation returns the list to be displayed to the user
	 *
	 * @return List of TaskObjects
	 */
	public ArrayList<TaskObject> getDisplayList() {
		return taskNote.getDisplayList();
	}

	/**
	 * This operation gets the command type and executes the requested action
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	public String executeCommand(String userCommand) {

		COMMAND_TYPE commandType;
		try {
			parser.setInputString(userCommand);
			commandType = parser.getCommandType();
		} catch (Exception e) {
			commandType = parser.getCommandType();
		}
		String feedback = executeAction(commandType, userCommand);
		return feedback;
	}

	/**
	 * This operation executes the user command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeAction(COMMAND_TYPE commandType, String userCommand) {
		String response;
		taskNote.reIntializeSearchList();
		switch (commandType) {
		case ADD:
			response = executeAdd(userCommand);
			break;
		case DELETE:
			response = executeDelete(userCommand);
			break;
		case SEARCH:
			response = executeSearch(userCommand);
			break;
		case UPDATE:
			response = executeUpdate(userCommand);
			break;
		case UNDO:
			response = executeUndo();
			break;
		case REDO:
			response = executeRedo(userCommand);
			break;
		case DONE:
			response = executeMarkAsComplete(userCommand);
			break;
		case UNDONE:
			response = executeMarkAsIncomplete(userCommand);
			break;
		case CHANGE_FILE_PATH:
			response = executeChangeFilePath(userCommand);
			break;
		case SHOW:
			response = executeShow(userCommand);
			break;
		case CHANGE_CATEGORY:
			// TODO: Parser
			response = executeChangeCategory(userCommand);
			// response = "";
			break;
		case HELP:
			// TODO: Parser
			response = executeHelp(userCommand);
			break;
		case INVALID:
			response = Constants.WARNING_INVALID_COMMAND;
			break;
		case EXIT:
			System.exit(0);
		default:
			throw new Error("Unrecognized command type");
		}
		return response;
	}

	/**
	 * This operation executes the User's Add command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeAdd(String userCommand) {
		TaskObject taskObject;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			taskObject = parser.parseAdd(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskObject = parser.parseAdd(throwException);
		}
		command = new AddCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's Delete command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeDelete(String userCommand) {
		ArrayList<Integer> deleteIds;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			deleteIds = parser.parseDelete(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			//deleteIds = parser.parseDelete(throwException);
			deleteIds = new ArrayList<Integer>();
		}
		command = new DeleteCommand(taskNote, deleteIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's Search command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeSearch(String userCommand) {
		ArrayList<TaskObject> displayList = taskNote.getDisplayList();
		ArrayList<Integer> searchIds;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			searchIds = parser.parseSearch(displayList, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			searchIds = parser.parseSearch(displayList, throwException);
		}
		command = new SearchCommand(taskNote, searchIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's Update command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeUpdate(String userCommand) {
		int updateTaskId;
		TaskObject updatedTaskObject;
		boolean throwException = true;
		String parserUpdateIdFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		String parserUpdateObjectFeedback = new String(Constants.STRING_CONSTANT_SPACE);

		try {
			updateTaskId = parser.getTaskId(throwException);
		} catch (Exception e) {
			throwException = false;
			parserUpdateIdFeedback = e.getMessage();
			updateTaskId = parser.getTaskId(throwException);
		}

		TaskObject oldTaskObject = null;
		throwException = true;
		if (taskNote.isValidTaskId(updateTaskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			oldTaskObject = displayList.get(updateTaskId);
			try {
				updatedTaskObject = parser.parseUpdate(oldTaskObject, throwException);
			} catch (Exception e) {
				throwException = false;
				parserUpdateObjectFeedback = e.getMessage();
				updatedTaskObject = parser.parseUpdate(oldTaskObject, throwException);
			}
		} else {
			updatedTaskObject = null;
		}
		ArrayList<TaskObject> taskList = taskNote.getTaskList();
		int originalUpdateTaskId = taskList.indexOf(oldTaskObject);
		command = new UpdateCommand(taskNote, originalUpdateTaskId, updatedTaskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserUpdateIdFeedback);
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserUpdateObjectFeedback);
		return response;
	}

	/**
	 * This operation executes the User's Undo command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeUndo() {
		command = new UndoCommand(taskNote);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		return response;
	}

	/**
	 * This operation executes the User's Redo command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeRedo(String userCommand) {
		command = new RedoCommand(taskNote);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		return response;
	}

	/**
	 * This operation executes the User's Mark As Completed command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeMarkAsComplete(String userCommand) {
		int taskId;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			taskId = parser.getTaskId(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskId = parser.getTaskId(throwException);
		}
		command = new DoneCommand(taskNote, taskId);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's Mark As Incomplete command
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeMarkAsIncomplete(String userCommand) {
		int taskId;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			taskId = parser.getTaskId(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskId = parser.getTaskId(throwException);
		}
		command = new UndoneCommand(taskNote, taskId);
		/*TaskObject taskObject;
		if (taskNote.isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			taskObject = displayList.get(taskId);
		} else {
			taskObject = null;
		}
		command = new UndoneCommand(taskNote, taskObject);
		*/
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's request to change the directory the
	 * task file exists in
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeChangeFilePath(String userCommand) {
		String filePath;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			filePath = parser.parseFilePath(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			filePath = parser.parseFilePath(throwException);
		}
		command = new ChangeFilePathCommand(taskNote, filePath);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}

	/**
	 * This operation executes the User's request to show all tasks whose
	 * deadlines are within the specified time interval
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */

	private static String executeShow(String userCommand) {
		ShowInterval timeInterval;
		int countInterval;
		boolean throwException = true;
		String parserShowFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		String parserIntervalFeedback = new String(Constants.STRING_CONSTANT_SPACE);

		try {
			timeInterval = parser.parseShow(throwException);
		} catch (Exception e) {
			throwException = false;
			parserShowFeedback = e.getMessage();
			timeInterval = parser.parseShow(throwException);
		}

		throwException = true;
		try {
			countInterval = parser.getInterval(throwException);
		} catch (Exception e) {
			throwException = false;
			parserIntervalFeedback = e.getMessage();
			countInterval = parser.getInterval(throwException);
		}
		command = new ShowCommand(taskNote, timeInterval, countInterval);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserShowFeedback);
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserIntervalFeedback);
		return response;
	}

	/**
	 * This operation executes the User's request to show all tasks whose
	 * deadlines are within the specified time interval
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeChangeCategory(String userCommand) {
		// TODO: Parser
		ShowCategory category;
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		try {
			category = parser.parseChangeCategory(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			// category = Parser.parseChangeCateogry(userCommand,
			// throwException);
		}
		category = parseChangeCategory(userCommand);
		command = new ChangeCategoryCommand(taskNote, category);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}
	
	private static ShowCategory parseChangeCategory(String userCommand) {
		String[] parts = userCommand.split("\\s+");
		String category = parts[1];
		if (category.equalsIgnoreCase("ALL")) {
			return ShowCategory.ALL;
		} else if (category.equalsIgnoreCase("OUTSTANDING")) {
			return ShowCategory.OUTSTANDING;
		} else if (category.equalsIgnoreCase("OVERDUE")) {
			return ShowCategory.OVERDUE;
		} else if (category.equalsIgnoreCase("COMPLETED")) {
			return ShowCategory.COMPLETED;
		} else {
			return ShowCategory.COMPLETED;
		}
	}

	/**
	 * This operation executes the User's request to show the General 
	 * Help message as well as the Help message for specific commands
	 *
	 * @param User
	 *            Command
	 * @return Status of Operation
	 */
	private static String executeHelp(String userCommand) {
		boolean throwException = true;
		String parserFeedback = new String(Constants.STRING_CONSTANT_SPACE);
		COMMAND_TYPE commandType;
		try {
			commandType = parser.parseHelp(throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			commandType = parser.parseHelp(throwException);
		}
		command = new HelpCommand(taskNote, commandType);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.STRING_CONSTANT_NEWLINE).concat(parserFeedback);
		return response;
	}
}
