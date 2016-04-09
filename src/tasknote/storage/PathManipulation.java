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
	
	/**
	 * This method takes the entered path and just check if it is valid without removing all the absolute path
	 * @param enteredPath
	 * @return true if the path is valid
	 */
	public boolean isValidPath(String enteredPath) throws NullPointerException, InvalidPathException{
		return iSFilePathValid(enteredPath);
	}
	
	private boolean iSFilePathValid(String enteredPath){
		File temp = new File(enteredPath);
		File directory = temp.getParentFile();
		return directory.exists();
	}
	
	/**
	 * Test if the path that may contains absolute path can be changed and add to history
	 * @param newPathName
	 * @param currentFullPath
	 * @return true if user is allowed to change path
	 * @throws NullPointerException (Entered NULL as newPath)
	 * @throws InvalidPathException (Invalid path format)
	 */
	public boolean isValidPath(String newPathName, String currentFullPath) throws NullPointerException, InvalidPathException{
		if(iSFilePathValid(newPathName, currentFullPath)){
			pathHistory.addHistory(newPathName);
			return true;
		}
		return false;
	}
	
	private boolean iSFilePathValid(String enteredPath, String currentFullPath) throws NullPointerException, InvalidPathException{
		String newFullPath = extractFullPath(enteredPath, currentFullPath);
		File directory = new File(newFullPath);
		return directory.exists();
	}
	
	/**
	 * This method extract the full path directory and normalize all the absolute path commands like "." and ".."
	 * @param enteredPath
	 * @param currentFullPath
	 * @return String of full path without fileName
	 * @throws NullPointerException
	 * @throws InvalidPathException
	 */
	public String extractFullPath(String enteredPath, String currentFullPath) throws NullPointerException, InvalidPathException{
		Path currentDirectory = extractCurrentDirectory(currentFullPath);
		Path newFullPath = currentDirectory.resolve(enteredPath);
		newFullPath = newFullPath.normalize();
		return newFullPath.toString();
	}

	private Path extractCurrentDirectory(String currentFullPath) {
		Path path = Paths.get(currentFullPath);
		return path.getParent();
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
}