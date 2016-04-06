package tasknote.shared;
//@@author A0126172M
/**
 * TaskListIOException is used to handle errors in writing or reading the list to/from file.
 */
public class TaskListIOException extends Exception{

	public TaskListIOException(){}
	
	public TaskListIOException(String message){
		super(message);
	}
}