package ExceptionChecks;


public class InvalidNumberOfDriversException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidNumberOfDriversException() {
		super();
	}
	
	public InvalidNumberOfDriversException(String s) {
		super(s);
	}
	
}
