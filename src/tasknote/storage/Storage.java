package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.TaskListIOException;

import java.io.IOException;
import java.util.ArrayList;

public class Storage{
	private FileManipulation fileManipulator;
	
	/**
	 * constructor to construct FileManipulator to manipulate items from/to file
	 */
	public Storage(){
		fileManipulator = new FileManipulation();
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
	 */
	public void saveTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException{
		fileManipulator.writeTasks(overrideTasks);
	}
	
	/**
	 * 
	 */
	public void cleanFile(){
		
	}
}