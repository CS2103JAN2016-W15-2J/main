package tasknote.shared;

public class Constants {
	
	/* 
	 * These are the String Constants that will be displayed before or
	 * after each user operation
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
	public static final String MESSAGE_SHOW_SUCCESSFUL = "%d Deadline Tasks are due";
	public static final String MESSAGE_SHOW_NO_RESULTS = "No Deadline Tasks are due within this period";
	public static final String MESSAGE_SHOW_UNSUCCESSFUL = "Unable to show deadline tasks within this period";
	
	/* These are the warnings that will be displayed in the logs if the user does 
	 * not enter valid commands
	 */
	public static final String WARNING_NULL_COMMAND = "Command type string cannot be null!";
	public static final String WARNING_INVALID_COMMAND = "Invalid Command. Please try again.";
	public static final String WARNING_INVALID_DELETE_INDEX = "Invalid Deletion Index. Please try again.";
	public static final String WARNING_EXECUTE_ADD = "Error occured while ADDING task: %s";
	public static final String WARNING_EXECUTE_DELETE = "Error occured while DELETING task(s): %s";
	public static final String WARNING_EXECUTE_UPDATE = "Error occured while UPDATING task(s): %s";
	public static final String WARNING_EXECUTE_SEARCH = "Error occured while SEARCHING and retrieving results from task list: %s";
	public static final String WARNING_EXECUTE_COMPLETE = "Error occured while marking task as DONE: %s";
	public static final String WARNING_EXECUTE_UNDO = "Error occured while executing UNDO operation: %s";
	public static final String WARNING_EXECUTE_REDO = "Error occured while executing REDO operation: %s";
	public static final String WARNING_EXECUTE_SORT_SAVE = "Error occured while SORTING tasks and SAVING to storage: %s";
	
	public static final String WARNING_INVALID_DELETE_ID = "Invalid DELETE ID: %d. Unable to execute DELETE operation.";
	
	public static final String INFO_DELETE_LIST = "No valid delete IDs have been specified. Size of Delete ID list = %d";
	public static final String FINE_DELETE_LIST_VALIDITY = "Checking validity of %d entries to be deleted from List";
	public static final String FINER_VALID_DELETE_ID = "Processed Valid ID [%d] : %s";
	
	/* These are the error messages that will be displayed if feedback is 
	 * requested before and user requested action is performed 
	 */
	public static final String ERROR_FEEDBACK = "Error: Feedback requested before action execution";
	
	/*
	 * These are strings that are used for taskStatus in TaskObject
	 */
	public static final String STRING_TASKSTATUS_DEFAULT = "TASK_DEFAULT";
	public static final String STRING_TASKSTATUS_OUTSTANDING = "TASK_OUTSTANDING";
	public static final String STRING_TASKSTATUS_COMPLETED = "TASK_COMPLETED";
	public static final String STRING_TASKSTATUS_INVALID_STORAGE = "TASK_INVALID_STORAGE";
	
	/* These are the integer constants used in the program 
	 * while executing specific user operations
	 */
	public static final int EMPTY_LIST_SIZE = 0;
	public static final int DECREMENT_PRECEDING_OBJECTS = 1;
	public static final int INCREMENT_DAY_TOMORROW = 1;

}
