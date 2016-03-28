package tasknote.logic.Commands;

import tasknote.logic.TaskNote;
import tasknote.logic.ShowCategory;
import tasknote.shared.Constants;

public class ChangeCategoryCommand implements Command {

	private TaskNote taskNote;
	private ShowCategory category;
	private String statusOfOperation;

	public ChangeCategoryCommand(TaskNote taskNote, ShowCategory category) {
		this.taskNote = taskNote;
		this.category = category;
		statusOfOperation = new String();
	}
	
	public void execute() {
		statusOfOperation = taskNote.displayCategory(category);
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
