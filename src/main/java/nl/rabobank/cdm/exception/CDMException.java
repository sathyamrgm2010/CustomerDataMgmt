package nl.rabobank.cdm.exception;

public class CDMException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String status;

	public CDMException(String status, String errorMessage) {
		super(errorMessage);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
