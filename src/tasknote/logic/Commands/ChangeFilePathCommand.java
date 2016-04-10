/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;

public class ChangeFilePathCommand implements Command {

	private TaskNote taskNote;
	private String filePath;
	private String statusOfOperation;

	public ChangeFilePathCommand(TaskNote taskNote, String filePath) {
		this.filePath = filePath;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.changeFilePath(filePath);
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
