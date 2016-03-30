package tasknote.shared;

public class Constants {

	/*
	 * These are the String Constants that will be displayed before or after
	 * each user operation
	 */
	public static final String MESSAGE_ADD_SUCCESSFUL = "Added Successfully: %d. %s";
	public static final String MESSAGE_ADD_UNSUCCESSFUL = "Add Failed";
	public static final String MESSAGE_DELETE_SUCCESSFUL = "Deleted %d task(s) Successfully";
	public static final String MESSAGE_DELETE_UNSUCCESSFUL = "Deletion Failed";
	public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks contain the entered search string";
	public static final String MESSAGE_SEARCH_SUCCESSFUL = "Search Successful: %d Result(s) Retrieved";
	public static final String MESSAGE_UPDATE_SUCCESSFUL = "Task was Successfully Updated";
	public static final String MESSAGE_UPDATE_UNSUCCESSFUL = "Update Failed";
	public static final String MESSAGE_UNDO_SUCCESSFUL = "The Last Operation has been Undone Successfully";
	public static final String MESSAGE_UNDO_UNSUCCESSFUL = "Undo Command Failed to be executed";
	public static final String MESSAGE_REDO_SUCCESSFUL = "The Last Operation has been re-done Successfully";
	public static final String MESSAGE_REDO_UNSUCCESSFUL = "Redo Command Failed to be executed";
	public static final String MESSAGE_DONE_SUCCESSFUL = "Task \"%s\" has been marked as completed Successfully";
	public static final String MESSAGE_DONE_UNSUCCESSFUL = "Mark as complete failed";
	public static final String MESSAGE_SHOW_SUCCESSFUL_DEADLINE_INTERVAL = "%d Deadline Task(s) due [ %d %s ]";
	public static final String MESSAGE_SHOW_SUCCESSFUL_DEADLINE = "%d Deadline Task(s) due [ %s ]";
	public static final String MESSAGE_SHOW_SUCCESSFUL_ALL = "All Tasks are displayed";
	public static final String MESSAGE_SHOW_NO_RESULTS = "No Deadline Tasks are due within this period";
	public static final String MESSAGE_SHOW_UNSUCCESSFUL = "Unable to show deadline tasks within this period";
	public static final String MESSAGE_CHANGE_PATH_SUCCESSFUL = "File path successfuly changed to: %s";
	public static final String MESSAGE_CHANGE_PATH_UNSUCCESSFUL = "Unsuccessful operation. Unable to change file path to: %s";
	public static final String MESSAGE_CHANGE_CATEGORY_SUCCESSFUL = "%s tasks have been successfully displayed";
	public static final String MESSAGE_CHANGE_CATEGORY_UNSUCCESSFUL = "Unsuccessful operation. Unable to display %s tasks";
	

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
	public static final String MESSAGE_HELP_ADD = "1. add <TaskName> on <time> by <date> at <location>\n"
												+ "2. add <TaskName> from <start time> to <end time> by <date> at <location>";
	public static final String MESSAGE_HELP_DELETE = "1. delete <TaskID> <more TaskIDs...>\n"
													+ "2. delete <start TaskID> to <end TaskID>";
	public static final String MESSAGE_HELP_SEARCH = "search <keyword> <more keywords...>";
	public static final String MESSAGE_HELP_UPDATE = "edit <TaskID> <new TaskName> on <time> by <date> at <location>";
	public static final String MESSAGE_HELP_UNDO = "undo";
	public static final String MESSAGE_HELP_REDO = "redo";
	public static final String MESSAGE_HELP_DONE = "done <TaskID>";
	public static final String MESSAGE_HELP_CHANGE_FILE_PATH = "relocate <new file path>";
	public static final String MESSAGE_HELP_SHOW = "1. show all\n"
												+ "2. show today/tomrorow\n"
												+ "3. show next [next ...] weeks/day";
	public static final String MESSAGE_HELP_CHANGE_CATEGORY = "";
	public static final String MESSAGE_HELP_HELP = "";
	public static final String MESSAGE_HELP_EXIT = "exit";
	
	
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
	public static final String INFO_DONE_SUCCESSFUL = "Task marked as complete successfully";
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
	
	public static final String EMPTY_STRING = "";
	public static final String NEW_LINE_STRING = "\n";
	
	/*
	 * These are the integer constants used in the program while executing
	 * specific user operations
	 */
	public static final int EMPTY_LIST_SIZE = 0;
	public static final int ZERO_TIME_INTERVAL = 0;
	public static final int DECREMENT_PRECEDING_OBJECTS = 1;
	public static final int INCREMENT_DAY_TOMORROW = 1;

}
