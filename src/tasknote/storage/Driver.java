package tasknote.storage;
//@@author A0126172M
public class Driver{
	public static void main(String[] args){
		StorageTest storageTestMainFrame = new StorageTest();
		
		try {
			storageTestMainFrame.setUp();
			storageTestMainFrame.test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}