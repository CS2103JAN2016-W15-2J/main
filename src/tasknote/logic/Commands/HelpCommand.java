/** @@author A0108371L */
package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.shared.Constants;
import tasknote.shared.COMMAND_TYPE;

public class HelpCommand implements Command {
	
	private TaskNote taskNote;
	private COMMAND_TYPE commandType;
	private String statusOfOperation;

	public HelpCommand(TaskNote taskNote, COMMAND_TYPE commandType) {
		this.taskNote = taskNote;
		this.commandType = commandType;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.displayHelpMessage(commandType);
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
