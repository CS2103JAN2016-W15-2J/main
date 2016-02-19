package tasknote.storage;
import java.util.ArrayList;

import tasknote.shared.*;

public class Driver{
	public static void main(String[] args){
		Storage storage = new Storage();
		ArrayList<TaskObject> list = storage.loadTasks();
		for(int i=0; i<list.size(); ++i){
			System.out.println(list);
		}
	}
}