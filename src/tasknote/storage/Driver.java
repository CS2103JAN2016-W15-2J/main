package tasknote.storage;

import java.util.ArrayList;
import tasknote.shared.*;

public class Driver{
	public static void main(String[] args){
		ArrayList<TaskObject> test = new ArrayList<TaskObject>();
		try{
			test.add(new TaskObject());
			Storage storage = new Storage();
			storage.writeTasks(test);
			/*
			ArrayList<TaskObject> list = storage.loadTasks();
			for(int i=0; i<list.size(); ++i){
				System.out.println(list.get(i));
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}