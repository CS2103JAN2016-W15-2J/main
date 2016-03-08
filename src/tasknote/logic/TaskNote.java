package tasknote.logic;

import tasknote.storage.Storage;
import tasknote.shared.TaskObject;
import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.Constants;

import java.util.ArrayList;

public class TaskNote {

	/*
	 * These are the various lists that will be used to store the TaskObjects,
	 * Search IDs of Tasks and Search results which will then be displayed to user
	 */
	private static ArrayList<TaskObject> taskList;
	private static ArrayList<TaskObject> searchList;
	private static ArrayList<TaskObject> displayList;
	
	/*
	 * This is the storage object that will be used to load tasks into the 
	 * taskList and it will be called to save the tasks after each user operation
	 */
	private static Storage storage = new Storage();
	
	/*
	 * These integers are used to store the number of results 
	 * retrieved upon user's search (searchIdSize) and the number 
	 * of tasks to be deleted (deleteIdSize)
	 */
	private static int searchIdSize;
	private static int deleteIdSize;
	
	public TaskNote() {
		taskList = new ArrayList<TaskObject>();
		searchList = new ArrayList<TaskObject>();
		displayList = new ArrayList<TaskObject>();
	}
	
	/**
	 * This operation loads the tasks from the storage
	 * after each time the application is opened
	 *
	 */
	public void loadTasks(){
		try{
			storage = new Storage();
			taskList = storage.loadTasks();
		}catch(Exception e){
			taskList = new ArrayList<TaskObject>();
		}
		refreshDisplay(taskList);
	}
	
	/**
	 * This operation returns the taskList containing 
	 * Task Objects
	 * 
	 * @return List of Tasks
	 */
	public ArrayList<TaskObject> getTaskList(){
		return taskList;
	}
	
	/**
	 * This operation returns the Search List containing 
	 * Task Objects that matched the User's search
	 * 
	 * @return List of Tasks that matched User's search
	 */
	public ArrayList<TaskObject> getSearchList(){
		return searchList;
	}

	/**
	 * This method is called by UI after each User Operation
	 * to display the list of tasks to the User
	 *
	 * @return List of Tasks to be displayed to the User
	 */
	public ArrayList<TaskObject> getDisplayList(){
		return displayList;
	}
	
	/**
	 * This operation reinitializes the Search List to a new list
	 * 
	 */
	public void reIntializeSearchList(){
		searchList = new ArrayList<TaskObject>();
	}

	/**
	 * This operation refreshes the list of task to be
	 * displayed to the user after each user operation
	 *
	 * @param List of Tasks to be displayed to the User
	 */
	public void refreshDisplay(ArrayList<TaskObject> list){
		displayList = new ArrayList<TaskObject>();
		for(int i = 0; i < list.size(); i++){
			displayList.add(list.get(i));
		}
	}

	/**
	 * This operation adds a taskObject to the ArrayList
	 * of TaskObjects, sorts it based on Date and Time and
	 * saves it in the Storage
	 *
	 * @param task object
	 * @return Status of Operation
	 */
	public String addTask(TaskObject object){
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
	public String deleteTask(ArrayList<Integer> deleteIds){
		deleteIdSize = deleteIds.size();
		boolean isSuccess = isValidIdList(deleteIds);
		if(isSuccess){
			try{
				deleteFromTaskList(deleteIds);
				storage.saveTasks(taskList);
			}catch(Exception e){
				isSuccess = false;
			}
		}
		return showFeedback(COMMAND_TYPE.DELETE, isSuccess, null);
	}
	
	public boolean isValidIdList(ArrayList<Integer> idList){
		boolean isValid = true;
		if(deleteIdSize > 0){
			for(int i = 0; i < idList.size(); i++){
				int taskId = idList.get(i);
				if(!isValidTaskId(taskId)){
					isValid = false;
					break;
				}
			}
		}else{
			isValid = false;
		}
		return isValid;
	}
	
	//TODO: Check if all IDs to be deleted are valid before deleting
	public static void deleteFromTaskList(ArrayList<Integer> deleteIds){
		for(int i = 0; i < deleteIds.size(); i++){
			TaskObject task = displayList.get(deleteIds.get(i));
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
	public String getSearchResults(ArrayList<Integer> searchIds){
		boolean isSuccess = true;
		searchIdSize = searchIds.size();
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
	public String updateTask(int updateTaskId, TaskObject updatedTaskObject){
		boolean isSuccess = isValidTaskId(updateTaskId);
		if(isSuccess && updatedTaskObject != null){
			try{
				taskList.remove(updateTaskId);
				taskList.add(updatedTaskObject);
				sortAndSave(taskList);
			}catch(Exception e){
				isSuccess = false;
			}
		}
		return showFeedback(COMMAND_TYPE.UPDATE, isSuccess, updatedTaskObject);
	}
	
	public boolean isValidTaskId(int taskId){
		boolean isValid = true;
		if(taskId >= displayList.size() || taskId < Constants.EMPTY_LIST_SIZE){
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * This operation sets the completion status of the task to be true
	 *
	 * @param Task Object
	 * @return status of the operation
	 */
	public String markTaskAsCompleted(TaskObject taskObject){
		boolean isSuccess = true;
		try{
			taskObject.setIsMarkedDone(isSuccess);
			sortAndSave(taskList);
		}catch(Exception e){
			isSuccess = false;
		}
		return showFeedback(COMMAND_TYPE.DONE, isSuccess, taskObject);
	}

	/**
	 * This operation sorts the list of Tasks and
	 * saves them to Storage
	 *
	 * @param Task List
	 * @throws Exception 
	 */
	private static void sortAndSave(ArrayList<TaskObject> taskList) throws Exception{
		try{
			sortByDate(taskList);
			storage.saveTasks(taskList);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * This operation sorts all tasks based on the
	 * date-time of the task
	 *
	 * @param list to be sorted
	 * @throws Exception 
	 */
	private static void sortByDate(ArrayList<TaskObject> list) throws Exception{
		//TODO
		try{
			//Sort by Date
		}catch(Exception e){
			System.out.println("Sort by date has an error");
			throw e;
		}
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
				return String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, deleteIdSize);
			}else{
				return Constants.MESSAGE_DELETE_UNSUCCESSFUL;
			}
		case SEARCH:
			if(isSuccess){
				return String.format(Constants.MESSAGE_SEARCH_SUCCESSFUL, searchIdSize);
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
		case DONE:
			if(isSuccess && task != null){
				String taskName = task.getTaskName();
				return String.format(Constants.MESSAGE_DONE_SUCCESSFUL, taskName);
			}else{
				return Constants.MESSAGE_DONE_UNSUCCESSFUL;
			}
		default:
			throw new Error("Unrecognized command type");
		}
	}
}
