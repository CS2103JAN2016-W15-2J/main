package tasknote.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;

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
		for(int index = magicValuesRetriever.getZero(); index < magicValuesRetriever.getTaskNumberToTriggerObjectReadAndWrite(); ++index){
			storeItemIntoTaskObject(index, linesInTask[index], returnObject);
		}
		return returnObject;
	}
	
	private void storeItemIntoTaskObject(int index, String string, TaskObject returnObject) throws IOException, ClassNotFoundException {
		String[] content;
		if(string == null){
			return;
		}else{
			content = string.split(magicValuesRetriever.getTaskObjectTitle(index));
		}
		switch(index){
			case 0:
				returnObject.setTaskName(content[1].trim());
				break;
			case 1:
				ObjectInputStream objectReader = new ObjectInputStream(new ByteArrayInputStream(content[1].trim().getBytes()));
				GregorianCalendar tempCalendarObject = (GregorianCalendar) objectReader.readObject();
				returnObject.setTaskObjectCalendar(tempCalendarObject);
				objectReader.close();
				break;
			case 2:
				returnObject.setDateDay(Integer.parseInt(content[1]));
				break;
			case 3:
				returnObject.setDateMonth(Integer.parseInt(content[1]));
				break;
			case 4:
				returnObject.setDateYear(Integer.parseInt(content[1]));
				break;
			case 5:
				returnObject.setDateHour(Integer.parseInt(content[1]));
				break;
			case 6:
				returnObject.setDateMinute(Integer.parseInt(content[1]));
				break;
			case 7:
				returnObject.setDuration(Integer.parseInt(content[1]));
				break;
			case 8:
				returnObject.setLocation(content[1].trim());
				break;
			case 9:
				returnObject.setNotifyTime(Long.parseLong(content[1]));
				break;
			case 10:
				if(content[1].trim().equalsIgnoreCase("true")){
					returnObject.setIsNotified(true);
				}else{
					returnObject.setIsNotified(false);
				}
				break;
			case 11:
				returnObject.setTaskColor(content[1].trim());
				break;
			case 12:
				returnObject.setTaskType(content[1].trim());
				break;
			case 13:
				if(content[1].trim().equalsIgnoreCase("true")){
					returnObject.setIsMarkedDone(true);
				}else{
					returnObject.setIsMarkedDone(false);
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
		
		for(int index = magicValuesRetriever.getZero(); index < magicValuesRetriever.getTaskNumberToTriggerObjectReadAndWrite(); ++index){
			convertedString.append(extractItemFromTaskObject(index, task));
		}
		
		return convertedString.toString();
	}
	
	private StringBuffer extractItemFromTaskObject(int index, TaskObject task){
		StringBuffer tempBuffer = new StringBuffer("");
		tempBuffer.append(magicValuesRetriever.getTaskObjectTitle(index));
		tempBuffer.append(magicValuesRetriever.getSpace());
		switch(index){
			case 0:
				tempBuffer.append(task.getTaskName());
				break;
			case 1:
				tempBuffer.append(task.getTaskObjectCalendar());
				break;
			case 2:
				tempBuffer.append(task.getDateDay());
				break;
			case 3:
				tempBuffer.append(task.getDateMonth());
				break;
			case 4:
				tempBuffer.append(task.getDateYear());
				break;
			case 5:
				tempBuffer.append(task.getDateHour());
				break;
			case 6:
				tempBuffer.append(task.getDateMinute());
				break;
			case 7:
				tempBuffer.append(task.getDuration());
				break;
			case 8:
				tempBuffer.append(task.getLocation());
				break;
			case 9:
				tempBuffer.append(task.getNotifyTime());
				break;
			case 10:
				tempBuffer.append(task.getIsNotified());
				break;
			case 11:
				tempBuffer.append(task.getTaskColor());
				break;
			case 12:
				tempBuffer.append(task.getTaskType());
				break;
			case 13:
				tempBuffer.append(task.getIsMarkedDone());
				break;
			default:
				break;
		}
		tempBuffer.append(magicValuesRetriever.getNewLine());
		return tempBuffer;
	}
}