package tasknote.storage;

public class StorageMagicStringsAndNumbers{
	
	/**
	 * Magic Strings
	 */
	private final static String[] STRING_TASKOBJECT = { "taskName:"
														, "dateDay:"
														, "dateMonth:"
														, "dateYear:"
														, "dateHour:"
														, "dateMinute:"
														, "duration:"
														, "location:"
														, "notifyTime:"
														, "isNotified:"
														, "taskStatus:"
														, "taskType:"
														, "isMarkedDone:"
														, "firstDayOfWeek:"
														, "timeZoneID:"
														, "isDayLightTimeOn:"
														, ""};
	private final static String[] STRING_TASK_STATUS = {"TASK_DEFAULT",
														"TASK_OUTSTANDING",
														"TASK_COMPLETED",
														"TASK_INVALID_STORAGE"};
	
	private final static String SPACE = " ";
	private final static String NEWLINE = "\n";
	
	private final static String LOCALE_MONDAY = "Monday";
	private final static String LOCALE_SUNDAY = "Sunday";
	private final static String LOCALE_LANGAUGE = "en"; //assumption since we are not displaying other languages other than English
	/**
	 * file name
	 */
	private final String defaultFileName = "taskContents.txt";
	private final String defaultPathFileName = "pathContents.txt";
	
	/**
	 * Magic Integers
	 */
	private final static int SUM_OF_TASKOBJECT_ITEMS = 17;
	private final static int SUM_OF_TASK_STATUS = 4;
	private final static int BUFFERSIZE = 32768;
	private final static int LOCALE_US = 1;
	private final static int LOCALE_GB = 2;
	
	public StorageMagicStringsAndNumbers(){}
	
	public String getLanguage(){
		return LOCALE_LANGAUGE;
	}
	
	public String getTaskObjectTitle(int index){
		return STRING_TASKOBJECT[index];
	}
	
	public String getTaskStatus(int index){
		return STRING_TASK_STATUS[index];
	}
	
	public int getSundayFirstDayOfWeek(){
		return LOCALE_US;
	}
	
	public int getMondayFirstDayOfWeek(){
		return LOCALE_GB;
	}
	
	public String getStringOfFirstDayOfWeek(int localeValue){
		switch(localeValue){
			case LOCALE_US:
				return LOCALE_SUNDAY;
			case LOCALE_GB:
				return LOCALE_MONDAY;
		}
		return "";
	}
	
	public String getSpace(){
		return SPACE;
	}
	
	public String getNewLine(){
		return NEWLINE;
	}
	
	public int getBufferSize(){
		return BUFFERSIZE;
	}
	
	public int getTotalTitles(){
		return SUM_OF_TASKOBJECT_ITEMS;
	}
	
	public int getTotalTaskStatus(){
		return SUM_OF_TASK_STATUS;
	}
	
	public String getFileName(){
		return defaultFileName;
	}
	
	public String getPathFileName(){
		return defaultPathFileName;
	}
}