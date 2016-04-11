//@@author A0126172M
package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.AddDuplicateAliasException;
import tasknote.shared.TaskListIOException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
	private FileManipulation fileManipulator;
	private PathManipulation pathManipulator;
	private AliasManipulation aliasManipulator;
	private StorageConstants constants;

	private static final Logger storageLog = Logger.getLogger(Storage.class.getName());

	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage() {
		initializeFamilyClasses();
		readAndSetAlias();
	}

	/**
	 * read all the tasks from file and return to logic
	 * 
	 * @return ArrayList/<TaskObject/>
	 * @throws IOException
	 *             // there is something wrong with reading from textfile
	 * @throws TaskListIOException
	 *             //there is something wrong with contents in the file
	 */
	public ArrayList<TaskObject> loadTasks() throws IOException, TaskListIOException {
		ArrayList<TaskObject> tasks = fileManipulator.getTasks();
		saveTasks(tasks);
		return tasks;
	}

	/**
	 * write all the tasks from logic into file
	 * 
	 * @param overrideTasks
	 * @throws TaskListIOException
	 *             // there is something wrong with the contents in the
	 *             overrideTasks
	 * @throws IOException
	 *             // there is something wrong with writing into textfile
	 */
	public void saveTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException, IOException {
		cleanTextFile();
		fileManipulator.writeTasks(overrideTasks);
	}

	/**
	 * clear all the contents in the textfile
	 */
	public void cleanTextFile() throws IOException {
		fileManipulator.cleanTextFile();
	}

	/**
	 * use user entered PATH to change to a new location
	 * 
	 * @param newPathName
	 * @return true if path successfully changed
	 */
	public boolean changePath(String newPathName) throws IOException {
		String textFileName = getNormalizedFullPath(newPathName);
		if (handlePathChangeForMacAndWindows(textFileName)) {
			pathManipulator.pushHistory(textFileName);
			return true;
		}

		return logFailedPathEntered(textFileName);
	}

	private String getNormalizedFullPath(String newPathName) throws IOException {
		String textFileName = concatPathIfNeeded(newPathName, fileManipulator.getTextFileName());
		textFileName = pathManipulator.normalizePath(textFileName);
		return textFileName;
	}

	/**
	 * undo PATH operation
	 * 
	 * @return true if successfully undo PATH
	 * @throws IOException
	 */
	public boolean undoPath() throws InvalidPathException, IOException {
		try {
			String previousPath = pathManipulator.extractUndoPathString();
			if (pathManipulator.isValidFilePath(previousPath)) {
				return fileManipulator.moveFile(previousPath);
			}
			throw new InvalidPathException(previousPath, constants.getFailedValidPathUsed(previousPath));
		} catch (NullPointerException npe) {
			return logUndoFailed();
		}
	}

	/**
	 * re-do PATH operation
	 * 
	 * @return true if successfully re-do PATH
	 * @throws IOException
	 */
	public boolean redoPath() throws InvalidPathException, IOException {
		try {
			String nextPath = pathManipulator.extractRedoPathString();
			if (pathManipulator.isValidFilePath(nextPath)) {
				return fileManipulator.moveFile(nextPath);
			}
			throw new InvalidPathException(nextPath, constants.getFailedValidPathUsed(nextPath));
		} catch (NullPointerException npe) {
			return logRedoFailed();
		}
	}

	/*
	 * @@author A0126172M-unused
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
	//@@author A0126172M

	/*
	 * private helper methods
	 */

	private void initializeFamilyClasses() {
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
		aliasManipulator = new AliasManipulation();
		constants = new StorageConstants();
	}

	private String concatPathIfNeeded(String pathName, String previousTextFileName)
			throws InvalidPathException, NullPointerException, IOException {
		String textFileName = getFileName(pathName, previousTextFileName);
		if (!pathManipulator.isAbsolutePath(pathName)) {
			return produceFullPathWithDirectoryCommand(pathName, previousTextFileName, textFileName);
		} else if (isPathSlashEnteredAtTheEnd(pathName)) {
			return addFileNameAndProduceFullPath(pathName, textFileName);
		} else if (isFileNameEntered(pathName)) {
			return produceFullPathWithNewFileName(pathName, textFileName);
		} else {
			return addSlashAndProduceFullPath(pathName, textFileName);
		}
	}

	private boolean isFileNameEntered(String pathName) {
		return pathName.endsWith(constants.getTextFileEnding());
	}

	private boolean isPathSlashEnteredAtTheEnd(String pathName) {
		return pathName.endsWith(constants.getSlash()) || pathName.endsWith(constants.getPathSlash());
	}

	private String produceFullPathWithDirectoryCommand(String newPath, String previousTextFileName, String textFileName)
			throws IOException {
		newPath = removeFileNameFromNewPath(newPath);
		String currentPath = extractCurrentPath(previousTextFileName);
		String newFullPath = pathManipulator.extractNewFullPath(newPath, currentPath);
		return addSlashAndProduceFullPath(newFullPath, textFileName);
	}

	private String removeFileNameFromNewPath(String newPath) {
		if (isFileNameEntered(newPath)) {
			String newFileName = fileManipulator.extractTextFileName(newPath);
			return extractCurrentPath(newPath, newFileName);
		}
		return newPath;
	}

	private String extractNewCurrentFullPath(String previousTextFileName, String fileName) {
		String currentPath = extractCurrentPath(previousTextFileName);
		String newCurrentFullPath = constants.addFileNameToPath(currentPath, fileName);
		return newCurrentFullPath;
	}

	private String extractCurrentPath(String previousTextFileName) {
		String currentFullPath = fileManipulator.readFullPathFromPathFile();
		return extractCurrentPath(currentFullPath, previousTextFileName);
	}

	private String extractCurrentPath(String currentFullPath, String previousTextFileName) {
		return currentFullPath.replace(previousTextFileName, constants.getEmptyString());
	}

	private String addFileNameAndProduceFullPath(String pathName, String previousTextFileName) throws IOException {
		return constants.addFileNameToPath(pathName, previousTextFileName);
	}

	private String produceFullPathWithNewFileName(String pathName, String previousTextFileName) {
		String fileName = getFileName(pathName, previousTextFileName);
		pathName = getPathIfOnlyFileNameEntered(pathName, previousTextFileName, fileName);
		return pathName;
	}

	private String getPathIfOnlyFileNameEntered(String pathName, String previousTextFileName, String fileName) {
		if (fileName.equals(pathName)) {
			String newFullPath = extractNewCurrentFullPath(previousTextFileName, fileName);
			return newFullPath;
		}
		return pathName;
	}

	private String getFileName(String pathName, String previousTextFileName) {
		if (isFileNameEntered(pathName)) {
			return extractCorrectTextFileName(pathName, previousTextFileName);
		}
		return previousTextFileName;
	}

	private String extractCorrectTextFileName(String pathName, String previousTextFileName) {
		String fileName = fileManipulator.extractTextFileName(pathName);
		if (isEmptyFileName(fileName)) {
			return previousTextFileName;
		}
		return fileName;
	}

	private boolean isEmptyFileName(String fileName) {
		return fileName == null;
	}

	private String addSlashAndProduceFullPath(String pathName, String previousTextFileName) throws IOException {
		pathName = addSlashToFullPath(pathName);
		return concatPathIfNeeded(pathName, previousTextFileName);
	}

	private String addSlashToFullPath(String pathName) {
		return pathName.concat(constants.getSlash());
	}

	private boolean handlePathChangeForMacAndWindows(String textFileName) throws IOException {
		
		if (isValidFilePath(textFileName)) {
			return fileManipulator.moveFile(textFileName);
		}
		
		return false;
	}

	private boolean isValidFilePath(String textFileName) {
		return pathManipulator.isValidFilePath(textFileName);
	}
	
	/*
	 * @@author A0126172M-unused
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
	//@@author A0126172M
	// logging methods

	private boolean logFailedPathEntered(String textFileName) {
		storageLog.log(Level.WARNING, String.format(constants.getFailedPathChange(), textFileName));
		return false;
	}

	private boolean logUndoFailed() {
		storageLog.log(Level.FINE, constants.getFailedUndo());
		return false;
	}

	private boolean logRedoFailed() {
		storageLog.log(Level.FINE, constants.getFailedRedo());
		return false;
	}

	private void logSaveModifiedAliasFailed() {
		storageLog.log(Level.WARNING, constants.getFailedAliasSave());
	}
	
	private void logFailedToFindAliasFile() {
		storageLog.log(Level.WARNING, constants.getFailedToFindAliasFile());
	}
}