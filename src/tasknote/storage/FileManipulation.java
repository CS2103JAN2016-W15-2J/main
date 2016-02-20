package tasknote.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
	 * @param 
	 *
	 */
	public ArrayList<TaskObject> getTasks(){
		String string = "";
		return storageManipulator.convertStringToList(string);
	}
	
	/**
	 * This method get ArrayList/<TaskObject/> from logic and write into file
	 *
	 * @param 
	 * @param 
	 * @throws IOException 
	 *
	 */
	public boolean writeTasks(ArrayList<TaskObject> overrideTasks) throws IOException{
		String stringToFile = storageManipulator.convertListToString(overrideTasks);
		writeToFile(stringToFile);
		return true;
	}
	
	private boolean writeToFile(String stringToFile) throws IOException {
		// TODO Auto-generated method stub
		throw new IOException();
		return false;
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
