package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.TaskListIOException;

import java.io.IOException;
import java.util.ArrayList;

public class Storage{
	private StorageMagicStringsAndNumbers magicValuesRetriever;
	private FileManipulation fileManipulator;
	private PathManipulation pathManipulator;
	
	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage(){
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
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
	 * @throws IOException
	 */
	public void cleanFile() throws IOException{
		fileManipulator.cleanFile();
	}

}