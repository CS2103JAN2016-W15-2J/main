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
 * User Commands. The Command object will be used to create a new instance of
 * the corresponding Command Class. This Class interacts with the Parser
 * Component, which parses the User Command and returns relevant data. Once each
 * command is executed, the task list will be updated with the relevant task
 * objects to be displayed to the User. Each method in this class that executes
 * a command returns a feedback String to the caller.
 *
 * @author Murali Girish Narayanan
 */

public class TaskNoteControl {

	private static TaskNote taskNote;
	private static Command command;

	public TaskNoteControl() {
		taskNote = new TaskNote();
		taskNote.loadTasks();
	}

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
		boolean throwException = true;
		COMMAND_TYPE commandType;
		try {
			commandType = Parser.getCommandType(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			commandType = Parser.getCommandType(userCommand, throwException);
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
			// response = executeChangeCategory(userCommand);
			response = "";
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
		String parserFeedback = new String(" ");
		try {
			taskObject = Parser.parseAdd(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskObject = Parser.parseAdd(userCommand, throwException);
		}
		command = new AddCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		String parserFeedback = new String(" ");
		try {
			deleteIds = Parser.parseDelete(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			deleteIds = Parser.parseDelete(userCommand, throwException);
		}
		command = new DeleteCommand(taskNote, deleteIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		String parserFeedback = new String(" ");
		try {
			searchIds = Parser.parseSearch(userCommand, displayList, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			searchIds = Parser.parseSearch(userCommand, displayList, throwException);
		}
		command = new SearchCommand(taskNote, searchIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		// TODO:Parser - change method name to getTaskId
		int updateTaskId;
		TaskObject updatedTaskObject;
		boolean throwException = true;
		String parserUpdateIdFeedback = new String(" ");
		String parserUpdateObjectFeedback = new String(" ");

		try {
			updateTaskId = Parser.getUpdateTaskId(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserUpdateIdFeedback = e.getMessage();
			updateTaskId = Parser.getUpdateTaskId(userCommand, throwException);
		}

		TaskObject oldTaskObject = null;
		throwException = true;
		if (taskNote.isValidTaskId(updateTaskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			oldTaskObject = displayList.get(updateTaskId);
			try {
				updatedTaskObject = Parser.parseUpdate(userCommand, oldTaskObject, throwException);
			} catch (Exception e) {
				throwException = false;
				parserUpdateObjectFeedback = e.getMessage();
				updatedTaskObject = Parser.parseUpdate(userCommand, oldTaskObject, throwException);
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
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserUpdateIdFeedback);
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserUpdateObjectFeedback);
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
		// TODO:Parser - change method name to getTaskId
		int taskId;
		boolean throwException = true;
		String parserFeedback = new String(" ");
		try {
			// TODO:Parser - change method name to getTaskId
			taskId = Parser.getUpdateTaskId(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskId = Parser.getUpdateTaskId(userCommand, throwException);
		}
		TaskObject taskObject;
		if (taskNote.isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			taskObject = displayList.get(taskId);
		} else {
			taskObject = null;
		}
		command = new DoneCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		// TODO:Parser - change method name to getTaskId
		int taskId;
		boolean throwException = true;
		String parserFeedback = new String(" ");
		try {
			// TODO:Parser - change method name to getTaskId
			taskId = Parser.getUpdateTaskId(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			taskId = Parser.getUpdateTaskId(userCommand, throwException);
		}
		TaskObject taskObject;
		if (taskNote.isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			taskObject = displayList.get(taskId);
		} else {
			taskObject = null;
		}
		command = new UndoneCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		String parserFeedback = new String(" ");
		try {
			filePath = Parser.parseFilePath(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			filePath = Parser.parseFilePath(userCommand, throwException);
		}
		command = new ChangeFilePathCommand(taskNote, filePath);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
		String parserShowFeedback = new String(" ");
		String parserIntervalFeedback = new String(" ");

		try {
			timeInterval = Parser.parseShow(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserShowFeedback = e.getMessage();
			timeInterval = Parser.parseShow(userCommand, throwException);
		}

		throwException = true;
		try {
			countInterval = Parser.getInterval(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserIntervalFeedback = e.getMessage();
			countInterval = Parser.getInterval(userCommand, throwException);
		}
		command = new ShowCommand(taskNote, timeInterval, countInterval);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserShowFeedback);
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserIntervalFeedback);
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
		String parserFeedback = new String(" ");
		try {
			// category = Parser.parseChangeCateogry(userCommand,
			// throwException);
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
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
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
	
	private static String executeHelp(String userCommand) {
		boolean throwException = true;
		String parserFeedback = new String(" ");
		COMMAND_TYPE commandType;
		try {
			commandType = Parser.parseHelp(userCommand, throwException);
		} catch (Exception e) {
			throwException = false;
			parserFeedback = e.getMessage();
			commandType = Parser.parseHelp(userCommand, throwException);
		}
		command = new HelpCommand(taskNote, commandType);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		response = response.concat(Constants.NEW_LINE_STRING).concat(parserFeedback);
		return response;
	}
}
