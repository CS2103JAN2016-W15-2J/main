package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class CompleteCommand implements Command  {
	
	private TaskNote taskNote;
	private TaskObject taskObject;
	private String statusOfOperation;

	public CompleteCommand(TaskNote taskNote, TaskObject taskObject) {
		this.taskObject = taskObject;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.markTaskAsCompleted(taskObject);
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
