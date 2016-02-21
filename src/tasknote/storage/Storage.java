package tasknote.storage;

import tasknote.shared.TaskObject;
import tasknote.shared.TaskListIOException;

import java.io.IOException;
import java.util.ArrayList;

public class Storage{
	private FileManipulation fileManipulator;
	
	public Storage(){
		fileManipulator = new FileManipulation();
	}
	
	public ArrayList<TaskObject> loadTasks() throws IOException, TaskListIOException{
		return fileManipulator.getTasks();
	}
	
	public void saveTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException{
		fileManipulator.writeTasks(overrideTasks);
	}
}