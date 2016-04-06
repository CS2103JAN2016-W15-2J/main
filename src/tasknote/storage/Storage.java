package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.AddDuplicateAliasException;
import tasknote.shared.TaskListIOException;

import java.io.FileNotFoundException;
import java.io.IOException;
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
		if(handlePathChangeForMacAndWindows(textFileName)){
			return true;
		}
		
		return logFailedPathEntered(textFileName);
	}
	
	/**
	 * undo PATH operation
	 * @return true if successfully undo PATH
	 */
	public boolean undoPath(){
		try{
			String previousPath = pathManipulator.extractUndoPathString();
			return fileManipulator.moveFile(previousPath);
		}catch(NullPointerException npe){
			return logUndoFailed();
		}
	}
	
	/**
	 * re-do PATH operation
	 * @return true if successfully re-do PATH
	 */
	public boolean redoPath(){
		try{
			String nextPath = pathManipulator.extractRedoPathString();
			return fileManipulator.moveFile(nextPath);
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
	
	private String concatPathIfNeeded(String pathName, String previousTextFileName){
		if(isFullPathWithUnixCommand(pathName)){
			return produceFullPathWithDirectoryCommand(pathName, previousTextFileName);
		}else if(isPathSlashEnteredAtTheEnd(pathName)){
			return addFileNameAndProduceFullPath(pathName, previousTextFileName);
		}else if(isFileNameEntered(pathName)){
			return produceFullPathWithNewFileName(pathName, previousTextFileName);
		}else{
			return addSlashAndProduceFullPath(pathName, previousTextFileName);
		}
	}

	private boolean isFullPathWithUnixCommand(String pathName) {
		return isFileNameEntered(pathName) && hasCurrentOrParentDirectoryGiven(pathName);
	}
	
	private boolean isFileNameEntered(String pathName) {
		return pathName.endsWith(constants.getTextFileEnding());
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
	
	private boolean isPathSlashEnteredAtTheEnd(String pathName) {
		return pathName.endsWith(constants.getSlash()) || pathName.endsWith(constants.getPathSlash());
	}
	
	private String produceFullPathWithDirectoryCommand(String pathName, String previousTextFileName) {
		String fileName = fileManipulator.extractTextFileName(pathName);
		String newCurrentFullPath = extractNewCurrentFullPath(previousTextFileName, fileName);
		
		if(hasParentPath(pathName)){
			return replaceParentDirectoryAndProduceFullPath(fileName, newCurrentFullPath);
		}
		return newCurrentFullPath;
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
	
	private String replaceParentDirectoryAndProduceFullPath(String fileName, String newFullPath) {
		String parentPath = pathManipulator.getParentPath(newFullPath);
		return concatPathIfNeeded(parentPath,fileName);
	}
	
	private String addFileNameAndProduceFullPath(String pathName, String previousTextFileName) {
		String newPath = constants.addFileNameToPath(pathName, previousTextFileName);
		return concatPathIfNeeded(newPath,previousTextFileName);
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
	
	private String addSlashAndProduceFullPath(String pathName, String previousTextFileName) {
		pathName = addSlashToFullPath(pathName);
		return concatPathIfNeeded(pathName, previousTextFileName);
	}
	
	private String addSlashToFullPath(String pathName) {
		return pathName.concat(constants.getSlash());
	}
	
	private boolean handlePathChangeForMacAndWindows(String textFileName) {
		
		if(isPathForMac(textFileName)){
			return fileManipulator.moveFile(textFileName);
		}
		
		String textFileNameForWindows = convertFilePathForWindows(textFileName);
		
		if(isPathForWindows(textFileNameForWindows)){
			return fileManipulator.moveFile(textFileNameForWindows);
		}
		
		return false;
	}
	
	private boolean isPathForMac(String textFileName) {
		return pathManipulator.canChangePath(textFileName);
	}
	
	private String convertFilePathForWindows(String textFileName) {
		return textFileName.replace(constants.getSlash(), constants.getPathSlash());
	}

	private boolean isPathForWindows(String textFileName) {
		return pathManipulator.canChangePath(textFileName);
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