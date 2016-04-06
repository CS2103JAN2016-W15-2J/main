package tasknote.shared;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import tasknote.shared.TaskObject.TASK_STATUS;
//@@author A0126172M
public class AutoUpdateTaskObject extends TimerTask{
	private Timer timeChecker = new Timer();
	
	private final int NUMBER_SECONDS_PER_MINUTE = 60;
	private final int INTERVAL = 60000; //1 min
	private final GregorianCalendar clock = new GregorianCalendar();
	
	private int startTime;
	private int currentYear;
	private int currentMonth;
	private int currentDay;
	private int currentHour;
	private int currentMinute;
	private int currentSecond;
	
	private TaskObject taskObject;
	
	public AutoUpdateTaskObject(){
		//startTime = NUMBER_SECONDS_PER_MINUTE - currentSecond;
		startTime = 0;
	}
	
	public void setTaskObject(TaskObject taskObject){
		this.taskObject = taskObject;
	}
	
	public AutoUpdateTaskObject(TaskObject taskObject){
		this.taskObject = taskObject;
		currentSecond = clock.get(Calendar.SECOND);
		startTime = 0;
		//startTime = NUMBER_SECONDS_PER_MINUTE - currentSecond; //approximate to start of the minute
	}
	
	public void update(TaskObject taskObject){
		assert(this.taskObject!=null);
		timeChecker.schedule(new AutoUpdateTaskObject(taskObject), startTime, INTERVAL);
		run();
	}
	
	public void update(){
		assert(taskObject!=null);
		timeChecker.schedule(new AutoUpdateTaskObject(taskObject), startTime, INTERVAL);
		run();
	}
	
	public void run(){
		updateClock();
		//printTime();
		
		boolean notOverdue = true;
		if(!taskObject.isCompleted() && !taskObject.isFloatingTask()){
			notOverdue = updateIfOverdue(notOverdue);
		}
		
		setOverdue(notOverdue);
	}

	private void printTime() {
		System.out.println(taskObject.getDateDay() + " " + taskObject.getDateMonth() + ", " + taskObject.getDateYear());
		System.out.println(currentDay + " " + currentMonth + ", " + currentYear);
	}

	private boolean updateIfOverdue(boolean notOverdue) {
		if(notOverdue){
			notOverdue = updateIfOverdueYear();
			if(isSameYear() && notOverdue){
				notOverdue = updateIfOverdueMonth();
				if(isSameMonth() && notOverdue){
					notOverdue = updateIfOverdueDay();
					if(isSameDay() && notOverdue){
						notOverdue = updateIfOverdueHour();
						if(isSameHour() && notOverdue){
							notOverdue = updateIfOverdueMinute();
						}
					}
				}
			}
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

	private void setOverdue(boolean notOverdue) {
		if(!notOverdue){
			taskObject.setTaskStatus(TaskObject.TASK_STATUS.TASK_OVERDUE);
		}
	}

	private void updateClock() {
		currentYear = clock.get(Calendar.YEAR);
		currentMonth = clock.get(Calendar.MONTH) + 1;
		currentDay = clock.get(Calendar.DATE);
		currentHour = clock.get(Calendar.HOUR_OF_DAY);
		currentMinute = clock.get(Calendar.MINUTE);
	}
	
	private boolean updateIfOverdueYear(){
		if(taskObject.getDateYear() < currentYear){
			return false;
		}
		return true;
	}
	
	private boolean updateIfOverdueMonth(){
		if(taskObject.getDateMonth() < currentMonth){
			return false;
		}
		return true;
	}
	
	private boolean updateIfOverdueDay(){
		if(taskObject.getDateDay() < currentDay){
			return false;
		}
		return true;
	}
	
	private boolean updateIfOverdueHour(){
		if(taskObject.getDateHour() < currentHour){
			return false;
		}
		return true;
	}
	
	private boolean updateIfOverdueMinute(){
		if(taskObject.getDateMinute() < currentMinute){
			return false;
		}
		return true;
	}
}