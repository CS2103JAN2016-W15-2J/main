package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.TaskListIOException;

import java.io.IOException;
import java.util.ArrayList;

public class Storage{
	private FileManipulation fileManipulator;
	private PathManipulation pathManipulator;
	
	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage(){
		fileManipulator = new FileManipulation();
		pathManipulator = new PathManipulation();
	}
	
	/**
	 * read all the tasks from file and return to logic
	 * 
	 * @return ArrayList/<TaskObject/>
	 * @throws IOException
	 * @throws TaskListIOException
	 */
	public ArrayList<TaskObject> loadTasks() throws IOException, TaskListIOException{
		return fileManipulator.getTasks();
	}
	
	/**
	 * write all the tasks from logic into file
	 * 
	 * @param overrideTasks
	 * @throws TaskListIOException
	 * @throws IOException 
	 */
	public void saveTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException, IOException{
		cleanFile();
		fileManipulator.writeTasks(overrideTasks);
	}
	
	/**
	 * 
	 */
	public void cleanFile() throws IOException{
		fileManipulator.cleanFile();
	}
<<<<<<< HEAD
	
	public boolean changePath(String newPathName){
		String textFileName = concatPathIfNeeded(newPathName, fileManipulator.getTextFileName());
		textFileName = textFileName.replace(magicValuesRetriever.getSlash(), magicValuesRetriever.getPathSlash());
		
		if(pathManipulator.canChangePath(textFileName)){
			fileManipulator.changeFileName(textFileName);
			return true;
		}
		
		return false;
	}
	
	public boolean undoPath(){
		String previousPath = pathManipulator.extractUndoPathString();
		return fileManipulator.changeFileName(previousPath);
	}
	
	public boolean redoPath(){
		String nextPath = pathManipulator.extractRedoPathString();
		return fileManipulator.changeFileName(nextPath);
	}
	
	private String concatPathIfNeeded(String pathName, String previousTextFileName){
		if(pathName.contains(magicValuesRetriever.getTextFileEnding())){
			return pathName;
		}else{
			return magicValuesRetriever.produceFullPathName(pathName, previousTextFileName);
		}
	}
=======
>>>>>>> parent of 411c9bc... DONE: PATH manipulation
}