//@@author A0126172M
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

/**
 * FileManipulation class focuses on reading and writing of the file
 */
public class FileManipulation {

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
	public FileManipulation() {
		initializeFamilyClasses();
		initializeFiles();
		createFilesIfNotExist();
	}

	private void initializeFamilyClasses() {
		constants = new StorageConstants();
		storageManipulator = new StorageConversion();
	}

	private void initializeFiles() {
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

	private String extractCanonicalFileName() {
		if (isFileInvalid(pathFile)) {
			return constants.getFileName();
		}
		return readFullPathFromPathFile();
	}

	private void createFilesIfNotExist() {
		createPathFileIfNotExist();
		createAliasFileIfNotExist();
		createTextFileIfNotExist();
	}

	private void createPathFileIfNotExist() {
		if (isFileInvalid(pathFile)) {
			createNewFile(pathFile);
		}
	}

	private void createAliasFileIfNotExist() {
		if (isFileInvalid(aliasFile)) {
			createNewFile(aliasFile);
		}
	}

	private void createTextFileIfNotExist() {
		if (isFileInvalid(textFile)) {
			createNewFile(textFile);
			storeNewTextFilePath();
		}
	}

	private boolean isFileInvalid(File file) {
		return !file.exists();
	}

	private void createNewFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			fileLog.log(Level.WARNING, constants.getFailedToCreateNewFile(file.toString()));
		}
	}

	private void storeNewTextFilePath() {
		try {
			BufferedOutputStream fileWriter = new BufferedOutputStream(initializePathFileOutputStream());
			writeCanonicalTextPathToPathFile(fileWriter);
			fileWriter.close();
		} catch (IOException ioe) {
			fileLog.log(Level.WARNING, constants.getFailedToStorePathFile());
		}
	}

	private void writeCanonicalTextPathToPathFile(BufferedOutputStream fileWriter) throws IOException {
		byte[] textFileCanonicalPathName = getByteArrayOfFullPath();
		fileWriter.write(textFileCanonicalPathName, 0, textFileCanonicalPathName.length);
	}

	private byte[] getByteArrayOfFullPath() throws IOException {
		String fullPath = textFile.getCanonicalPath();
		return fullPath.getBytes();
	}

	private OutputStream initializePathFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(pathFile);
	}

	/**
	 * read full path of the current text file from pathFile able to handle one
	 * round of empty path file read
	 * 
	 * @return String of current full path stored
	 */
	public String readFullPathFromPathFile() {
		try {
			return readPathFromPathFile();
		} catch (IOException ioe) {
			return handleEmptyPathFile();
		}
	}

	private String handleEmptyPathFile() {
		try {
			initializeFiles();
			return readPathFromPathFile();
		} catch (IOException ioe) {
			fileLog.log(Level.WARNING, constants.getFailedToReadPathFile());
			return null;
		}
	}

	private String readPathFromPathFile() throws FileNotFoundException, IOException {
		BufferedReader fileReader = initializeFileReader();
		String pathFile = fileReader.readLine();
		fileReader.close();
		return pathFile;
	}

	private BufferedReader initializeFileReader() throws FileNotFoundException {
		return new BufferedReader(initializePathFileReader());
	}

	private FileReader initializePathFileReader() throws FileNotFoundException {
		return new FileReader(pathFile);
	}

	private String handleEmptyFileNameExtracted(String fileName) {
		if (isNullString(fileName)) {
			return constants.getFileName();
		}
		return fileName;
	}

	private boolean isNullString(String content) {
		return content == null;
	}

	private void initializeTextFile(String fileName) {
		textFile = new File(fileName);
		if (isFileNameEntered(fileName)) {
			textFileName = extractTextFileName(fileName);
		}
	}

	private boolean isFileNameEntered(String fileName) {
		return fileName.endsWith(constants.getTextFileEnding());
	}

	/**
	 * extract textFile name from full PATH string
	 * 
	 * @param fullPath
	 * @return String textFile name
	 */
	public String extractTextFileName(String fullPath) {
		String[] pathListNameForWindows = fullPath.split(constants.getPathDivision());
		String[] pathListNameForMac = fullPath.split(constants.getSlash());
		if (isFileNameForWindows(pathListNameForWindows, pathListNameForMac)) {
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
		return constants.getLastIndexOfArray(pathListName.length);
	}

	/**
	 * get textFile name
	 * 
	 * @return String textFile name
	 */
	public String getTextFileName() {
		return textFileName;
	}

	/**
	 * This method reads from file and converts it into ArrayList/
	 * <TaskObject/> to return to logic
	 *
	 * @param
	 * @return ArrayList<TaskObject>
	 * @throws IOException
	 *             (implies reader got error)
	 * @throws TaskListIOException
	 *             (implies file contents got error)
	 *
	 */
	public ArrayList<TaskObject> getTasks() throws IOException, TaskListIOException {
		ArrayList<TaskObject> returnTaskList = new ArrayList<TaskObject>();
		BufferedReader fileReader = new BufferedReader(new FileReader(textFile));
		loopToGetFullTaskList(returnTaskList, fileReader);
		fileReader.close();
		return returnTaskList;
	}

	private void loopToGetFullTaskList(ArrayList<TaskObject> returnTaskList, BufferedReader fileReader)
			throws IOException, TaskListIOException, NullPointerException {
		try {
			while (true) {
				addOneTaskObjectRead(returnTaskList, fileReader);
			}
		} catch (ClassNotFoundException cnfe) {
			fileLog.log(Level.WARNING, constants.getStorageManipulatorNotInitialized());
		} catch (IOException ioe) {
			fileLog.log(Level.WARNING, constants.getFailedToReadFromTextFile());
			throw new TaskListIOException();
		} catch (NullPointerException npe) {
			// read success [NOT logged to avoid overcrowd console]
		}
	}

	private void addOneTaskObjectRead(ArrayList<TaskObject> returnTaskList, BufferedReader fileReader)
			throws IOException, ClassNotFoundException {
		String[] objectRead = new String[constants.getTotalTitles()];
		int linesRead = iterateOnceToStoreOneObject(fileReader, objectRead);
		returnTaskList.add(storageManipulator.convertStringToTaskObject(objectRead, linesRead));
	}

	private int iterateOnceToStoreOneObject(BufferedReader fileReader, String[] objectRead) throws IOException {
		int numberOfLinesRead = 0;
		String lineRead = attemptToReadLineOrEndRead(fileReader);
		while (isValidRead(numberOfLinesRead, lineRead)) {
			objectRead[numberOfLinesRead] = lineRead;
			++numberOfLinesRead;
			lineRead = attemptToReadLineOrEndRead(fileReader);
		}
		return numberOfLinesRead;
	}

	private boolean isValidRead(int numberOfLinesRead, String lineRead) {
		return !isEndOfTaskObjectRead(lineRead) && isWithinValidLinesRead(numberOfLinesRead);
	}

	private boolean isEndOfTaskObjectRead(String lineRead) {
		lineRead = lineRead.trim();
		return lineRead.equals(constants.getEmptyString());
	}

	private boolean isWithinValidLinesRead(int numberOfLinesRead) {
		return numberOfLinesRead < constants.getTotalTitles();
	}

	private String attemptToReadLineOrEndRead(BufferedReader fileReader) throws IOException {
		String lineRead = fileReader.readLine();
		throwNullPointerExceptionIfNoMoreLinesToRead(lineRead);
		return lineRead;
	}

	private void throwNullPointerExceptionIfNoMoreLinesToRead(String lineRead) {
		if (isNullString(lineRead)) {
			fileLog.log(Level.FINE, "Successfully read from text file");
			throw new NullPointerException();
		}
	}

	/**
	 * This method get ArrayList/<TaskObject/> from logic and write into file
	 *
	 * @param
	 * @param overrideTasks
	 * @throws TaskListIOException
	 *             (implies something wrong with writing)
	 *
	 */
	public void writeTasks(ArrayList<TaskObject> overrideTasks) throws TaskListIOException {
		for (int index = 0; index < overrideTasks.size(); ++index) {
			String stringToWriteToFile = storageManipulator.convertTaskObjectToString(overrideTasks.get(index));
			writeToFile(stringToWriteToFile);
		}
	}

	private void writeToFile(String stringToFile) throws TaskListIOException {
		try {
			byte[] bufferMemory = stringToFile.getBytes();
			BufferedOutputStream fileWriter = new BufferedOutputStream(initializeContinuousTextFileOutputStream());
			writeOneObjectToFile(bufferMemory, fileWriter);
			fileWriter.close();
		} catch (IOException ioe) {
			throw new TaskListIOException();
		}
	}

	private FileOutputStream initializeContinuousTextFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(textFile, true);
	}

	private void writeOneObjectToFile(byte[] bufferMemory, BufferedOutputStream fileWriter) throws IOException {
		fileWriter.write(bufferMemory, 0, bufferMemory.length);
		fileWriter.flush();
	}

	public void writeAlias(HashMap<String, String> alias) throws IOException {
		BufferedOutputStream fileWriter = new BufferedOutputStream(initializeAliasFileOutputStream());
		Map<String, String> aliasMap = alias;
		iterateAliasMapToWriteToFile(alias, fileWriter, aliasMap);
		fileWriter.close();
	}

	private FileOutputStream initializeAliasFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(aliasFile);
	}

	private void iterateAliasMapToWriteToFile(HashMap<String, String> alias, BufferedOutputStream fileWriter,
			Map<String, String> aliasMap) throws IOException {
		for (String aliasCommand : aliasMap.keySet()) {
			writeAliasPairToAliasFile(alias, fileWriter, aliasCommand);
		}
	}

	private void writeAliasPairToAliasFile(HashMap<String, String> alias, BufferedOutputStream fileWriter,
			String aliasCommand) throws IOException {
		String aliasPair = generateAliasPair(alias, aliasCommand);
		byte[] bufferMemory = aliasPair.getBytes();
		writeOneObjectToFile(bufferMemory, fileWriter);
	}

	private String generateAliasPair(HashMap<String, String> alias, String aliasCommand) {
		String command = alias.get(aliasCommand);
		return constants.getAliasPair(aliasCommand, command);
	}

	public HashMap<String, String> readAliasFromAliasFile() throws FileNotFoundException {
		HashMap<String, String> alias = new HashMap<String, String>();
		BufferedReader read = new BufferedReader(initializeAliasFileReader());
		try {
			loopReadLineToFillAlias(alias, read);
		} catch (IOException ioe) {
			fileLog.log(Level.WARNING, constants.getFailedToReadFromAliasFile());
		} catch (NullPointerException npe) {
			// read success [NOT logged to avoid overcrowd console]
		}
		closeRead(read);
		return alias;
	}

	private FileReader initializeAliasFileReader() throws FileNotFoundException {
		return new FileReader(aliasFile);
	}

	private void loopReadLineToFillAlias(HashMap<String, String> alias, BufferedReader read) throws IOException {
		while (true) {
			String aliasLine = attemptToReadLineOrEndRead(read);
			String[] aliasPair = extractAliasPair(aliasLine);
			addAliasPairToAlias(alias, aliasPair);
		}
	}

	private String[] extractAliasPair(String aliasLine) {
		return aliasLine.split(constants.getSpace());
	}

	private void addAliasPairToAlias(HashMap<String, String> alias, String[] aliasPair) {
		if (aliasPair.length == constants.getPairCount()) {
			String aliasCommand = aliasPair[constants.getAliasCommandIndex()];
			String command = aliasPair[constants.getCommandIndex()];
			alias.put(aliasCommand, command);
		}
	}

	private void closeRead(BufferedReader read) {
		try {
			read.close();
		} catch (IOException ioe) {
			fileLog.log(Level.WARNING, constants.getFailedToCloseRead());
		}
	}

	/**
	 * This method attempts to change file name into a new one desired by the
	 * user and move the file to the new path
	 *
	 * @param String
	 *            desired new file name
	 * @return boolean true for success copy of file or false if the fileName is
	 *         not valid
	 * @throws IOException
	 *             when failed to move the file
	 */

	public boolean moveFile(String fileName) throws IOException {

		assert (isFileNameEntered(fileName));

		if (isFilePathValid(fileName)) {
			copyFileAndDeletePrevious(fileName);
			initializeTextFile(fileName);
			createTextFileIfNotExist();
			storeNewTextFilePath();
			return true;
		}
		return false;
	}

	private boolean isFilePathValid(String fileName) {
		File testFile = new File(fileName);
		try {
			testFile.getCanonicalPath();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void copyFileAndDeletePrevious(String newFileName) throws IOException {
		File tempFile = new File(newFileName);
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(textFile));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

		copyFileContents(inputStream, outputStream);
		closeStream(inputStream, outputStream);
		deleteOldFile();

	}

	private void copyFileContents(BufferedInputStream inputStream, BufferedOutputStream outputStream)
			throws IOException {

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

	private void deleteOldFile() throws IOException {
		Files.delete(textFile.toPath());
		textFile.delete();
	}

	private boolean isPositive(int length) {
		return length > constants.getMaximumNonPositiveValue();
	}

	private void closeStream(BufferedInputStream inStream, BufferedOutputStream outStream) throws IOException {
		inStream.close();
		outStream.close();
	}

	/**
	 * this method cleans the textFile
	 * 
	 * @throws IOException
	 */
	public void cleanTextFile() throws IOException {
		BufferedOutputStream fileWriter = new BufferedOutputStream(initializeTextFileOutputStream());
		fileWriter.close();
	}

	/**
	 * this method cleans the aliasFile
	 * 
	 * @throws IOException
	 */
	public void cleanAliasFile() throws IOException {
		BufferedOutputStream fileWriter = new BufferedOutputStream(initializeAliasFileOutputStream());
		fileWriter.close();
	}

	private FileOutputStream initializeTextFileOutputStream() throws FileNotFoundException {
		return new FileOutputStream(textFile);
	}
}
