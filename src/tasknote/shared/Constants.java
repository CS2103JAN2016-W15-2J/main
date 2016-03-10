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
	public static final String MESSAGE_UNDONE_SUCCESSFUL = "The Last Operation has been Undone Successfully";
	public static final String MESSAGE_UNDONE_UNSUCCESSFUL = "Undo Command Failed to be executed Successfully";
	public static final String MESSAGE_DONE_SUCCESSFUL = "Task \"%s\" has been marked as completed Successfully";
	public static final String MESSAGE_DONE_UNSUCCESSFUL = "Mark as complete failed";
	
	/* These are the warnings that will be displayed if the user does 
	 * not enter valid commands
	 */
	public static final String WARNING_NULL_COMMAND = "Command type string cannot be null!";
	public static final String WARNING_INVALID_COMMAND = "Invalid Command. Please try again.";
	public static final String WARNING_INVALID_DELETE_INDEX = "Invalid Deletion Index. Please try again.";
	
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
	public static int EMPTY_LIST_SIZE = 0;
	public static int DECREMENT_PRECEDING_OBJECTS = 1;

}
