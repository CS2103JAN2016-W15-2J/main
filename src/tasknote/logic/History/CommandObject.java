package tasknote.logic.History;

import tasknote.shared.TaskObject;

public class CommandObject {
	
	private String undoCommandType;
	private TaskObject taskObject;
	private int precedingTasks;
	
	public CommandObject(String undoCommandType, TaskObject taskObject) {
		this.undoCommandType = undoCommandType;
		this.taskObject = taskObject;
		this.precedingTasks = 0;
	}
	
	public String getUndoCommandType() {
		return this.undoCommandType;
	}
	
	public TaskObject getTaskObject() {
		return this.taskObject;
	}
	
	public int getPrecedingTasks() {
		return this.precedingTasks;
	}
	
	public void setPrecedingTasks(int numPrecedingTasks) {
		this.precedingTasks = numPrecedingTasks;
	}

}
