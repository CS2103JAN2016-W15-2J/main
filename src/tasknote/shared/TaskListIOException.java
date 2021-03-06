//@@author A0126172M
package tasknote.shared;

/**
 * TaskListIOException is used to handle errors in writing or reading the list
 * to/from file.
 */
public class TaskListIOException extends Exception {

	private static final long serialVersionUID = 1L;

	public TaskListIOException() {
	}

	public TaskListIOException(String message) {
		super(message);
	}
}