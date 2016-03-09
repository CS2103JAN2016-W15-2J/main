package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;

import java.util.ArrayList;


public class DeleteCommand implements Command {
	
	private TaskNote taskNote;
	private ArrayList<Integer> deleteIds;
	private String statusOfOperation;

	public DeleteCommand(TaskNote taskNote, ArrayList<Integer> deleteIds) {
		this.deleteIds = deleteIds;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}

	public void execute() {
		statusOfOperation = taskNote.deleteTask(deleteIds);
	}

	public void refreshDisplay() {
		taskNote.refreshDisplay(taskNote.getTaskList());
	}

	public String getFeedBack() {
		if(statusOfOperation.equals("")){
			statusOfOperation = Constants.ERROR_FEEDBACK;
		}
		return statusOfOperation;
	}
}
