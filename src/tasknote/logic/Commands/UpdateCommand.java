/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class UpdateCommand implements Command {
	
	private TaskNote taskNote;
	private int updateTaskId;
	private TaskObject updatedTaskObject;
	private String statusOfOperation;

	/******************* UpdateCommand Constructor *********************/
	public UpdateCommand(TaskNote taskNote, int updateTaskId, TaskObject updatedTaskObject) {
		this.updateTaskId = updateTaskId;
		this.updatedTaskObject = updatedTaskObject;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}

	public void execute() {
		statusOfOperation = taskNote.updateTask(updateTaskId, updatedTaskObject);
	}
	
	public void refreshDisplay() {
		taskNote.refreshDisplay(taskNote.getOutstandingTasksList());
	}

	public String getFeedBack() {
		if(statusOfOperation.equals("")){
			statusOfOperation = Constants.ERROR_FEEDBACK;
		}
		return statusOfOperation;
	}
}
