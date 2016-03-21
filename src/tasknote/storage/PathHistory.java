package tasknote.storage;

import java.util.Stack;

public class PathHistory{
	Stack<String> history;
	Stack<String> backup;
	
	public PathHistory(){
		initializeStackHistory();
	}

	private void initializeStackHistory() {
		history = new Stack<String>();
		backup = new Stack<String>();
	}
	
	public void addHistory(String newPath){
		history.push(newPath);
		backup.clear();
	}
	
	public String undo(){
		if(history.isEmpty()){
			return null;
		}
		backup.push(history.peek());
		return history.pop();
	}
	
	public String redo(){
		if(backup.isEmpty()){
			return null;
		}
		history.push(backup.peek());
		return backup.pop();
	}
}