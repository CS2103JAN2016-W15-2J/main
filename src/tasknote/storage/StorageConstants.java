//@@author A0126172M
package tasknote.storage;

/**
 * StorageConstants stores magic strings and numbers required for Storage
 */
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
	 * Magic Strings
	 */
	private final String STRING_SPACE = " ";
	private final String STRING_NEWLINE = "\n";
	private final String STRING_EMPTY_STRING = "";
	private final String STRING_SLASH = "/";
	private final String STRING_PATH_SLASH = "\\";
	private final String STRING_PATH_DIVISION = "\\\\";
	private final String STRING_TEXT_FILE_END = ".txt";
	private final String STRING_ALIAS_PAIR = "%1$s %2$s\n";
	private final String STRING_NULL = "null";
	
	/**
	 * Logging messages
	 */
	private final String LOGGING_MESSAGE_FAILED_PATH_CHANGE = "Invalid PATH. PATH trace entered: %1$s.";
	private final String LOGGING_MESSAGE_FAILED_UNDO = "This is the maximum PATH history you can undo.";
	private final String LOGGING_MESSAGE_FAILED_REDO = "This is the maximum PATH history you can redo.";
	private final String LOGGING_MESSAGE_FAILED_ALIAS_SAVE = "Failed to save modified alias.";
	private final String LOGGING_MESSAGE_FAILED_TO_FIND_ALIAS_FILE = "Failed to find alias file.";
	private final String LOGGING_MESSAGE_FAILED_TO_READ_PATH_FROM_FILE = "Failed to read path from path file.";
	private final String LOGGING_MESSAGE_FAILED_TO_WRITE_PATH_TO_FILE = "Failed to write path into path file.";
	private final String LOGGING_MESSAGE_STORAGE_MANIPULATOR_NOT_INITIALIZED = "Storage Manipulator not intialized. Failed I/O.";
	private final String LOGGING_MESSAGE_FAILED_TO_READ_FROM_TEXT_FILE = "Failed to read from textfile.";
	private final String LOGGING_MESSAGE_INVALID_PATH = "Invalid PATH: %1$s.";		
	private final String LOGGING_MESSAGE_FAILED_TO_CREATE_FILE = "Failed to create new file: %1$s.";
	private final String LOGGING_MESSAGE_FAILED_TO_CLOSE_ALIAS_READ = "Failed to close alias read.";
	private final String LOGGING_MESSAGE_FAILED_TO_READ_FROM_ALIAS_FILE = "Failed to read alias from alias file.";
	
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
	private final int BUFFERSIZE = 32768;
	private final int INTEGER_MONTH_NORMALIZE_CONSTANT = 1;
	private final int INTEGER_ARRAY_NORMALIZE_INDEX = 1;
	private final int INTEGER_PAIR_COUNT = 2;
	private final int INTEGER_ALIAS_COMMAND_INDEX = 0;
	private final int INTEGER_COMMAND_INDEX = 1;
	private final int INTEGER_ZERO = 0;
	
	public StorageConstants(){}
	
	public String getTaskObjectTitle(int index){
		return STRING_TASKOBJECT[index];
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
	
	public String getAliasPair(String aliasCommand, String command) {
		return String.format(STRING_ALIAS_PAIR, aliasCommand,command);
	}
	
	public Object getNullString() {
		return STRING_NULL;
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
	
	public String getFailedToReadPathFile(){
		return LOGGING_MESSAGE_FAILED_TO_READ_PATH_FROM_FILE;
	}
	
	public String getFailedToStorePathFile(){
		return LOGGING_MESSAGE_FAILED_TO_WRITE_PATH_TO_FILE;
	}
	
	public String getStorageManipulatorNotInitialized(){
		return LOGGING_MESSAGE_STORAGE_MANIPULATOR_NOT_INITIALIZED;
	}
	
	public String getFailedToReadFromTextFile(){
		return LOGGING_MESSAGE_FAILED_TO_READ_FROM_TEXT_FILE;
	}
	
	public String getFailedValidPathUsed(String path){
		return String.format(LOGGING_MESSAGE_INVALID_PATH, path);
	}
	
	public String getFailedToCreateNewFile(String file){
		return String.format(LOGGING_MESSAGE_FAILED_TO_CREATE_FILE, file);
	}
	
	public String getFailedToCloseRead() {
		return LOGGING_MESSAGE_FAILED_TO_CLOSE_ALIAS_READ;
	}
	
	public String getFailedToReadFromAliasFile() {
		return LOGGING_MESSAGE_FAILED_TO_READ_FROM_ALIAS_FILE;
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
	
	public String addFileNameToPath(String pathName, String fileName){
		return String.format(FORMAT_PATH_NAME, pathName, fileName);
	}

	public int getNormalizedMonth(int month) {
		return month + INTEGER_MONTH_NORMALIZE_CONSTANT;
	}

	public int getLastIndexOfArray(int length) {
		return length - INTEGER_ARRAY_NORMALIZE_INDEX;
	}

	public int getPairCount() {
		return INTEGER_PAIR_COUNT;
	}

	public int getAliasCommandIndex() {
		return INTEGER_ALIAS_COMMAND_INDEX;
	}

	public int getCommandIndex() {
		return INTEGER_COMMAND_INDEX;
	}

	public int getMaximumNonPositiveValue() {
		return INTEGER_ZERO;
	}
}