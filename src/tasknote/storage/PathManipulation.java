package tasknote.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
//@@author A0126172M
/**
 * This class aid the PATH manipulation operations and initiate pathHistory
 * to allow undo and redo of the PATH
 * 
 * @author User
 *
 */

public class PathManipulation{
	private PathHistory pathHistory;
	
	/**
	 * Constructor of PathManipulation
	 */
	public PathManipulation(){
		initializeFamilyClasses();
	}

	private void initializeFamilyClasses() {
		pathHistory = new PathHistory();
	}
	
	public boolean isValidFilePath(String pathName){
		File tempFile = new File(pathName);
		File directory = tempFile.getParentFile();
		return directory.exists();
	}
	
	public boolean isAbsolutePath(String pathName) throws InvalidPathException, NullPointerException{
		Path path = Paths.get(pathName);
		return path.isAbsolute();
	}
	
	public void pushHistory(String pathName){
		if(isValidFilePath(pathName)){
			pathHistory.addHistory(pathName);
		}
	}
	
	public String extractUndoPathString(){
		return pathHistory.undo();
	}
	
	public String extractRedoPathString(){
		return pathHistory.redo();
	}
	
	public String getParentPath(String pathName) throws IOException{
		File tempFile = new File(pathName);
		File parentFile = tempFile.getParentFile().getParentFile();
		return parentFile.getCanonicalPath();
	}

	public String extractNewFullPath(String newPath, String oldPath) {
		Path previousPath = Paths.get(oldPath);
		Path nextPath = Paths.get(newPath);
		Path newFullPath = previousPath.relativize(previousPath.resolve(nextPath));
		newFullPath = newFullPath.normalize();
		newFullPath = previousPath.resolve(newFullPath).normalize();
		return newFullPath.toString();
	}
}