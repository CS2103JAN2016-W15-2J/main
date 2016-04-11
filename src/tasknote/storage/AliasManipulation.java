/*
 * @@author A0126172M-unused
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