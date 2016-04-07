package tasknote.logic.History;

import java.util.Stack;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

public class CommandHistory {
	
	private static Stack<CommandObject> undoStack;
	private static Stack<CommandObject> redoStack;
	
	/*
	 * These are the COMMAND TYPES for the possible User Commands that 
	 * Undo function can be used on
	 */
	private static final COMMAND_TYPE undoAddCommand = COMMAND_TYPE.DELETE;
	private static final COMMAND_TYPE undoDeleteCommand = COMMAND_TYPE.ADD;
	private static final COMMAND_TYPE undoUpdateCommand = COMMAND_TYPE.UPDATE;
	private static final COMMAND_TYPE undoTaskCompletionCommand = COMMAND_TYPE.DONE;
	private static final COMMAND_TYPE undoChangeFilePathCommand = COMMAND_TYPE.CHANGE_FILE_PATH;
	
	private static final COMMAND_TYPE redoAddCommand = COMMAND_TYPE.ADD;
	private static final COMMAND_TYPE redoDeleteCommand = COMMAND_TYPE.DELETE;
	private static final COMMAND_TYPE redoUpdateCommand = COMMAND_TYPE.UPDATE;
	private static final COMMAND_TYPE redoTaskCompletionCommand = COMMAND_TYPE.DONE;
	private static final COMMAND_TYPE redoChangeFilePathCommand = COMMAND_TYPE.CHANGE_FILE_PATH;
	
	/*
	 * This is the Integer Constant for the number of associated preceding 
	 * tasks for an undo object
	 */
	private static final int numPrecedingObjects = 2;
	
	
	public CommandHistory() {
		undoStack = new Stack<CommandObject>();
		redoStack = new Stack<CommandObject>();
	}
	
	/**
	 * This operation adds the inverse of the User's 
	 * Add Command into the Undo Stack
	 *
	 * @param: taskObject
	 */
	public void pushAddToUndo(TaskObject taskObject) {
		undoStack.push(new CommandObject(undoAddCommand, taskObject));
	}
	
	/**
	 * This operation adds the inverse of the Undo
	 * Delete Command into the Redo Stack as an ADD
	 * COMMAND TYPE
	 *
	 * @param: taskObject
	 */
	public void pushAddToRedo(TaskObject taskObject) {
		redoStack.push(new CommandObject(redoAddCommand, taskObject));
	}
	
	/**
	 * This operation adds the inverse of the User's 
	 * Delete Command into the Undo Stack
	 * 
	 * @param: taskObject
	 */
	public void pushDeleteToUndo(TaskObject taskObject) {
		undoStack.push(new CommandObject(undoDeleteCommand, taskObject));
	}
	
	/**
	 * This operation adds the inverse of the Undo
	 * Add Command into the Redo Stack as an Delete
	 * COMMAND TYPE
	 *
	 * @param: taskObject
	 */
	public void pushDeleteToRedo(TaskObject taskObject) {
		redoStack.push(new CommandObject(redoDeleteCommand, taskObject));
	}
	
	/**
	 * This operation adds the old and new task objects to 
	 * the Undo Stack as well as a null object, whose 
	 * numPrecedingUndoObjects will be set to 2
	 *
	 * @param: oldTaskObject, newTaskObject
	 */
	public void pushUpdateToUndo(TaskObject oldTaskObject, TaskObject newTaskObject) {
		pushAddToUndo(newTaskObject);
		pushDeleteToUndo(oldTaskObject);
		CommandObject undoObject = new CommandObject(undoUpdateCommand, null);
		undoObject.setPrecedingObjects(numPrecedingObjects);
		undoStack.push(undoObject);
	}
	
	/**
	 * This operation adds the old and new task objects to 
	 * the Redo Stack as well as a null object, whose 
	 * numPrecedingUndoObjects will be set to 2
	 * 
	 * Old Object is still the object that was created first
	 * New Object is the object that was created after the Old Object
	 *
	 * @param: oldTaskObject, newTaskObject
	 */
	public void pushUpdateToRedo(TaskObject oldTaskObject, TaskObject newTaskObject) {
		pushAddToRedo(newTaskObject);
		pushDeleteToRedo(oldTaskObject);
		CommandObject redoObject = new CommandObject(redoUpdateCommand, null);
		redoObject.setPrecedingObjects(numPrecedingObjects);
		redoStack.push(redoObject);
	}
	
	/**
	 * This operation adds the inverse of the User's 
	 * Complete Command into the Undo Stack
	 *
	 * @param: taskObject
	 */
	public void pushTaskCompletionToUndo(TaskObject taskObject) {
		undoStack.push(new CommandObject(undoTaskCompletionCommand, taskObject));
	}
	
	/**
	 * This operation adds the inverse of the Undo
	 * Complete Command into the Redo Stack 
	 *
	 * @param: taskObject
	 */
	public void pushTaskCompletionToRedo(TaskObject taskObject) {
		redoStack.push(new CommandObject(redoTaskCompletionCommand, taskObject));
	}
	
	/**
	 * This operation adds the User's Change File
	 * Path Command into the Undo Stack
	 *
	 */
	public void pushChangeFilePathToUndo() {
		undoStack.push(new CommandObject(undoChangeFilePathCommand, null));
	}
	
	/**
	 * This operation adds the User's Change File
	 * Path Command into the Redo Stack
	 *
	 */
	public void pushChangeFilePathToRedo() {
		redoStack.push(new CommandObject(redoChangeFilePathCommand, null));
	}
	
	public CommandObject peekUndoStack() {
		return undoStack.peek();
	}
	
	public CommandObject peekRedoStack() {
		return redoStack.peek();
	}
	
	public CommandObject popUndoStack() {
		return undoStack.pop();
	}
	
	public CommandObject popRedoStack() {
		return redoStack.pop();
	}
	
	public boolean isUndoStackEmpty() {
		return undoStack.isEmpty();
	}
	
	public boolean isRedoStackEmpty() {
		return redoStack.isEmpty();
	}

}
