package tasknote.storage;

import tasknote.shared.TaskObject;

import java.io.File;
import java.util.ArrayList;

public class StorageConversion{
	private StorageMagicStringsAndNumbers magicValuesRetriever;
	
	public StorageConversion(){
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
	}
	
	public ArrayList<TaskObject> convertStringToList(String tasks){
		return null;
	}
	
	public String convertListToString(ArrayList<TaskObject> overrideTasks){
		return null;
	}
}