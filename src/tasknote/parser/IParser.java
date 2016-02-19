package tasknote.parser;

import tasknote.shared.COMMAND_TYPE;
import tasknote.shared.TaskObject;

import java.util.ArrayList;

public interface IParser {
	
	public COMMAND_TYPE getCommand(String userCommand);
	
	public TaskObject getTaskObject(String userCommand);
	
	public ArrayList<Integer> deletedObjects(String userCommand);
	
	
	
	
}
