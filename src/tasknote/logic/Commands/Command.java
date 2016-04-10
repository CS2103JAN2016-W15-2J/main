/** @@author A0108371L */
package tasknote.logic.Commands;

public interface Command {
	public void execute();
	public void refreshDisplay();
	public String getFeedBack();
}
