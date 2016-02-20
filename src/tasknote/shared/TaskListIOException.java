/**
 * TaskListIOException is used to handle errors in writing or reading the list from file.
 */
public class TaskListIOException extends Exception{
	public TaskListIOException(){}
	
	public TaskListIOException(String message){
		super(message);
	}
}