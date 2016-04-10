//@@author A0126172M
package tasknote.shared;
/**
 * AddDuplicateAliasException is used to handle errors in adding duplicated alias
 */
public class AddDuplicateAliasException extends Exception{

	public AddDuplicateAliasException(){}
	
	public AddDuplicateAliasException(String message){
		super(message);
	}
}