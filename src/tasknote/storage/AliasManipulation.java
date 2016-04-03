package tasknote.storage;

import java.util.HashMap;

/**
 * AliasManipulation stores the alias and interact with the AliasHistory
 * to allow undo/redo
 * 
 * @author User
 *
 */
public class AliasManipulation{
	private AliasHistory aliasHistory;
	
	private HashMap<String,String> alias;
	
	/**
	 * Constructor for AliasManipulation
	 */
	public AliasManipulation(){
		aliasHistory = new AliasHistory();
		alias = new HashMap<String,String>();
	}
	
	/**
	 * return current alias stored
	 * @return HashMap<String,String> alias
	 */
	public HashMap<String, String> getAlias(){
		return alias;
	}
	
	/**
	 * get corresponding alias command from command
	 * @param command
	 * @return String alias command
	 */
	public String getAlias(String command){
		return alias.get(command);
	}
	
	/**
	 * set current alias with newly created alias
	 * @param alias
	 */
	public void setAlias(HashMap<String, String> alias){
		this.alias = alias;
	}
	
	/**
	 * add a new alias command with the command and return the whole HashMap<String, String>
	 * @param command
	 * @param aliasCommand
	 * @return HashMap<String,String> modified alias
	 */
	public HashMap<String,String> addAlias(String command, String aliasCommand){
		addOrReplaceAlias(command, aliasCommand);
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	/**
	 * remove the command from the alias HashMap and return the current alias HashMap
	 * @param command
	 * @return HashMap<String, String> modified alias
	 */
	public HashMap<String,String> removeAlias(String command){
		alias.remove(command);
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	/**
	 * undo operation for alias
	 * @return true if undo operation is a success
	 */
	public boolean undo(){
		if(aliasHistory.isUndoValid()){
			alias = aliasHistory.undo();
			return true;
		}
		return false;
	}
	
	/**
	 * redo operation for alias
	 * @return true if redo operation is a success
	 */
	public boolean redo(){
		if(aliasHistory.isRedoValid()){
			alias = aliasHistory.redo();
			return true;
		}
		return false;
	}
	
	private void addOrReplaceAlias(String command, String aliasCommand) {
		if(alias.containsKey(command)){
			alias.replace(command, aliasCommand);
		}else{
			alias.put(command, aliasCommand);
		}
	}
}