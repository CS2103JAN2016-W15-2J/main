/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.CommandType;

public class HelpCommand implements Command {
	
	private TaskNote taskNote;
	private CommandType commandType;
	private String statusOfOperation;

	/******************* HelpCommand Constructor *********************/
	public HelpCommand(TaskNote taskNote, CommandType commandType) {
		this.taskNote = taskNote;
		this.commandType = commandType;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = TaskNote.displayHelpMessage(commandType);
	}

	public void refreshDisplay() {
		taskNote.refreshDisplay(taskNote.getDisplayList());
	}

	public String getFeedBack() {
		if(statusOfOperation.equals("")){
			statusOfOperation = Constants.ERROR_FEEDBACK;
		}
		return statusOfOperation;
	}
}
