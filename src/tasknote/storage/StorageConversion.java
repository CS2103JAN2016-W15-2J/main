package tasknote.storage;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
	 * 
	 * @param tasks
	 * @return ArrayList<TaskObject>
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public TaskObject convertStringToTaskObject(String[] linesInTask) throws ClassNotFoundException, IOException{
		TaskObject returnObject = new TaskObject();
		
		for(int index = magicValuesRetriever.getZero(); index < magicValuesRetriever.getTotalTitles(); ++index){
			storeItemIntoTaskObject(index, linesInTask[index], returnObject);
		}
		GregorianCalendar returnCalendar = returnObject.getTaskObjectCalendar();
		returnCalendar.set(returnObject.getDateYear(), returnObject.getDateMonth(), returnObject.getDateDay(), returnObject.getDateHour(), returnObject.getDateMinute());
		
		return returnObject;
	}
	
	private void storeItemIntoTaskObject(int index, String string, TaskObject returnObject) throws IOException, ClassNotFoundException {
		String[] content;
		if(string == null){
			return;
		}else{
			content = string.split(magicValuesRetriever.getTaskObjectTitle(index));
		}
		if(content.length == 1){
			return;
		}
		switch(index){
			case 0:
				returnObject.setTaskName(content[1].trim());
				break;
			case 1:
				returnObject.setDateDay(Integer.parseInt(content[1].trim()));
				break;
			case 2:
				returnObject.setDateMonth(Integer.parseInt(content[1].trim()));
				break;
			case 3:
				returnObject.setDateYear(Integer.parseInt(content[1].trim()));
				break;
			case 4:
				returnObject.setDateHour(Integer.parseInt(content[1].trim()));
				break;
			case 5:
				returnObject.setDateMinute(Integer.parseInt(content[1].trim()));
				break;
			case 6:
				returnObject.setDuration(Integer.parseInt(content[1].trim()));
				break;
			case 7:
				returnObject.setLocation(content[1].trim());
				break;
			case 8:
				returnObject.setNotifyTime(Long.parseLong(content[1].trim()));
				break;
			case 9:
				if(content[1].trim().equalsIgnoreCase("true")){
					returnObject.setIsNotified(true);
				}else{
					returnObject.setIsNotified(false);
				}
				break;
			case 10: //note is enum!
				String taskStatus = content[1].trim();
				for(int indexEnum = magicValuesRetriever.getZero(); indexEnum < magicValuesRetriever.getTotalTaskStatus(); ++indexEnum){
					if(taskStatus.equalsIgnoreCase(magicValuesRetriever.getTaskStatus(indexEnum))){
						returnObject.setTaskStatus(magicValuesRetriever.getTaskStatus(indexEnum));
					}
				}
				break;
			case 11:
				returnObject.setTaskType(content[1].trim());
				break;
			case 12:
				if(content[1].trim().equalsIgnoreCase("true")){
					returnObject.setIsMarkedDone(true);
				}else{
					returnObject.setIsMarkedDone(false);
				}
				break;
			case 13:
				String firstDayOfWeek = content[1].trim();
				if(firstDayOfWeek.equalsIgnoreCase(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()))){
					Locale locale = new Locale(magicValuesRetriever.getLanguage(),magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()));
					returnObject.setTaskObjectCalendar(new GregorianCalendar(locale));
				}else if(firstDayOfWeek.equalsIgnoreCase(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()))){
					Locale locale = new Locale(magicValuesRetriever.getLanguage(),magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()));
					returnObject.setTaskObjectCalendar(new GregorianCalendar(locale));
				}
				break;
			case 14:
				returnObject.getTaskObjectCalendar().getTimeZone().setID(content[1].trim());;
				break;
			case 15:
				if(content[1].trim().equalsIgnoreCase("true")){
					returnObject.getTaskObjectCalendar().getTimeZone().useDaylightTime();
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 
	 * @param overrideTasks
	 * @return String to write into file
	 */
	public String convertTaskObjectToString(TaskObject task){
		StringBuffer convertedString = new StringBuffer("");
		GregorianCalendar taskCalendar = task.getTaskObjectCalendar();
		TimeZone taskTimeZone = taskCalendar.getTimeZone();
		
		for(int index = magicValuesRetriever.getZero(); index < magicValuesRetriever.getTotalTitles(); ++index){
			convertedString.append(extractItemFromTaskObject(index, task, taskCalendar, taskTimeZone));
		}
		
		return convertedString.toString();
	}
	
	private StringBuffer extractItemFromTaskObject(int index, TaskObject task, GregorianCalendar taskCalendar, TimeZone taskTimeZone){
		StringBuffer tempBuffer = new StringBuffer("");
		tempBuffer.append(magicValuesRetriever.getTaskObjectTitle(index));
		tempBuffer.append(magicValuesRetriever.getSpace());
		switch(index){
			case 0:
				tempBuffer.append(task.getTaskName());
				break;
			case 1:
				tempBuffer.append(task.getDateDay());
				break;
			case 2:
				tempBuffer.append(task.getDateMonth());
				break;
			case 3:
				tempBuffer.append(task.getDateYear());
				break;
			case 4:
				tempBuffer.append(task.getDateHour());
				break;
			case 5:
				tempBuffer.append(task.getDateMinute());
				break;
			case 6:
				tempBuffer.append(task.getDuration());
				break;
			case 7:
				tempBuffer.append(task.getLocation());
				break;
			case 8:
				tempBuffer.append(task.getNotifyTime());
				break;
			case 9:
				tempBuffer.append(task.getIsNotified());
				break;
			case 10:
				tempBuffer.append(task.getTaskStatus());
				break;
			case 11:
				tempBuffer.append(task.getTaskType());
				break;
			case 12:
				tempBuffer.append(task.getIsMarkedDone());
				break;
			case 13:
				if(taskCalendar.getFirstDayOfWeek() == magicValuesRetriever.getSundayFirstDayOfWeek()){
					tempBuffer.append(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getSundayFirstDayOfWeek()));
				}else if(taskCalendar.getFirstDayOfWeek() == magicValuesRetriever.getMondayFirstDayOfWeek()){
					tempBuffer.append(magicValuesRetriever.getStringOfFirstDayOfWeek(magicValuesRetriever.getMondayFirstDayOfWeek()));
				}
				break;
			case 14:
				tempBuffer.append(taskTimeZone.getID());
				break;
			case 15:
				tempBuffer.append(taskTimeZone.observesDaylightTime());
				break;
			default:
				break;
		}
		tempBuffer.append(magicValuesRetriever.getNewLine());
		return tempBuffer;
	}
}