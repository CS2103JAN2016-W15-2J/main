# A0129529Wgenerated
###### \src\tasknote\parser\DateMessage.java
``` java
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the extraWordsUsed
	 */
	public int getExtraWordsUsed() {
		return extraWordsUsed;
	}

	/**
	 * @param extraWordsUsed the extraWordsUsed to set
	 */
	public void setExtraWordsUsed(int extraWordsUsed) {
		this.extraWordsUsed = extraWordsUsed;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
```
###### \src\tasknote\parser\DateParser.java
``` java
	/**
	 * @return the allPhrases
	 */
	public ArrayList<String> getAllPhrases() {
		return allPhrases;
	}

	/**
	 * @param allPhrases
	 *            the allPhrases to set
	 */
	public void setAllPhrases(ArrayList<String> allPhrases) {
		this.allPhrases = allPhrases;
	}

	/**
	 * @return the listPointer
	 */
	public int getListPointer() {
		return listPointer;
	}

	/**
	 * @param listPointer
	 *            the listPointer to set
	 */
	public void setListPointer(int listPointer) {
		this.listPointer = listPointer;
	}

	/**
	 * @return the currentPhrase
	 */
	public String getCurrentPhrase() {
		return currentPhrase;
	}

	/**
	 * @param currentPhrase
	 *            the currentPhrase to set
	 */
	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}

	/**
	 * @return the phraseCount
	 */
	public int getPhraseCount() {
		return phraseCount;
	}

	/**
	 * @param phraseCount
	 *            the phraseCount to set
	 */
	public void setPhraseCount(int phraseCount) {
		this.phraseCount = phraseCount;
	}

	/**
	 * @return the dateMessage
	 */
	public DateMessage getDateMessage() {
		return dateMessage;
	}

	/**
	 * @param dateMessage
	 *            the dateMessage to set
	 */
	public void setDateMessage(DateMessage dateMessage) {
		this.dateMessage = dateMessage;
	}

```
###### \src\tasknote\parser\Parser.java
``` java
	/**
	 * @return the allPhrases
	 */
	public ArrayList<String> getAllPhrases() {
		return allPhrases;
	}

	/**
	 * @param allPhrases
	 *            the allPhrases to set
	 */
	public void setAllPhrases(ArrayList<String> allPhrases) {
		this.allPhrases = allPhrases;
	}

	/**
	 * @return the listPointer
	 */
	public int getListPointer() {
		return listPointer;
	}

	/**
	 * @param listPointer
	 *            the listPointer to set
	 */
	public void setListPointer(int listPointer) {
		this.listPointer = listPointer;
	}

	/**
	 * @return the objectForThisCommand
	 */
	public TaskObject getObjectForThisCommand() {
		return objectForThisCommand;
	}

	/**
	 * @param objectForThisCommand
	 *            the objectForThisCommand to set
	 */
	public void setObjectForThisCommand(TaskObject objectForThisCommand) {
		this.objectForThisCommand = objectForThisCommand;
	}
}
```
###### \src\tasknote\parser\TimeMessage.java
``` java
	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
	}

	/**
	 * @return the minute
	 */
	public int getMinute() {
		return minute;
	}

	/**
	 * @param minute the minute to set
	 */
	public void setMinute(int minute) {
		this.minute = minute;
	}

	/**
	 * @return the extraHours
	 */
	public int getExtraHours() {
		return extraHours;
	}

	/**
	 * @param extraHours the extraHours to set
	 */
	public void setExtraHours(int extraHours) {
		this.extraHours = extraHours;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
```
###### \src\tasknote\parser\TimeParser.java
``` java
	/**
	 * @return the currentPhrase
	 */
	public String getCurrentPhrase() {
		return currentPhrase;
	}

	/**
	 * @param currentPhrase
	 *            the currentPhrase to set
	 */
	public void setCurrentPhrase(String currentPhrase) {
		this.currentPhrase = currentPhrase;
	}

```
###### \src\tasknote\shared\TaskObject.java
``` java
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
		return getTaskStatus() == TaskStatus.TASK_COMPLETED;
	}

	/**
	 * @param markedDone the markedDone to set
	 */
	public void setIsMarkedDone(boolean isMarkedDone) {
		if(isMarkedDone) {
		    setTaskStatus(TaskStatus.TASK_COMPLETED);
		} else {
		    setTaskStatus(TaskStatus.TASK_OUTSTANDING);
		}
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
	 * getTaskStatus() extract the current taskStatus this TaskObject has
	 * @return the current taskStatus of the taskObject
	 */
	public TaskStatus getTaskStatus() {
		String status = this.taskStatus.get();
		
		switch(status) {
            case Constants.STRING_TASKSTATUS_DEFAULT:
                return TaskStatus.TASK_DEFAULT;
            case Constants.STRING_TASKSTATUS_OUTSTANDING:
                return TaskStatus.TASK_OUTSTANDING;
            case Constants.STRING_TASKSTATUS_OVERDUE:
                return TaskStatus.TASK_OVERDUE;
            case Constants.STRING_TASKSTATUS_COMPLETED:
                return TaskStatus.TASK_COMPLETED;
            case Constants.STRING_TASKSTATUS_INVALID_STORAGE:
                return TaskStatus.TASK_INVALID_STORAGE;
            default:
                return null;
		}
	}

	/**
	 * @param submit a TaskStatus to replace the current TaskStatus of the object
	 */
	public void setTaskStatus(TaskStatus taskStatus) {
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

```
