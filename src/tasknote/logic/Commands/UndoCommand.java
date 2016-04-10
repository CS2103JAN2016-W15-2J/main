/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;

public class UndoCommand implements Command {
	
	private TaskNote taskNote;
	private String statusOfOperation;

	public UndoCommand(TaskNote taskNote) {
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.undoLastCommand();
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
