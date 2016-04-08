package tasknote.shared;

import javafx.beans.property.SimpleStringProperty;

public class TaskObject implements Comparable<TaskObject> {
    public static final String TASK_TYPE_FLOATING = "floating";
    public static final String TASK_TYPE_DEADLINE = "deadline";
    public static final String TASK_TYPE_EVENT = "event";
    
    public String[] monthInString = {"", "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"
    };
    
    public final int DEFAULT_DATETIME_VALUE = -1;
    public final int DEFAULT_DURATION_VALUE = 0;
    public final String DEFAULT_LOCATION_VALUE = "";
    
    private final String FORMAT_DATE = "%d %s %d";
    private final String FORMAT_TIME_MORNING = "%1$02d:%2$02dAM";
    private final String FORMAT_TIME_EVENING = "%1$02d:%2$02dPM";
    
    public static enum TASK_STATUS {
        TASK_DEFAULT, TASK_OUTSTANDING, TASK_COMPLETED, TASK_OVERDUE, TASK_INVALID_STORAGE
    };
    
	private String taskName;
	private int taskID;
	
	private int dateDay;
	private int dateMonth;
	private int dateYear;
	private int dateHour;
	private int dateMinute;
	
	private int endDateDay;
	private int endDateMonth;
	private int endDateYear;
	private int endDateHour;
	private int endDateMinute;
	
	private int duration;
	
	private String location;
	
	private int notifyTime;
	private boolean isNotified;
	
	private SimpleStringProperty taskStatus = new SimpleStringProperty();
	private String taskType;
	
	private boolean isMarkedDone;
	
	/**
	 * For Storage
	 */
	public TaskObject(){
		setTaskName(taskName);
		
		setDateDay(DEFAULT_DATETIME_VALUE);
		setDateMonth(DEFAULT_DATETIME_VALUE);
		setDateYear(DEFAULT_DATETIME_VALUE);
		setDateHour(DEFAULT_DATETIME_VALUE);
		setDateMinute(DEFAULT_DATETIME_VALUE);
		
		setEndDateDay(DEFAULT_DATETIME_VALUE);
		setEndDateMonth(DEFAULT_DATETIME_VALUE);
		setEndDateYear(DEFAULT_DATETIME_VALUE);
		setEndDateHour(DEFAULT_DATETIME_VALUE);
		setEndDateMinute(DEFAULT_DATETIME_VALUE);
		
		setDuration(DEFAULT_DURATION_VALUE);
		
		setLocation(DEFAULT_LOCATION_VALUE);
		
		setNotifyTime(0);
		setIsNotified(false);
		
		setTaskStatus(TASK_STATUS.TASK_DEFAULT);
		setTaskType(TASK_TYPE_FLOATING);
		
		setIsMarkedDone(false);
	}
	
	public TaskObject(String taskName) {
		
		setTaskName(taskName);
		
		setDateDay(DEFAULT_DATETIME_VALUE);
		setDateMonth(DEFAULT_DATETIME_VALUE);
		setDateYear(DEFAULT_DATETIME_VALUE);
		setDateHour(DEFAULT_DATETIME_VALUE);
		setDateMinute(DEFAULT_DATETIME_VALUE);
		
		setDuration(DEFAULT_DURATION_VALUE);
		
		setEndDateDay(DEFAULT_DATETIME_VALUE);
		setEndDateMonth(DEFAULT_DATETIME_VALUE);
		setEndDateYear(DEFAULT_DATETIME_VALUE);
		setEndDateHour(DEFAULT_DATETIME_VALUE);
		setEndDateMinute(DEFAULT_DATETIME_VALUE);
		
		setLocation(DEFAULT_LOCATION_VALUE);
		
		setNotifyTime(0);
		setIsNotified(false);
		
		setTaskStatus(TASK_STATUS.TASK_DEFAULT);
		setTaskType(TASK_TYPE_FLOATING);
		
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
	public int getNotifyTime() {
		return this.notifyTime;
	}

	/**
	 * @param notifyTime the notifyTime to set
	 */
	public void setNotifyTime(int notifyTime) {
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
		String status = this.taskStatus.get();
		
		switch(status) {
            case Constants.STRING_TASKSTATUS_DEFAULT:
                return TASK_STATUS.TASK_DEFAULT;
            case Constants.STRING_TASKSTATUS_OUTSTANDING:
                return TASK_STATUS.TASK_OUTSTANDING;
            case Constants.STRING_TASKSTATUS_OVERDUE:
                return TASK_STATUS.TASK_OVERDUE;
            case Constants.STRING_TASKSTATUS_COMPLETED:
                return TASK_STATUS.TASK_COMPLETED;
            case Constants.STRING_TASKSTATUS_INVALID_STORAGE:
                return TASK_STATUS.TASK_INVALID_STORAGE;
            default:
                return null;
		}
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(TASK_STATUS taskStatus) {
	    switch(taskStatus) {
	        case TASK_DEFAULT:
	            this.taskStatus.set(Constants.STRING_TASKSTATUS_DEFAULT);
	            return;
	        case TASK_OUTSTANDING:
	            this.taskStatus.set(Constants.STRING_TASKSTATUS_OUTSTANDING);
	            return;
	        case TASK_OVERDUE:
	            this.taskStatus.set(Constants.STRING_TASKSTATUS_OVERDUE);
	            return;
	        case TASK_COMPLETED:
	            this.taskStatus.set(Constants.STRING_TASKSTATUS_COMPLETED);
	            return;
	        case TASK_INVALID_STORAGE:
	            this.taskStatus.set(Constants.STRING_TASKSTATUS_INVALID_STORAGE);
	            return;
	        default:
	            return;
	    }
	}
	
	/**
	 * @param string of taskStatus to set
	 */
	public void setTaskStatus(String taskStatus){
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_DEFAULT)){
		    setTaskStatus(TASK_STATUS.TASK_DEFAULT);
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_OUTSTANDING)){
		    setTaskStatus(TASK_STATUS.TASK_OUTSTANDING);
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_OVERDUE)){
		    setTaskStatus(TASK_STATUS.TASK_OVERDUE);
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_COMPLETED)){
		    setTaskStatus(TASK_STATUS.TASK_COMPLETED);
			return;
		}
		if(taskStatus.equalsIgnoreCase(Constants.STRING_TASKSTATUS_INVALID_STORAGE)){
		    setTaskStatus(TASK_STATUS.TASK_INVALID_STORAGE);
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
		if(isMarkedDone) {
		    setTaskStatus(TASK_STATUS.TASK_COMPLETED);
		} else {
		    setTaskStatus(TASK_STATUS.TASK_DEFAULT);
		}
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
	 * 
	 */
	public String toString(){
		return "task name = " + taskName
				+ "\nDate = " + dateDay + "/" + dateMonth + "/" + dateYear
				+ "\nEndDate = " + endDateDay + "/" + endDateMonth + "/" + endDateYear
				+ "\nTime = " + dateHour + " " + dateMinute
				+ "\nDuration = " + duration
				+ "\nlocation = " + location
				+ "\nNotify Time = " + notifyTime
				+ "\nisNotified = " + isNotified
				+ "\ntaskStatus = " + taskStatus
				+ "\ntaskType = " + taskType
				+ "\nisMarkedDone = " + isMarkedDone;
	}

	@Override
	public int compareTo(TaskObject otherTaskObject) {
		
		if (this.getDateYear() != otherTaskObject.getDateYear()) {
			return Integer.compare(this.getDateYear(), otherTaskObject.getDateYear());
		}
		
		if (this.getDateMonth() != otherTaskObject.getDateMonth()) {
			return Integer.compare(this.getDateMonth(), otherTaskObject.getDateMonth());
		}
		
		if (this.getDateDay() != otherTaskObject.getDateDay()) {
			return Integer.compare(this.getDateDay(), otherTaskObject.getDateDay());
		}
		
		if (this.getDateHour() != otherTaskObject.getDateHour()) {
			return Integer.compare(this.getDateHour(), otherTaskObject.getDateHour());
		}
		
		if (this.getDateMinute() != otherTaskObject.getDateMinute()) {
			return Integer.compare(this.getDateMinute(), otherTaskObject.getDateMinute());
		}
		
		if (this.getDuration() != otherTaskObject.getDuration()) {
			return Integer.compare(this.getDuration(), otherTaskObject.getDuration());
		}
		
		return this.getTaskName().compareTo(otherTaskObject.getTaskName());
	}
	
	public boolean equals(Object comparingObject) {
		if (!(comparingObject instanceof TaskObject)) {
			return false;
		}
		
		TaskObject comparingTaskObject = (TaskObject) comparingObject;
		
		if (isTaskObjectNameNotSame(comparingTaskObject)) {
			return false;
		}
		
		if (comparingTaskObject.getDateHour() != this.getDateHour()) {
			return false;
		}
		
		if (comparingTaskObject.getDateMinute() != this.getDateMinute()) {
			return false;
		}
		
		if (comparingTaskObject.getDateDay() != this.getDateDay()) {
			return false;
		}
		
		if (comparingTaskObject.getDateMonth() != this.getDateMonth()) {
			return false;
		}
		
		if (comparingTaskObject.getDateYear() != this.getDateYear()) {
			return false;
		}
		
		if (comparingTaskObject.getEndDateDay() != this.getEndDateDay()) {
			return false;
		}
		
		if (comparingTaskObject.getEndDateMonth() != this.getEndDateMonth()) {
			return false;
		}
		
		if (comparingTaskObject.getEndDateYear() != this.getEndDateYear()) {
			return false;
		}
		
		if (comparingTaskObject.getEndDateHour() != this.getEndDateHour()) {
			return false;
		}
		
		if (comparingTaskObject.getEndDateMinute() != this.getEndDateMinute()) {
			return false;
		}
		
		if (comparingTaskObject.getDuration() != this.getDuration()) {
			return false;
		}
		
		if (isTaskObjectLocationNotSame(comparingTaskObject)) {
			return false;
		}
		
		if (comparingTaskObject.getNotifyTime() != this.getNotifyTime()) {
			return false;
		}
		
		if (comparingTaskObject.getIsNotified() != this.getIsNotified()) {
			return false;
		}
		
		if (comparingTaskObject.getTaskStatus() != this.getTaskStatus()) {
			return false;
		}
		
		if (comparingTaskObject.getIsMarkedDone() != this.getIsMarkedDone()) {
			return false;
		}
		
		return true;
	}
	
	
	
	private boolean isTaskObjectLocationNotSame(TaskObject comparingTaskObject) {
		return (isNull(comparingTaskObject.getLocation()) && isNull(this.getLocation())) ? false : !comparingTaskObject.getLocation().equals(this.getLocation());
	}

	private boolean isTaskObjectNameNotSame(TaskObject comparingTaskObject) {
		return (isNull(comparingTaskObject.getTaskName()) && isNull(this.getTaskName())) ? false : !comparingTaskObject.getTaskName().equals(this.getTaskName());
	}
	
	private boolean isNull(String string) {
		if(string == null){
			return true;
		}
		return false;
	}

	public void deepCopy(TaskObject sourceTaskObject) {
		
		this.setTaskName(sourceTaskObject.getTaskName());
		this.setTaskID(sourceTaskObject.getTaskID());
		this.setDateDay(sourceTaskObject.getDateDay());
		this.setDateMonth(sourceTaskObject.getDateMonth());
		this.setDateYear(sourceTaskObject.getDateYear());
		this.setDateHour(sourceTaskObject.getDateHour());
		this.setDateMinute(sourceTaskObject.getDateMinute());
		this.setDuration(sourceTaskObject.getDuration());
		this.setIsMarkedDone(sourceTaskObject.getIsMarkedDone());
		this.setIsNotified(sourceTaskObject.getIsNotified());
		this.setLocation(sourceTaskObject.getLocation());
		this.setNotifyTime(sourceTaskObject.getNotifyTime());
		this.setTaskStatus(sourceTaskObject.getTaskStatus());
		this.setTaskType(sourceTaskObject.getTaskType());
		this.setEndDateDay(sourceTaskObject.getEndDateDay());
		this.setEndDateMonth(sourceTaskObject.getEndDateMonth());
		this.setEndDateYear(sourceTaskObject.getEndDateYear());
		this.setEndDateHour(sourceTaskObject.getEndDateHour());
		this.setEndDateMinute(sourceTaskObject.getEndDateMinute());
	}

	/**
	 * @return the endDateDay
	 */
	public int getEndDateDay() {
		return endDateDay;
	}

	/**
	 * @param endDateDay the endDateDay to set
	 */
	public void setEndDateDay(int endDateDay) {
		this.endDateDay = endDateDay;
	}

	/**
	 * @return the endDateMonth
	 */
	public int getEndDateMonth() {
		return endDateMonth;
	}

	/**
	 * @param endDateMonth the endDateMonth to set
	 */
	public void setEndDateMonth(int endDateMonth) {
		this.endDateMonth = endDateMonth;
	}

	/**
	 * @return the endDateYear
	 */
	public int getEndDateYear() {
		return endDateYear;
	}

	/**
	 * @param endDateYear the endDateYear to set
	 */
	public void setEndDateYear(int endDateYear) {
		this.endDateYear = endDateYear;
	}
	
	/**
	 * @return the endDateHour
	 */
	public int getEndDateHour() {
		return endDateHour;
	}

	/**
	 * @param endDateHour the endDateHour to set
	 */
	public void setEndDateHour(int endDateHour) {
		this.endDateHour = endDateHour;
	}

	/**
	 * @return the endDateMinute
	 */
	public int getEndDateMinute() {
		return endDateMinute;
	}

	/**
	 * @param endDateMinute the endDateMinute to set
	 */
	public void setEndDateMinute(int endDateMinute) {
		this.endDateMinute = endDateMinute;
	}
	
    /**
     * @@author A0129561
     * @return Returns the TaskStatus, as represented by a class implementing
     *         the interface ObservableValue.
     */
    public SimpleStringProperty getObservableTaskStatus() {
        return this.taskStatus;
    }
	
    private boolean isYearMonthDayDefault() {
        return (getDateYear() == DEFAULT_DATETIME_VALUE && getDateMonth() == DEFAULT_DATETIME_VALUE 
                && getDateDay() == DEFAULT_DATETIME_VALUE);
    }
    
    private boolean isYearMonthDayNonDefault() {
        return (getDateYear() != DEFAULT_DATETIME_VALUE && getDateMonth() != DEFAULT_DATETIME_VALUE
                && getDateDay() != DEFAULT_DATETIME_VALUE);
    }
    
    private boolean isEndYearMonthDayNonDefault() {
        return (getEndDateDay() != DEFAULT_DATETIME_VALUE && getEndDateMonth() != DEFAULT_DATETIME_VALUE 
                && getEndDateYear() != DEFAULT_DATETIME_VALUE);
    }
    
    private boolean isHourMinuteDefault() {
        return (getDateHour() == DEFAULT_DATETIME_VALUE && getDateMinute() == DEFAULT_DATETIME_VALUE);
    }
    
    private boolean isHourMinuteNonDefault() {
        return (getDateHour() != DEFAULT_DATETIME_VALUE && getDateMinute() != DEFAULT_DATETIME_VALUE);
    }
    
    private boolean isEndHourMinuteNonDefault() {
        return (getEndDateMinute() != DEFAULT_DATETIME_VALUE && getEndDateHour() != DEFAULT_DATETIME_VALUE);
    }
    
	/**
     * @@author A0129561
     * Set the taskType based on its current properties.
     * @return If TaskObject is set to either floating, deadline, or event.
     */
    public boolean isTaskTypeSet() {
        if (isYearMonthDayDefault() && isHourMinuteDefault() && getDuration() == DEFAULT_DURATION_VALUE) {
            this.taskType = TASK_TYPE_FLOATING;
            return true;
        } else if (isYearMonthDayNonDefault() && getDuration() == DEFAULT_DURATION_VALUE) {
            this.taskType = TASK_TYPE_DEADLINE;
            return true;
        } else if (isYearMonthDayNonDefault() && isHourMinuteNonDefault() && getDuration() > DEFAULT_DURATION_VALUE) {
            this.taskType = TASK_TYPE_EVENT;
            return true;
        } else {
            return false;
        }
    }
	
    /**
     * @@author A0129561
     * @return Get formatted date. If date is not set, will return empty string.
     */
    public String getFormattedDate() {
        String taskDate = "";
        
        if(isYearMonthDayNonDefault()) {
            assert(0 <= dateDay && dateDay <= 31);
            assert(0 <= dateMonth && dateMonth <= 12);
            
            taskDate = String.format(FORMAT_DATE, dateDay, monthInString[dateMonth], dateYear);
        } 
        
        return taskDate;
    }
    
    /**
     * @@author A0129561
     * @return Get formatted time. If time is not set, will return empty string.
     */
    public String getFormattedTime() {
        String taskTime = "";
        
        if(isHourMinuteNonDefault()) {
            assert(0 <= dateMinute && dateMinute <= 59);
            assert(0 <= dateHour && dateHour <= 23);
            
            if(dateHour < 12) {
                taskTime = String.format(FORMAT_TIME_MORNING, dateHour, dateMinute);
            } else if(dateHour == 12) {
                taskTime = String.format(FORMAT_TIME_EVENING, dateHour, dateMinute);
            } else if(dateHour > 12){
                taskTime = String.format(FORMAT_TIME_EVENING, (dateHour - 12), dateMinute);
            }
        } 
        
        return taskTime;
    }
    
    /**
     * @@author A0129561
     * @return Get formatted end date. If end date is not set, will return empty string.
     */
    public String getFormattedEndDate() {
        String taskDate = "";
        
        if(isEndYearMonthDayNonDefault()) {
            assert(0 <= endDateDay && endDateDay <= 31);
            assert(0 <= endDateMonth && endDateMonth <= 12);
            
            taskDate = String.format(FORMAT_DATE, endDateDay, monthInString[endDateMonth], endDateYear);
        } 
        
        return taskDate;
    }
    
    /**
     * @@author A0129561
     * @return Get formatted end time. If end time is not set, will return empty string.
     */
    public String getFormattedEndTime() {
        String taskTime = "";
        
        if(isEndHourMinuteNonDefault()) {
            assert(0 <= endDateMinute && endDateMinute <= 59);
            assert(0 <= endDateHour && endDateHour <= 23);
            
            if(endDateHour < 12) {
                taskTime = String.format(FORMAT_TIME_MORNING, endDateHour, endDateMinute);
            } else if(endDateHour == 12) {
                taskTime = String.format(FORMAT_TIME_EVENING, endDateHour, endDateMinute);
            } else if(endDateHour > 12){
                taskTime = String.format(FORMAT_TIME_EVENING, (endDateHour - 12), endDateMinute);
            }
        } 
        
        return taskTime;
    }
	
	/**
	 * @@author A0126172M
	 * @return true if taskObject is a floating task
	 */
	public boolean isFloatingTask(){
		return this.taskType.equals(TASK_TYPE_FLOATING);
	}
	
	/**
	 * @@author A0126172M
	 * @return true if taskObject is completed
	 */
	public boolean isCompleted(){
		return getTaskStatus() == TASK_STATUS.TASK_COMPLETED;
	}
}
