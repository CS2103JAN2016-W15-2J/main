package tasknote.logic;

import tasknote.logic.Commands.AddTask;
import tasknote.logic.Commands.DeleteTask;
import tasknote.logic.Commands.SearchTask;
import tasknote.logic.Commands.UpdateTask;
import tasknote.logic.Commands.CompleteTask;
import tasknote.parser.Parser;
import tasknote.shared.TaskObject;
import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;

import java.util.ArrayList;

public class TaskNoteControl {
	
	public static TaskNote taskNote;
	public static AddTask addTask;
	public static DeleteTask deleteTask;
	public static SearchTask searchTask;
	public static UpdateTask updateTask;
	public static CompleteTask completeTask;
	
	
	public TaskNoteControl() {
		taskNote = new TaskNote();
		taskNote.loadTasks();
	}
	
	public ArrayList<TaskObject> getDisplayList() {
		return taskNote.getDisplayList();
	}
	
	/**
	 * This operation gets the command type and executes 
	 * the requested action
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public String executeCommand(String userCommand){
		COMMAND_TYPE commandType = Parser.getCommandType(userCommand);
		String feedback = executeAction(commandType, userCommand);
		return feedback;
	}
	
	/**
	 * This operation executes the user command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeAction(COMMAND_TYPE commandType, String userCommand){
		String response;
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
			//TODO
			response = executeUndo(userCommand);
			break;
		case REDO:
			//TODO
			response = executeRedo(userCommand);
			break;
		case DONE:
			//TODO: parser
			response = executeMarkAsComplete(userCommand);
			break;
		case CHANGE_FILE_PATH:
			//TODO
			response = executeChangeFilePath(userCommand);
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
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeAdd(String userCommand){
		TaskObject taskObject = Parser.parseAdd(userCommand);
		addTask = new AddTask(taskNote, taskObject);
		addTask.execute();
		addTask.refreshDisplay();
		String response = addTask.getFeedBack();
		return response;
	}
	
	/**
	 * This operation executes the User's Delete command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeDelete(String userCommand){
		ArrayList<Integer> deleteIds = Parser.parseDelete(userCommand);
		deleteTask = new DeleteTask(taskNote, deleteIds);
		deleteTask.execute();
		deleteTask.refreshDisplay();
		String response = deleteTask.getFeedBack();
		return response;
	}
	
	/**
	 * This operation executes the User's Search command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeSearch(String userCommand){
		ArrayList<TaskObject> displayList = taskNote.getDisplayList();
		ArrayList<Integer> searchIds = Parser.parseSearch(userCommand, displayList);
		searchTask = new SearchTask(taskNote, searchIds);
		searchTask.execute();
		searchTask.refreshDisplay();
		String response = searchTask.getFeedBack();
		return response;
	}
	
	/**
	 * This operation executes the User's Update command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeUpdate(String userCommand){
		//TODO:Parser - change method name to getTaskId
		int updateTaskId = Parser.getUpdateTaskId(userCommand);
		TaskObject updatedTaskObject;
		if(taskNote.isValidTaskId(updateTaskId)){
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			TaskObject oldTaskObject = displayList.get(updateTaskId);
			updatedTaskObject = Parser.parseUpdate(userCommand, oldTaskObject);
		}else{
			updatedTaskObject = null;
		}
		updateTask = new UpdateTask(taskNote, updateTaskId, updatedTaskObject);
		updateTask.execute();
		updateTask.refreshDisplay();
		String response = updateTask.getFeedBack();
		return response;
	}
	
	/**
	 * This operation executes the User's Undo command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeUndo(String userCommand){
		//TODO
		return "";
	}
	
	/**
	 * This operation executes the User's Redo command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeRedo(String userCommand){
		//TODO
		return "";
	}
	
	/**
	 * This operation executes the User's Mark As Completed command
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeMarkAsComplete(String userCommand){
		//TODO:Parser - change method name to getTaskId
		int taskId = Parser.getUpdateTaskId(userCommand);
		TaskObject taskObject;
		if(taskNote.isValidTaskId(taskId)){
			ArrayList<TaskObject> displayList = taskNote.getDisplayList();
			taskObject = displayList.get(taskId);
		}else{
			taskObject = null;
		}
		completeTask = new CompleteTask(taskNote, taskObject);
		completeTask.execute();
		completeTask.refreshDisplay();
		String response = completeTask.getFeedBack();
		return response;
	}
	
	/**
	 * This operation executes the User's request to change 
	 * the directory the task file exists in
	 *
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeChangeFilePath(String userCommand){
		//TODO
		return "";
	}
	
}
