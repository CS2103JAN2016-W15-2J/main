package tasknote.storage;
//@@author A0126172M
/**
 * This class initiate the jUnit test
 */
public class Driver{
	public static void main(String[] args){
		StorageTest storageTestMainFrame = new StorageTest();
		try {
			storageTestMainFrame.setUp();
			storageTestMainFrame.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}