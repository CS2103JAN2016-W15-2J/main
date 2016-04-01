package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class DoneCommand implements Command  {
	
	private TaskNote taskNote;
	private TaskObject taskObject;
	private boolean isComplete;
	private String statusOfOperation;

	public DoneCommand(TaskNote taskNote, TaskObject taskObject) {
		this.taskObject = taskObject;
		this.taskNote = taskNote;
		isComplete = true;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.setTaskCompletionStatus(taskObject, isComplete);
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
