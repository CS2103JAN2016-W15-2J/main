/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class UndoneCommand implements Command  {
	
	private TaskNote taskNote;
	private TaskObject taskObject;
	private int taskId;
	private boolean isComplete;
	private String statusOfOperation;

	/******************* UndoneCommand Constructor *********************/
	public UndoneCommand(TaskNote taskNote, int taskId) {
		this.taskId = taskId;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		//statusOfOperation = taskNote.setTaskCompletionStatus(taskObject, isComplete);
		statusOfOperation = taskNote.markTaskAsIncomplete(taskId);
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