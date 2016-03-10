package tasknote.logic.History;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class CommandObject {
	
	private COMMAND_TYPE undoCommandType;
	private TaskObject taskObject;
	private int precedingObjects;
	
	/******************* CommandObject Constructor *********************/
	public CommandObject(COMMAND_TYPE undoCommandType, TaskObject taskObject) {
		this.undoCommandType = undoCommandType;
		this.taskObject = taskObject;
		this.precedingObjects = 0;
	}
	
	/******************* Accessors *********************/
	public COMMAND_TYPE getUndoCommandType() {
		return this.undoCommandType;
	}
	
	public TaskObject getTaskObject() {
		return this.taskObject;
	}
	
	public int getPrecedingObjects() {
		return this.precedingObjects;
	}
	
	/******************* Mutators *********************/
	public void setPrecedingObjects(int numPrecedingObjects) {
		this.precedingObjects = numPrecedingObjects;
	}

}
