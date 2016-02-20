package tasknote.storage;

import tasknote.shared.TaskObject;

import java.io.IOException;
import java.util.ArrayList;

public class Storage{
	private FileManipulation fileManipulator;
	
	public Storage(){
		fileManipulator = new FileManipulation();
	}
	
	public ArrayList<TaskObject> loadTasks(){
		return fileManipulator.getTasks();
	}
	
	public void writeTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException{
		fileManipulator.writeTasks(overrideTasks);
	}
}