package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.Constants;
import tasknote.shared.TaskListIOException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage{
	private FileManipulation fileManipulator;
	private PathManipulation pathManipulator;
	private AliasManipulation aliasManipulator;
	private StorageConstants constants;
	
	private static final Logger storageLog = Logger.getLogger( Storage.class.getName() );
	
	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage(){
		initializeFamilyClasses();
		readAndSetAlias();
	}
	
	/**
	 * read all the tasks from file and return to logic
	 * 
	 * @return ArrayList/<TaskObject/>
	 * @throws IOException // there is something wrong with reading/writing from textfile
	 * @throws TaskListIOException //there is something wrong with contents in the file
	 */
	public ArrayList<TaskObject> loadTasks() throws IOException, TaskListIOException{
		return fileManipulator.getTasks();
	}
	
	/**
	 * write all the tasks from logic into file
	 * 
	 * @param overrideTasks
	 * @throws TaskListIOException // there is something wrong with the contents in the overrideTasks
	 * @throws IOException // there is something wrong with reading/writing from textfile
	 */
	public void saveTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException, IOException{
		cleanFile();
		fileManipulator.writeTasks(overrideTasks);
	}
	
	/**
	 * clear all the contents in the textfile
	 */
	public void cleanFile() throws IOException{
		fileManipulator.cleanFile();
	}
	
	/**
	 * use user entered PATH to change to a new location
	 * @param newPathName
	 * @return true if path successfully changed
	 */
	public boolean changePath(String newPathName){
		String textFileName = concatPathIfNeeded(newPathName, fileManipulator.getTextFileName());
		if(isPathForMac(textFileName)){
			return fileManipulator.changeFileName(textFileName);	
		}
		
		String textFileNameForWindows = convertFilePathForWindows(textFileName);
		
		if(isPathForWindows(textFileNameForWindows)){
			return fileManipulator.changeFileName(textFileNameForWindows);
		}
			
		return logFailedPathEntered(textFileName);
	}
	
	/**
	 * 
	 * @return true if successfully undo path
	 */
	public boolean undoPath(){
		try{
			String previousPath = pathManipulator.extractUndoPathString();
			return fileManipulator.changeFileName(previousPath);
		}catch(NullPointerException npe){
			return logUndoFailed();
		}
	}
	
	/**
	 * 
	 * @return true if successfully redo path
	 */
	public boolean redoPath(){
		try{
			String nextPath = pathManipulator.extractRedoPathString();
			return fileManipulator.changeFileName(nextPath);
		}catch(NullPointerException npe){
			return logRedoFailed();
		}
	}
	
	// private helper methods
	private void initializeFamilyClasses() {
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
		aliasManipulator = new AliasManipulation();
		constants = new StorageConstants();
	}
	
	private void readAndSetAlias() {
		HashMap<String, String> alias = readAlias();
		aliasManipulator.setAlias(alias);
	}
	
	public HashMap<String, String> addAlias(String command, String aliasCommand){
		aliasManipulator.addAlias(command, aliasCommand);
		return aliasManipulator.getAlias();
	}
	
	public HashMap<String, String> readAlias(){
		try {
			return fileManipulator.readAliasFromAliasFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashMap<String, String>();
	}
	
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
	
	private String convertFilePathForWindows(String textFileName) {
		return textFileName.replace(constants.getSlash(), constants.getPathSlash());
	}

	private boolean isPathForWindows(String textFileName) {
		return pathManipulator.canChangePath(textFileName);
	}

	private boolean isPathForMac(String textFileName) {
		return pathManipulator.canChangePath(textFileName);
	}
	
	private String concatPathIfNeeded(String pathName, String previousTextFileName){
		if(isFileNameEntered(pathName) && hasCurrentOrParentDirectoryGiven(pathName)){
			return produceFullPathWithDirectoryCommand(pathName, previousTextFileName);
		}else if(isPathSlashEnteredAtTheEnd(pathName)){
			return addFileNameAndProduceFullPath(pathName, previousTextFileName);
		}else if(isFileNameEntered(pathName)){
			return produceFullPathWithNewFileName(pathName, previousTextFileName);
		}else{
			return addSlashAndProduceFullPath(pathName, previousTextFileName);
		}
	}

	private String produceFullPathWithNewFileName(String pathName, String previousTextFileName) {
		String fileName = getFileName(pathName, previousTextFileName);
		if(fileName.equals(pathName)){
			String currentFullPath = fileManipulator.readFullPathFromPathFile();
			String currentPath = extractCurrentPath(currentFullPath,previousTextFileName);
			String newFullPath = constants.addFileNameToPath(currentPath, fileName);
			return newFullPath;
		}
		return pathName;
	}

	private String getFileName(String pathName, String previousTextFileName) {
		String fileName = fileManipulator.extractTextFileName(pathName);
		if(isEmptyFileName(fileName)){
			fileName = previousTextFileName;
		}
		return fileName;
	}

	private boolean isEmptyFileName(String fileName) {
		return fileName == null;
	}
	
	private String produceFullPathWithDirectoryCommand(String pathName, String previousTextFileName) {
		String fileName = fileManipulator.extractTextFileName(pathName);
		String currentFullPath = fileManipulator.readFullPathFromPathFile();
		String currentPath = extractCurrentPath(currentFullPath,previousTextFileName);
		String newFullPath = constants.addFileNameToPath(currentPath, fileName);
		if(hasParentPath(pathName)){
			String parentPath = pathManipulator.getParentPath(newFullPath);
			return concatPathIfNeeded(parentPath,fileName);
		}
		return newFullPath;
	}

	private boolean hasCurrentOrParentDirectoryGiven(String pathName) {
		return hasParentPath(pathName) || hasCurrentPath(pathName);
	}

	private boolean hasCurrentPath(String pathName) {
		return pathName.startsWith(constants.getCurrentDirectory());
	}

	private boolean hasParentPath(String pathName) {
		return pathName.startsWith(constants.getParentDirectory());
	}

	private String extractCurrentPath(String currentFullPath, String previousTextFileName) {
		return currentFullPath.replace(previousTextFileName, constants.getEmptyString());
	}

	private String addFileNameAndProduceFullPath(String pathName, String previousTextFileName) {
		String newPath = constants.addFileNameToPath(pathName, previousTextFileName);
		return concatPathIfNeeded(newPath,previousTextFileName);
	}

	private String addSlashAndProduceFullPath(String pathName, String previousTextFileName) {
		pathName = addSlashToFullPath(pathName);
		return concatPathIfNeeded(pathName, previousTextFileName);
	}

	private String addSlashToFullPath(String pathName) {
		return pathName.concat(constants.getSlash());
	}

	private boolean isPathSlashEnteredAtTheEnd(String pathName) {
		return pathName.endsWith(constants.getSlash()) || pathName.endsWith(constants.getPathSlash());
	}

	private boolean isFileNameEntered(String pathName) {
		return pathName.endsWith(constants.getTextFileEnding());
	}
}