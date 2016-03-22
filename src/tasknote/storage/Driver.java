package tasknote.storage;

import java.util.ArrayList;

import tasknote.shared.*;

public class Driver{
	public static void main(String[] args){
		ArrayList<TaskObject> test = new ArrayList<TaskObject>();
		try{
			TaskObject temp = new TaskObject();
			//temp.setTaskObjectCalendar(new GregorianCalendar(2015, 2, 21, 14, 41, 0));
			temp.setDateYear(2015);
			temp.setDateMonth(2);
			temp.setDateDay(21);
			temp.setDateHour(14);
			temp.setDateMinute(41);
			test.add(temp);
			test.add(new TaskObject());
			Storage storage = new Storage();
			storage.saveTasks(test);
			
			ArrayList<TaskObject> list = storage.loadTasks();
			System.out.println("#number of items: " + list.size());
			for(int i=0; i<list.size(); ++i){
				System.out.println("item #" + i + ":\n" + list.get(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}