package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

import java.util.ArrayList;

public class SearchTask implements Command {
	
	private TaskNote taskNote;
	private ArrayList<Integer> searchIds;
	private String statusOfOperation;

	public SearchTask(TaskNote taskNote, ArrayList<Integer> searchIds) {
		this.searchIds = searchIds;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}

	public void execute() {
		statusOfOperation = taskNote.getSearchResults(searchIds);
	}
	
	public ArrayList<TaskObject> getSearchResults(){
		return taskNote.getSearchList();
	}

	public void refreshDisplay() {
		taskNote.refreshDisplay(getSearchResults());
	}

	public String getFeedBack() {
		if(statusOfOperation.equals("")){
			statusOfOperation = Constants.ERROR_FEEDBACK;
		}
		return statusOfOperation;
	}
}
