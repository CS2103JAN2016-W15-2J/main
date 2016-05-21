/** @@author A0108371L */
package tasknote.logic;

import tasknote.storage.Storage;
import tasknote.shared.TaskObject;
import tasknote.shared.TaskObject.TaskStatus;
import tasknote.shared.CommandType;
import tasknote.shared.Constants;
import tasknote.logic.History.CommandHistory;
import tasknote.logic.History.CommandObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class TaskNote {

	/*
	 * These are the various lists that will be used to store the TaskObjects,
	 * Search IDs of Tasks and Search results which will then be displayed to
	 * user
	 */
	private static ArrayList<TaskObject> taskList;
	private static ArrayList<TaskObject> searchList;
	private static ArrayList<TaskObject> showIntervalList;
	private static ArrayList<TaskObject> displayList;

	private static CommandHistory history;

	private static ShowInterval showType;
	private static ShowCategory taskCategory;
	
	private static CommandType undoCommandType;
	private static CommandType redoCommandType;

	/*
	 * This is the storage object that will be used to load tasks into the
	 * taskList and it will be called to save the tasks after each user
	 * operation
	 */
	private static Storage storage = new Storage();

	/*
	 * This variable is used to store the filePath
	 */
	private static String filePath;

	/*
	 * These integers are used to store the number of results retrieved upon
	 * user's operation
	 */
	private static int searchIdSize;
	private static int deleteIdSize;
	private static int showCountInterval;

	private static Logger logger = Logger.getLogger(TaskNote.class.getName());

	public TaskNote() {
		taskList = new ArrayList<TaskObject>();
		searchList = new ArrayList<TaskObject>();
		showIntervalList = new ArrayList<TaskObject>();
		displayList = new ArrayList<TaskObject>();
		history = new CommandHistory();
		filePath = new String();
	}

	/**
	 * This operation loads the tasks from the storage after each time the
	 * application is opened
	 *
	 */
	public void loadTasks() {
		try {
			storage = new Storage();
			taskList = storage.loadTasks();
		} catch (Exception e) {
			taskList = new ArrayList<TaskObject>();
		}
		refreshDisplay(taskList);
	}

	/**
	 * This operation returns the taskList containing Task Objects
	 * 
	 * @return List of Tasks
	 */
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	/**
	 * This operation returns the Search List containing Task Objects that
	 * matched the User's search
	 * 
	 * @return List of Tasks that matched User's search
	 */
	public ArrayList<TaskObject> getSearchList() {
		return searchList;
	}

	/**
	 * This operation returns the Show Interval List containing Task Objects
	 * that have deadlines in the given interval by the user
	 * 
	 * @return List of Tasks that matched User's search
	 */
	public ArrayList<TaskObject> getShowIntervalList() {
		return showIntervalList;
	}

	/**
	 * This method is called by UI after each User Operation to display the list
	 * of tasks to the User
	 *
	 * @return List of Tasks to be displayed to the User
	 */
	public ArrayList<TaskObject> getDisplayList() {
		return displayList;
	}

	/**
	 * This operation reinitializes the Search List to a new list
	 * 
	 */
	public void reIntializeSearchList() {
		searchList = new ArrayList<TaskObject>();
	}

	/**
	 * This operation refreshes the list of task to be displayed to the user
	 * after each user operation
	 *
	 * @param List
	 *            of Tasks to be displayed to the User
	 */
	public void refreshDisplay(ArrayList<TaskObject> list) {
		displayList = new ArrayList<TaskObject>();
		for (int i = 0; i < list.size(); i++) {
			displayList.add(list.get(i));
		}
	}
	
	public ArrayList<TaskObject> getOutstandingTasksList() {
		ArrayList<TaskObject> list = new ArrayList<TaskObject>();
		for(int i = 0; i < taskList.size(); i++) {
			TaskObject taskObject = taskList.get(i);
			if(taskObject.getTaskStatus() == TaskStatus.TASK_OUTSTANDING) {
			list.add(taskObject);
			}
		}
		return list;
	}

	/**
	 * This operation adds a taskObject to the ArrayList of TaskObjects, sorts
	 * it based on Date and Time and saves it in the Storage
	 *
	 * @param task
	 *            object
	 * @return Status of Operation
	 */
	public String addTask(TaskObject taskObject) {
		boolean isSuccess = true;
		try {
			assert (isNotNullTaskObject(taskObject) == isSuccess);
			taskList.add(taskObject);
			sortAndSave(taskList);
			history.pushAddToUndo(taskObject);
			logger.log(Level.INFO, String.format(Constants.INFO_ADD_SUCCESSFUL));
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_ADD_FAILURE, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_ADD_INVALID_OBJECT, er));
		}
		return showFeedback(CommandType.ADD, isSuccess, taskObject);
	}

	/**
	 * This operation deletes a task in the ArrayList of TaskObjects and saves
	 * it in the Storage
	 *
	 * @param Id
	 *            of the Task stored in ArrayList
	 * @return Status of the operation
	 */
	public String deleteTask(ArrayList<Integer> deleteIds) {
		deleteIdSize = deleteIds.size();
		String deletionErrorFeedback = new String();
		boolean isSuccess = true;
        
        if (deleteIdSize > Constants.EMPTY_LIST_SIZE_CONSTANT && isValidIdList(deleteIds)) {
            try {
                deleteFromTaskList(deleteIds);
                storage.saveTasks(taskList);
            } catch (Exception ex) {
                isSuccess = false;
                logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_DELETE_FAILURE, ex));
            }
        } else {
            isSuccess = false;
            deletionErrorFeedback = (deleteIdSize > Constants.EMPTY_LIST_SIZE_CONSTANT) 
                                    ? Constants.WARNING_INVALID_DELETE_INDEX 
                                    : Constants.WARNING_EMPTY_DELETEID_LIST;
            logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_DELETE_INVALID_LIST));
        }

		String feedback = showFeedback(CommandType.DELETE, isSuccess, null);
		feedback = feedback.concat(Constants.STRING_CONSTANT_NEWLINE).concat(Constants.STRING_CONSTANT_NEWLINE);
		feedback = feedback.concat(deletionErrorFeedback);
		return feedback;
	}

	/**
	 * This operation searches retrieves all relevant tasks based on the given
	 * IDs from the ArrayList of TaskObjects.
	 *
	 * @param userCommand
	 * @return status of the operation
	 */
	public String searchTasks(ArrayList<Integer> searchIds) {
		boolean isSuccess = true;
		searchIdSize = searchIds.size();
		reIntializeSearchList();
		try {
			assert (searchIdSize > Constants.EMPTY_LIST_SIZE_CONSTANT);
			for (int i = 0; i < searchIds.size(); i++) {
				searchList.add(displayList.get(searchIds.get(i)));
			}
			logger.log(Level.INFO, Constants.INFO_SEARCH_SUCCESSFUL);
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SEARCH_FAILURE, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SEARCH_NO_RESULT, er));
		}
		return showFeedback(CommandType.SEARCH, isSuccess, null);
	}

	/**
	 * This operation removes the old task from the taskList, adds the updated
	 * task into the taskList, sorts and saves the list
	 *
	 * @param userCommand
	 * @return status of the operation
	 */
	public String updateTask(int updateTaskId, TaskObject updatedTaskObject) {
		boolean isSuccess = true;
		try {
			assert (isValidTaskId(updateTaskId) && updatedTaskObject != null);
			TaskObject oldTaskObject = taskList.remove(updateTaskId);
			taskList.add(updateTaskId, updatedTaskObject);
			sortAndSave(taskList);
			history.pushUpdateToUndo(oldTaskObject, updatedTaskObject);
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_UPDATE_FAILURE, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_UPDATE_INVALID_OBJECTID, er));
		}

		return showFeedback(CommandType.UPDATE, isSuccess, updatedTaskObject);
	}

	/**
	 * This operation reverts the action executed by the last command
	 * 
	 * @return status of the operation
	 */
	public String undoLastCommand() {
		boolean isSuccess = true;
		TaskObject taskObject = null;
		try {
			CommandObject commandObject = history.peekUndoStack();
			CommandType commandType = commandObject.getRevertCommandType();
			setUndoCommandType(commandType);
			int numPrecedingObjects = commandObject.getPrecedingObjects();
			recoverByUndo(numPrecedingObjects);
			sortAndSave(taskList);
			logger.log(Level.INFO, Constants.INFO_UNDO_SUCCESSFUL);
		} catch (Exception e) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_UNDO, e));
		}

		return showFeedback(CommandType.UNDO, isSuccess, taskObject);
	}
	
	/**
	 * This operation reverts the last action reverted by Undo command
	 * 
	 * @return status of the operation
	 */
	public String redoLastUndoCommand() {
		boolean isSuccess = true;
		TaskObject taskObject = null;
		try {
			CommandObject commandObject = history.peekRedoStack();
			CommandType commandType = commandObject.getRevertCommandType();
			int numPrecedingObjects = commandObject.getPrecedingObjects();
			recoverByRedo(numPrecedingObjects);
			sortAndSave(taskList);
			logger.log(Level.INFO, Constants.INFO_REDO_SUCCESSFUL);
		} catch (Exception e) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_REDO, e));
		}
		return showFeedback(CommandType.REDO, isSuccess, taskObject);
	}
	
	/**
	 * This operation marks a task as completed if it's
	 * current task status is set to be false (outstanding or overdue)
	 *
	 * @param taksId
	 * @return status of the operation
	 */
	public String markTaskAsComplete(int taskId) {
		TaskObject taskObject;
		String doneFeedback = new String();
		int enteredTaskId = taskId + Constants.INCREMENT_COUNT_CONSTANT;
		boolean markAsComplete = true;
		if (isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = getDisplayList();
			taskObject = displayList.get(taskId);
			if(taskObject.getIsMarkedDone()) {
				doneFeedback = String.format(Constants.WARNING_EXECUTE_DONE_TASK_COMPLETED, enteredTaskId);
			} else {
				doneFeedback = setTaskCompletionStatus(taskObject, markAsComplete);
			}
		} else {
			taskObject = null;
			doneFeedback = Constants.MESSAGE_DONE_UNSUCCESSFUL.concat(Constants.STRING_CONSTANT_NEWLINE);
			doneFeedback = doneFeedback.concat(String.format(Constants.WARNING_EXECUTE_DONE_INVALID_ID, enteredTaskId));
		}
		return doneFeedback;
	}
	
	public String markTaskAsIncomplete(int taskId) {
		TaskObject taskObject;
		String undoneFeedback = new String();
		int enteredTaskId = taskId + Constants.INCREMENT_COUNT_CONSTANT;
		boolean markAsComplete = false;
		if (isValidTaskId(taskId)) {
			ArrayList<TaskObject> displayList = getDisplayList();
			taskObject = displayList.get(taskId);
			if(!taskObject.getIsMarkedDone()) {
				System.out.println(taskObject.getIsMarkedDone());
				undoneFeedback = String.format(Constants.WARNING_EXECUTE_DONE_TASK_INCOMPLETE, enteredTaskId);
			} else {
				undoneFeedback = setTaskCompletionStatus(taskObject, markAsComplete);
			}
		} else {
			taskObject = null;
			undoneFeedback = Constants.MESSAGE_UNDONE_UNSUCCESSFUL.concat(Constants.STRING_CONSTANT_NEWLINE);
			undoneFeedback = undoneFeedback.concat(String.format(Constants.WARNING_EXECUTE_DONE_INVALID_ID, enteredTaskId));
		}
		return undoneFeedback;
	}

	/**
	 * This operation sets the completion status of the task to be true
	 *
	 * @param TaskObject
	 * @return status of the operation
	 */
	public String setTaskCompletionStatus(TaskObject taskObject, boolean isComplete) {
		String feedback = new String();
		boolean isSuccess = true;
		try {
			assert (isValidTaskObject(taskObject) == isSuccess);
			taskObject.setIsMarkedDone(isComplete);
			history.pushTaskCompletionToUndo(taskObject);
			sortAndSave(taskList);
			if(isComplete) {
				logger.log(Level.INFO, Constants.INFO_DONE_SUCCESSFUL);
			} else {
				logger.log(Level.INFO, Constants.INFO_UNDONE_SUCCESSFUL);
			}
		} catch (Exception ex) {
			isSuccess = false;
			if(isComplete) {
				logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_COMPLETE_FAILURE, ex));
			} else {
				logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_INCOMPLETE_FAILURE, ex));
			}
		} catch (Error er) {
			isSuccess = false;
			if(isComplete) {
				logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_COMPLETE_INVALID_OBJECT, er));
			} else {
				logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_INCOMPLETE_INVALID_OBJECT, er));
			}
		}
		if(isComplete) {
			feedback = showFeedback(CommandType.DONE, isSuccess, taskObject);
		} else {
			feedback = showFeedback(CommandType.UNDONE, isSuccess, taskObject);
		}
		return feedback;
	}

	/**
	 * This operation changes the File Path to the new location specified by the
	 * user
	 *
	 * @param file
	 *            path
	 * @return status of the operation
	 */
	public String changeFilePath(String newFilePath) {
		boolean isSuccess;
		filePath = newFilePath;

		try {
			assert (!filePath.equals(Constants.STRING_CONSTANT_EMPTY) && isNotNullFilePath(filePath));
			isSuccess = storage.changePath(filePath);
			if (isSuccess) {
				history.pushChangeFilePathToUndo();
				logger.log(Level.INFO, String.format(Constants.INFO_EXECUTE_CHANGE_PATH_SUCCESSFUL, filePath));
			} else {
				logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_CHANGE_PATH_FALSE, filePath));
			}
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_CHANGE_PATH_FAILURE, filePath, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SHOW_INVALID_FILEPATH, er));
		}
		return showFeedback(CommandType.CHANGE_FILE_PATH, isSuccess, null);
	}
	
	/**
	 * This operation shows the tasks within the specified time interval
	 *
	 * @param showIDs
	 * @return status of the operation
	 */
	public String showTasks(ShowInterval timeInterval, int countInterval) {
		boolean isSuccess = true;
		showIntervalList = new ArrayList<TaskObject>();
		try {
			assert (timeInterval != null);
			showCountInterval = countInterval;
			switch (timeInterval) {
			case TODAY:
				getTodayTasks();
				showType = ShowInterval.TODAY;
				break;
			case TOMORROW:
				getTomorrowTasks();
				showType = ShowInterval.TOMORROW;
				break;
			case DAY:
				getDayTasks(countInterval);
				showType = ShowInterval.DAY;
				break;
			case WEEK:
				getWeekTasks(countInterval);
				showType = ShowInterval.WEEK;
				break;
			case ALL:
				getAllTasksInInterval();
				showType = ShowInterval.ALL;
				break;
			default:
				throw new Error("Unrecognized ShowInterval type");
			}
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SHOW_FAILURE, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SHOW_INVALID_INTERVAL, er));
		}
		return showFeedback(CommandType.SHOW, isSuccess, null);
	}

	/**
	 * This operation executes the retrieval of tasks in the user specified
	 * display category
	 *
	 * @param category
	 * @return status of the operation
	 */
	public String displayCategory(ShowCategory category) {
		boolean isSuccess = true;
		try {
			assert (category != null);
			switch (category) {
			case ALL:
				displayAllTasksInInterval();
				taskCategory = ShowCategory.ALL;
				break;
			case OUTSTANDING:
				displayTasks(TaskStatus.TASK_OUTSTANDING);
				taskCategory = ShowCategory.OUTSTANDING;
				break;
			case OVERDUE:
				displayTasks(TaskStatus.TASK_OVERDUE);
				taskCategory = ShowCategory.OVERDUE;
				break;
			case COMPLETED:
				displayTasks(TaskStatus.TASK_COMPLETED);
				taskCategory = ShowCategory.COMPLETED;
				break;
			default:
				throw new Error("Unrecognized ShowInterval type");
			}
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SHOW_CATEGORY_FAILURE, category, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SHOW_CATEGORY_INVALID, er));
		}
		return showFeedback(CommandType.CHANGE_CATEGORY, isSuccess, null);
	}
	
	/**
	 * This operation displays help messages for each command
	 *
	 * @param commandType
	 * @return help message for requested command
	 */
	public static String displayHelpMessage(CommandType commandType) {
		String helpMessage;
		switch(commandType) {
		case ADD:
			helpMessage = Constants.MESSAGE_HELP_ADD;
			break;
		case DELETE:
			helpMessage = Constants.MESSAGE_HELP_DELETE;
			break;
		case SEARCH:
			helpMessage = Constants.MESSAGE_HELP_SEARCH;
			break;
		case UPDATE:
			helpMessage = Constants.MESSAGE_HELP_UPDATE;
			break;
		case UNDO:
			helpMessage = Constants.MESSAGE_HELP_UNDO;
			break;
		case REDO:
			helpMessage = Constants.MESSAGE_HELP_REDO;
			break;
		case DONE:
			helpMessage = Constants.MESSAGE_HELP_DONE;
			break;
		case UNDONE:
			helpMessage = Constants.MESSAGE_HELP_UNDONE;
			break;
		case CHANGE_FILE_PATH:
			helpMessage = Constants.MESSAGE_HELP_CHANGE_FILE_PATH;
			break;
		case SHOW:
			helpMessage = Constants.MESSAGE_HELP_SHOW;
			break;
		case CHANGE_CATEGORY:
			helpMessage = Constants.MESSAGE_HELP_CHANGE_CATEGORY;
			break;
		case HELP:
			helpMessage = Constants.MESSAGE_HELP;
			break;
		case EXIT:
			helpMessage = Constants.MESSAGE_HELP_EXIT;
			break;
		default:
			helpMessage = new String();
		}
		return helpMessage;
	}
	
	/**
	 * This operation recovers the previous status of the tasks
	 * by executing the Undo operation
	 *
	 * @param numPrecedingObjects
	 * @throws Exception
	 */
    private void recoverByUndo(int numPrecedingObjects) throws Exception {
        int undoCount = Constants.EMPTY_LIST_SIZE_CONSTANT;
        while (undoCount <= numPrecedingObjects) {
            CommandObject commandObject = history.popUndoStack();
            CommandType commandType = commandObject.getRevertCommandType();
            if (commandType == CommandType.ADD) {
                undoDelete(commandObject);
            } else if (commandType == CommandType.DELETE) {
                undoAdd(commandObject);
            } else if (commandType == CommandType.UPDATE) {
                undoUpdate(commandObject);
            } else if (commandType == CommandType.DONE) {
                undoDone(commandObject);
            } else if (commandType == CommandType.CHANGE_FILE_PATH) {
                undoChangeFilePath();
            }
            history.peekRedoStack().setPrecedingObjects(numPrecedingObjects);
            undoCount++;
        }
    }
	
	/**
	 * This operation recovers the previous status of the tasks
	 * by executing the Undo operation
	 *
	 * @param numPrecedingObjects
	 * @throws Exception 
	 */
	private void recoverByRedo(int numPrecedingObjects) throws Exception {
		int redoCount = Constants.EMPTY_LIST_SIZE_CONSTANT;
        while (redoCount <= numPrecedingObjects) {
            CommandObject commandObject = history.popRedoStack();
            CommandType commandType = commandObject.getRevertCommandType();
            setRedoCommandType(commandType);
            if (commandType == CommandType.ADD) {
                redoAdd(commandObject);
            } else if (commandType == CommandType.DELETE) {
                redoDelete(commandObject);
            } else if (commandType == CommandType.UPDATE) {
                redoUpdate(commandObject);
            } else if (commandType == CommandType.DONE) {
                redoDone(commandObject);
            } else if (commandType == CommandType.CHANGE_FILE_PATH) {
                redoChangeFilePath();
            }
            history.peekUndoStack().setPrecedingObjects(numPrecedingObjects);
            redoCount++;
        }
	}
	
	/**
	 * This operation sets the type of Command that has been undone
	 *
	 * @param commandType
	 */
	private void setUndoCommandType(CommandType commandType) {
		if(commandType == CommandType.ADD) {
			undoCommandType = CommandType.DELETE;
		} else if(commandType == CommandType.DELETE) {
			undoCommandType = CommandType.ADD;
		}else {
			undoCommandType = commandType;
		}
	}
	
	/**
	 * This operation sets the type of Command that has been redone
	 *
	 * @param commandType
	 */
	private void setRedoCommandType(CommandType commandType) {
		if(commandType == CommandType.ADD) {
			redoCommandType = CommandType.DELETE;
		} else if(commandType == CommandType.DELETE) {
			redoCommandType = CommandType.ADD;
		}else {
			redoCommandType = commandType;
		}
	}
	
	/**
	 * This operation reverts the previous add command executed
	 *
	 * @param commandType
	 */
	private void undoDelete(CommandObject commandObject) {
		try{
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushDeleteToRedo(taskObject);
			taskList.add(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous delete command executed
	 *
	 * @param commandType
	 */
	private void undoAdd(CommandObject commandObject) {
		try {
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushAddToRedo(taskObject);
			taskList.remove(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous update command executed
	 *
	 * @param commandType
	 */
	private void undoUpdate(CommandObject commandObject) {
		try {
			CommandObject oldObject = history.popUndoStack();
			CommandObject newObject = history.popUndoStack();
			TaskObject oldTaskObject = oldObject.getTaskObject();
			TaskObject newTaskObject = newObject.getTaskObject();
			history.pushUpdateToRedo(oldTaskObject, newTaskObject);
			history.pushAddToUndo(newTaskObject);
			history.pushDeleteToUndo(oldTaskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous done command executed
	 *
	 * @param commandType
	 */
	private void undoDone(CommandObject commandObject) {
		try {
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushTaskCompletionToRedo(taskObject);
			taskList.remove(taskObject);
			boolean isComplete = taskObject.getIsMarkedDone();
			taskObject.setIsMarkedDone(!isComplete);
			isComplete = taskObject.getIsMarkedDone();
			taskList.add(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous change file path
	 * command executed
	 *
	 * @param commandType
	 * @throws Exception 
	 */
	private void undoChangeFilePath() throws Exception {
		try {
			boolean isPathUndone = storage.undoPath();
			if (isPathUndone) {
				history.pushChangeFilePathToRedo();
			}
		} catch (Exception e) {
			System.out.println("ERROR THROWN BY STORAGE = " + e);
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous undo command for 
	 * Add operation
	 *
	 * @param commandType
	 */
	private void redoAdd(CommandObject commandObject) {
		try{
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushAddToUndo(taskObject);
			taskList.add(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous undo command for 
	 * Delete operation
	 *
	 * @param commandType
	 */
	private void redoDelete(CommandObject commandObject) {
		try{
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushDeleteToUndo(taskObject);
			taskList.remove(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous undo command for 
	 * Update operation
	 *
	 * @param commandType
	 */
	private void redoUpdate(CommandObject commandObject) {
		try{
			CommandObject oldObject = history.popRedoStack();
			CommandObject newObject = history.popRedoStack();
			TaskObject oldTaskObject = oldObject.getTaskObject();
			TaskObject newTaskObject = newObject.getTaskObject();
			history.pushUpdateToUndo(oldTaskObject, newTaskObject);
			history.pushAddToUndo(oldTaskObject);
			history.pushDeleteToUndo(newTaskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous undo command for 
	 * Done operation
	 *
	 * @param commandType
	 */
	private void redoDone(CommandObject commandObject) {
		try{
			TaskObject taskObject = commandObject.getTaskObject();
			history.pushTaskCompletionToUndo(taskObject);
			taskList.remove(taskObject);
			boolean isComplete = taskObject.getIsMarkedDone();
			taskObject.setIsMarkedDone(!isComplete);
			isComplete = taskObject.getIsMarkedDone();
			taskList.add(taskObject);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation reverts the previous undo command for 
	 * Change File Path operation
	 *
	 * @param commandType
	 * @throws Exception 
	 */
	private void redoChangeFilePath() throws Exception {
		try{
			boolean isPathRedone = storage.redoPath();
			if (isPathRedone) {
				history.pushChangeFilePathToUndo();
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * This operation populates tasks in Search Interval List with task objects
	 * that have deadlines today
	 * 
	 */
	private void getTodayTasks() {
		LocalDateTime now = LocalDateTime.now();
		int currentYear = now.getYear();
		int currentMonth = now.getMonthValue();
		int currentDay = now.getDayOfMonth();

		try {
			populateTdyTmrShowList(currentYear, currentMonth, currentDay);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This operation populates tasks in Search Interval List with task objects
	 * that have deadlines tomorrow
	 * 
	 */
	private void getTomorrowTasks() {
		LocalDateTime now = LocalDateTime.now();
		now = now.plusDays(Constants.INCREMENT_DAY_TOMORROW_CONSTANT);

		int tomorrowYear = now.getYear();
		int tomorrowMonth = now.getMonthValue();
		int tomorrowDay = now.getDayOfMonth();

		try {
			populateTdyTmrShowList(tomorrowYear, tomorrowMonth, tomorrowDay);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This operation populates tasks in Search Interval List with task objects
	 * that have deadlines within the next specified number of days by the user
	 * 
	 * E.g. of User Command: Show next 5 days
	 * 
	 * @param: number
	 *             of days
	 * 
	 */
	private void getDayTasks(int days) {
		try {
			assert (days > Constants.ZERO_TIME_INTERVAL_CONSTANT);
			LocalDateTime startDateTime = LocalDateTime.now();
			LocalDateTime endDateTime = startDateTime.plusDays(days);
			populateDayWeekShowList(startDateTime, endDateTime);
		} catch (Exception ex) {
			throw ex;
		} catch (Error er) {
			throw er;
		}
	}

	/**
	 * This operation populates tasks in Search Interval List with task objects
	 * that have deadlines within the next specified number of weeks by the user
	 * 
	 * E.g. of User Command: Show next 1 week
	 * 
	 * @param: number
	 *             of weeks
	 * 
	 */
	private void getWeekTasks(int weeks) {
		try {
			assert (weeks > Constants.ZERO_TIME_INTERVAL_CONSTANT);
			LocalDateTime startDateTime = LocalDateTime.now();
			LocalDateTime endDateTime = startDateTime.plusWeeks(weeks);
			populateDayWeekShowList(startDateTime, endDateTime);
		} catch (Exception ex) {
			throw ex;
		} catch (Error er) {
			throw er;
		}
	}
	
	/**
	 * This operation retrieves all the tasks from the Task List
	 * and populates the showInterval List
	 *
	 */
	private void getAllTasksInInterval() {
		showIntervalList = new ArrayList<TaskObject>();
		try {
		    for(TaskObject taskObject : taskList) {
		        showIntervalList.add(taskObject);
		    }
		} catch (Exception e) {
			throw e;
		}
	}

	private void displayAllTasksInInterval() {
		getAllTasksInInterval();
		refreshDisplay(showIntervalList);
	}

	/**
	 * This operation populates tasks in the user specified category in the list
	 * to be displayed to the user
	 * 
	 * @param: taskStatus
	 * 
	 */
	private void displayTasks(TaskStatus taskStatus) {
		ArrayList<TaskObject> list = new ArrayList<TaskObject>();
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskStatus() == taskStatus) {
				list.add(task);
			}
		}
		refreshDisplay(list);
	}

	/**
	 * This operation populates tasks that are either due today
	 * or tomorrow based on the calling method
	 * 
	 * @param: year, month, day
	 */
	private void populateTdyTmrShowList(int year, int month, int day) {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject taskObject = taskList.get(i);
			String taskType = taskObject.getTaskType();
			if (taskType.equalsIgnoreCase(Constants.STRING_TASKTYPE_DEADLINE)) {
				int taskDay = taskObject.getDateDay();
				int taskMonth = taskObject.getDateMonth();
				int taskYear = taskObject.getDateYear();

				if (year == taskYear && month == taskMonth && day == taskDay) {
					showIntervalList.add(taskObject);
				}
			}
		}
	}
	
	/**
	 * This operation populates tasks that are either due within 
	 * a specified number of days or weeks
	 * 
	 * @param: startDateTime, endDateTime
	 */
	private void populateDayWeekShowList(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject taskObject = taskList.get(i);
			String taskType = taskObject.getTaskType();
			if (taskType.equalsIgnoreCase(Constants.STRING_TASKTYPE_DEADLINE)) {
				LocalDateTime taskDateTime = getTaskDateTime(taskObject);
				if ((taskDateTime.isEqual(startDateTime) || taskDateTime.isAfter(startDateTime))
						&& (taskDateTime.isEqual(endDateTime) || taskDateTime.isBefore(endDateTime))) {
					showIntervalList.add(taskObject);
				}
			}
		}
	}
	
	/**
	 * This operation returns a date time object based on the
	 * taskObject's date time if specified. Otherwise it assigns
	 * the latest hour and minute possible on the given day.
	 * 
	 * @param: taskObject
	 */
	private LocalDateTime getTaskDateTime(TaskObject taskObject) {
		int taskDay = taskObject.getDateDay();
		int taskMonth = taskObject.getDateMonth();
		int taskYear = taskObject.getDateYear();
		int taskHour = taskObject.getDateHour();
		int taskMinute = taskObject.getDateMinute();
		if(taskHour == Constants.INVALID_VALUE_CONSTANT) {
			taskHour = Constants.TIME_LATEST_HOUR_CONSTANT;
		}
		if(taskMinute == Constants.INVALID_VALUE_CONSTANT) {
			taskMinute = Constants.TIME_LATEST_MINUTE_CONSTANT;
		}
		LocalDateTime taskDateTime = LocalDateTime.of(taskYear, taskMonth, taskDay, taskHour, taskMinute);
		return taskDateTime;
	}

	private boolean isNotNullFilePath(String filePath) {
		boolean isNotNull = (filePath != null);
		return isNotNull;
	}

	private boolean isNotNullTaskObject(TaskObject taskObject) {
		boolean isNotNull = (taskObject != null);
		return isNotNull;
	}

	private boolean isValidTaskObject(TaskObject taskObject) {
		boolean isValid = taskList.contains(taskObject);
		return isValid;
	}

	/**
	 * This operation returns True if all IDs are valid;
	 * Otherwise False
	 * 
	 * @param: ID List
	 */
	private boolean isValidIdList(ArrayList<Integer> idList) {
		boolean isValid = true;
		if (deleteIdSize > Constants.EMPTY_LIST_SIZE_CONSTANT) {
			logger.log(Level.FINE, String.format(Constants.FINE_DELETE_LIST_VALIDITY, idList.size()));
			for (int i = 0; i < idList.size(); i++) {
				int taskId = idList.get(i);
				if (!isValidTaskId(taskId)) {
					isValid = false;
					break;
				}
				logger.log(Level.FINER,
						String.format(Constants.FINER_VALID_DELETE_ID, i, displayList.get(i).getTaskName()));
			}
		} else {
			isValid = false;
			logger.log(Level.INFO, String.format(Constants.INFO_DELETE_LIST, deleteIdSize));
		}
		return isValid;
	}
	
	/**
	 * This operation deletes the corresponding tasks based on IDs
	 * from the taskList
	 * 
	 * @param: deleteIdList
	 */
	private static void deleteFromTaskList(ArrayList<Integer> deleteIds) {
		for (int i = 0; i < deleteIds.size(); i++) {
			TaskObject taskObject = displayList.get(deleteIds.get(i));
			int index = taskList.indexOf(taskObject);
			taskList.remove(index);
			history.pushDeleteToUndo(taskObject);
		}
		CommandObject commandObject = history.peekUndoStack();
		commandObject.setPrecedingObjects(deleteIdSize - Constants.DECREMENT_PRECEDING_OBJECTS_CONSTANT);
	}

	public boolean isValidTaskId(int taskId) {
		boolean isValid = true;
		if (taskId >= displayList.size() || taskId < Constants.EMPTY_LIST_SIZE_CONSTANT) {
			isValid = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_INVALID_DELETE_ID, taskId));
		}
		return isValid;
	}
	
	private static int getLatestTaskIndex(TaskObject taskObject) {
		int latestIndex = Constants.INVALID_VALUE_CONSTANT;
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject currentTaskObject = taskList.get(i);
			if(taskObject.equals(currentTaskObject)){
				latestIndex = i;
			}
		}
		return latestIndex;
	}

	/**
	 * This operation sorts the list of Tasks and saves them to Storage
	 *
	 * @param Task
	 *            List
	 * @throws Exception
	 */
	private static void sortAndSave(ArrayList<TaskObject> taskList) throws Exception {
		try {
			sortByDate(taskList);
			storage.saveTasks(taskList);
		} catch (Exception e) {
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_SORT_SAVE, e));
			throw e;
		}
	}

	/**
	 * This operation sorts all tasks based on the date-time of the task
	 *
	 * @param list
	 *            to be sorted
	 * @throws Exception
	 */
	private static void sortByDate(ArrayList<TaskObject> list) throws Exception {
		try {
			// Sort by Date-Time
			Collections.sort(list);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * This operation constructs the feedback to be displayed to the User after
	 * each User Operation
	 *
	 * @param Command
	 *            type, isSuccess(true if User operation is executed
	 *            successfully; Otherwise false), Task Object
	 *
	 * @return Feedback to the User
	 */
	private static String showFeedback(CommandType commandType, boolean isSuccess, TaskObject taskObject) {

		switch (commandType) {
		case ADD:
			return getAddFeedback(isSuccess, taskObject);
		case DELETE:
			return getDeleteFeedback(isSuccess, taskObject);
		case SEARCH:
			return getSearchFeedback(isSuccess, taskObject);
		case UPDATE:
			return getUpdateFeedback(isSuccess, taskObject);
		case UNDO:
			return getUndoFeedback(isSuccess, taskObject);
		case REDO:
			return getRedoFeedback(isSuccess, taskObject);
		case DONE:
			return getDoneFeedback(isSuccess, taskObject);
		case UNDONE:
			return getUndoneFeedback(isSuccess, taskObject);
		case CHANGE_FILE_PATH:
			return getChangeFilePathFeedback(isSuccess, taskObject);
		case SHOW:
			return getShowFeedback(isSuccess, taskObject);
		case CHANGE_CATEGORY:
			return getChangeCategoryFeedback(isSuccess, taskObject);
		default:
			throw new Error("Unrecognized command type");
		}
	}
	
	private static String getAddFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess && taskObject != null) {
			feedback = Constants.MESSAGE_ADD_SUCCESSFUL;
			feedback = getFeedbackDetails(feedback, taskObject);
		} else {
			feedback =  Constants.MESSAGE_ADD_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getDeleteFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback =  String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, deleteIdSize);
		} else {
			feedback = Constants.MESSAGE_DELETE_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getSearchFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback = String.format(Constants.MESSAGE_SEARCH_SUCCESSFUL, searchIdSize);
		} else {
			feedback = Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getUpdateFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess && taskObject != null) {
			feedback =  Constants.MESSAGE_UPDATE_SUCCESSFUL;
			feedback = getFeedbackDetails(feedback, taskObject);
		} else if (isSuccess && taskObject == null) {
			feedback =  Constants.MESSAGE_UPDATE_SUCCESSFUL;
		} else {
			feedback = Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getUndoFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback = Constants.MESSAGE_UNDO_SUCCESSFUL;
			feedback = getUndoFeedbackDetails(feedback);
		} else {
			feedback = Constants.MESSAGE_UNDO_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getRedoFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback = Constants.MESSAGE_REDO_SUCCESSFUL;
			feedback = getRedoFeedbackDetails(feedback);
		} else {
			feedback = Constants.MESSAGE_REDO_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getDoneFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess && taskObject != null) {
			String taskName = taskObject.getTaskName();
			feedback = String.format(Constants.MESSAGE_DONE_SUCCESSFUL, taskName);
		} else {
			feedback = Constants.MESSAGE_DONE_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getUndoneFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess && taskObject != null) {
			String taskName = taskObject.getTaskName();
			feedback = String.format(Constants.MESSAGE_UNDONE_SUCCESSFUL, taskName);
		} else {
			feedback = Constants.MESSAGE_UNDONE_UNSUCCESSFUL;
		}
		return feedback;
	}
	
	private static String getChangeFilePathFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback = String.format(Constants.MESSAGE_CHANGE_PATH_SUCCESSFUL, filePath);
		} else {
			feedback = String.format(Constants.MESSAGE_CHANGE_PATH_UNSUCCESSFUL, filePath);
		}
		return feedback;
	}
	
	private static String getShowFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			int numTasks = showIntervalList.size();
			if (numTasks > Constants.EMPTY_LIST_SIZE_CONSTANT) {
				if (showType == ShowInterval.ALL) {
					feedback = Constants.MESSAGE_SHOW_SUCCESSFUL_ALL;
				} else {
					if (showCountInterval > Constants.EMPTY_LIST_SIZE_CONSTANT) {
						feedback = String.format(Constants.MESSAGE_SHOW_SUCCESSFUL_DEADLINE_INTERVAL, numTasks,
								showCountInterval, showType);
					} else {
						feedback = String.format(Constants.MESSAGE_SHOW_SUCCESSFUL_DEADLINE, numTasks, showType);
					}
				}
			} else {
				feedback = String.format(Constants.MESSAGE_SHOW_NO_RESULTS, numTasks, showType);
			}

		} else {
			feedback = String.format(Constants.MESSAGE_SHOW_UNSUCCESSFUL, showType);
		}
		return feedback;
	}
	
	private static String getChangeCategoryFeedback(boolean isSuccess, TaskObject taskObject) {
		String feedback = new String();
		if (isSuccess) {
			feedback = String.format(Constants.MESSAGE_CHANGE_CATEGORY_SUCCESSFUL, taskCategory);
		} else {
			feedback = String.format(Constants.MESSAGE_CHANGE_CATEGORY_UNSUCCESSFUL, taskCategory);
		}
		return feedback;
	}
	
	private static String getFeedbackDetails(String feedback, TaskObject taskObject) {
		feedback = getFeedbackName(feedback, taskObject);
		if(taskObject.getTaskType() != null) {
			feedback = getFeedbackDateTime(feedback, taskObject);
		}
		feedback = getFeedbackLocation(feedback, taskObject);
		return feedback;
	}
	
	private static String getUndoFeedbackDetails(String feedback) {
		String feedbackUndoOperation = String.format(feedback, undoCommandType);
		return feedbackUndoOperation;
	}
	
	private static String getRedoFeedbackDetails(String feedback) {
		String feedbackUndoOperation = String.format(feedback, redoCommandType);
		return feedbackUndoOperation;
	}
	
	private static String getFeedbackDateTime(String feedback, TaskObject taskObject){
		String taskType = taskObject.getTaskType();
		if(taskType.equalsIgnoreCase(Constants.STRING_TASKTYPE_DEADLINE)){
			feedback = getFeedbackDate(feedback, taskObject);
			feedback = getFeedbackTime(feedback,taskObject);
		}
		return feedback;
	}
	
	private static String getFeedbackName(String feedback, TaskObject taskObject) {
		//int taskIndex = taskList.indexOf(taskObject);
		int taskIndex = getLatestTaskIndex(taskObject);
		String taskName = taskObject.getTaskName();
		String feedbackName = String.format(Constants.STRING_TASK_NAME_INDEX, ++taskIndex, taskName);
		feedback = feedback.concat(feedbackName);
		return feedback;
	}
	
	private static String getFeedbackDate(String feedback, TaskObject taskObject) {
		if(!taskObject.getFormattedDate().equals(Constants.STRING_CONSTANT_EMPTY)){
			String date = String.format(Constants.STRING_TASK_DATE, taskObject.getFormattedDate());
			feedback = feedback.concat(date);
		}
		return feedback;
	}
	
	private static String getFeedbackTime(String feedback, TaskObject taskObject) {
		if(!taskObject.getFormattedTime().equals(Constants.STRING_CONSTANT_EMPTY)){
			String time = String.format(Constants.STRING_TASK_TIME, taskObject.getFormattedTime());
			feedback = feedback.concat(time);
		}
		return feedback;
	}
	
	private static String getFeedbackLocation(String feedback, TaskObject taskObject) {
		if(taskObject.getLocation() != null && !taskObject.getLocation().equals(Constants.STRING_CONSTANT_EMPTY)) {
			String location = String.format(Constants.STRING_TASK_LOCATION,taskObject.getLocation()); 
			feedback = feedback.concat(location);
		}
		return feedback;
	}
}
