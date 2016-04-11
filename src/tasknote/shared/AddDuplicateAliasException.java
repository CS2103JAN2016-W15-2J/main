//@@author A0126172M
package tasknote.shared;

/**
 * AddDuplicateAliasException is used to handle errors in adding duplicated
 * alias
 */
public class AddDuplicateAliasException extends Exception {

	private static final long serialVersionUID = 1L;

	public AddDuplicateAliasException() {
	}

	public AddDuplicateAliasException(String message) {
		super(message);
	}
}