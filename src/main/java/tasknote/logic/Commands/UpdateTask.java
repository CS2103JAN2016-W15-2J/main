package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class UpdateTask implements Command {
	
	private TaskNote taskNote;
	private int updateTaskId;
	private TaskObject updatedTaskObject;
	private String statusOfOperation;

	public UpdateTask(TaskNote taskNote, int updateTaskId, TaskObject updatedTaskObject) {
		this.updateTaskId = updateTaskId;
		this.updatedTaskObject = updatedTaskObject;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}

	public void execute() {
		statusOfOperation = taskNote.updateTask(updateTaskId, updatedTaskObject);
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
