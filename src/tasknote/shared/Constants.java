package tasknote.shared;

public class Constants {
	
	/* 
	 * These are the String Constants that will be displayed before or
	 * after each user operation
	 */
	private static final String MESSAGE_ADD_SUCCESSFUL = "Added Successfully: %d. %s";
	private static final String MESSAGE_ADD_UNSUCCESSFUL = "Add Failed";
	private static final String MESSAGE_DELETE_SUCCESSFUL = "Deleted %d task(s) Successfuly";
	private static final String MESSAGE_DELETE_UNSUCCESSFUL = "Deletion Failed";
	private static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks contain the entered search string";
	private static final String MESSAGE_SEARCH_SUCCESSFUL = "Search Successful: %d Result(s) Retrieved";
	private static final String MESSAGE_UPDATE_SUCCESSFUL = "Task was Successfully Updated";
	private static final String MESSAGE_UPDATE_UNSUCCESSFUL = "Update Failed";
	
	/* These are the warnings that will be displayed if the user does 
	 * not enter valid commands
	 */
	private static final String WARNING_NULL_COMMAND = "Command type string cannot be null!";
	private static final String WARNING_INVALID_COMMAND = "Invalid Command. Please try again.";
	private static final String WARNING_INVALID_DELETE_INDEX = "Invalid Deletion Index. Please try again.";

}
