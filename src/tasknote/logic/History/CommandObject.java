/** @@author A0108371L */
package tasknote.logic.History;

import tasknote.shared.CommandType;
import tasknote.shared.TaskObject;

public class CommandObject {
	
	private CommandType commandType;
	private TaskObject taskObject;
	private int precedingObjects;
	
	/******************* CommandObject Constructor *********************/
	public CommandObject(CommandType commandType, TaskObject taskObject) {
		this.commandType = commandType;
		this.taskObject = taskObject;
		this.precedingObjects = 0;
	}
	
	/******************* Accessors *********************/
	public CommandType getRevertCommandType() {
		return this.commandType;
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
