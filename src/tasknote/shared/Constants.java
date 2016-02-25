package tasknote.shared;

public class Constants {
	
	/* 
	 * These are the String Constants that will be displayed before or
	 * after each user operation
	 */
	public static final String MESSAGE_ADD_SUCCESSFUL = "Added Successfully: %d. %s";
	public static final String MESSAGE_ADD_UNSUCCESSFUL = "Add Failed";
	public static final String MESSAGE_DELETE_SUCCESSFUL = "Deleted %d task(s) Successfuly";
	public static final String MESSAGE_DELETE_UNSUCCESSFUL = "Deletion Failed";
	public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks contain the entered search string";
	public static final String MESSAGE_SEARCH_SUCCESSFUL = "Search Successful: %d Result(s) Retrieved";
	public static final String MESSAGE_UPDATE_SUCCESSFUL = "Task was Successfully Updated";
	public static final String MESSAGE_UPDATE_UNSUCCESSFUL = "Update Failed";
	
	/* These are the warnings that will be displayed if the user does 
	 * not enter valid commands
	 */
	public static final String WARNING_NULL_COMMAND = "Command type string cannot be null!";
	public static final String WARNING_INVALID_COMMAND = "Invalid Command. Please try again.";
	public static final String WARNING_INVALID_DELETE_INDEX = "Invalid Deletion Index. Please try again.";

}
