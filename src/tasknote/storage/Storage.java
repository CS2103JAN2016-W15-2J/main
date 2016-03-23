package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.TaskListIOException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Storage{
	private FileManipulation fileManipulator;
	private PathManipulation pathManipulator;
	private StorageMagicStringsAndNumbers magicValuesRetriever;
	
	private static final Logger log = Logger.getLogger( Storage.class.getName() );
	
	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage(){
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
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
		textFileName = textFileName.replace(magicValuesRetriever.getSlash(), magicValuesRetriever.getPathSlash());
		
		if(pathManipulator.canChangePath(textFileName)){
			fileManipulator.changeFileName(textFileName);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return true if successfully undo path
	 */
	public boolean undoPath(){
		String previousPath = pathManipulator.extractUndoPathString();
		return fileManipulator.changeFileName(previousPath);
	}
	
	/**
	 * 
	 * @return true if successfully redo path
	 */
	public boolean redoPath(){
		String nextPath = pathManipulator.extractRedoPathString();
		return fileManipulator.changeFileName(nextPath);
	}
	
	// private helper methods
	private String concatPathIfNeeded(String pathName, String previousTextFileName){
		if(pathName.endsWith(magicValuesRetriever.getTextFileEnding())){
			return pathName;
		}else{
			return magicValuesRetriever.produceFullPathName(pathName, previousTextFileName);
		}
	}
}