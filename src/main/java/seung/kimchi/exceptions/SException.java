package seung.kimchi.exceptions;

import seung.kimchi.types.SError;

/**
 * 예외 클래스 입니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SException extends Exception {

	private static final long serialVersionUID = 9215065372476702353L;
	
	private SError error;
	
	/**
	 * 예외를 생성합니다.
	 * 
	 * @param cause 원인
	 * @since 0.0.1
	 */
	public SException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * 예외를 생성합니다.
	 * 
	 * @param cause   원인
	 * @param message 메시지
	 * @since 0.0.1
	 */
	public SException(Throwable cause, String message) {
		super(message, cause);
	}
	
	/**
	 * 예외를 생성합니다.
	 * 
	 * @param cause 원인
	 * @param error 오류
	 * @since 0.0.1
	 * @see {@link SError}
	 */
	public SException(Throwable cause, SError error) {
		super(error.message(), cause);
		this.error = error;
	}
	
	/**
	 * 예외를 생성합니다.
	 * 
	 * @param message 메시지
	 * @since 0.0.1
	 */
	public SException(String message) {
		super(message);
	}
	
	/**
	 * 예외를 생성합니다.
	 * 
	 * @param error {@link SError}
	 * @since 0.0.1
	 */
	public SException(SError error) {
		super(error.message());
		this.error = error;
	}
	
	/**
	 * 예외의 오류를 확인합니다.
	 * 
	 * @return {@link SError}
	 * @since 0.0.1
	 */
	public SError error() {
		return this.error;
	}
	
	/**
	 * 예외의 오류를 확인합니다.
	 * 
	 * @return {@link SError#code()}
	 * @since 0.0.1
	 */
	public String error_code() {
		return this.error.code();
	}
	
}
