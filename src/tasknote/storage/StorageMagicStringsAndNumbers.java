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
														, ""
														, "firstDayOfWeek:"
														, "timeZoneID:"
														, "isDayLightTimeOn:"
														, ""};
	
	private final String[] STRING_TASK_STATUS = {"TASK_DEFAULT",
												 "TASK_OUTSTANDING",
												 "TASK_COMPLETED",
												 "TASK_INVALID_STORAGE"};
	
	private final String STRING_SPACE = " ";
	private final String STRING_NEWLINE = "\n";
	private final String STRING_EMPTY_STRING = "";
	private final String STRING_SLASH = "/";
	private final String STRING_PATH_SLASH = "\\";
	private final String STRING_PATH_DIVISION = "\\\\";
	private final String STRING_TEXT_FILE_END = ".txt";
	
	/*
	private final static String LOCALE_MONDAY = "Monday";
	private final static String LOCALE_SUNDAY = "Sunday";
	private final static String LOCALE_LANGAUGE = "en"; //assumption since we are not displaying other languages other than English
	private final static int LOCALE_US = 1;
	private final static int LOCALE_GB = 2;
	*/
	
	/**
	 * file name
	 */
	private final String defaultFileName = "taskContents.txt";
	private final String defaultPathFileName = "pathContents.txt";
	
	private static final String FORMAT_PATH_NAME = "%1$s%2$s";
	
	/**
	 * Magic Integers
	 */
	private final int SUM_OF_TASKOBJECT_ITEMS = 14;
	private final int SUM_OF_TASK_STATUS = 4;
	private final int BUFFERSIZE = 32768;
	
	public StorageMagicStringsAndNumbers(){}
	
	/*
	public String getLanguage(){
		return LOCALE_LANGAUGE;
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
	*/
	
	public String getTaskObjectTitle(int index){
		return STRING_TASKOBJECT[index];
	}
	
	public String getTaskStatus(int index){
		return STRING_TASK_STATUS[index];
	}
	
	public String getSpace(){
		return STRING_SPACE;
	}
	
	public String getNewLine(){
		return STRING_NEWLINE;
	}
	
	public String getEmptyString(){
		return STRING_EMPTY_STRING;
	}
	
	public String getSlash(){
		return STRING_SLASH;
	}
	
	public String getPathSlash(){
		return STRING_PATH_SLASH;
	}
	
	public String getPathDivision(){
		return STRING_PATH_DIVISION;
	}
	
	public String getTextFileEnding(){
		return STRING_TEXT_FILE_END;
	}
	
	public String produceFullPathName(String pathName, String fileName){
		return String.format(FORMAT_PATH_NAME, pathName, fileName);
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