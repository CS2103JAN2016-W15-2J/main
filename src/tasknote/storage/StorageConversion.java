//@@author A0126172M
package tasknote.storage;

import java.io.IOException;

import tasknote.shared.TaskObject;

/**
 * StorageConversion class is a helper class for FileManipulation. It takes the Strings that are read
 * from the textFile and convert it into a TaskObject OR do it in the reverse manner
 */
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
	 * special constants for StorageConversion
	 */
	private final int NO_CONTENT = 1;
	private final int CONTENT = 1;
	
	/**
	 * Constructor
	 */
	public StorageConversion(){
		constants = new StorageConstants();
		deadlineUpdater = new StorageDeadlineUpdater();
	}
	
	/**
	 * To convert a series of Strings into one taskObject
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
		
		deadlineUpdater.updateTaskStatus(returnObject);
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
		return CASE_TASK_END;
	}
	
	private String[] extractContent(int index, String string) {
		return string.split(constants.getTaskObjectTitle(index));
	}
	
	private void setTaskEndDateMinute(TaskObject returnObject, String[] content) {
		returnObject.setEndDateMinute(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskEndDateHour(TaskObject returnObject, String[] content) {
		returnObject.setEndDateHour(Integer.parseInt(content[CONTENT].trim()));
		
	}

	private void setTaskEndDateYear(TaskObject returnObject, String[] content) {
		returnObject.setEndDateYear(Integer.parseInt(content[CONTENT].trim()));
		
	}

	private void setTaskEndDateMonth(TaskObject returnObject, String[] content) {
		returnObject.setEndDateMonth(Integer.parseInt(content[CONTENT].trim()));
		
	}

	private void setTaskEndDateDay(TaskObject returnObject, String[] content) {
		returnObject.setEndDateDay(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskType(TaskObject returnObject, String[] content) {
		returnObject.setTaskType(content[CONTENT].trim());
	}

	private void setTaskStatus(TaskObject returnObject, String[] content) {
		String taskStatus = content[CONTENT].trim();
		returnObject.setTaskStatus(taskStatus);
	}

	private void setTaskNotifyTime(TaskObject returnObject, String[] content) {
		returnObject.setNotifyTime(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskLocation(TaskObject returnObject, String[] content) {
		returnObject.setLocation(content[CONTENT].trim());
	}

	private void setTaskDuration(TaskObject returnObject, String[] content) {
		returnObject.setDuration(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskMinute(TaskObject returnObject, String[] content) {
		returnObject.setDateMinute(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskHour(TaskObject returnObject, String[] content) {
		returnObject.setDateHour(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskYear(TaskObject returnObject, String[] content) {
		returnObject.setDateYear(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskMonth(TaskObject returnObject, String[] content) {
		returnObject.setDateMonth(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskDay(TaskObject returnObject, String[] content) {
		returnObject.setDateDay(Integer.parseInt(content[CONTENT].trim()));
	}

	private void setTaskName(TaskObject returnObject, String[] content) {
		String name = content[CONTENT].trim();
		returnObject.setTaskName(extractNullOrName(name));
	}

	private String extractNullOrName(String name) {
		return name.equals(constants.getNullString()) ? null : name;
	}

	private boolean isNoContentFound(String[] content) {
		return content.length == NO_CONTENT;
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
		StringBuffer convertedString = new StringBuffer(constants.getEmptyString());
		
		for(int index = 0; index < constants.getTotalTitles(); ++index){
			convertedString.append(extractItemFromTaskObject(index, task));
		}
		
		return convertedString.toString();
	}
	
	private StringBuffer extractItemFromTaskObject(int taskObjectLine, TaskObject task){
		StringBuffer taskObjectBuffer = initializeTempBufferLine(taskObjectLine);
		switch(taskObjectLine){
			case CASE_TASK_NAME:
				writeTaskNameToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DATE_DAY:
				writeTaskDayToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DATE_MONTH:
				writeTaskMonthToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DATE_YEAR:
				writeTaskYearToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DATE_HOUR:
				writeTaskHourToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DATE_MINUTE:
				writeTaskMinuteToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_DURATION:
				writeTaskDurationToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_LOCATION:
				writeTaskLocationToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_NOTIFY_TIME:
				writeTaskNotifyTimeToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_STATUS:
				writeTaskGetStatusToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_TYPE:
				writeGetTaskTypeToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END_DATE_DAY:
				writeGetTaskEndDateDayToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END_DATE_MONTH:
				writeGetTaskEndDateMonthToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END_DATE_YEAR:
				writeGetTaskEndDateYearToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END_DATE_HOUR:
				writeGetTaskEndDateHourToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END_DATE_MINUTE:
				writeGetTaskEndDateMinuteToStringBuffer(task, taskObjectBuffer);
				break;
			case CASE_TASK_END:
				break;
			default:
				break;
		}
		taskObjectBuffer.append(constants.getNewLine());
		return taskObjectBuffer;
	}

	private StringBuffer initializeTempBufferLine(int taskObjectLine) {
		StringBuffer tempBuffer = new StringBuffer(constants.getEmptyString());
		tempBuffer.append(constants.getTaskObjectTitle(taskObjectLine));
		tempBuffer.append(constants.getSpace());
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