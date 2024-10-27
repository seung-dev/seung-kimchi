package seung.kimchi.exceptions;

import seung.kimchi.types.SError;

public class SException extends Exception {

	private static final long serialVersionUID = 9215065372476702353L;
	
	private SError error;
	
	public SException(Throwable cause) {
		super(cause);
	}
	
	public SException(Throwable cause, String message) {
		super(message, cause);
	}
	
	public SException(Throwable cause, SError error) {
		super(error.message(), cause);
		this.error = error;
	}
	
	public SException(String message) {
		super(message);
	}
	
	public SException(SError error) {
		super(error.message());
		this.error = error;
	}
	
	public SError error() {
		return this.error;
	}
	
	public String error_code() {
		return this.error.code();
	}
	
	
}
