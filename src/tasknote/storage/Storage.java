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
//@@author A0126172M
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
	 * @throws IOException // there is something wrong with reading from textfile
	 * @throws TaskListIOException //there is something wrong with contents in the file
	 */
	public ArrayList<TaskObject> loadTasks() throws IOException, TaskListIOException{
		ArrayList<TaskObject> tasks = fileManipulator.getTasks();
		saveTasks(tasks);
		return tasks;
	}
	
	/**
	 * write all the tasks from logic into file
	 * 
	 * @param overrideTasks
	 * @throws TaskListIOException // there is something wrong with the contents in the overrideTasks
	 * @throws IOException // there is something wrong with writing into textfile
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
		String currentFullPath = fileManipulator.readFullPathFromPathFile();
		if(hasHandledPathChangeForMacAndWindows(textFileName, currentFullPath)){
			return true;
		}
		return logFailedPathEntered(textFileName);
	}
	
	/**
	 * undo PATH operation
	 * @return true if successfully undo PATH
	 */
	public boolean undoPath() throws InvalidPathException{
		try{
			String previousPath = pathManipulator.extractUndoPathString();		
			if(pathManipulator.isValidPath(previousPath)){
				return fileManipulator.moveFile(previousPath);
			}
			throw new InvalidPathException(previousPath,constants.getFailedValidPathUsed(previousPath));
		}catch(NullPointerException npe){
			return logUndoFailed();
		}
	}
	
	/**
	 * re-do PATH operation
	 * @return true if successfully re-do PATH
	 */
	public boolean redoPath() throws InvalidPathException{
		try{
			String nextPath = pathManipulator.extractRedoPathString();
			if(pathManipulator.isValidPath(nextPath)){
				return fileManipulator.moveFile(nextPath);
			}
			throw new InvalidPathException(nextPath,constants.getFailedValidPathUsed(nextPath));
		}catch(NullPointerException npe){
			return logRedoFailed();
		}
	}
	
	/**
	 * get alias command with command
	 * @param command
	 * @return String aliasCommand
	 */
	public String getAlias(String aliasCommand){
		return aliasManipulator.getAlias(aliasCommand);
	}
	
	/**
	 * get full alias HashMap
	 * @return HashMap<String, String> alias
	 */
	public HashMap<String, String> getAlias(){
		return aliasManipulator.getAlias();
	}
	
	/**
	 * remove an alias command from the alias HashMap
	 * @param command
	 * @return HashMap<String, String> alias
	 */
	public HashMap<String,String> removeAlias(String command){
		HashMap<String,String> alias = aliasManipulator.removeAlias(command);
		saveModifiedAlias(alias);
		return alias;
	}
	
	/**
	 * write alias into aliasFile
	 * @param overrideAlias
	 * @throws IOException //there is something wrong writing into aliasFile
	 */
	public void saveAlias(HashMap<String,String> overrideAlias) throws IOException{
		fileManipulator.writeAlias(overrideAlias);
	}
	
	/**
	 * add alias command into command in HashMap
	 * @param command
	 * @param aliasCommand
	 * @return current alias HashMap
	 */
	public boolean addAlias(String command, String alias) throws AddDuplicateAliasException{
		aliasManipulator.addAlias(command, alias);
		saveCurrentAlias();
		return true;
	}
	
	/**
	 * undo alias operation
	 * @return true if successfully undo
	 */
	public boolean undoAlias(){
		boolean isUndoSuccessful = aliasManipulator.undo();
		if(isUndoSuccessful){
			saveCurrentAlias();
		}
		return isUndoSuccessful;
	}
	
	/**
	 * redo alias operation
	 * @return true if successfully redo
	 */
	public boolean redoAlias(){
		boolean isRedoSuccessful = aliasManipulator.redo();
		if(isRedoSuccessful){
			saveCurrentAlias();
		}
		return isRedoSuccessful;
	}
	
	/*
	 *  private helper methods
	 */
	
	private void initializeFamilyClasses() {
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
		aliasManipulator = new AliasManipulation();
		constants = new StorageConstants();
	}
	
	private String concatPathIfNeeded(String enteredPath, String previousTextFileName){
		String currentFullPath = getCurrentFullPath();
		String newFullDirectoryPath = pathManipulator.extractFullPath(enteredPath, currentFullPath);
		if(isFileNameEntered(enteredPath)){
			return produceFullPathWtihNewFileName(previousTextFileName, newFullDirectoryPath);
		}else{
			return produceFullPathWithOldFileName(previousTextFileName,newFullDirectoryPath);
		}
	}

	private String produceFullPathWtihNewFileName(String previousTextFileName, String newFullDirectoryPath) {
		if(!isPathSlashEnteredAtTheEnd(newFullDirectoryPath)){
			newFullDirectoryPath = addSlashToFullPath(newFullDirectoryPath);
		}
		return produceFullPathWithNewFileName(newFullDirectoryPath, previousTextFileName);
	}

	private String produceFullPathWithOldFileName(String previousTextFileName, String newFullDirectoryPath) {
		if(!isPathSlashEnteredAtTheEnd(newFullDirectoryPath)){
			newFullDirectoryPath = addSlashToFullPath(newFullDirectoryPath);
		}
		return addFileNameAndProduceFullPath(newFullDirectoryPath, previousTextFileName);
	}

	private String getCurrentFullPath() {
		String currentFullPath = fileManipulator.readFullPathFromPathFile();
		assert(!isNullPath(currentFullPath));
		return currentFullPath;
	}

	private boolean isNullPath(String string) {
		return string == null;
	}
	
	private boolean isFileNameEntered(String pathName) {
		return pathName.endsWith(constants.getTextFileEnding());
	}
	
	private boolean isPathSlashEnteredAtTheEnd(String pathName) {
		return isSlashForMacUsers(pathName) || isSlashForWindowsUsers(pathName);
	}

	private boolean isSlashForWindowsUsers(String pathName) {
		return pathName.endsWith(constants.getPathSlash());
	}

	private boolean isSlashForMacUsers(String pathName) {
		return pathName.endsWith(constants.getSlash());
	}

	private String extractNewCurrentFullPath(String previousTextFileName, String fileName) {
		String currentPath = extractCurrentPath(previousTextFileName);
		String newCurrentFullPath = constants.addFileNameToPath(currentPath, fileName);
		return newCurrentFullPath;
	}

	private String extractCurrentPath(String previousTextFileName) {
		String currentFullPath = fileManipulator.readFullPathFromPathFile();
		return extractCurrentPath(currentFullPath,previousTextFileName);
	}
	
	private String extractCurrentPath(String currentFullPath, String previousTextFileName) {
		return currentFullPath.replace(previousTextFileName, constants.getEmptyString());
	}
	
	private String addFileNameAndProduceFullPath(String pathName, String previousTextFileName) {
		return constants.addFileNameToPath(pathName, previousTextFileName);
	}
	
	private String produceFullPathWithNewFileName(String pathName, String previousTextFileName) {
		String fileName = getFileName(pathName, previousTextFileName);
		if(fileName.equals(pathName)){
			String newFullPath = extractNewCurrentFullPath(previousTextFileName, fileName);
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
	
	private String addSlashToFullPath(String pathName) {
		return pathName.concat(constants.getSlash());
	}
	
	private boolean hasHandledPathChangeForMacAndWindows(String textFileName, String currentFullPath) {
		
		if(isPathForMac(textFileName, currentFullPath)){
			return fileManipulator.moveFile(textFileName);
		}
		
		String textFileNameForWindows = convertFilePathForWindows(textFileName);
		
		if(isPathForWindows(textFileNameForWindows, currentFullPath)){
			return fileManipulator.moveFile(textFileNameForWindows);
		}
		
		return false;
	}
	
	private boolean isPathForMac(String textFileName, String currentFullPath) {
		return pathManipulator.isValidPath(textFileName, currentFullPath);
	}
	
	private String convertFilePathForWindows(String textFileName) {
		return textFileName.replace(constants.getSlash(), constants.getPathSlash());
	}

	private boolean isPathForWindows(String textFileName, String currentFullPath) {
		return pathManipulator.isValidPath(textFileName, currentFullPath);
	}
	
	// alias related helper methods
	
	private void readAndSetAlias() {
		HashMap<String, String> alias = readAlias();
		aliasManipulator.setAlias(alias);
	}
	
	private HashMap<String, String> readAlias(){
		try {
			return fileManipulator.readAliasFromAliasFile();
		} catch (FileNotFoundException e) {
			storageLog.log(Level.WARNING, constants.getFailedToFindAliasFile());
		}
		return new HashMap<String, String>();
	}
	
	private void saveCurrentAlias() {
		HashMap<String,String> alias = aliasManipulator.getAlias();
		saveModifiedAlias(alias);
	}
	
	private void saveModifiedAlias(HashMap<String, String> alias) {
		try{
			saveAlias(alias);
		}catch(IOException ioe){
			logSaveModifiedAliasFailed();
		}
	}
	
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
}