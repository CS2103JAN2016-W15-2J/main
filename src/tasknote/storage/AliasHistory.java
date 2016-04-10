//@@author A0126172M
package tasknote.storage;

import java.util.HashMap;
import java.util.Stack;

/**
 * This class deals with undo/redo of alias command
 */
public class AliasHistory{
	Stack<HashMap<String,String>> history;
	Stack<HashMap<String,String>> backup;
	HashMap<String,String> current;
	
	/**
	 * constructor to initialize Stack for undo/redo
	 */
	public AliasHistory(){
		initializeStackHistory();
	}
	
	private void initializeStackHistory() {
		history = new Stack<HashMap<String,String>>();
		backup = new Stack<HashMap<String,String>>();
	}
	
	/**
	 * add alias to history stack
	 * @param alias
	 */
	public void addHistory(HashMap<String,String> alias){
		if(isCurrentAliasExist()){
			history.push(current);
		}
		current = alias;
		backup.clear();
	}

	private boolean isCurrentAliasExist() {
		return current!=null;
	}
	
	/**
	 * undo operation for alias
	 * @return HashMap<String,String> alias if undo is available,
	 * 		   or null when there is nothing to undo
	 */
	public HashMap<String,String> undo(){
		if(isUndoValid()){
			backup.push(current);
			current = history.peek();
			return history.pop();
		}
		return null;
	}
	
	/**
	 * redo operation for alias
	 * @return HashMap<String,String> alias if redo is available,
	 * 		   or null when there is nothing to redo
	 */
	public HashMap<String,String> redo(){
		if(isRedoValid()){
			history.push(current);
			current = backup.peek();
			return backup.pop();
		}
		return null;
	}
	
	/**
	 * check if undo operation is valid
	 * @return true if undo is valid
	 */
	public boolean isUndoValid(){
		return !history.isEmpty();
	}
	
	/**
	 * check if redo operation is valid
	 * @return true if redo is valid
	 */
	public boolean isRedoValid(){
		return !backup.isEmpty();
	}
}