package tasknote.storage;

import java.util.HashMap;

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
	
	public HashMap<String, String> getAlias(){
		return alias;
	}
	
	public String getAlias(String command){
		return alias.get(command);
	}
	
	public void setAlias(HashMap<String, String> alias){
		this.alias = alias;
	}
	
	public HashMap<String,String> addAlias(String command, String aliasCommand){
		if(alias.containsKey(command)){
			alias.replace(command, aliasCommand);
		}else{
			alias.put(command, aliasCommand);
		}
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	public HashMap<String,String> removeAlias(String command){
		alias.remove(command);
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	public boolean undo(){
		if(aliasHistory.isUndoValid()){
			alias = aliasHistory.undo();
			return true;
		}
		return false;
	}
	
	public boolean redo(){
		if(aliasHistory.isRedoValid()){
			alias = aliasHistory.redo();
			return true;
		}
		return false;
	}
}