/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.logic.ShowInterval;
import tasknote.shared.Constants;


public class ShowCommand implements Command {

	private TaskNote taskNote;
	private ShowInterval timeInterval;
	private int countInterval;
	private String statusOfOperation;

	/******************* ShowCommand Constructor *********************/
	public ShowCommand(TaskNote taskNote, ShowInterval timeInterval, int countInterval) {
		this.taskNote = taskNote;
		this.timeInterval = timeInterval;
		this.countInterval = countInterval;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.showTasks(timeInterval, countInterval);
	}

	public void refreshDisplay() {
		taskNote.refreshDisplay(taskNote.getShowIntervalList());
	}

	public String getFeedBack() {
		if(statusOfOperation.equals("")){
			statusOfOperation = Constants.ERROR_FEEDBACK;
		}
		return statusOfOperation;
	}
}
