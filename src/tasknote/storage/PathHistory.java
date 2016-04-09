package tasknote.storage;

import java.util.Stack;

//@@author A0126172M

/**
 * This class deals with the history of operations of PATH using Stacks
 */

public class PathHistory{
	Stack<String> history;
	Stack<String> backup;
	String current;
	
	/**
	 * Constructor for PathHistory
	 */
	public PathHistory(){
		initializeStackHistory();
	}
	
	private void initializeStackHistory() {
		history = new Stack<String>();
		backup = new Stack<String>();
	}
	
	/**
	 * add newPath to history Stack
	 * @param newPath
	 */
	public void addHistory(String newPath){
		if(current!=null){
			history.push(current);
		}
		current = newPath;
		backup.clear();
	}
	
	/**
	 * undo operation for path
	 * @return String previous path if undo is available,
	 * 		   or null when there is nothing to undo
	 */
	public String undo(){
		if(history.isEmpty()){
			return null;
		}
		backup.push(current);
		current = history.peek();
		return history.pop();
	}
	
	/**
	 * redo operation for path
	 * @return String previous path if undo is available,
	 * 		   or null when there is nothing to undo
	 */
	public String redo(){
		if(backup.isEmpty()){
			return null;
		}
		history.push(current);
		current = backup.peek();
		return backup.pop();
	}
}