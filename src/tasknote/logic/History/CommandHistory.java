package tasknote.logic.History;

import java.util.Stack;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class CommandHistory {
	
	/*
	 * These are the COMMAND TYPES for the possible User Commands that 
	 * Undo function can be used on
	 */
	private static final COMMAND_TYPE undoAddCommand = COMMAND_TYPE.DELETE;
	private static final COMMAND_TYPE undoDeleteCommand = COMMAND_TYPE.ADD;
	private static final COMMAND_TYPE undoUpdateCommand = COMMAND_TYPE.UPDATE;
	
	/*
	 * This is the Integer Constant for the number of associated preceding 
	 * tasks for an undo object
	 */
	private static final int numPrecedingObjects = 2;
	
	private static Stack<CommandObject> undoStack = new Stack<CommandObject>();
	
	/**
	 * This operation adds the inverse of the User's 
	 * Add Command into the Undo Stack
	 *
	 * @param: taskObject
	 */
	public void undoAdd(TaskObject taskObject) {
		undoStack.push(new CommandObject(undoAddCommand, taskObject));
	}
	
	/**
	 * This operation adds the inverse of the User's 
	 * Delete Command into the Undo Stack
	 * 
	 * @param: taskObject
	 */
	public void undoDelete(TaskObject taskObject) {
		undoStack.push(new CommandObject(undoDeleteCommand, taskObject));
	}
	
	/**
	 * This operation adds the old and new task objects to 
	 * the Undo Stack as well as a null object, whose 
	 * numPrecedingUndoObjects will be set to 2
	 *
	 * @param: oldTaskObject, newTaskObject
	 */
	public void undoUpdate(TaskObject oldTaskObject, TaskObject newTaskObject) {
		undoAdd(newTaskObject);
		undoDelete(oldTaskObject);
		CommandObject undoObject = new CommandObject(undoUpdateCommand, null);
		undoObject.setPrecedingTasks(numPrecedingObjects);
		undoStack.push(undoObject);
	}
	
	public boolean isUndoStackEmpty() {
		return undoStack.isEmpty();
	}

}
