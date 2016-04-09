package tasknote.storage;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathDemo{
	private static final String PATH[] = {	null,
											"",
											".",
											"..",
											"C:/NUS/CS2103T/main",
											"../main/../.",
											"./hello/today"
											};
	private static final String FINAL_PATH = "C:/NUS/CS2103T/main/file.txt";
	
	public PathDemo(){}
	
	public static void main(String[] args){
		int size = PathDemo.PATH.length;
		for(int i=0; i<size; ++i){
			try{
				System.out.println("Path = " + PathDemo.PATH[i]);
				Path path = Paths.get(PathDemo.PATH[i]);
				Path currentPath = Paths.get(PathDemo.FINAL_PATH);
				Path resolvedPath = currentPath.getParent().resolve(path);
				System.out.println(resolvedPath.normalize());
				System.out.println();
			}catch(InvalidPathException ipe){
				System.out.println("Caught invalid path.");
			}catch(NullPointerException npe){
				System.out.println("Caught null pointer.");
			}catch(IllegalArgumentException iae){
				System.out.println("Caught IllegalArgumentException.");
			}
		}
	}
}