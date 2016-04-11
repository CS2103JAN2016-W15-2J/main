//@@author A0126172M
package tasknote.storage;

import java.util.Stack;

/**
 * This class deals with the history of operations of PATH using Stacks
 *
 */
public class PathHistory {
	Stack<String> history;
	Stack<String> backup;
	String current;

	/**
	 * Constructor for PathHistory
	 */
	public PathHistory() {
		initializeStackHistory();
	}

	private void initializeStackHistory() {
		history = new Stack<String>();
		backup = new Stack<String>();
	}

	/**
	 * add newPath to history Stack
	 * 
	 * @param newPath
	 */
	public void addHistory(String newPath) {
		if (isCurrentPathExist()) {
			history.push(current);
		}
		current = newPath;
		backup.clear();
	}

	private boolean isCurrentPathExist() {
		return current != null;
	}

	/**
	 * undo operation for path
	 * 
	 * @return String previous path if undo is available, or null when there is
	 *         nothing to undo
	 */
	public String undo() {
		if (history.isEmpty()) {
			return handleEmptyHistory();
		}
		setUpUndo();
		return history.pop();
	}

	private String handleEmptyHistory() {
		String returnPath = current;
		resetCurrent();
		return returnPath;
	}

	private void setUpUndo() {
		backup.push(current);
		current = history.peek();
	}

	/**
	 * redo operation for path
	 * 
	 * @return String previous path if undo is available, or null when there is
	 *         nothing to undo
	 */
	public String redo() {
		if (backup.isEmpty()) {
			return null;
		}
		setUpRedo();
		return backup.pop();
	}

	private void resetCurrent() {
		current = null;
	}

	private void setUpRedo() {
		history.push(current);
		current = backup.peek();
	}
}