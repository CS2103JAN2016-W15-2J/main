package tasknote.storage;

import java.io.IOException;

import tasknote.shared.TaskObject;

public class StorageConversion{
	private StorageMagicStringsAndNumbers magicValuesRetriever;
    
	/**
	 * Constructor
	 */
	public StorageConversion(){
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
	}
	
	/**
	 * To convert a series of String into one taskObject
	 * @param tasks
	 * @return ArrayList<TaskObject>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public TaskObject convertStringToTaskObject(String[] linesInTask) throws ClassNotFoundException, IOException{
		TaskObject returnObject = new TaskObject();
		
		for(int index = 0; index < magicValuesRetriever.getTotalTitles(); ++index){
			storeItemIntoTaskObject(index, linesInTask[index], returnObject);
		}
		//setGregorianCalendar(returnObject, getGregorianCalendarFromTask(returnObject));
		
		return returnObject;
	}
	
	private void storeItemIntoTaskObject(int taskObjectLine, String string, TaskObject returnObject) throws IOException, ClassNotFoundException {
		String[] content;
		if(isNullString(string)){
			return;
		}else{
			content = extractContent(taskObjectLine, string);
		}
		if(isNoContentFound(content)){
			return;
		}
		switch(taskObjectLine){
			case 0:
				setTaskName(returnObject, content);
				break;
			case 1:
				setTaskDay(returnObject, content);
				break;
			case 2:
				setTaskMonth(returnObject, content);
				break;
			case 3:
				setTaskYear(returnObject, content);
				break;
			case 4:
				setTaskHour(returnObject, content);
				break;
			case 5:
				setTaskMinute(returnObject, content);
				break;
			case 6:
				setTaskDuration(returnObject, content);
				break;
			case 7:
				setTaskLocation(returnObject, content);
				break;
			case 8:
				setTaskNotifyTime(returnObject, content);
				break;
			case 9: //note is enum!
				setTaskStatus(returnObject, content);
				break;
			case 10:
				setTaskType(returnObject, content);
				break;
			case 11:
				setTaskEndDateDay(returnObject, content);
				break;
			case 12:
				setTaskEndDateMonth(returnObject, content);
				break;
			case 13:
				setTaskEndDateYear(returnObject, content);
				break;
			case 14:
				setTaskEndDateHour(returnObject, content);
				break;
			case 15:
				setTaskEndDateMinute(returnObject, content);
				break;
			case 16:
				break;
			/*
			case 19:
				setTaskIsMarkedDone(returnObject, content);
				break;
			case 20:
				setTaskIsNotified(returnObject, content);
				break;
			case 21:
				setTaskLocale(returnObject, content);
				break;
			case 22:
				setTaskGregorianCalendar(returnObject, content);
				break;
			case 23:
				setTaskTimeZoneDayLightTime(returnObject, content);
				break;
			*/
			default:
				break;
		}
	}
	
	private void setTaskEndDateMinute(TaskObject returnObject, String[] content) {
		returnObject.setEndDateMinute(Integer.parseInt(content[1].trim()));
	}

	private void setTaskEndDateHour(TaskObject returnObject, String[] content) {
		returnObject.setEndDateHour(Integer.parseInt(content[1].trim()));
		
	}

	private void setTaskEndDateYear(TaskObject returnObject, String[] content) {
		returnObject.setEndDateYear(Integer.parseInt(content[1].trim()));
		
	}

	private void setTaskEndDateMonth(TaskObject returnObject, String[] content) {
		returnObject.setEndDateMonth(Integer.parseInt(content[1].trim()));
		
	}

	private void setTaskEndDateDay(TaskObject returnObject, String[] content) {
		returnObject.setEndDateDay(Integer.parseInt(content[1].trim()));
	}

	/*
	* GregorianCalendar Methods
	
	private boolean isTimeZoneUsingDayLightTime(String[] content) {
		return content[1].trim().equalsIgnoreCase("true");
	}
	
	private void setGregorianCalendar(TaskObject returnObject, GregorianCalendar returnCalendar) {
		returnCalendar.set(returnObject.getDateYear(), returnObject.getDateMonth(), returnObject.getDateDay(), returnObject.getDateHour(), returnObject.getDateMinute());
	}
	
	private void setTaskTimeZoneDayLightTime(TaskObject returnObject, String[] content) {
		if(isTimeZoneUsingDayLightTime(content)){
			getTimeZoneFromTask(returnObject).useDaylightTime();
		}
	}
	
	private void setTaskGregorianCalendar(TaskObject returnObject, String[] content) {
		getTimeZoneFromTask(returnObject).setID(content[1].trim());
	}
	
	private TimeZone getTimeZoneFromTask(TaskObject returnObject) {
		return getGregorianCalendarFromTask(returnObject).getTimeZone();
	}
	
	private GregorianCalendar getGregorianCalendarFromTask(TaskObject returnObject) {
		return returnObject.getTaskObjectCalendar();
	}
	
	private void setLocaleToTaskObjectCalendar(TaskObject returnObject, Locale locale) {
		returnObject.setTaskObjectCalendar(new GregorianCalendar(locale));
	}
	
	private void setTaskLocale(TaskObject returnObject, String[] content) {
		String firstDayOfWeek = content[1].trim();
		if(isLocaleSundayFirstDayOfWeek(firstDayOfWeek)){
			Locale locale = getLocaleSundayFirstDayOfWeek();
			setLocaleToTaskObjectCalendar(returnObject, locale);
		}else if(isLocaleMondayFirstDayOfWeek(firstDayOfWeek)){
			Locale locale = getLocaleMondayFirstDayOfWeek();
			setLocaleToTaskObjectCalendar(returnObject, locale);
		}
	}

	private Locale getLocaleMondayFirstDayOfWeek() {
		return new Locale(magicValuesRetriever.getLanguage(),magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()));
	}

	private Locale getLocaleSundayFirstDayOfWeek() {
		return new Locale(magicValuesRetriever.getLanguage(),magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()));
	}

	private boolean isLocaleMondayFirstDayOfWeek(String firstDayOfWeek) {
		return firstDayOfWeek.equalsIgnoreCase(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()));
	}

	private boolean isLocaleSundayFirstDayOfWeek(String firstDayOfWeek) {
		return firstDayOfWeek.equalsIgnoreCase(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()));
	}
	
	private void writeTaskTimeZoneDayLightTime(TimeZone taskTimeZone, StringBuffer tempBuffer) {
		tempBuffer.append(taskTimeZone.observesDaylightTime());
	}

	private void writeTaskTimeZoneID(TimeZone taskTimeZone, StringBuffer tempBuffer) {
		tempBuffer.append(taskTimeZone.getID());
	}

	private boolean isLocaleMondayFirstDayOfWeek(GregorianCalendar taskCalendar) {
		return taskCalendar.getFirstDayOfWeek() == magicValuesRetriever.getMondayFirstDayOfWeek();
	}

	private boolean isLocaleSundayFirstDayOfWeek(GregorianCalendar taskCalendar) {
		return taskCalendar.getFirstDayOfWeek() == magicValuesRetriever.getSundayFirstDayOfWeek();
	}

	private void writeLocaleMondayFirstDayOfWeekToStringBuffer(StringBuffer tempBuffer) {
		tempBuffer.append(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()));
	}

	private void writeLocaleSundayFirstDayOfWeekToStringBuffer(StringBuffer tempBuffer) {
		tempBuffer.append(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()));
	}
	
	private void setTaskIsMarkedDone(TaskObject returnObject, String[] content) {
		if(isTimeZoneUsingDayLightTime(content)){
			returnObject.setIsMarkedDone(true);
		}else{
			returnObject.setIsMarkedDone(false);
		}
	}
	
	private void setTaskIsNotified(TaskObject returnObject, String[] content) {
		if(isTimeZoneUsingDayLightTime(content)){
			returnObject.setIsNotified(true);
		}else{
			returnObject.setIsNotified(false);
		}
	}
	
	private void writeIsMarkedDoneToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getIsMarkedDone());
	}
	
	private void writeTaskIsNotifiedToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getIsNotified());
	}
	
	*/

	private void setTaskType(TaskObject returnObject, String[] content) {
		returnObject.setTaskType(content[1].trim());
	}

	private void setTaskStatus(TaskObject returnObject, String[] content) {
		String taskStatus = content[1].trim();
		for(int indexEnum = 0; indexEnum < magicValuesRetriever.getTotalTaskStatus(); ++indexEnum){
			if(taskStatus.equalsIgnoreCase(magicValuesRetriever.getTaskStatus(indexEnum))){
				returnObject.setTaskStatus(magicValuesRetriever.getTaskStatus(indexEnum));
			}
		}
	}

	private void setTaskNotifyTime(TaskObject returnObject, String[] content) {
		returnObject.setNotifyTime(Integer.parseInt(content[1].trim()));
	}

	private void setTaskLocation(TaskObject returnObject, String[] content) {
		returnObject.setLocation(content[1].trim());
	}

	private void setTaskDuration(TaskObject returnObject, String[] content) {
		returnObject.setDuration(Integer.parseInt(content[1].trim()));
	}

	private void setTaskMinute(TaskObject returnObject, String[] content) {
		returnObject.setDateMinute(Integer.parseInt(content[1].trim()));
	}

	private void setTaskHour(TaskObject returnObject, String[] content) {
		returnObject.setDateHour(Integer.parseInt(content[1].trim()));
	}

	private void setTaskYear(TaskObject returnObject, String[] content) {
		returnObject.setDateYear(Integer.parseInt(content[1].trim()));
	}

	private void setTaskMonth(TaskObject returnObject, String[] content) {
		returnObject.setDateMonth(Integer.parseInt(content[1].trim()));
	}

	private void setTaskDay(TaskObject returnObject, String[] content) {
		returnObject.setDateDay(Integer.parseInt(content[1].trim()));
	}

	private void setTaskName(TaskObject returnObject, String[] content) {
		String name = content[1].trim();
		returnObject.setTaskName(extractNullOrName(name));
	}

	private String extractNullOrName(String name) {
		return name.equals("null") ? null : name;
	}

	private boolean isNoContentFound(String[] content) {
		return content.length == 1;
	}

	private String[] extractContent(int index, String string) {
		return string.split(magicValuesRetriever.getTaskObjectTitle(index));
	}

	private boolean isNullString(String string) {
		return string == null;
	}

	/**
	 * To convert one task into a string for storage
	 * @param task
	 * @return String for store into file
	 */
	public String convertTaskObjectToString(TaskObject task){
		StringBuffer convertedString = new StringBuffer("");
		//GregorianCalendar taskCalendar = getGregorianCalendarFromTask(task);
		//TimeZone taskTimeZone = taskCalendar.getTimeZone();
		
		for(int index = 0; index < magicValuesRetriever.getTotalTitles(); ++index){
			convertedString.append(extractItemFromTaskObject(index, task));
		}
		
		return convertedString.toString();
	}
	
	private StringBuffer extractItemFromTaskObject(int taskObjectLine, TaskObject task){
		StringBuffer tempBuffer = new StringBuffer("");
		tempBuffer.append(magicValuesRetriever.getTaskObjectTitle(taskObjectLine));
		tempBuffer.append(magicValuesRetriever.getSpace());
		switch(taskObjectLine){
			case 0:
				writeTaskNameToStringBuffer(task, tempBuffer);
				break;
			case 1:
				writeTaskDayToStringBuffer(task, tempBuffer);
				break;
			case 2:
				writeTaskMonthToStringBuffer(task, tempBuffer);
				break;
			case 3:
				writeTaskYearToStringBuffer(task, tempBuffer);
				break;
			case 4:
				writeTaskHourToStringBuffer(task, tempBuffer);
				break;
			case 5:
				writeTaskMinuteToStringBuffer(task, tempBuffer);
				break;
			case 6:
				writeTaskDurationToStringBuffer(task, tempBuffer);
				break;
			case 7:
				writeTaskLocationToStringBuffer(task, tempBuffer);
				break;
			case 8:
				writeTaskNotifyTimeToStringBuffer(task, tempBuffer);
				break;
			case 9:
				writeTaskGetStatusToStringBuffer(task, tempBuffer);
				break;
			case 10:
				writeGetTaskTypeToStringBuffer(task, tempBuffer);
				break;
			case 11:
				writeGetTaskEndDateDayToStringBuffer(task, tempBuffer);
				break;
			case 12:
				writeGetTaskEndDateMonthToStringBuffer(task, tempBuffer);
				break;
			case 13:
				writeGetTaskEndDateYearToStringBuffer(task, tempBuffer);
				break;
			case 14:
				writeGetTaskEndDateHourToStringBuffer(task, tempBuffer);
				break;
			case 15:
				writeGetTaskEndDateMinuteToStringBuffer(task, tempBuffer);
				break;
			case 16:
				break;
			
			/*
			case 9:
				writeTaskIsNotifiedToStringBuffer(task, tempBuffer);
				break;
			case 12:
				writeIsMarkedDoneToStringBuffer(task, tempBuffer);
				break;
			case 13:
				if(isLocaleSundayFirstDayOfWeek(taskCalendar)){
					writeLocaleSundayFirstDayOfWeekToStringBuffer(tempBuffer);
				}else if(isLocaleMondayFirstDayOfWeek(taskCalendar)){
					writeLocaleMondayFirstDayOfWeekToStringBuffer(tempBuffer);
				}
				break;
			case 14:
				writeTaskTimeZoneID(taskTimeZone, tempBuffer);
				break;
			case 15:
				writeTaskTimeZoneDayLightTime(taskTimeZone, tempBuffer);
				break;
			*/
			default:
				break;
		}
		tempBuffer.append(magicValuesRetriever.getNewLine());
		return tempBuffer;
	}

	private void writeGetTaskEndDateMinuteToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getEndDateMinute());
	}

	private void writeGetTaskEndDateHourToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getEndDateHour());
	}

	private void writeGetTaskEndDateYearToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getEndDateYear());	
	}

	private void writeGetTaskEndDateMonthToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getEndDateMonth());
	}

	private void writeGetTaskEndDateDayToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getEndDateDay());
	}

	private void writeGetTaskTypeToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getTaskType());
	}

	private void writeTaskGetStatusToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getTaskStatus());
	}

	private void writeTaskNotifyTimeToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getNotifyTime());
	}

	private void writeTaskLocationToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getLocation());
	}

	private void writeTaskDurationToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDuration());
	}

	private void writeTaskMinuteToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDateMinute());
	}

	private void writeTaskHourToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDateHour());
	}

	private void writeTaskYearToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDateYear());
	}

	private void writeTaskMonthToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDateMonth());
	}

	private void writeTaskDayToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getDateDay());
	}

	private void writeTaskNameToStringBuffer(TaskObject task, StringBuffer tempBuffer) {
		tempBuffer.append(task.getTaskName());
	}
}