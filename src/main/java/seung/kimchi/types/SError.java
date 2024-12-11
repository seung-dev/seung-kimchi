package seung.kimchi.types;

/**
 * 오류코드와 오류메시지 집합입니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public enum SError {

	SUCCESS                        ("S000", "성공")
	, DATA_IS_EMPTY                ("S100", "조회된 결과가 없습니다.")
	, RESULT_IS_EMPTY              ("F900", "결과를 확인할 수 없습니다.")
	, VALUE_EXISTS                 ("F901", "이미 사용중입니다.")
	, FAIL                         ("F999", "요청이 실패하였습니다. 관리자에게 문의하세요.")
	, USERNAME_IS_NOT_AVAILABLE    ("A100", "사용할 수 없는 계정입니다.")
	, OAUTH2_IS_NOT_AVAILABLE      ("A101", "사용할 수 없는 계정입니다.")
	, ORGANIZATION_IS_NOT_AVAILABLE("A110", "사용할 수 없는 조직입니다.")
	, SIGNUP_FAILED                ("A120", "회원 등록이 실패하였습니다.")
	, SIGNUP_EMAIL_SEND_FAILED     ("A121", "회원 등록이 실패하였습니다.")
	, SIGNIN_FAILED                ("A200", "인증에 실패하였습니다.")
	, SIGNIN_DISABLED              ("A210", "계정이 비활성화 되었습니다. 관리자에게 문의하세요.")
	, PASSWORD_EXPIERED            ("A220", "비밀번호가 만료되었습니다.")
	, PASSWORD_IS_NOT_MATCH        ("A230", "인증에 실패하였습니다. 5회 실패시 계정이 잠깁니다.")
	, SIGNIN_LOCKED                ("A240", "인증에 실패하였습니다. 5회 실패시 계정이 잠깁니다.")
	, TOKEN_IS_EXPIERED            ("A300", "토큰이 만료되었습니다.")
	, TOKEN_IS_INVALID             ("A310", "유효한 토큰이 아닙니다.")
	, TOKEN_IS_NOT_SECURE          ("A320", "유효한 토큰이 아닙니다.")
	, TOKEN_IS_MALFORMED           ("A330", "유효한 토큰이 아닙니다.")
	, TOKEN_IS_UNSUPPORTED         ("A340", "유효한 토큰이 아닙니다.")
	, OTP_IS_NOT_MATCH             ("A400", "OTP가 일치하지 않습니다.")
	, FORBIDEN                     ("H403", "Forbidden")
	, TASK_IS_NOT_ACTIVE           ("T000", "업무가 활성화 되지 않았습니다.")
	, RESPONSE_IS_NOT_VALID        ("T100", "필요한 정보를 찾을 수 없습니다.")
	, REQUEST                      ("R000", "요청")
	, REQUEST_REPEAT               ("R100", "재요청")
	, IN_PROGRESS                  ("P000", "진행중")
	, HOLD                         ("P999", "중지")
	;
	
	private final String code;
	
	private final String message;
	
	/**
	 * 오류를 생성합니다.
	 * 
	 * @param code    코드
	 * @param message 메시지
	 * @since 0.0.1
	 */
	SError(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 오류에 해당하는 코드를 반환합니다.
	 * 
	 * @return 오류코드
	 * @since 0.0.1
	 */
	public String code() {
		return this.code;
	}// end of code
	
	/**
	 * 오류에 해당하는 메시지를 반환합니다.
	 * 
	 * @return 오류메시지
	 * @since 0.0.1
	 */
	public String message() {
		return this.message;
	}// end of message
	
	/**
	 * 요청값과 일치하는 오류를 반환합니다.
	 * 
	 * @return 오류
	 * @since 0.0.1
	 */
	public static SError resolve(String error_code) {
		for(SError s_error : values()) {
			if(s_error.code.equals(error_code)) {
				return s_error;
			}
		}
		return null;
	}// end of resolve
	
	/**
	 * 요청값과 오류코드가 일치하는지 확인합니다.
	 * 
	 * @return 일치하면 {@code true}를 반환합니다.
	 * @since 0.0.1
	 */
	public boolean equals(String error_code) {
		return this.code.equals(error_code);
	}// end of equals
	
}
