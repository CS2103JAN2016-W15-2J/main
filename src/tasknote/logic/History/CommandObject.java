package tasknote.logic.History;

import tasknote.shared.TaskObject;

public class CommandObject {
	
	private String undoCommandType;
	private TaskObject taskObject;
	private int precedingTasks;
	
	/******************* CommandObject Constructor *********************/
	public CommandObject(String undoCommandType, TaskObject taskObject) {
		this.undoCommandType = undoCommandType;
		this.taskObject = taskObject;
		this.precedingTasks = 0;
	}
	
	/******************* Accessors *********************/
	public String getUndoCommandType() {
		return this.undoCommandType;
	}
	
	public TaskObject getTaskObject() {
		return this.taskObject;
	}
	
	public int getPrecedingTasks() {
		return this.precedingTasks;
	}
	
	/******************* Mutators *********************/
	public void setPrecedingTasks(int numPrecedingTasks) {
		this.precedingTasks = numPrecedingTasks;
	}

}
