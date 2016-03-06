package tasknote.logic.History;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class CommandObject {
	
	private COMMAND_TYPE undoCommandType;
	private TaskObject taskObject;
	private int precedingTasks;
	
	/******************* CommandObject Constructor *********************/
	public CommandObject(COMMAND_TYPE undoCommandType, TaskObject taskObject) {
		this.undoCommandType = undoCommandType;
		this.taskObject = taskObject;
		this.precedingTasks = 0;
	}
	
	/******************* Accessors *********************/
	public COMMAND_TYPE getUndoCommandType() {
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
