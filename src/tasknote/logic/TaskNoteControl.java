package tasknote.logic;

import tasknote.parser.Parser;
import tasknote.storage.Storage;
import tasknote.shared.TaskObject;
import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;

import java.util.ArrayList;

public class TaskNoteControl {
	
	/* 
	 * These are the various lists that will be used to store the TaskObjects,
	 * Search IDs of Tasks and Search results which will then be displayed to user
	 */
	private static ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private static ArrayList<Integer> searchIds = new ArrayList<Integer>();
	private static ArrayList<Integer> deleteIds = new ArrayList<Integer>();
	private static ArrayList<TaskObject> searchList = new ArrayList<TaskObject>();	
	private static ArrayList<TaskObject> displayList = new ArrayList<TaskObject>();
	
	/**
	 * This operation loads the tasks from the storage
	 * after each time the application is opened
	 * 
	 */
	public static void loadTasks(){
		//TODO: Storage 
		//taskList = Storage.loadTasks();
		refreshDisplay(taskList);
	}
	
	/**
	 * This method is called by UI after each User Operation
	 * to display the list of tasks to the User
	 * 
	 * @return List of Tasks to be displayed to the User
	 */
	public static ArrayList<TaskObject> getDisplayList(){
		return displayList;
	}
	
	/**
	 * This operation refreshes the list of task to be
	 * displayed to the user after each user operation
	 * 
	 * @param List of Tasks to be displayed to the User
	 */
	private static void refreshDisplay(ArrayList<TaskObject> list){
		displayList = new ArrayList<TaskObject>();
		for(int i = 0; i < list.size(); i++){
			displayList.add(list.get(i));
		}
	}

	/**
	 * This operation executes the user command
	 * 
	 * @param User Command
	 * @return Status of Operation
	 */
	public static String executeCommand(String userCommand){
		COMMAND_TYPE commandType = Parser.getCommandType(userCommand);
		String response = new String();
		
		switch (commandType) {
		case ADD:
			response = executeAdd(userCommand);
			refreshDisplay(taskList);
			break;
		case DELETE:
			response = executeDelete(userCommand);
			refreshDisplay(taskList);
			break;
		case SEARCH:
			response = executeSearch(userCommand);
			refreshDisplay(searchList);
			break;
		case UPDATE:
			response = executeUpdate(userCommand);
			refreshDisplay(taskList);
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
	 * This operation gets the TaskObject to be added from 
	 * the parser and adds the task to the task list
	 * 
	 * @param User Command
	 * @return Status of Operation
	 */
	private static String executeAdd(String userCommand){
		TaskObject addObject = Parser.parseAdd(userCommand);
		String response = addTask(addObject);
		return response;
	}
	
	/**
	 * This operation gets the set of Task IDs to be deleted
	 * from the parser and deletes them from the task list
	 * 
	 * @param User Command
	 * @return Status of Operation
	 */
	private static String executeDelete(String userCommand){
		deleteIds.clear();
		//TODO: Parser
		//deleteIds = Parser.parseDelete(userCommand);
		String response = deleteTask(deleteIds);
		return response;
	}
	
	/**
	 * This operation gets the set of IDs of Tasks that 
	 * match User's search and gets the corresponding Task Objects
	 * 
	 * @param User Command
	 * @return Status of Operation
	 */
	private static String executeSearch(String userCommand){
		searchIds.clear();
		searchList.clear();
		//TODO: Parser
		//searchIds = Parser.parseSearch(userCommand);
		String response = getSearchResults();
		return response;
	}
	
	/**
	 * This operation gets the task ID of the task to be updated 
	 * and the updated Task object from the parser and replaces 
	 * the old object in the TaskList
	 * 
	 * @param User Command
	 * @return Status of Operation
	 */
	private static String executeUpdate(String userCommand){
		//TODO: Parser
		//int updateTaskId = Parser.getUpdateTaskId(userCommand);
		//TaskObject updatedTaskObject = Parser.parseUpdate(userCommand);
		//String response = updateTask(updateTaskId, updatedTaskObject);
		String response = "";
		return response;
	}
	
	/**
	 * This operation adds a taskObject to the ArrayList 
	 * of TaskObjects, sorts it based on Date and Time and 
	 * saves it in the Storage
	 * 
	 * @param task object
	 * @return Status of Operation
	 */
	private static String addTask(TaskObject object){
		boolean isSuccess = true;
		try{
			taskList.add(object);
			sortAndSave(taskList);
		}catch(Exception e){
			isSuccess = false;
		}
		return showFeedback(COMMAND_TYPE.ADD, isSuccess, object);
	}
	
	/**
	 * This operation deletes a task in the ArrayList of 
	 * TaskObjects and saves it in the Storage
	 * 
	 * @param Id of the Task stored in ArrayList
	 * @return Status of the operation
	 */
	private static String deleteTask(ArrayList<Integer> deleteIds){
		boolean isSuccess = true;
		try{
			deleteFromTaskList(deleteIds);
			//TODO: Storage 
			//Storage.saveTasks(taskList);
		}catch(Exception e){
			isSuccess = false;
		}
		return showFeedback(COMMAND_TYPE.DELETE, isSuccess, null);
	}
	
	private static void deleteFromTaskList(ArrayList<Integer> deleteIds){
		for(int i = 0; i < deleteIds.size(); i++){
			TaskObject task = displayList.get(i);
			int index = taskList.indexOf(task);
			taskList.remove(index);
		}
	}
	
	/**
	 * This operation searches retrieves all relevant tasks 
	 * based on the given IDs from the ArrayList of TaskObjects.
	 * 
	 * @param userCommand
	 * @return status of the operation
	 */
	private static String getSearchResults(){
		boolean isSuccess = true;
		try{
			for(int i = 0; i < searchIds.size(); i++){
				searchList.add(taskList.get(searchIds.get(i)));
			}
		}catch(Exception e){
			isSuccess = false;
		}
		return showFeedback(COMMAND_TYPE.SEARCH, isSuccess, null);
	}
	
	/**
	 * This operation removes the old task from the taskList, adds
	 * the updated task into the taskList, sorts and saves the list
	 * 
	 * @param userCommand
	 * @return status of the operation
	 */
	private static String updateTask(int updateTaskId, TaskObject updatedTaskObject){
		boolean isSuccess = true;
		try{
			taskList.remove(updateTaskId);
			taskList.add(updatedTaskObject);
			sortAndSave(taskList);
		}catch(Exception e){
			isSuccess = false;
		}
		return showFeedback(COMMAND_TYPE.UPDATE, isSuccess, updatedTaskObject);
	}
	
	/**
	 * This operation sorts the list of Tasks and 
	 * saves them to Storage
	 * 
	 * @param Task List
	 */
	private static void sortAndSave(ArrayList<TaskObject> taskList){
		sortByDate(taskList);
		//TODO: Storage 
		//Storage.saveTasks(taskList);
	}
	
	/**
	 * This operation sorts all tasks based on the 
	 * date-time of the task
	 * 
	 * @param list to be sorted
	 */
	private static void sortByDate(ArrayList<TaskObject> list){
		//TODO
	}
	
	/**
	 * This operation constructs the feedback to be 
	 * displayed to the User after each User Operation
	 * 
	 * @param Command type, isSuccess(true if User operation 
	 * 			is executed successfully; Otherwise false), Task Object
	 * 
	 * @return Feedback to the User
	 */
	private static String showFeedback(COMMAND_TYPE commandType, boolean isSuccess, TaskObject task){
		
		switch(commandType) {
		case ADD:
			if(isSuccess && task != null){
				int taskIndex = taskList.indexOf(task);
				String taskName = task.getTaskName();
				return String.format(Constants.MESSAGE_ADD_SUCCESSFUL, ++taskIndex, taskName);
			}else{
				return Constants.MESSAGE_ADD_UNSUCCESSFUL; 
			}
		case DELETE:
			if(isSuccess){
				return String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, deleteIds.size());
			}else{
				return Constants.MESSAGE_DELETE_UNSUCCESSFUL; 
			}
		case SEARCH:
			if(isSuccess){
				return String.format(Constants.MESSAGE_SEARCH_SUCCESSFUL, searchIds.size());
			}else{
				return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
			}
		case UPDATE:
			if(isSuccess && task != null){
				//TODO: Feedback which fields were updated
				return String.format(Constants.MESSAGE_UPDATE_SUCCESSFUL);
			}else{
				//TODO
				return Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
			}
		default:
			throw new Error("Unrecognized command type");
		}
	}
}
