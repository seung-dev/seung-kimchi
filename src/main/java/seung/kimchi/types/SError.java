package seung.kimchi.types;

public enum SError {

	SUCCESS("S000", "성공")
	, BAD_SQL("F800", "org.springframework.jdbc.BadSqlGrammarException")
	, RESULT_IS_EMPTY("F998", "결과를 확인할 수 없습니다.")
	, FAIL("F999", "요청이 실패하였습니다. 관리자에게 문의하세요.")
	, VALUE_EXISTS("V100", "이미 사용중입니다.")
	, USERNAME_IS_NOT_AVAILABLE("A010", "사용할 수 없는 계정입니다.")
	, ORGANIZATION_IS_NOT_AVAILABLE("A011", "사용할 수 없는 조직 도메인입니다.")
	, OAUTH2_IS_NOT_AVAILABLE("A020", "사용할 수 없는 계정입니다.")
	, SIGNIN_FAILED("A100", "인증에 실패하였습니다.")
	, SIGNIN_DISABLED("A110", "계정이 비활성화 되었습니다. 관리자에게 문의하세요.")
	, PASSWORD_EXPIERED("A120", "비밀번호가 만료되었습니다.")
	, PASSWORD_IS_NOT_MATCH("A121", "인증에 실패하였습니다. 5회 실패시 계정이 잠깁니다.")
	, SIGNIN_LOCKED("A130", "인증에 실패하였습니다. 5회 실패시 계정이 잠깁니다.")
	, TOKEN_IS_VALID("A200", "Token is verified.")
	, TOKEN_IS_INVALID("A201", "유요한 토큰이 아닙니다.")
	, TOKEN_IS_NOT_SECURE("A202", "io.jsonwebtoken.security.SecurityException")
	, TOKEN_IS_MALFORMED("A203", "io.jsonwebtoken.MalformedJwtException")
	, TOKEN_IS_EXPIERED("A204", "io.jsonwebtoken.ExpiredJwtException")
	, TOKEN_IS_UNSUPPORTED("A205", "io.jsonwebtoken.UnsupportedJwtException")
	, OTP_IS_NOT_MATCH("A206", "OTP가 일치하지 않습니다.")
	, SIGNUP_FAILED("A300", "회원 등록이 실패하였습니다.")
	, SIGNUP_EMAIL_SEND_FAILED("A301", "회원 등록이 실패하였습니다.")
	, FORBIDEN("H403", "Forbidden")
	, TASK_IS_NOT_ACTIVE("T000", "업무가 활성화 되지 않았습니다.")
	, RESPONSE_IS_NOT_VALID("T100", "필요한 정보를 찾을 수 없습니다.")
	, REQUEST("R000", "요청")
	, REQUEST_REPEAT("R100", "재요청")
	, IN_PROGRESS("P000", "진행중")
	, HOLD("P999", "중지")
	;
	
	private final String code;
	private final String message;
	
	SError(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String code() {
		return this.code;
	}// end of code
	
	public String message() {
		return this.message;
	}// end of message
	
	public static SError resolve(String error_code) {
		for(SError s_error : values()) {
			if(s_error.code.equals(error_code)) {
				return s_error;
			}
		}
		return null;
	}// end of resolve
	
	public boolean equals(String error_code) {
		return this.code.equals(error_code);
	}// end of equals
	
}
