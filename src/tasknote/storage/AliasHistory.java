package tasknote.storage;

import java.util.HashMap;
import java.util.Stack;

public class AliasHistory{
	Stack<HashMap<String,String>> history;
	Stack<HashMap<String,String>> backup;
	
	public AliasHistory(){
		initializeStackHistory();
	}

	private void initializeStackHistory() {
		history = new Stack<HashMap<String,String>>();
		backup = new Stack<HashMap<String,String>>();
	}
	
	public void addHistory(HashMap<String,String> newPath){
		history.push(newPath);
		backup.clear();
	}
	
	public HashMap<String,String> undo(){
		if(history.isEmpty()){
			return null;
		}
		backup.push(history.peek());
		return history.pop();
	}
	
	public HashMap<String,String> redo(){
		if(backup.isEmpty()){
			return null;
		}
		history.push(backup.peek());
		return backup.pop();
	}
	
	public boolean isUndoValid(){
		return !history.isEmpty();
	}
	
	public boolean isRedoValid(){
		return !backup.isEmpty();
	}
}