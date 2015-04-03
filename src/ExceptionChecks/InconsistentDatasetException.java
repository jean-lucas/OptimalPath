package ExceptionChecks;


// This will be thrown when a city is found within one of the store locations dataset but not
// within our cities data set. 
// This is a big issue and will be resolved in further versions


public class InconsistentDatasetException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InconsistentDatasetException() {
		super();
	}
	
	public InconsistentDatasetException(String s) {
		super(s);
	}

}
