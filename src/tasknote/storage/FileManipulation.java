package tasknote.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import tasknote.shared.TaskListIOException;
import tasknote.shared.TaskObject;

public class FileManipulation{
	private static File textFile;
	
	private StorageMagicStringsAndNumbers magicValuesRetriever;
	private StorageConversion storageManipulator;

	/**
	 * Constructor to initialize file
	 */
	public FileManipulation(){
		magicValuesRetriever = new StorageMagicStringsAndNumbers();
		storageManipulator = new StorageConversion();
		initializeFile();
		if(isFileNotExist()){
			createNewFile();
		}
	}

	private void initializeFile(){
		textFile = new File(magicValuesRetriever.getFileName());
	}

	private boolean isFileNotExist(){
		return !textFile.exists();
	}

	private void createNewFile(){
		try{
			textFile.createNewFile(); 
		}catch(IOException e){
			// todo
		}
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
				String[] objectRead = new String[magicValuesRetriever.getTotalTitles()];
				for(int index = magicValuesRetriever.getZero(); index < magicValuesRetriever.getTotalTitles(); ++index){
					objectRead[index] = fileReader.readLine();
					if(objectRead[index] == null){
						throw new NullPointerException();
					}
				}
				returnTaskList.add(storageManipulator.convertStringToTaskObject(objectRead));
			}
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(NullPointerException npe){
			
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
		for(int index = magicValuesRetriever.getZero(); index < overrideTasks.size(); ++index){
			String stringToWriteToFile = storageManipulator.convertTaskObjectToString(overrideTasks.get(index));
			writeToFile(stringToWriteToFile);
		}
	}
	
	private void writeToFile(String stringToFile) throws TaskListIOException{
		try{
			//initialize
			byte[] bufferMemory = stringToFile.getBytes();
			int totalNumberOfBytesToWrite = bufferMemory.length;
			int maxWriteLength = magicValuesRetriever.getBufferSize();
			BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(textFile,true));
			
			loopWriteOneObjectToFile(bufferMemory, totalNumberOfBytesToWrite, maxWriteLength, fileWriter);
			
			fileWriter.close();
		}catch(IOException ioe){
			throw new TaskListIOException();
		}
	}

	private void loopWriteOneObjectToFile(byte[] bufferMemory, int totalNumberOfBytesToWrite, int maxWriteLength,
			BufferedOutputStream fileWriter) throws IOException{
		//while(isPositive(totalNumberOfBytesToWrite)){
			fileWriter.write(bufferMemory,magicValuesRetriever.getZero(),bufferMemory.length);
			fileWriter.flush();
		//}
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
	public boolean canChangeFileName(String newFileName){
		if(magicValuesRetriever.canChangeFileName(newFileName)){
			copyFileAndDeletePrevious(newFileName);
			return true;
		}else{
			return false;
		}
	}

	private void copyFileAndDeletePrevious(String newFileName) {
		try{
			
			//initialize
			File tempFile = new File(newFileName);
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(textFile));
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
			
			copyFileContents(inputStream, outputStream);
			deleteOldFile();
			setNewFileEnvironment();
			closeStream(inputStream,outputStream);
			
		}catch(IOException e){
			// do nothing
		}
	}

	private void copyFileContents(BufferedInputStream inputStream, BufferedOutputStream outputStream)
			throws IOException{
		byte[] bufferMemory = new byte[magicValuesRetriever.getBufferSize()];

		int length = inputStream.read(bufferMemory);

		while (isPositive(length)) {
			outputStream.write(bufferMemory, magicValuesRetriever.getZero(), length);
			length = inputStream.read(bufferMemory);
		}
	}
	
	private void setNewFileEnvironment(){
		initializeFile();
	}

	private void deleteOldFile(){
		textFile.delete();
	}

	private boolean isPositive(int length){
		return length > magicValuesRetriever.getZero();
	}

	private void closeStream(BufferedInputStream inStream, BufferedOutputStream outStream){
		try{
			inStream.close();
			outStream.close();
		}catch(IOException e){
			// do nothing
		}
	}
}
