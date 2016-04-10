/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.TaskObject;

public class AddCommand implements Command {

	private TaskNote taskNote;
	private TaskObject taskObject;
	private String statusOfOperation;

	/******************* AddCommand Constructor *********************/
	public AddCommand(TaskNote taskNote, TaskObject taskObject) {
		this.taskObject = taskObject;
		this.taskNote = taskNote;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.addTask(taskObject);
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
