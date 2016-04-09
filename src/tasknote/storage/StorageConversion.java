package tasknote.storage;

import java.io.IOException;

import tasknote.shared.TaskObject;

//@@author A0126172M
public class StorageConversion{
	private StorageConstants constants;
	private StorageDeadlineUpdater deadlineUpdater;
	
	/**
	 * constants for TaskObject cases
	 */
	private final int CASE_TASK_NAME = 0;
	private final int CASE_TASK_DATE_DAY = 1;
	private final int CASE_TASK_DATE_MONTH = 2;
	private final int CASE_TASK_DATE_YEAR = 3;
	private final int CASE_TASK_DATE_HOUR = 4;
	private final int CASE_TASK_DATE_MINUTE = 5;
	private final int CASE_TASK_DURATION = 6;
	private final int CASE_TASK_LOCATION = 7;
	private final int CASE_TASK_NOTIFY_TIME = 8;
	private final int CASE_TASK_STATUS = 9;
	private final int CASE_TASK_TYPE = 10;
	private final int CASE_TASK_END_DATE_DAY = 11;
	private final int CASE_TASK_END_DATE_MONTH = 12;
	private final int CASE_TASK_END_DATE_YEAR = 13;
	private final int CASE_TASK_END_DATE_HOUR = 14;
	private final int CASE_TASK_END_DATE_MINUTE = 15;
	private final int CASE_TASK_END = 16;
    
	/**
	 * Constructor
	 */
	public StorageConversion(){
		constants = new StorageConstants();
		deadlineUpdater = new StorageDeadlineUpdater();
	}
	
	/**
	 * To convert a series of String into one taskObject
	 * @param tasks
	 * @return ArrayList<TaskObject>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public TaskObject convertStringToTaskObject(String[] taskLinesRead, int linesRead) throws ClassNotFoundException, IOException{
		TaskObject returnObject = new TaskObject();
		
		for(int index = 0; index < linesRead; ++index){
			storeItemIntoTaskObject(taskLinesRead[index], returnObject);
		}
		
		deadlineUpdater.update(returnObject);
		return returnObject;
	}
	
	private void storeItemIntoTaskObject(String taskLine, TaskObject returnObject) throws IOException, ClassNotFoundException {		
		
		if(isNullString(taskLine)){
			return;
		}
		
		int taskOperation = extractTaskOperation(taskLine);
		String[] content = extractContent(taskOperation, taskLine);
		
		if(isNoContentFound(content)){
			return;
		}
		
		setTaskOperation(returnObject, taskOperation, content);
	}

	private void setTaskOperation(TaskObject returnObject, int taskOperation, String[] content) {
		switch(taskOperation){
			case CASE_TASK_NAME:
				setTaskName(returnObject, content);
				break;
			case CASE_TASK_DATE_DAY:
				setTaskDay(returnObject, content);
				break;
			case CASE_TASK_DATE_MONTH:
				setTaskMonth(returnObject, content);
				break;
			case CASE_TASK_DATE_YEAR:
				setTaskYear(returnObject, content);
				break;
			case CASE_TASK_DATE_HOUR:
				setTaskHour(returnObject, content);
				break;
			case CASE_TASK_DATE_MINUTE:
				setTaskMinute(returnObject, content);
				break;
			case CASE_TASK_DURATION:
				setTaskDuration(returnObject, content);
				break;
			case CASE_TASK_LOCATION:
				setTaskLocation(returnObject, content);
				break;
			case CASE_TASK_NOTIFY_TIME:
				setTaskNotifyTime(returnObject, content);
				break;
			case CASE_TASK_STATUS:
				setTaskStatus(returnObject, content);
				break;
			case CASE_TASK_TYPE:
				setTaskType(returnObject, content);
				break;
			case CASE_TASK_END_DATE_DAY:
				setTaskEndDateDay(returnObject, content);
				break;
			case CASE_TASK_END_DATE_MONTH:
				setTaskEndDateMonth(returnObject, content);
				break;
			case CASE_TASK_END_DATE_YEAR:
				setTaskEndDateYear(returnObject, content);
				break;
			case CASE_TASK_END_DATE_HOUR:
				setTaskEndDateHour(returnObject, content);
				break;
			case CASE_TASK_END_DATE_MINUTE:
				setTaskEndDateMinute(returnObject, content);
				break;
			default:
				break;
		}
	}
	
	private int extractTaskOperation(String taskObjectLine){
		for(int titleIndex = 0; titleIndex < constants.getTotalTitles(); ++titleIndex){
			if(taskObjectLine.startsWith(constants.getTaskObjectTitle(titleIndex))){
				return titleIndex;
			}
		}
		return -1;
	}
	
	private String[] extractContent(int index, String string) {
		return string.split(constants.getTaskObjectTitle(index));
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

	private void setTaskType(TaskObject returnObject, String[] content) {
		returnObject.setTaskType(content[1].trim());
	}

	private void setTaskStatus(TaskObject returnObject, String[] content) {
		String taskStatus = content[1].trim();
		for(int indexEnum = 0; indexEnum < constants.getTotalTaskStatus(); ++indexEnum){
			if(taskStatus.equalsIgnoreCase(constants.getTaskStatus(indexEnum))){
				returnObject.setTaskStatus(constants.getTaskStatus(indexEnum));
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
		
		for(int index = 0; index < constants.getTotalTitles(); ++index){
			convertedString.append(extractItemFromTaskObject(index, task));
		}
		
		return convertedString.toString();
	}
	
	private StringBuffer extractItemFromTaskObject(int taskObjectLine, TaskObject task){
		StringBuffer tempBuffer = new StringBuffer("");
		tempBuffer.append(constants.getTaskObjectTitle(taskObjectLine));
		tempBuffer.append(constants.getSpace());
		switch(taskObjectLine){
			case CASE_TASK_NAME:
				writeTaskNameToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DATE_DAY:
				writeTaskDayToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DATE_MONTH:
				writeTaskMonthToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DATE_YEAR:
				writeTaskYearToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DATE_HOUR:
				writeTaskHourToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DATE_MINUTE:
				writeTaskMinuteToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_DURATION:
				writeTaskDurationToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_LOCATION:
				writeTaskLocationToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_NOTIFY_TIME:
				writeTaskNotifyTimeToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_STATUS:
				writeTaskGetStatusToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_TYPE:
				writeGetTaskTypeToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END_DATE_DAY:
				writeGetTaskEndDateDayToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END_DATE_MONTH:
				writeGetTaskEndDateMonthToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END_DATE_YEAR:
				writeGetTaskEndDateYearToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END_DATE_HOUR:
				writeGetTaskEndDateHourToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END_DATE_MINUTE:
				writeGetTaskEndDateMinuteToStringBuffer(task, tempBuffer);
				break;
			case CASE_TASK_END:
				break;
			default:
				break;
		}
		tempBuffer.append(constants.getNewLine());
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