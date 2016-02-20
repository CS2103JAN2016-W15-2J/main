package tasknote.storage;

import tasknote.shared.*;

import java.io.File;
import java.util.ArrayList;

public class Storage{
	
	private final static String fileName = "taskContents.txt";
	
	/* classes relevant to create/open/read/write from the text file. */
	private static File textFile;
	
	public Storage(){
		createFile();
		if(isFileNotExist()){
			createNewFile();
		}else{
			
		}
	}
	
	private void createFile(){
		textFile = new File(fileName);
	}
	
	private boolean isFileNotExist(){
		return !textFile.exists();
	}
	
	private void createNewFile(){
		
	}
	
	public ArrayList<TaskObject> loadTasks(){
		return null;
	}
	
	public ArrayList<TaskObject> writeTasks(){
		return null;
	}
}