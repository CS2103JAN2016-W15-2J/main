package tasknote.storage;

import java.io.File;
import java.io.IOException;

/**
 * This class manipulates the PATH of the storage file
 */

public class PathManipulation{
	private PathHistory pathHistory;
	
	public PathManipulation(){
		initializeFamilyClasses();
	}

	private void initializeFamilyClasses() {
		pathHistory = new PathHistory();
	}
	
	public boolean canChangePath(String newPathName){
		if(iSFilePathValid(newPathName)){
			pathHistory.addHistory(newPathName);
			return true;
		}
		return false;
	}
	
	private boolean iSFilePathValid(String pathName){
		File tempFile = new File(pathName);
		File directory = tempFile.getParentFile();
		return directory.exists();
	}
	
	public String extractUndoPathString(){
		return pathHistory.undo();
	}
	
	public String extractRedoPathString(){
		return pathHistory.redo();
	}
	
	public String getParentPath(String pathName){
		try{
			File tempFile = new File(pathName);
			File parentFile = tempFile.getParentFile().getParentFile();
			return parentFile.getCanonicalPath();
		}catch(IOException ioe){
			
		}
		return null;
	}
}