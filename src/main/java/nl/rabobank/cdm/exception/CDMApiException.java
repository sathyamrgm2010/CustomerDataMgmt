package nl.rabobank.cdm.exception;

public class CDMApiException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public CDMApiException(String errorMessage) {
		super(errorMessage);
	}

}
