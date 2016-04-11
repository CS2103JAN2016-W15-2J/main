//@@author A0126172M
package tasknote.storage;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class aid the PATH manipulation operations and initiate pathHistory to
 * allow undo and redo of the PATH
 * 
 */

public class PathManipulation {
	private PathHistory pathHistory;

	/**
	 * Constructor of PathManipulation
	 */
	public PathManipulation() {
		initializeFamilyClasses();
	}

	private void initializeFamilyClasses() {
		pathHistory = new PathHistory();
	}

	/**
	 * This method checks if the path is valid
	 * 
	 * @param pathName
	 * @return true if it is a valid path
	 */
	public boolean isValidFilePath(String pathName) {
		File tempFile = new File(pathName);
		File directory = tempFile.getParentFile();
		return directory.exists();
	}

	/**
	 * This method checks if the path is an absolute path
	 * 
	 * @param pathName
	 * @return true if the path is an absolute path
	 * @throws InvalidPathException
	 *             implies the the user entered path is not in a path format
	 * @throws NullPointerException
	 *             implies the user entered path is a null string
	 */
	public boolean isAbsolutePath(String pathName) throws InvalidPathException, NullPointerException {
		Path path = Paths.get(pathName);
		return path.isAbsolute();
	}

	/**
	 * this method push the pathName into history
	 * 
	 * @param pathName
	 */
	public void pushHistory(String pathName) {
		if (isValidFilePath(pathName)) {
			pathHistory.addHistory(pathName);
		}
	}

	/**
	 * this method calls PathHistory to get the previous valid PATH
	 * 
	 * @return previous valid PATH change
	 */
	public String extractUndoPathString() {
		return pathHistory.undo();
	}

	/**
	 * this method calls PathHistroy to get the undo-ed previous valid PATH
	 * 
	 * @return undo-ed valid PATH change
	 */
	public String extractRedoPathString() {
		return pathHistory.redo();
	}

	/**
	 * this methods finds the absolute path of the new path with reference to
	 * the old path
	 * 
	 * @param newPath
	 * @param oldPath
	 * @return absolute path of the relative new path with reference to the old
	 *         path
	 */
	public String extractNewFullPath(String newPath, String oldPath) {
		assert(isAbsolutePath(oldPath));
		Path previousPath = Paths.get(oldPath);
		Path nextPath = Paths.get(newPath);
		Path newFullPath = extractFullLocalizedPath(previousPath, nextPath);
		return extractAbsoluteNewFullPath(previousPath, newFullPath);
	}

	private String extractAbsoluteNewFullPath(Path previousPath, Path newFullPath) {
		newFullPath = previousPath.resolve(newFullPath).normalize();
		return newFullPath.toString();
	}

	private Path extractFullLocalizedPath(Path previousPath, Path nextPath) {
		Path combinedPath = previousPath.resolve(nextPath);
		Path newFullPath = previousPath.relativize(combinedPath);
		return newFullPath.normalize();
	}
	
	public String normalizePath(String userPath){
		Path path = Paths.get(userPath);
		return normalizedPathString(path);
	}

	private String normalizedPathString(Path path) {
		path = path.normalize();
		return path.toString();
	}
}