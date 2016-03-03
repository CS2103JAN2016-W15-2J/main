package tasknote.shared;

import java.util.GregorianCalendar;

public class TaskObject implements Comparable {
	
    public static enum TASK_STATUS {
        TASK_DEFAULT, TASK_OUTSTANDING, TASK_COMPLETED, TASK_INVALID_STORAGE
    };
    
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
	
	private TASK_STATUS taskStatus;
	private String taskType;
	
	private boolean isMarkedDone;
	
	/**
	 * For Storage
	 */
	public TaskObject(){
		setTaskName(taskName);
		
		setDateDay(-1);
		setDateMonth(-1);
		setDateYear(-1);
		setDateHour(-1);
		setDateMinute(-1);
		
		setDuration(0);
		
		setTaskObjectCalendar(new GregorianCalendar());
		
		setLocation("");
		
		setNotifyTime(0);
		setIsNotified(false);
		
		setTaskStatus(TASK_STATUS.TASK_DEFAULT);
		setTaskType("floating");
		
		setIsMarkedDone(false);
	}
	
	public TaskObject(String taskName) {
		
		setTaskName(taskName);
		
		setDateDay(-1);
		setDateMonth(-1);
		setDateYear(-1);
		setDateHour(-1);
		setDateMinute(-1);
		
		setDuration(0);
		
		setTaskObjectCalendar(new GregorianCalendar());
		
		setLocation("");
		
		setNotifyTime(0);
		setIsNotified(false);
		
		setTaskStatus(TASK_STATUS.TASK_DEFAULT);
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
	 * @return the taskStatus
	 */
	public TASK_STATUS getTaskStatus() {
		return this.taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(TASK_STATUS taskStatus) {
		this.taskStatus = taskStatus;
	}
	
	/**
	 * @param string of taskStatus to set
	 */
	public void setTaskStatus(String taskStatus){
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_DEFAULT)){
			this.taskStatus = TASK_STATUS.TASK_DEFAULT;
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_OUTSTANDING)){
			this.taskStatus = TASK_STATUS.TASK_OUTSTANDING;
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_COMPLETED)){
			this.taskStatus = TASK_STATUS.TASK_COMPLETED;
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_INVALID_STORAGE)){
			this.taskStatus = TASK_STATUS.TASK_INVALID_STORAGE;
			return;
		}
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
				+ "\ntaskStatus = " + taskStatus
				+ "\ntaskType = " + taskType
				+ "\nisMarkedDone = " + isMarkedDone
				+ "\nFirstDayOfWeek = " +  taskObjectCalendar.getFirstDayOfWeek()
				+ "\nTimeZoneID = " + taskObjectCalendar.getTimeZone().getID()
				+ "\nisDayLightTimeOn = " + taskObjectCalendar.getTimeZone().observesDaylightTime() + "\n";
	}
	
    /**
     * @return Get formatted date. If date is not set, will return empty string.
     * @@author MunKeat
     */
	public String getFormattedDate() {
	    String taskDate = "";
	    
        if(dateDay != -1 && dateMonth != -1 && dateYear != -1) {
            taskDate = (dateDay + "-" + dateMonth + "-" + dateYear);
        } 
        
        return taskDate;
	}
	
    /**
     * @return Get formatted time. If time is not set, will return empty string.
     * @@author MunKeat
     */
	public String getFormattedTime() {
        String taskTime = "";
        
        if(dateMinute != -1 && dateHour != -1) {
            taskTime = (dateHour + ":" + dateMinute);
        } 
        
        return taskTime;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean equals(Object comparingObject) {
		if (!(comparingObject instanceof TaskObject)) {
			return false;
		}
		
		TaskObject comparingTaskObject = (TaskObject) comparingObject;
		
		if (!comparingTaskObject.getTaskName().equals(this.getTaskName())) {
			return false;
		}
		
		if (comparingTaskObject.getDateHour() != this.getDateHour()) {
			return false;
		}
		
		if (comparingTaskObject.getDateMinute() != this.getDateMinute()) {
			return false;
		}
		
		// TODO
		
		return true;
	}
}
