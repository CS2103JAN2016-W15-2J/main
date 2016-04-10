/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;

public class RedoCommand implements Command {
	
	private TaskNote taskNote;
	private String statusOfOperation;

	public RedoCommand(TaskNote taskNote) {
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.redoLastUndoCommand();
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
