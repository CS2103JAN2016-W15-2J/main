package tasknote.storage;

import java.io.File;

/**
 * This class manipulates the PATH of the storage file
 */

public class PathManipulation{
	private StorageMagicStringsAndNumbers magicValuesRetriever;
	private PathHistory pathHistory;
	
	public PathManipulation(){
		initializeFamilyClasses();
	}

	private void initializeFamilyClasses() {
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
		pathHistory = new PathHistory();
	}
	
	
	private boolean iSFilePathValid(String path){
		//todo
		return false;
	}
	
	public boolean changePath(String oldPathName, String newPathName){
		//todo
		return false;
	}
	
	public String undo(){
		return pathHistory.undo();
	}
	
	public String redo(){
		return pathHistory.redo();
	}
}