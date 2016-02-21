package tasknote.shared;

import java.util.GregorianCalendar;

public class TaskObject {
	
	private String taskName;
	private int taskID;
	
	private int dateDay;
	private int dateMonth;
	private int dateYear;
	private int dateHour;
	private int dateMinute;
	
	private int duration;
	
	private GregorianCalendar taskObjectCalendar;
	
	private String location;
	
	private long notifyTime;
	private boolean isNotified;
	
	private String taskColor;
	private String taskType;
	
	private boolean isMarkedDone;
	
	public TaskObject(String taskName) {
		
		setTaskName(taskName);
		
		setDateDay(0);
		setDateMonth(0);
		setDateYear(0);
		setDateHour(0);
		setDateMinute(0);
		
		setDuration(0);
		
		setTaskObjectCalendar(new GregorianCalendar());
		
		setLocation("");
		
		setNotifyTime(0);
		setIsNotified(false);
		
		setTaskColor("Red");
		setTaskType("floating");
		
		setIsMarkedDone(false);
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return this.taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**
	 * @return the taskID
	 */
	public int getTaskID() {
		return this.taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	/**
	 * @return the date
	 */
	public int getDateDay() {
		return this.dateDay;
	}

	/**
	 * @param date the date to set
	 */
	public void setDateDay(int dateDay) {
		this.dateDay = dateDay;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the notifyTime
	 */
	public long getNotifyTime() {
		return this.notifyTime;
	}

	/**
	 * @param notifyTime the notifyTime to set
	 */
	public void setNotifyTime(long notifyTime) {
		this.notifyTime = notifyTime;
	}

	/**
	 * @return the isNotified
	 */
	public boolean getIsNotified() {
		return this.isNotified;
	}

	/**
	 * @param isNotified the isNotified to set
	 */
	public void setIsNotified(boolean isNotified) {
		this.isNotified = isNotified;
	}

	/**
	 * @return the taskColor
	 */
	public String getTaskColor() {
		return this.taskColor;
	}

	/**
	 * @param taskColor the taskColor to set
	 */
	public void setTaskColor(String taskColor) {
		this.taskColor = taskColor;
	}

	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return this.taskType;
	}

	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/**
	 * @return the markedDone
	 */
	public boolean getIsMarkedDone() {
		return this.isMarkedDone;
	}

	/**
	 * @param markedDone the markedDone to set
	 */
	public void setIsMarkedDone(boolean isMarkedDone) {
		this.isMarkedDone = isMarkedDone;
	}

	/**
	 * @return the taskObjectCalendar
	 */
	public GregorianCalendar getTaskObjectCalendar() {
		return this.taskObjectCalendar;
	}

	/**
	 * @param taskObjectCalendar the taskObjectCalendar to set
	 */
	public void setTaskObjectCalendar(GregorianCalendar taskObjectCalendar) {
		this.taskObjectCalendar = taskObjectCalendar;
	}

	/**
	 * @return the dateMonth
	 */
	public int getDateMonth() {
		return this.dateMonth;
	}

	/**
	 * @param dateMonth the dateMonth to set
	 */
	public void setDateMonth(int dateMonth) {
		this.dateMonth = dateMonth;
	}

	/**
	 * @return the dateYear
	 */
	public int getDateYear() {
		return this.dateYear;
	}

	/**
	 * @param dateYear the dateYear to set
	 */
	public void setDateYear(int dateYear) {
		this.dateYear = dateYear;
	}

	/**
	 * @return the dateHour
	 */
	public int getDateHour() {
		return this.dateHour;
	}

	/**
	 * @param dateHour the dateHour to set
	 */
	public void setDateHour(int dateHour) {
		this.dateHour = dateHour;
	}

	/**
	 * @return the dateMinute
	 */
	public int getDateMinute() {
		return this.dateMinute;
	}

	/**
	 * @param dateMinute the dateMinute to set
	 */
	public void setDateMinute(int dateMinute) {
		this.dateMinute = dateMinute;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * @return the string for printing for debugging
	 */
	public String toString(){
		return "task name = " + taskName
				+ "\nDate = " + dateDay + "/" + dateMonth + "/" + dateYear
				+ "\nGregorianCalendar = " + taskObjectCalendar
				+ "\nTime = " + dateHour + " " + dateMinute
				+ "\nDuration = " + duration
				+ "\nlocation = " + location
				+ "\nNotify Time = " + notifyTime
				+ "\nisNotified = " + isNotified
				+ "\ntasColor = " + taskColor
				+ "\ntaskType = " + taskType
				+ "\nisMarkedDone = " + isMarkedDone + "\n";
	}
}
