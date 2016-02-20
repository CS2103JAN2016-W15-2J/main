package tasknote.storage;

import java.io.File;
import java.io.IOException;

public class StorageMagicStringsAndNumbers{
	
	/**
	 * Magic Strings
	 */
	private final static String[] STRING_TASKOBJECT = { "taskName:"
														+ "taskObjectCalendar:"
														+ "dateDay:"
														+ "dateMonth:"
														+ "dateYear:"
														+ "dateHour:"
														+ "dateMinute:"
														+ "duration:"
														+ "location:"
														+ "notifyTime:"
														+ "isNotified:"
														+ "taskColor:"
														+ "taskType:"
														+ "isMarkedDone:" };
	/**
	 * file name
	 */
	private String fileName = "taskContents.txt";
	
	/**
	 * Magic Integers
	 */
	private final static int SUM_OF_TASKOBJECT_ITEMS = 14;
	private final static int TASK_NUMBER_TO_TRIGGER_READ_AND_WRITE = 1;
	private final static int BUFFERSIZE = 32768;
	private final static int ZERO = 0;
	
	public StorageMagicStringsAndNumbers(){}
	
	public String getTaskObjectTitle(int index){
		return STRING_TASKOBJECT[index];
	}
	
	public int getTaskNumberToTriggerObjectReadAndWrite(){
		return TASK_NUMBER_TO_TRIGGER_READ_AND_WRITE;
	}
	
	public int getBufferSize(){
		return BUFFERSIZE;
	}
	
	public int getZero(){
		return ZERO;
	}
	
	public int getTotalTitles(){
		return SUM_OF_TASKOBJECT_ITEMS;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public boolean canChangeFileName(String fileName){
		if(isFileNameAcceptable(fileName)){
			this.fileName = fileName;
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isFileNameAcceptable(String tempFileName){
		File tempFile = new File(tempFileName);
		try{
			tempFile.getCanonicalPath();
			return true;
		}catch(IOException e){}
		return false;
	}
}