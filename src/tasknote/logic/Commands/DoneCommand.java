/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class DoneCommand implements Command  {
	
	private TaskNote taskNote;
	private int taskId;
	private String statusOfOperation;

	/******************* DoneCommand Constructor *********************/
	public DoneCommand(TaskNote taskNote, int taskId) {
		this.taskId = taskId;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.markTaskAsComplete(taskId);
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
