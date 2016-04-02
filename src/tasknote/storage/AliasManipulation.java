package tasknote.storage;

import java.util.HashMap;

public class AliasManipulation{
	private AliasHistory aliasHistory;
	
	private HashMap<String,String> alias;
	
	public AliasManipulation(){
		aliasHistory = new AliasHistory();
		alias = new HashMap<String,String>();
	}
	
	public HashMap<String, String> getAlias(){
		return alias;
	}
	
	public void setAlias(HashMap<String, String> alias){
		this.alias = alias;
	}
	
	public HashMap<String,String> addAlias(String command, String aliasCommand){
		alias.put(command, aliasCommand);
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	public HashMap<String,String> removeAlias(String command){
		alias.remove(command);
		aliasHistory.addHistory(alias);
		return alias;
	}
	
	public HashMap<String,String> undo(){
		if(aliasHistory.isUndoValid()){
			alias = aliasHistory.undo();
			return alias;
		}
		return null;
	}
	
	public HashMap<String, String> redo(){
		if(aliasHistory.isRedoValid()){
			alias = aliasHistory.redo();
			return alias;
		}
		return null;
	}
}