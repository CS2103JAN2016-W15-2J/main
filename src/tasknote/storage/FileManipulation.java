package tasknote.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

//@author A0126172M
public class FileManipulation{
	
	private StorageConstants constants;
	private StorageConversion storageManipulator;
	
	private static File textFile;
	private static File pathFile;
	private static File aliasFile;
	
	private String textFileName;
	
	private static final Logger fileLog = Logger.getLogger(FileManipulation.class.getName());

	/**
	 * Constructor to initialize file and classes
	 */
	public FileManipulation(){
		initializeFamilyClasses();
		initializeFiles();
		createFileIfNotExist();
	}
	
	private void initializeFamilyClasses() {
		constants = new StorageConstants();
		storageManipulator = new StorageConversion();
	}
	
	private void initializeFiles(){
		initializePathFile();
		initializeAliasFile();
		String fileName = extractFileName();
		initializeTextFile(fileName);
		storeNewTextFilePath();
	}
	
	private void initializePathFile() {
		pathFile = new File(getDefaultPathFileName());
	}
	
	private String getDefaultPathFileName() {
		return constants.getPathFileName();
	}
	
	private void initializeAliasFile() {
		aliasFile = new File(getDefaultAliasFileName());
	}
	
	private String getDefaultAliasFileName() {
		return constants.getAliasFileName();
	}

	private String extractFileName() {
		String fileName = extractCanonicalFileName();
		fileName = handleEmptyFileNameExtracted(fileName);
		return fileName;
	}
	
	private String extractCanonicalFileName(){
		if(isFileNotExist(pathFile)){
			return constants.getFileName();
		}
		return readFullPathFromPathFile();
	}
	
	private void createFileIfNotExist() {
		createPathFileIfNotExist();
		createAliasFileIfNotExist();
		createTextFileIfNotExist();
	}

	private void createPathFileIfNotExist() {
		if(isFileNotExist(pathFile)){
			createNewFile(pathFile);
		}
	}
	
	private void createAliasFileIfNotExist() {
		if(isFileNotExist(aliasFile)){
			createNewFile(aliasFile);
		}
	}

	private void createTextFileIfNotExist() {
		if(isFileNotExist(textFile)){
			createNewFile(textFile);
			storeNewTextFilePath();
		}
	}

	private boolean isFileNotExist(File file){
		return !file.exists();
	}

	private void createNewFile(File file){
		try{
			file.createNewFile(); 
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	private void storeNewTextFilePath(){
		try{
			BufferedOutputStream fileWriter = new BufferedOutputStream(initializePathFileOutputStream());
			byte[] textFileCanonicalPathName = getByteArrayOfFullPath();
			fileWriter.write(textFileCanonicalPathName,0,textFileCanonicalPathName.length);
			fileWriter.close();
		}catch(IOException ioe){
			fileLog.log(Level.WARNING, constants.getFailedToStorePathFile());
		}
	}

	private byte[] getByteArrayOfFullPath() throws IOException {
		String fullPath = textFile.getCanonicalPath();
		return fullPath.getBytes();
	}
	
	private OutputStream initializePathFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(pathFile);
	}
	
	/**
	 * read full path of the current text file from pathFile
	 * @return String of current full path stored
	 */
	public String readFullPathFromPathFile() {
		try{
			return readPathFromPathFile();
		}catch(IOException ioe){
			fileLog.log(Level.WARNING, constants.getFailedToReadPathFile());
			return null;
		}
	}

	private String readPathFromPathFile() throws FileNotFoundException, IOException {
		BufferedReader fileReader = initializeFileReader();
		String fileName = fileReader.readLine();
		fileReader.close();
		return fileName;
	}
	
	private BufferedReader initializeFileReader() throws FileNotFoundException {
		return new BufferedReader(initializePathFileReader());
	}

	private FileReader initializePathFileReader() throws FileNotFoundException {
		return new FileReader(pathFile);
	}
	
	private String handleEmptyFileNameExtracted(String fileName) {
		if(isNullString(fileName)){
			return constants.getFileName();
		}
		return fileName;
	}
	
	private boolean isNullString(String content){
		return content == null;
	}
	
	private void initializeTextFile(String fileName) {
		textFile = new File(fileName);
		textFileName = extractTextFileName(fileName);
	}
	
	/**
	 * extract textFile name from full PATH string
	 * @param fullPath
	 * @return String textFile name
	 */
	public String extractTextFileName(String fullPath) {
		String[] pathListNameForWindows = fullPath.split(constants.getPathDivision());
		String[] pathListNameForMac = fullPath.split(constants.getSlash());
		if(isFileNameForWindows(pathListNameForWindows, pathListNameForMac)){
			return pathListNameForWindows[getLastIndexOfArray(pathListNameForWindows)];
		}
		return pathListNameForMac[getLastIndexOfArray(pathListNameForMac)];
	}

	private boolean isFileNameForWindows(String[] pathListNameForWindows, String[] pathListNameForMac) {
		return supposedTextFileLength(pathListNameForWindows) < supposedTextFileLength(pathListNameForMac);
	}

	private int supposedTextFileLength(String[] pathListName) {
		return pathListName[getLastIndexOfArray(pathListName)].length();
	}
	
	private int getLastIndexOfArray(String[] pathListName) {
		return pathListName.length-1;
	}
	
	/**
	 * get textFile name
	 * @return String textFile name
	 */
	public String getTextFileName(){
		return textFileName;
	}

	/**
	 * This method reads from file and converts it into ArrayList/<TaskObject/>
	 * to return to logic
	 *
	 * @param
	 * @return ArrayList<TaskObject>
	 * @throws IOException (implies reader got error)
	 * @throws TaskListIOException (implies file contents got error)
	 *
	 */
	public ArrayList<TaskObject> getTasks() throws IOException,TaskListIOException{
		ArrayList<TaskObject> returnTaskList = new ArrayList<TaskObject>();
		BufferedReader fileReader = new BufferedReader(new FileReader(textFile));
		loopToGetFullTaskList(returnTaskList, fileReader);
		fileReader.close();
		return returnTaskList;
	}
	
	/*
	private void loopToGetFullTaskList(ArrayList<TaskObject> returnTaskList,
			BufferedReader fileReader) throws IOException,TaskListIOException, NullPointerException{
		try{
			while(true){
				String[] objectRead = new String[constants.getTotalTitles()];
				iterateOnceToStoreOneObject(fileReader, objectRead);
				returnTaskList.add(storageManipulator.convertStringToTaskObject(objectRead));
			}
		}catch(ClassNotFoundException cnfe){
			fileLog.log(Level.WARNING, constants.getStorageManipulatorNotInitialized());
		}catch(IOException ioe){
			fileLog.log(Level.WARNING, constants.getFailedToReadFromTextFile());
		}catch(NullPointerException npe){
			//read success [NOT logged to avoid overcrowd console]
		}
	}

	private void iterateOnceToStoreOneObject(BufferedReader fileReader, String[] objectRead) throws IOException {
		for(int index = 0; index < constants.getTotalTitles(); ++index){
			objectRead[index] = fileReader.readLine();
			throwNullPointerExceptionIfNoMoreLinesToRead(objectRead, index);
		}
	}
	
	private void throwNullPointerExceptionIfNoMoreLinesToRead(String[] objectRead, int index) {
		if(isNullObject(objectRead, index)){
			throw new NullPointerException();
		}
	}
	
	private boolean isNullObject(String[] objectRead, int index) {
		return objectRead[index] == null;
	}
	*/
	
	private void loopToGetFullTaskList(ArrayList<TaskObject> returnTaskList,
			BufferedReader fileReader) throws IOException,TaskListIOException, NullPointerException{
		try{
			while(true){
				String[] objectRead = new String[constants.getTotalTitles()];
				int linesRead = iterateOnceToStoreOneObject(fileReader, objectRead);
				returnTaskList.add(storageManipulator.convertStringToTaskObject(objectRead, linesRead));
			}
		}catch(ClassNotFoundException cnfe){
			fileLog.log(Level.WARNING, constants.getStorageManipulatorNotInitialized());
		}catch(IOException ioe){
			fileLog.log(Level.WARNING, constants.getFailedToReadFromTextFile());
		}catch(NullPointerException npe){
			//read success [NOT logged to avoid overcrowd console]
		}
	}
	
	private int iterateOnceToStoreOneObject(BufferedReader fileReader, String[] objectRead) throws IOException{
		int numberOfLinesRead = 0;
		String lineRead = attemptToReadLineOrEndRead(fileReader);
		while(!lineRead.equals(constants.getEmptyString()) && !lineRead.startsWith(constants.getSpace()) && numberOfLinesRead < constants.getTotalTitles()){
			objectRead[numberOfLinesRead] = lineRead;
			++numberOfLinesRead;
			lineRead = attemptToReadLineOrEndRead(fileReader);
		}
		return numberOfLinesRead;
	}

	private String attemptToReadLineOrEndRead(BufferedReader fileReader) throws IOException {
		String lineRead = fileReader.readLine();
		throwNullPointerExceptionIfNoMoreLinesToRead(lineRead);
		return lineRead;
	}
	
	private void throwNullPointerExceptionIfNoMoreLinesToRead(String lineRead) {
		if(isNullString(lineRead)){
			fileLog.log(Level.FINE, "Successfully read from text file");
			throw new NullPointerException();
		}
	}
	
	/**
	 * This method get ArrayList/<TaskObject/> from logic and write into file
	 *
	 * @param 
	 * @param overrideTasks
	 * @throws TaskListIOException (implies something wrong with writing)
	 *
	 */
	public void writeTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException{
		for(int index = 0; index < overrideTasks.size(); ++index){
			String stringToWriteToFile = storageManipulator.convertTaskObjectToString(overrideTasks.get(index));
			writeToFile(stringToWriteToFile);
		}
	}
	
	private void writeToFile(String stringToFile) throws TaskListIOException{
		try{
			//initialize
			byte[] bufferMemory = stringToFile.getBytes();
			int totalNumberOfBytesToWrite = bufferMemory.length;
			int maxWriteLength = constants.getBufferSize();
			BufferedOutputStream fileWriter = new BufferedOutputStream(initializeContinuousTextFileOutputStream());
			
			loopWriteOneObjectToFile(bufferMemory, totalNumberOfBytesToWrite, maxWriteLength, fileWriter);
			
			fileWriter.close();
		}catch(IOException ioe){
			throw new TaskListIOException();
		}
	}

	private FileOutputStream initializeContinuousTextFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(textFile,true);
	}

	private void loopWriteOneObjectToFile(byte[] bufferMemory, int totalNumberOfBytesToWrite, int maxWriteLength,
			BufferedOutputStream fileWriter) throws IOException{
			fileWriter.write(bufferMemory,0,bufferMemory.length);
			fileWriter.flush();
	}
	
	public void writeAlias(HashMap<String,String> alias) throws IOException{
		BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(aliasFile));
		Map<String,String> aliasMap = alias;
		for(String command : aliasMap.keySet()){
			String aliasCommand = alias.get(command);
			String aliasPair = command + " " + aliasCommand + "\n";
			byte[] bufferMemory = aliasPair.getBytes();
			fileWriter.write(bufferMemory,0,bufferMemory.length);
			fileWriter.flush();
		}
		fileWriter.close();
	}
	
	public HashMap<String, String> readAliasFromAliasFile() throws FileNotFoundException{
		HashMap<String, String> alias = new HashMap<String, String>();
		BufferedReader read = new BufferedReader(new FileReader(aliasFile));
		try {
			while(true){
				String aliasLine = attemptToReadLineOrEndRead(read);
				String[] aliasPair = aliasLine.split(" ");
				if(aliasPair.length==2){
					String command = aliasPair[0];
					String aliasCommand = aliasPair[1];
					alias.put(command, aliasCommand);
				}
			}
		}catch(IOException ioe){
			
		}catch(NullPointerException npe){
		}
		
		try{
			read.close();
		}catch(IOException ioe){
			
		}
		return alias;
	}

	/**
	 * 
	 * [OPTIONAL]
	 * 
	 * This method attempts to change file name into a new one desired by the user
	 *
	 * @param	String desired new file name
	 * @return	boolean true for success copy of file and false otherwise
	 *
	 */
	
	public boolean moveFile(String fileName){
		if(isFileNameAcceptable(fileName)){
			copyFileAndDeletePrevious(fileName);
			initializeTextFile(fileName);
			storeNewTextFilePath();
			setNewFileEnvironment();
			return true;
		}
		return false;
	}
	
	private boolean isFileNameAcceptable(String tempFileName){
		File tempFile = new File(tempFileName);
		try{
			tempFile.getCanonicalPath();
			return true;
		}catch(IOException e){}
		return false;
	}

	private void copyFileAndDeletePrevious(String newFileName) {
		try{
			
			//initialize
			File tempFile = new File(newFileName);
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(textFile));
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
			
			copyFileContents(inputStream, outputStream);
			closeStream(inputStream,outputStream);
			deleteOldFile();
			
		}catch(IOException e){
			// do nothing
		}
	}

	private void copyFileContents(BufferedInputStream inputStream, BufferedOutputStream outputStream)
			throws IOException{
		
		byte[] bufferMemory = new byte[constants.getBufferSize()];
		int length = inputStream.read(bufferMemory);
		loopCopyFileContents(inputStream, outputStream, bufferMemory, length);
	
	}

	private void loopCopyFileContents(BufferedInputStream inputStream, BufferedOutputStream outputStream,
			byte[] bufferMemory, int length) throws IOException {
		
		while (isPositive(length)) {
			outputStream.write(bufferMemory, 0, length);
			length = inputStream.read(bufferMemory);
			outputStream.flush();
		}
	
	}
	
	private void setNewFileEnvironment(){
		initializeFiles();
	}

	private void deleteOldFile() throws IOException{
		Files.delete(textFile.toPath());
		textFile.delete();
	}

	private boolean isPositive(int length){
		return length > 0;
	}

	private void closeStream(BufferedInputStream inStream, BufferedOutputStream outStream){
		try{
			inStream.close();
			outStream.close();
		}catch(IOException e){
			// do nothing
		}
	}
	
	public void cleanFile() throws IOException{
		BufferedOutputStream fileWriter = new BufferedOutputStream(initializeTextFileOutputStream());
		fileWriter.close();
	}

	private FileOutputStream initializeTextFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(textFile);
	}
}
