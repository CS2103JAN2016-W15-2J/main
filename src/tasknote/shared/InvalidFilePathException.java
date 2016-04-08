package tasknote.shared;
//@@author A0126172M
/**
 * AddDuplicateAliasException is used to handle errors in adding duplicated alias
 */
public class InvalidFilePathException extends Exception{

	public InvalidFilePathException(){}
	
	public InvalidFilePathException(String message){
		super(message);
	}
}