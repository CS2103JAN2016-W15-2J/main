package tasknote.shared;

import javafx.beans.property.SimpleStringProperty;

public class Constants {

	/*
	 * These are the String Constants that will be displayed before or after
	 * each user operation
	 */
	public static final String MESSAGE_ADD_SUCCESSFUL = "Added Task Successfully\n";
	public static final String MESSAGE_ADD_UNSUCCESSFUL = "Add Failed";
	public static final String MESSAGE_DELETE_SUCCESSFUL = "Deleted %d task(s) Successfully";
	public static final String MESSAGE_DELETE_UNSUCCESSFUL = "Deletion Failed";
	public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks contain the entered search string";
	public static final String MESSAGE_SEARCH_SUCCESSFUL = "Search Successful: %d Result(s) Retrieved";
	public static final String MESSAGE_UPDATE_SUCCESSFUL = "Task was Successfully Updated\n";
	public static final String MESSAGE_UPDATE_UNSUCCESSFUL = "Update Failed";
	public static final String MESSAGE_UNDO_SUCCESSFUL = "The Last %s Operation has been Undone Successfully\n\n";
	public static final String MESSAGE_UNDO_UNSUCCESSFUL = "Undo Command Failed to be executed";
	public static final String MESSAGE_REDO_SUCCESSFUL = "The Last Undo [ %s ] Operation has been re-done Successfully\n\n";
	public static final String MESSAGE_REDO_UNSUCCESSFUL = "Redo Command Failed to be executed";
	public static final String MESSAGE_DONE_SUCCESSFUL = "Task \"%s\" has been marked as completed Successfully";
	public static final String MESSAGE_DONE_UNSUCCESSFUL = "Mark as complete failed";
	public static final String MESSAGE_UNDONE_SUCCESSFUL = "Task \"%s\" has been marked as Incomplete Successfully";
	public static final String MESSAGE_UNDONE_UNSUCCESSFUL = "Mark as Incomplete failed";
	public static final String MESSAGE_SHOW_SUCCESSFUL_DEADLINE_INTERVAL = "%d Deadline Task(s) due [ %d %s ]";
	public static final String MESSAGE_SHOW_SUCCESSFUL_DEADLINE = "%d Deadline Task(s) due [ %s ]";
	public static final String MESSAGE_SHOW_SUCCESSFUL_ALL = "All Tasks are displayed";
	public static final String MESSAGE_SHOW_NO_RESULTS = "No Deadline Tasks are due within this period";
	public static final String MESSAGE_SHOW_UNSUCCESSFUL = "Unable to show deadline tasks within this period";
	public static final String MESSAGE_CHANGE_PATH_SUCCESSFUL = "File path successfully changed to: %s";
	public static final String MESSAGE_CHANGE_PATH_UNSUCCESSFUL = "Unsuccessful operation. Unable to change file path to: %s";
	public static final String MESSAGE_CHANGE_CATEGORY_SUCCESSFUL = "%s tasks have been successfully displayed";
	public static final String MESSAGE_CHANGE_CATEGORY_UNSUCCESSFUL = "Unsuccessful operation. Unable to display %s tasks";
	
	
	/*
	 * These are strings that are used to display about tasks to users in notifications
	 */
	public static final String STRING_TASK_NAME_INDEX = "\nTask Index: \t%d"
			  										  + "\nTask Name: \t%s";
	public static final String STRING_TASK_LOCATION = "\nLocation: \t%s";
	public static final String STRING_TASK_DATE = "\nDate: \t\t%s";
	public static final String STRING_TASK_TIME = "\nTime: \t\t%s";
	
	/*
	 * These are the Messages to be displayed to inform user of valid 
	 * command entries
	 * 
	 * Format:
	 * 
	 * 	ADD
	 * 		Add deadline task: 
	 * 			add <TaskName> on <time> by <date>
	 * 		Add floating task:
	 * 			add <TaskName>
	 * 		Keywords: on, by, after ...
	 * 		Note: Place double quotes (E.g. Meet at "Gardens by the bay")
	 * 
	 */
	public static final String MESSAGE_HELP = "The Following are the List of Valid Commands: \n"
											+ "1. To add task: ADD\n"
											+ "2. To edit task: EDIT\n"
											+ "3. To do a sub string search for tasks: SEARCH\n"
											+ "4. To do exact search for tasks: SEARCH EXACT\n"
											+ "5. To delete task: DELETE\n"
											+ "6. To reverse previous action: UNDO\n"
											+ "7. To reverse previous Undo action: REDO\n"
											+ "8. To change file path: RELOCATE\n"
											+ "9. To display deadline tasks within specified time interval: SHOW\n"
											+ "10. To mark task as complete: DONE\n"
											+ "11. To mark task as incomplete: UNDONE\n\n"
											+ "For specific usage of a command, enter: HELP <Command>\n";
	public static final String MESSAGE_HELP_ADD = "ADD\n"
												+ "1. Add a Floating task (without deadline):\n"
												+ "		add <TaskName>\n\n"
												+ "2. Add a Deadline task:\n"
												+ "		add <TaskName> on <time> by <date> at <location>\n\n"
												+ "3. Add an Event task:\n"
												+ "		add <TaskName> from <start time> to <end time> by <date> at <location>";
	public static final String MESSAGE_HELP_DELETE = "DELETE\n"
													+ "1. Delete space separated IDs:\n"
													+ "		delete <TaskID> <more TaskIDs...>\n\n"
													+ "2. Delete range of IDs:\n"
													+ "		delete <start TaskID> to <end TaskID>";
	public static final String MESSAGE_HELP_SEARCH = "SEARCH\n"
												   + "1. Substring Search on single/multiple keywords:\n"
												   + "		search <keyword> <more keywords...>\n\n"
												   + "2. Exact search on single/multiple keywords:\n"
												   + "		search exact <keyword> <more keywords...>";
	public static final String MESSAGE_HELP_UPDATE = "EDIT\n"
												   + "Edit Task properties:\n"
												   + "		edit <TaskID> <new TaskName> on <time> by <date> at <location>";
	public static final String MESSAGE_HELP_UNDO = "UNDO\n"
												 + "Revert last operation:\n"
												 + "	undo";
	public static final String MESSAGE_HELP_REDO = "REDO\n"
												 + "Revert last undo operation:\n"
												 + "	redo";
	public static final String MESSAGE_HELP_DONE = "DONE\n"
												 + "Marked task with TaskID as Complete:\n"
												 + "	done <TaskID>";
	public static final String MESSAGE_HELP_UNDONE = "UNDONE\n"
			 									 + "Marked task with TaskID as Incomplete:\n"
			 									 + "	undone <TaskID>";
	public static final String MESSAGE_HELP_CHANGE_FILE_PATH = "RELOCATE\n"
															 + "Change file path:"
															 + "	relocate <new file path>";
	public static final String MESSAGE_HELP_SHOW = "SHOW\n"
												+ "1. Show All Tasks:"
												+ "		show all\n\n"
												+ "2. Show Tasks that are due today:\n"
												+ "		show today\n\n"
												+ "3. Show Tasks that are due tomorrow:\n"
												+ "		show tomorrow\n\n"
												+ "4. Show tasks that are due within next <d> days\n"
												+ "		show next <d> days\n"
												+ "		show next [next ... ] days\n\n"
												+ "3. Show tasks that are due within next <w> weeks\n"
												+ "		show next <w> weeks\n"
												+ "		show next [next ...] weeks";
	public static final String MESSAGE_HELP_CHANGE_CATEGORY = "";
	public static final String MESSAGE_HELP_EXIT = "EXIT\n"
												 + "Exit application:\n"
												 + "	exit";
	
	
	/*
	 * These are the warnings that will be displayed in the logs if the user
	 * does not enter valid commands
	 */
	public static final String WARNING_NULL_COMMAND = "Command type string cannot be null!";
	public static final String WARNING_INVALID_COMMAND = "Invalid Command. Please try again.";
	public static final String WARNING_INVALID_DELETE_INDEX = "Invalid Deletion Index. Please try again.";
	public static final String WARNING_EXECUTE_ADD_FAILURE = "Error occured while ADDING task: %s";
	public static final String WARNING_EXECUTE_ADD_INVALID_OBJECT = "Invalid object (NULL) passed to be added. Add failure: %s";
	public static final String WARNING_EXECUTE_DELETE_FAILURE = "Error occured while DELETING task(s): %s";
	public static final String WARNING_EXECUTE_DELETE_INVALID_LIST = "Delete ID list is either EMPTY or contains INVALID IDs: %s";
	public static final String WARNING_EXECUTE_UPDATE_FAILURE = "Error occured while UPDATING task(s): %s";
	public static final String WARNING_EXECUTE_UPDATE_INVALID_OBJECTID = "Task object Update Error: Either Update ID is INVALID or updated object is NULL: %s";
	public static final String WARNING_EXECUTE_SEARCH_FAILURE = "Error occured while SEARCHING and RETRIEVING results from task list: %s";
	public static final String WARNING_EXECUTE_SEARCH_NO_RESULT = "No valid search results due to empty Search ID list received: %s";
	public static final String WARNING_EXECUTE_COMPLETE_FAILURE = "Error occured while marking task as DONE: %s";
	public static final String WARNING_EXECUTE_COMPLETE_INVALID_OBJECT = "Invalid Task Object Error(not found in task list): %s";
	public static final String WARNING_EXECUTE_INCOMPLETE_FAILURE = "Error occured while marking task as UNDONE: %s";
	public static final String WARNING_EXECUTE_INCOMPLETE_INVALID_OBJECT = "Invalid Task Object Error(not found in task list): %s";
	public static final String WARNING_EXECUTE_UNDO = "Error occured while executing UNDO operation: %s";
	public static final String WARNING_EXECUTE_REDO = "Error occured while executing REDO operation: %s";
	public static final String WARNING_EXECUTE_SHOW_FAILURE = "Error while attempting to retrieve and SHOW taks for specifed time interval: %s";
	public static final String WARNING_EXECUTE_SHOW_INVALID_INTERVAL = "Invalid number of days/weeks entered: %s";
	public static final String WARNING_EXECUTE_CHANGE_PATH_FALSE = "Unable to execute change of file path. False returned by Storage";
	public static final String WARNING_EXECUTE_CHANGE_PATH_FAILURE = "Error: Change File Path to [ %s ] failed: %s";
	public static final String WARNING_EXECUTE_SHOW_INVALID_FILEPATH = "Error: Change File Path. Invalid file path [ %s ]";
	public static final String WARNING_EXECUTE_SHOW_CATEGORY_FAILURE = "Error: Unable to display tasks specified in category [ %s ] ;  Error: %s";
	public static final String WARNING_EXECUTE_SHOW_CATEGORY_INVALID = "Invalid Task Status Category specified. Valid Categories: [ALL, OUTSTANDING, OVERDUE, COMPLETED]; Error: %s";
	public static final String WARNING_EXECUTE_SORT_SAVE = "Error occured while SORTING tasks and SAVING to storage: %s";

	public static final String WARNING_INVALID_DELETE_ID = "Invalid DELETE ID: %d. Unable to execute DELETE operation.";

	/*
	 * These are the Info statements that will be displayed in the logs while
	 * executing each user operation
	 */
	public static final String INFO_DELETE_LIST = "No valid delete IDs have been specified. Size of Delete ID list = %d";
	public static final String INFO_ADD_SUCCESSFUL = "Task added to task list successfully";
	public static final String INFO_DONE_SUCCESSFUL = "Task marked as COMPLETE successfully";
	public static final String INFO_UNDONE_SUCCESSFUL = "Task marked as INCOMPLETE successfully";
	public static final String INFO_SEARCH_SUCCESSFUL = "Search result(s) retrieved successfully";
	public static final String INFO_UNDO_SUCCESSFUL = "Last operation is undone successfully";
	public static final String INFO_REDO_SUCCESSFUL = "Last operation is re-done successfully";
	public static final String INFO_EXECUTE_CHANGE_PATH_SUCCESSFUL = "File path has been successfully changed to: %s";
	
	public static final String FINE_DELETE_LIST_VALIDITY = "Checking validity of %d entries to be deleted from List";
	public static final String FINER_VALID_DELETE_ID = "Processed Valid ID [%d] : %s";

	/*
	 * These are the error messages that will be displayed if feedback is
	 * requested before and user requested action is performed
	 */
	public static final String ERROR_FEEDBACK = "Error: Feedback requested before action execution";

	/*
	 * These are strings that are used for taskStatus in TaskObject
	 */
	public static final String STRING_TASKSTATUS_DEFAULT = "TASK_DEFAULT";
	public static final String STRING_TASKSTATUS_OUTSTANDING = "TASK_OUTSTANDING";
	public static final String STRING_TASKSTATUS_OVERDUE = "TASK_OVERDUE";
	public static final String STRING_TASKSTATUS_COMPLETED = "TASK_COMPLETED";
	public static final String STRING_TASKSTATUS_INVALID_STORAGE = "TASK_INVALID_STORAGE";
	
	/*
	 * These are strings that are used for taskType in TaskObject
	 */
	public static final String STRING_TASKTYPE_DEADLINE = "DEADLINE";
	public static final String STRING_TASKTYPE_FLOATING = "FLOATING";
	
	/*
	 * These are the String constants used in the program while executing
	 * specific user operations
	 */
	public static final String STRING_CONSTANT_EMPTY = "";
	public static final String STRING_CONSTANT_SPACE = " ";
	public static final String STRING_CONSTANT_NEWLINE = "\n";
	public static final String STRING_CONSTANT_ESCAPED_DOUBLE_QUOTES = "\"";
	
	
	/*
	 * These are the integer constants used in the program while executing
	 * specific user operations
	 */
	public static final int EMPTY_LIST_SIZE_CONSTANT = 0;
	public static final int ZERO_TIME_INTERVAL_CONSTANT = 0;
	public static final int DECREMENT_PRECEDING_OBJECTS_CONSTANT = 1;
	public static final int INCREMENT_DAY_TOMORROW_CONSTANT = 1;
	public static final int INCREMENT_COUNT_CONSTANT = 1;
	public static final int INVALID_VALUE_CONSTANT = -1;
	public static final int TIME_LATEST_HOUR_CONSTANT = 11;
	public static final int TIME_LATEST_MINUTE_CONSTANT = 59;
	
	/*
	 * These are magic Strings that will be used for toString method
	 */
	private static final String STRING_TOSTRING_TASK_NAME = "TaskName = $1%s";
	private static final String STRING_TOSTRING_TASK_DATE = "\nDate = $1%s/$2%s/$3%s";
	private static final String STRING_TOSTRING_END_DATE = "\nEndDate = $1%s/$2%s/$3%s";
	private static final String STRING_TOSTRING_TIME = "\nTime = $1%s $2%s";
	private static final String STRING_TOSTRING_DURATION = "\nDuration = $1%s";
	private static final String STRING_TOSTRING_LOCATION = "\nLocation = $1%s";
	private static final String STRING_TOSTRING_NOTIFY_TIME = "\nNotifyTime = $1%s";
	private static final String STRING_TOSTRING_IS_NOTIFIED = "\nIsNotified = $1%s";
	private static final String STRING_TOSTRING_TASK_STATUS = "\nTaskStatus = $1%s";
	private static final String STRING_TOSTRING_TASK_TYPE = "\nTaskType = $1%s";
	private static final String STRING_TOSTRING_IS_MARKED_DONE = "\nisMarkedDone = $1%s";
	
	public static String produceTaskName(String taskName) {
		return String.format(STRING_TOSTRING_TASK_NAME, taskName);
	}

	public static String produceDate(int dateDay, int dateMonth, int dateYear) {
		return String.format(STRING_TOSTRING_TASK_DATE, dateDay, dateMonth, dateYear);
	}

	public static String produceEndDate(int endDateDay, int endDateMonth, int endDateYear) {
		return String.format(STRING_TOSTRING_END_DATE, endDateDay, endDateMonth, endDateYear);
	}

	public static String produceTime(int dateHour, int dateMinute) {
		return String.format(STRING_TOSTRING_TIME, dateHour, dateMinute);
	}

	public static String produceDuration(int duration) {
		return String.format(STRING_TOSTRING_DURATION, duration);
	}

	public static String produceLocation(String location) {
		return String.format(STRING_TOSTRING_LOCATION, location);
	}

	public static String produceNotifyTime(int notifyTime) {
		return String.format(STRING_TOSTRING_NOTIFY_TIME, notifyTime);
	}

	public static String produceIsNotified(boolean isNotified) {
		return String.format(STRING_TOSTRING_IS_NOTIFIED, isNotified);
	}

	public static String produceTaskStatus(SimpleStringProperty taskStatus) {
		return String.format(STRING_TOSTRING_TASK_STATUS, taskStatus);
	}
	
	public static String produceTaskType(String taskType) {
		return String.format(STRING_TOSTRING_TASK_TYPE, taskType);
	}

	public static String produceIsMarkedDone(boolean isMarkedDone) {
		return String.format(STRING_TOSTRING_IS_MARKED_DONE, isMarkedDone);
	}

}
