# A0126172Munused
###### \src\tasknote\storage\AliasHistory.java
``` java
 * Unused due to time constraints that alias was not implemented.
 */
package tasknote.storage;

import java.util.HashMap;
import java.util.Stack;

/**
 * This class deals with undo/redo of alias command
 */
public class AliasHistory {
	Stack<HashMap<String, String>> history;
	Stack<HashMap<String, String>> backup;
	HashMap<String, String> current;

	/**
	 * constructor to initialize Stack for undo/redo
	 */
	public AliasHistory() {
		initializeStackHistory();
	}

	private void initializeStackHistory() {
		history = new Stack<HashMap<String, String>>();
		backup = new Stack<HashMap<String, String>>();
	}

	/**
	 * add alias to history stack
	 * 
	 * @param alias
	 */
	public void addHistory(HashMap<String, String> alias) {
		if (isCurrentAliasExist()) {
			history.push(current);
		}
		current = alias;
		backup.clear();
	}

	private boolean isCurrentAliasExist() {
		return current != null;
	}

	/**
	 * undo operation for alias
	 * 
	 * @return HashMap<String,String> alias if undo is available, or null when
	 *         there is nothing to undo
	 */
	public HashMap<String, String> undo() {
		if (isUndoValid()) {
			return handleUndo();
		}
		return handleInvalidUndo();
	}

	private HashMap<String, String> handleUndo() {
		backup.push(current);
		current = history.peek();
		return history.pop();
	}

	private HashMap<String, String> handleInvalidUndo() {
		HashMap<String, String> returningAlias = current;
		resetCurrent();
		return returningAlias;
	}

	private void resetCurrent() {
		current = null;
	}

	/**
	 * redo operation for alias
	 * 
	 * @return HashMap<String,String> alias if redo is available, or null when
	 *         there is nothing to redo
	 */
	public HashMap<String, String> redo() {
		if (isRedoValid()) {
			return handleRedo();
		}
		return null;
	}

	private HashMap<String, String> handleRedo() {
		history.push(current);
		current = backup.peek();
		return backup.pop();
	}

	/**
	 * checks if undo operation is valid
	 * 
	 * @return true if undo is valid
	 */
	public boolean isUndoValid() {
		return !history.isEmpty();
	}

	/**
	 * checks if redo operation is valid
	 * 
	 * @return true if redo is valid
	 */
	public boolean isRedoValid() {
		return !backup.isEmpty();
	}
}
```
###### \src\tasknote\storage\AliasManipulation.java
``` java
 * Unused due to time constraints that alias was not implemented.
 */
package tasknote.storage;

import java.util.HashMap;

import tasknote.shared.AddDuplicateAliasException;

/**
 * AliasManipulation stores the alias and interact with the AliasHistory to
 * allow undo/redo
 */
public class AliasManipulation {
	private AliasHistory aliasHistory;

	private HashMap<String, String> alias;

	/**
	 * Constructor for AliasManipulation
	 */
	public AliasManipulation() {
		aliasHistory = new AliasHistory();
		alias = new HashMap<String, String>();
	}

	/**
	 * return current alias stored
	 * 
	 * @return HashMap<String,String> alias
	 */
	public HashMap<String, String> getAlias() {
		return alias;
	}

	/**
	 * get command from alias command
	 * 
	 * @param aliasCommand
	 * @return String command
	 */
	public String getAlias(String aliasCommand) {
		return alias.get(aliasCommand);
	}

	/**
	 * set current alias with newly created alias or undo/redo methods
	 * 
	 * @param alias
	 */
	public void setAlias(HashMap<String, String> alias) {
		this.alias = alias;
	}

	/**
	 * set current alias with your modified alias
	 * 
	 * @param alias
	 */
	public void setAliasAndPushToHistory(HashMap<String, String> alias) {
		this.alias = alias;
		aliasHistory.addHistory(alias);
	}

	/**
	 * reset the alias into an empty HashMap for cleanAliasFile
	 */
	public void resetAlias() {
		this.alias = new HashMap<String, String>();
		aliasHistory.addHistory(this.alias);
	}

	/**
	 * map aliasCommand as KEY and command as VALUE and return the whole
	 * HashMap<String, String>
	 * 
	 * @param command
	 * @param aliasCommand
	 * @return HashMap<String,String> modified alias
	 */
	public HashMap<String, String> addAlias(String command, String aliasCommand) throws AddDuplicateAliasException {
		insertAliasToHashMap(aliasCommand, command);
		aliasHistory.addHistory(this.alias);
		return this.alias;
	}

	/**
	 * remove the command from the alias HashMap and return the current alias
	 * HashMap
	 * 
	 * @param aliasCommand
	 * @return HashMap<String, String> modified alias
	 */
	public HashMap<String, String> removeAlias(String aliasCommand) {
		this.alias.remove(aliasCommand);
		aliasHistory.addHistory(this.alias);
		return this.alias;
	}

	/**
	 * undo operation for alias
	 * 
	 * @return true if undo operation is a success
	 */
	public boolean undo() {
		if (aliasHistory.isUndoValid()) {
			alias = aliasHistory.undo();
			return true;
		}
		return false;
	}

	/**
	 * redo operation for alias
	 * 
	 * @return true if redo operation is a success
	 */
	public boolean redo() {
		if (aliasHistory.isRedoValid()) {
			alias = aliasHistory.redo();
			return true;
		}
		return false;
	}

	private void insertAliasToHashMap(String aliasCommand, String command) throws AddDuplicateAliasException {
		if (alias.containsKey(aliasCommand)) {
			throw new AddDuplicateAliasException();
		} else {
			alias.put(aliasCommand, command);
		}
	}

}
```
###### \src\tasknote\storage\Storage.java
``` java
	 * Unused due to time constraints that alias was not implemented.
	 */
	/**
	 * get alias command with command
	 * 
	 * @param command
	 * @return String aliasCommand
	 */
	public String getAlias(String aliasCommand) {
		return aliasManipulator.getAlias(aliasCommand);
	}

	/**
	 * get full alias HashMap
	 * 
	 * @return HashMap<String, String> alias
	 */
	public HashMap<String, String> getAlias() {
		return aliasManipulator.getAlias();
	}

	/**
	 * remove an alias command from the alias HashMap
	 * 
	 * @param aliasCommand
	 * @return HashMap<String, String> alias
	 */
	public HashMap<String, String> removeAlias(String aliasCommand) {
		HashMap<String, String> alias = aliasManipulator.removeAlias(aliasCommand);
		saveModifiedAlias(alias);
		return alias;
	}

	/**
	 * write alias into aliasFile
	 * 
	 * @param overrideAlias
	 * @throws IOException
	 *             //there is something wrong writing into aliasFile
	 */
	public void saveAlias(HashMap<String, String> overrideAlias) throws IOException {
		fileManipulator.writeAlias(overrideAlias);
		aliasManipulator.setAliasAndPushToHistory(overrideAlias);
	}

	/**
	 * add alias command into command in HashMap
	 * 
	 * @param command
	 * @param aliasCommand
	 * @return current alias HashMap
	 */
	public boolean addAlias(String command, String alias) throws AddDuplicateAliasException {
		aliasManipulator.addAlias(command, alias);
		saveCurrentAlias();
		return true;
	}

	/**
	 * undo alias operation
	 * 
	 * @return true if successfully undo
	 */
	public boolean undoAlias() throws IOException {
		boolean isUndoSuccessful = aliasManipulator.undo();
		if (isUndoSuccessful) {
			setCurrentAlias();
		}
		return isUndoSuccessful;
	}

	/**
	 * redo alias operation
	 * 
	 * @return true if successfully redo
	 */
	public boolean redoAlias() throws IOException {
		boolean isRedoSuccessful = aliasManipulator.redo();
		if (isRedoSuccessful) {
			setCurrentAlias();
		}
		return isRedoSuccessful;
	}

	/**
	 * clear all the contents in the alias file and reset alias
	 */
	public void cleanAliasFile() throws IOException {
		fileManipulator.cleanAliasFile();
		aliasManipulator.resetAlias();
	}
```
###### \src\tasknote\storage\Storage.java
``` java
	 * Unused due to time constraints that alias was not implemented.
	 */
	// alias related helper methods

	private void readAndSetAlias() {
		HashMap<String, String> alias = readAlias();
		aliasManipulator.setAlias(alias);
	}

	private HashMap<String, String> readAlias() {
		try {
			return fileManipulator.readAliasFromAliasFile();
		} catch (FileNotFoundException e) {
			logFailedToFindAliasFile();
		}
		return new HashMap<String, String>();
	}

	private void setCurrentAlias() throws IOException {
		HashMap<String, String> alias = aliasManipulator.getAlias();
		fileManipulator.writeAlias(alias);
		aliasManipulator.setAlias(alias);
	}

	private void saveCurrentAlias() {
		HashMap<String, String> alias = aliasManipulator.getAlias();
		saveModifiedAlias(alias);
	}

	private void saveModifiedAlias(HashMap<String, String> alias) {
		try {
			saveAlias(alias);
		} catch (IOException ioe) {
			logSaveModifiedAliasFailed();
		}
	}
```
