package tasknote.logic;

import tasknote.logic.Commands.Command;
import tasknote.logic.Commands.AddCommand;
import tasknote.logic.Commands.DeleteCommand;
import tasknote.logic.Commands.SearchCommand;
import tasknote.logic.Commands.UpdateCommand;
import tasknote.logic.Commands.CompleteCommand;
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
 * This class is used to interact directly with the UI to process 
 * and execute User Commands. 
 * The Command object will be used to create a new instance of the
 * corresponding Command Class. 
 * This Class interacts with the Parser Component, which parses the 
 * User Command and returns relevant data. 
 * Once each command is executed, the task list will be updated with 
 * the relevant task objects to be displayed to the User. 
 * Each method in this class that executes a command returns a 
 * feedback String to the caller.
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
		COMMAND_TYPE commandType = Parser.getCommandType(userCommand);
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
		case CHANGE_FILE_PATH:
			//TODO: Parser
			//response = executeChangeFilePath(userCommand);
			response = "";
			break;
		case SHOW:
			//TODO: Parser
			//response = executeShow(userCommand);
			response = "";
			break;
		case CHANGE_CATEGORY:
			//TODO: Parser
			//response = executeChangeCategory(userCommand);
			response = "";
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
		TaskObject taskObject = Parser.parseAdd(userCommand);
		command = new AddCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
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
		ArrayList<Integer> deleteIds = Parser.parseDelete(userCommand);
		command = new DeleteCommand(taskNote, deleteIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
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
		ArrayList<Integer> searchIds = Parser.parseSearch(userCommand, displayList);
		command = new SearchCommand(taskNote, searchIds);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
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
		int updateTaskId = Parser.getUpdateTaskId(userCommand);
		TaskObject updatedTaskObject;

		if (taskNote.isValidTaskId(updateTaskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			TaskObject oldTaskObject = displayList.get(updateTaskId);
			updatedTaskObject = Parser.parseUpdate(userCommand, oldTaskObject);
		} else {
			updatedTaskObject = null;
		}

		command = new UpdateCommand(taskNote, updateTaskId, updatedTaskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
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
		int taskId = Parser.getUpdateTaskId(userCommand);
		TaskObject taskObject;
		if (taskNote.isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			taskObject = displayList.get(taskId);
		} else {
			taskObject = null;
		}
		command = new CompleteCommand(taskNote, taskObject);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
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
	/*
	private static String executeChangeFilePath(String userCommand) {
		//TODO: parser
		//String filePath = Parser.parseFilePath(userCommand);
		command = new ChangeFilePathCommand(taskNote, filePath);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		return response;
	}
	*/
	
	/**
	 * This operation executes the User's request to show
	 * all tasks whose deadlines are within the specified time interval
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	/*
	private static String executeShow(String userCommand){
		//TODO: Parser
		//E.g. Show next 7 weeks / Show all / Show tdy
		ShowInterval timeInterval = Parser.parseShow(userCommand); // returns WEEK / ALL / TODAY
		int countInterval = Parse.getInterval(userCommand); // returns 7 / -1 / -1
		command = new ShowCommand(taskNote, timeInterval, countInterval);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		return response;
	}
	*/
	
	/**
	 * This operation executes the User's request to show
	 * all tasks whose deadlines are within the specified time interval
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	private static String executeChangeCategory(String userCommand){
		//TODO: Parser
		ShowCategory category = Parser.parseChangeCateogry(userCommand);
		command = new ChangeCategoryCommand(taskNote, category);
		command.execute();
		command.refreshDisplay();
		String response = command.getFeedBack();
		return response;
	}
}
