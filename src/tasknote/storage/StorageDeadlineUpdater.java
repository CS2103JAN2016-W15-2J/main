//@@author A0126172M
package tasknote.storage;

import java.util.Calendar;
import java.util.GregorianCalendar;

import tasknote.shared.TaskObject;

/**
 * StorageDeadlineUpdater class checks if the deadline of the task is overdue when the user first opens
 * the application
 */
public class StorageDeadlineUpdater{
	
	private final GregorianCalendar clock = new GregorianCalendar();
	private StorageConstants constants;
	
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	private int currentHour;
	private int currentMinute;
	
	private TaskObject taskObject;
	
	/**
	 * constructor for StorageDeadlingUpdater
	 */
	public StorageDeadlineUpdater(){
		constants = new StorageConstants();
	}
	
	/**
	 * Set the TaskObject in the class
	 * @param taskObject
	 */
	public void setTaskObject(TaskObject taskObject){
		this.taskObject = taskObject;
	}
	
	/**
	 * Update the taskObject given if overdue
	 * @param taskObject
	 */
	public void updateTaskStatus(TaskObject taskObject){
		setTaskObject(taskObject);
		assert isValidTaskObject();
		updateTime();
		updateTaskObject();
	}

	private boolean isValidTaskObject() {
		return this.taskObject!=null;
	}
	
	private void updateTime() {
		updateCalendar();
		updateClock();
	}

	private void updateClock() {
		currentHour = clock.get(Calendar.HOUR_OF_DAY);
		currentMinute = clock.get(Calendar.MINUTE);
	}

	private void updateCalendar() {
		currentYear = clock.get(Calendar.YEAR);
		currentMonth = constants.getNormalizedMonth(clock.get(Calendar.MONTH));
		currentDay = clock.get(Calendar.DATE);
	}

	private void updateTaskObject() {
		boolean notOverdue = true;
		if(!taskObject.isCompleted() && !taskObject.isFloatingTask()){
			notOverdue = isOverdue(notOverdue);
		}
		
		setOverdue(notOverdue);
	}

	private void setOverdue(boolean notOverdue) {
		if(!notOverdue){
			taskObject.setTaskStatus(TaskObject.TaskStatus.TASK_OVERDUE);
		}
	}

	private boolean isOverdue(boolean notOverdue) {
		notOverdue = isOverdueYear(notOverdue);
		notOverdue = isOverdueMonth(notOverdue);
		notOverdue = isOverdueDay(notOverdue);
		notOverdue = isOverdueHour(notOverdue);
		notOverdue = isOverdueMinute(notOverdue);
		return notOverdue;
	}

	private boolean isOverdueMinute(boolean notOverdue) {
		if(isSameHour() && notOverdue){
			notOverdue = isOverdueMinute();
		}
		return notOverdue;
	}

	private boolean isOverdueHour(boolean notOverdue) {
		if(isSameDay() && notOverdue){
			notOverdue = isOverdueHour();
		}
		return notOverdue;
	}

	private boolean isOverdueDay(boolean notOverdue) {
		if(isSameMonth() && notOverdue){
			notOverdue = isOverdueDay();
		}
		return notOverdue;
	}

	private boolean isOverdueMonth(boolean notOverdue) {
		if(isSameYear() && notOverdue){
			notOverdue = isOverdueMonth();
		}
		return notOverdue;
	}

	private boolean isOverdueYear(boolean notOverdue) {
		if(notOverdue){
			notOverdue = isOverdueYear();
		}
		return notOverdue;
	}
	
	private boolean isSameYear() {
		return taskObject.getDateYear() == currentYear;
	}
	
	private boolean isSameMonth(){
		return taskObject.getDateMonth() == currentMonth;
	}
	
	private boolean isSameDay(){
		return taskObject.getDateDay() == currentDay;
	}
	
	private boolean isSameHour(){
		return taskObject.getDateHour() == currentHour;
	}
	
	private boolean isOverdueYear(){
		return taskObject.getDateYear() >= currentYear;
	}
	
	private boolean isOverdueMonth(){
		return taskObject.getDateMonth() >= currentMonth;
	}
	
	private boolean isOverdueDay(){
		return taskObject.getDateDay() >= currentDay;
	}
	
	private boolean isOverdueHour(){
		return taskObject.getDateHour() >= currentHour;
	}
	
	private boolean isOverdueMinute(){
		return taskObject.getDateMinute() >= currentMinute;
	}
}