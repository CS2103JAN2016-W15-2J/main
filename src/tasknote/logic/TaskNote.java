package tasknote.logic;

import tasknote.storage.Storage;
import tasknote.shared.TaskObject;
import tasknote.shared.TaskObject.TASK_STATUS;
import tasknote.shared.COMMAND_TYPE;
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

	/*
	 * This is the storage object that will be used to load tasks into the
	 * taskList and it will be called to save the tasks after each user
	 * operation
	 */
	private static Storage storage = new Storage();

	/*
	 * This is used to store the filePath
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
		return showFeedback(COMMAND_TYPE.ADD, isSuccess, taskObject);
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
		boolean isSuccess = true;
		try {
			assert (deleteIdSize > Constants.EMPTY_LIST_SIZE && isValidIdList(deleteIds));
			deleteFromTaskList(deleteIds);
			storage.saveTasks(taskList);
		} catch (Exception ex) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_DELETE_FAILURE, ex));
		} catch (Error er) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_DELETE_INVALID_LIST, er));
		}
		return showFeedback(COMMAND_TYPE.DELETE, isSuccess, null);
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
			assert (searchIdSize > Constants.EMPTY_LIST_SIZE);
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
		return showFeedback(COMMAND_TYPE.SEARCH, isSuccess, null);
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

		return showFeedback(COMMAND_TYPE.UPDATE, isSuccess, updatedTaskObject);
	}

	/**
	 * This operation reverts the action executed by the last command
	 * 
	 * @return status of the operation
	 */
	public String undoLastCommand() {
		boolean isSuccess = true;
		int undoCount = 0;
		try {
			CommandObject commandObject = history.peekUndoStack();
			int numPrecedingObjects = commandObject.getPrecedingObjects();

			while (undoCount <= numPrecedingObjects) {
				commandObject = history.popUndoStack();
				COMMAND_TYPE commandType = commandObject.getRevertCommandType();
				if (commandType == COMMAND_TYPE.ADD) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushDeleteToRedo(taskObject);
					taskList.add(taskObject);
				} else if (commandType == COMMAND_TYPE.DELETE) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushAddToRedo(taskObject);
					taskList.remove(taskObject);
				} else if (commandType == COMMAND_TYPE.UPDATE) {
					CommandObject oldObject = history.popUndoStack();
					CommandObject newObject = history.popUndoStack();
					TaskObject oldTaskObject = oldObject.getTaskObject();
					TaskObject newTaskObject = newObject.getTaskObject();
					history.pushUpdateToRedo(oldTaskObject, newTaskObject);
					history.pushAddToUndo(newTaskObject);
					history.pushDeleteToUndo(oldTaskObject);
				} else if (commandType == COMMAND_TYPE.DONE) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushTaskCompletionToRedo(taskObject);
					taskList.remove(taskObject);
					boolean isComplete = taskObject.getIsMarkedDone();
					taskObject.setIsMarkedDone(!isComplete);
					isComplete = taskObject.getIsMarkedDone();
					taskList.add(taskObject);
				}
				history.peekRedoStack().setPrecedingObjects(numPrecedingObjects);
				undoCount++;
			}
			sortAndSave(taskList);
			logger.log(Level.INFO, Constants.INFO_UNDO_SUCCESSFUL);
		} catch (Exception e) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_UNDO, e));
		}

		return showFeedback(COMMAND_TYPE.UNDO, isSuccess, null);
	}

	public String redoLastUndoCommand() {
		boolean isSuccess = true;
		int redoCount = 0;
		try {
			CommandObject commandObject = history.peekRedoStack();
			int numPrecedingObjects = commandObject.getPrecedingObjects();

			while (redoCount <= numPrecedingObjects) {
				commandObject = history.popRedoStack();
				COMMAND_TYPE commandType = commandObject.getRevertCommandType();
				if (commandType == COMMAND_TYPE.ADD) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushAddToUndo(taskObject);
					taskList.add(taskObject);
				} else if (commandType == COMMAND_TYPE.DELETE) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushDeleteToUndo(taskObject);
					taskList.remove(taskObject);
				} else if (commandType == COMMAND_TYPE.UPDATE) {
					CommandObject oldObject = history.popRedoStack();
					CommandObject newObject = history.popRedoStack();
					TaskObject oldTaskObject = oldObject.getTaskObject();
					TaskObject newTaskObject = newObject.getTaskObject();
					history.pushUpdateToUndo(oldTaskObject, newTaskObject);
					history.pushAddToUndo(oldTaskObject);
					history.pushDeleteToUndo(newTaskObject);
				} else if (commandType == COMMAND_TYPE.DONE) {
					TaskObject taskObject = commandObject.getTaskObject();
					history.pushTaskCompletionToUndo(taskObject);
					taskList.remove(taskObject);
					boolean isComplete = taskObject.getIsMarkedDone();
					taskObject.setIsMarkedDone(!isComplete);
					isComplete = taskObject.getIsMarkedDone();
					taskList.add(taskObject);
				}
				history.peekUndoStack().setPrecedingObjects(numPrecedingObjects);
				redoCount++;
			}
			sortAndSave(taskList);
			logger.log(Level.INFO, Constants.INFO_REDO_SUCCESSFUL);
		} catch (Exception e) {
			isSuccess = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_EXECUTE_REDO, e));
		}
		return showFeedback(COMMAND_TYPE.REDO, isSuccess, null);
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
			feedback = showFeedback(COMMAND_TYPE.DONE, isSuccess, taskObject);
		} else {
			feedback = showFeedback(COMMAND_TYPE.UNDONE, isSuccess, taskObject);
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
			assert (!filePath.equals(Constants.EMPTY_STRING) && isNotNullFilePath(filePath));
			isSuccess = storage.changePath(filePath);
			if (isSuccess) {
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
		return showFeedback(COMMAND_TYPE.CHANGE_FILE_PATH, isSuccess, null);
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
				getAllTasks();
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
		return showFeedback(COMMAND_TYPE.SHOW, isSuccess, null);
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
				displayAllTasks();
				taskCategory = ShowCategory.ALL;
				break;
			case OUTSTANDING:
				displayTasks(TASK_STATUS.TASK_OUTSTANDING);
				taskCategory = ShowCategory.OUTSTANDING;
				break;
			case OVERDUE:
				displayTasks(TASK_STATUS.TASK_OVERDUE);
				taskCategory = ShowCategory.OVERDUE;
				break;
			case COMPLETED:
				displayTasks(TASK_STATUS.TASK_COMPLETED);
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
		return showFeedback(COMMAND_TYPE.CHANGE_CATEGORY, isSuccess, null);
	}

	public static String displayHelpMessage(COMMAND_TYPE commandType) {
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
		now = now.plusDays(Constants.INCREMENT_DAY_TOMORROW);

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
			assert (days > Constants.ZERO_TIME_INTERVAL);
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
			assert (weeks > Constants.ZERO_TIME_INTERVAL);
			LocalDateTime startDateTime = LocalDateTime.now();
			LocalDateTime endDateTime = startDateTime.plusWeeks(weeks);
			populateDayWeekShowList(startDateTime, endDateTime);
		} catch (Exception ex) {
			throw ex;
		} catch (Error er) {
			throw er;
		}
	}

	private void getAllTasks() {
		try {
			for (int i = 0; i < taskList.size(); i++) {
				TaskObject taskObject = taskList.get(i);
				showIntervalList.add(taskObject);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void displayAllTasks() {
		getAllTasks();
		refreshDisplay(showIntervalList);
	}

	/**
	 * This operation populates tasks in the user specified category in the list
	 * to be displayed to the user
	 * 
	 * @param: taskStatus
	 * 
	 */
	private void displayTasks(TASK_STATUS taskStatus) {
		ArrayList<TaskObject> list = new ArrayList<TaskObject>();
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskStatus() == taskStatus) {
				list.add(task);
			}
		}
		refreshDisplay(list);
	}

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

	private LocalDateTime getTaskDateTime(TaskObject taskObject) {
		int taskDay = taskObject.getDateDay();
		int taskMonth = taskObject.getDateMonth();
		int taskYear = taskObject.getDateYear();
		int taskHour = taskObject.getDateHour();
		int taskMinute = taskObject.getDateMinute();
		if(taskHour == Constants.INVALID_VALUE_CONSTANT) {
			taskHour = Constants.TIME_LATEST_HOUR;
		}
		if(taskMinute == Constants.INVALID_VALUE_CONSTANT) {
			taskMinute = Constants.TIME_LATEST_MINUTE;
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

	private boolean isValidIdList(ArrayList<Integer> idList) {
		boolean isValid = true;
		if (deleteIdSize > 0) {
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

	private static void deleteFromTaskList(ArrayList<Integer> deleteIds) {
		for (int i = 0; i < deleteIds.size(); i++) {
			TaskObject taskObject = displayList.get(deleteIds.get(i));
			int index = taskList.indexOf(taskObject);
			taskList.remove(index);
			history.pushDeleteToUndo(taskObject);
		}
		CommandObject commandObject = history.peekUndoStack();
		commandObject.setPrecedingObjects(deleteIdSize - Constants.DECREMENT_PRECEDING_OBJECTS);
	}

	public boolean isValidTaskId(int taskId) {
		boolean isValid = true;
		if (taskId >= displayList.size() || taskId < Constants.EMPTY_LIST_SIZE) {
			isValid = false;
			logger.log(Level.WARNING, String.format(Constants.WARNING_INVALID_DELETE_ID, taskId));
		}
		return isValid;
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
			// System.out.println("Sort by date has an error");
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
	private static String showFeedback(COMMAND_TYPE commandType, boolean isSuccess, TaskObject task) {

		switch (commandType) {
		case ADD:
			if (isSuccess && task != null) {
				int taskIndex = taskList.indexOf(task);
				String taskName = task.getTaskName();
				return String.format(Constants.MESSAGE_ADD_SUCCESSFUL, ++taskIndex, taskName);
			} else {
				return Constants.MESSAGE_ADD_UNSUCCESSFUL;
			}
		case DELETE:
			if (isSuccess) {
				return String.format(Constants.MESSAGE_DELETE_SUCCESSFUL, deleteIdSize);
			} else {
				return Constants.MESSAGE_DELETE_UNSUCCESSFUL;
			}
		case SEARCH:
			if (isSuccess) {
				return String.format(Constants.MESSAGE_SEARCH_SUCCESSFUL, searchIdSize);
			} else {
				return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
			}
		case UPDATE:
			if (isSuccess && task != null) {
				// TODO: Feedback which fields were updated
				return Constants.MESSAGE_UPDATE_SUCCESSFUL;
			} else {
				// TODO
				return Constants.MESSAGE_UPDATE_UNSUCCESSFUL;
			}
		case UNDO:
			if (isSuccess) {
				// TODO: Feedback what was undone
				return Constants.MESSAGE_UNDO_SUCCESSFUL;
			} else {
				// TODO
				return Constants.MESSAGE_UNDO_UNSUCCESSFUL;
			}
		case REDO:
			if (isSuccess) {
				// TODO: Feedback what was re-did
				return Constants.MESSAGE_REDO_SUCCESSFUL;
			} else {
				// TODO
				return Constants.MESSAGE_REDO_UNSUCCESSFUL;
			}
		case DONE:
			if (isSuccess && task != null) {
				String taskName = task.getTaskName();
				return String.format(Constants.MESSAGE_DONE_SUCCESSFUL, taskName);
			} else {
				return Constants.MESSAGE_DONE_UNSUCCESSFUL;
			}
		case UNDONE:
			if (isSuccess && task != null) {
				String taskName = task.getTaskName();
				return String.format(Constants.MESSAGE_UNDONE_SUCCESSFUL, taskName);
			} else {
				return Constants.MESSAGE_UNDONE_UNSUCCESSFUL;
			}
		case CHANGE_FILE_PATH:
			if (isSuccess) {
				return String.format(Constants.MESSAGE_CHANGE_PATH_SUCCESSFUL, filePath);
			} else {
				return String.format(Constants.MESSAGE_CHANGE_PATH_UNSUCCESSFUL, filePath);
			}
		case SHOW:
			if (isSuccess) {
				int numTasks = showIntervalList.size();
				if (numTasks > 0) {
					if (showType == ShowInterval.ALL) {
						return Constants.MESSAGE_SHOW_SUCCESSFUL_ALL;
					} else {
						if (showCountInterval > Constants.EMPTY_LIST_SIZE) {
							return String.format(Constants.MESSAGE_SHOW_SUCCESSFUL_DEADLINE_INTERVAL, numTasks,
									showCountInterval, showType);
						} else {
							return String.format(Constants.MESSAGE_SHOW_SUCCESSFUL_DEADLINE, numTasks, showType);
						}
					}
				} else {
					return String.format(Constants.MESSAGE_SHOW_NO_RESULTS, numTasks, showType);
				}

			} else {
				return String.format(Constants.MESSAGE_SHOW_UNSUCCESSFUL, showType);
			}
		case CHANGE_CATEGORY:
			if (isSuccess) {
				return String.format(Constants.MESSAGE_CHANGE_CATEGORY_SUCCESSFUL, taskCategory);
			} else {
				return String.format(Constants.MESSAGE_CHANGE_CATEGORY_UNSUCCESSFUL, taskCategory);
			}
		default:
			throw new Error("Unrecognized command type");
		}
	}
}
