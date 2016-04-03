package tasknote.storage;

public class StorageConstants{
	
	/**
	 * Magic Strings for TaskObject
	 */
	private final String[] STRING_TASKOBJECT = {  "taskName:"
												, "dateDay:"
												, "dateMonth:"
												, "dateYear:"
												, "dateHour:"
												, "dateMinute:"
												, "duration:"
												, "location:"
												, "notifyTime:"
												, "taskStatus:"
												, "taskType:"
												, "endDateDay:"
												, "endDateMonth:"
												, "endDateYear:"
												, "endDateHour:"
												, "endDateMinute:"
												, "" };
	
	/**
	 * Task Status
	 */
	private final String[] STRING_TASK_STATUS = {"TASK_DEFAULT",
												 "TASK_OUTSTANDING",
												 "TASK_COMPLETED",
												 "TASK_INVALID_STORAGE"};
	
	/**
	 * Magic Strings
	 */
	private final String STRING_SPACE = " ";
	private final String STRING_NEWLINE = "\n";
	private final String STRING_EMPTY_STRING = "";
	private final String STRING_SLASH = "/";
	private final String STRING_PATH_SLASH = "\\";
	private final String STRING_PATH_DIVISION = "\\\\";
	private final String STRING_CURRENT_DIRECTORY = ".";
	private final String STRING_PARENT_DIRECTORY = "..";
	private final String STRING_TEXT_FILE_END = ".txt";
	
	/**
	 * Logging messages
	 */
	private final String LOGGING_MESSAGE_FAILED_PATH_CHANGE = "Invalid PATH. PATH trace entered: %1$s.";
	private final String LOGGING_MESSAGE_FAILED_UNDO = "This is the maximum PATH history you can undo.";
	private final String LOGGING_MESSAGE_FAILED_REDO = "This is the maximum PATH history you can redo.";
	private final String LOGGING_MESSAGE_FAILED_ALIAS_SAVE = "Failed to save modified alias.";
	private final String LOGGING_MESSAGE_FAILED_TO_FIND_ALIAS_FILE = "Failed to find alias file.";
	
	/**
	 * file/path name
	 */
	private final String DEFAULT_FILE_NAME = "taskContents.txt";
	private final String DEFAULT_PATH_FILE_NAME = "pathContents.txt";
	private final String DEFAULT_ALIAS_FILE_NAME = "aliasContents.txt";
	
	private final String FORMAT_PATH_NAME = "%1$s%2$s";
	
	/**
	 * Magic Integers
	 */
	private final int SUM_OF_TASKOBJECT_ITEMS = 17;
	private final int SUM_OF_TASK_STATUS = 4;
	private final int BUFFERSIZE = 32768;
	
	public StorageConstants(){}
	
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
	
	public String getCurrentDirectory(){
		return STRING_CURRENT_DIRECTORY;
	}
	
	public String getParentDirectory(){
		return STRING_PARENT_DIRECTORY;
	}
	
	public String getTextFileEnding(){
		return STRING_TEXT_FILE_END;
	}
	
	public String getFailedPathChange(){
		return LOGGING_MESSAGE_FAILED_PATH_CHANGE;
	}
	
	public String getFailedUndo(){
		return LOGGING_MESSAGE_FAILED_UNDO;
	}
	
	public String getFailedRedo(){
		return LOGGING_MESSAGE_FAILED_REDO;
	}
	
	public String getFailedAliasSave(){
		return LOGGING_MESSAGE_FAILED_ALIAS_SAVE;
	}
	
	public String getFailedToFindAliasFile(){
		return LOGGING_MESSAGE_FAILED_TO_FIND_ALIAS_FILE;
	}
	
	public String getFileName(){
		return DEFAULT_FILE_NAME;
	}
	
	public String getPathFileName(){
		return DEFAULT_PATH_FILE_NAME;
	}
	
	public String getAliasFileName(){
		return DEFAULT_ALIAS_FILE_NAME;
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
	
	public String addFileNameToPath(String pathName, String fileName){
		return String.format(FORMAT_PATH_NAME, pathName, fileName);
	}
}