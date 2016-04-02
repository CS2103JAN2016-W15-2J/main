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
import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

public class FileManipulation{
	
	private StorageConstants constants;
	private StorageConversion storageManipulator;
	
	private static File textFile;
	private static File pathFile;
	
	private String textFileName;

	/**
	 * Constructor to initialize file
	 */
	public FileManipulation(){
		initializeFamilyClasses();
		initializeFiles();
		createFileIfNotExist();
	}
	
	public String getTextFileName(){
		return textFileName;
	}

	private void initializeFamilyClasses() {
		constants = new StorageConstants();
		storageManipulator = new StorageConversion();
	}
	
	private void initializeFiles(){
		initializePathFile();
		String fileName = extractCanonicalFileName();
		fileName = handleEmptyFileNameExtracted(fileName);
		initializeTextFile(fileName);
		storeNewTextFilePath();
	}

	private String handleEmptyFileNameExtracted(String fileName) {
		if(isNullString(fileName)){
			fileName = constants.getFileName();
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

	public String extractTextFileName(String fileName) {
		String[] pathListNameForWindows = fileName.split(constants.getPathDivision());
		String[] pathListNameForMac = fileName.split(constants.getSlash());
		if(pathListNameForWindows[getLastIndexOfArray(pathListNameForWindows)].length() < pathListNameForMac[getLastIndexOfArray(pathListNameForMac)].length()){
			return pathListNameForWindows[getLastIndexOfArray(pathListNameForWindows)];
		}
		return pathListNameForMac[getLastIndexOfArray(pathListNameForMac)];
	}

	private int getLastIndexOfArray(String[] pathListName) {
		return pathListName.length-1;
	}

	private void initializePathFile() {
		pathFile = new File(getDefaultPathFileName());
	}

	private String getDefaultPathFileName() {
		return constants.getPathFileName();
	}
	
	private String extractCanonicalFileName(){
		if(isFileNotExist(pathFile)){
			return constants.getFileName();
		}
		return readFullPathFromPathFile();
	}

	public String readFullPathFromPathFile() {
		try{
			BufferedReader fileReader = initializeFileReader();
			String fileName = fileReader.readLine();
			fileReader.close();
			return fileName;
		}catch(IOException ioe){}
		return null;
	}

	private BufferedReader initializeFileReader() throws FileNotFoundException {
		return new BufferedReader(initializePathFileReader());
	}

	private FileReader initializePathFileReader() throws FileNotFoundException {
		return new FileReader(pathFile);
	}

	private void createFileIfNotExist() {
		createPathFileIfNotExist();
		createTextFileIfNotExist();
	}

	private void createPathFileIfNotExist() {
		if(isFileNotExist(pathFile)){
			createNewFile(pathFile);
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
			byte[] textFileCanonicalPathName = textFile.getCanonicalPath().getBytes();
			fileWriter.write(textFileCanonicalPathName,0,textFileCanonicalPathName.length);
			fileWriter.close();
		}catch(IOException ioe){
			
		}
	}
	
	private OutputStream initializePathFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(pathFile);
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

	private void loopToGetFullTaskList(ArrayList<TaskObject> returnTaskList,
			BufferedReader fileReader) throws IOException,TaskListIOException, NullPointerException{
		try{
			while(true){
				String[] objectRead = new String[constants.getTotalTitles()];
				iterateOnceToStoreOneObject(fileReader, objectRead);
				returnTaskList.add(storageManipulator.convertStringToTaskObject(objectRead));
			}
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(NullPointerException npe){
			
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
	
	public boolean changeFileName(String fileName){
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
			System.out.println(new String(bufferMemory));
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
