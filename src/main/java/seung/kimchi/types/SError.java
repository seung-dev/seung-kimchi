package seung.kimchi.types;

public enum SError {

	SUCCESS("S000", "성공")
	, REQUEST("R000", "요청")
	, IN_PROGRESS("P000", "진행중")
	, HOLD("P999", "대기")
	, RESPONSE_IS_INVALID("F900", "응답 내용 검증에 실패하였습니다.")
	, FAIL("F999", "요청이 실패하였습니다. 관리자에게 문의하세요.")
	, USERNAME_IS_NOT_AVAILABLE("A010", "사용할 수 없는 계정입니다.")
	, ORGANIZATION_IS_NOT_AVAILABLE("A011", "사용할 수 없는 조직 도메인입니다.")
	, OAUTH2_IS_NOT_AVAILABLE("A020", "사용할 수 없는 계정입니다.")
	, SIGNIN_FAILED("A100", "인증에 실패하였습니다.")
	, SIGNIN_DISABLED("A110", "계정이 비활성화 되었습니다. 관리자에게 문의하세요.")
	, PASSWORD_EXPIERED("A120", "비밀번호를 변경하세요.")
	, SIGNIN_LOCKED("A130", "5회 이상 인증에 실패하였습니다. 관리자에게 문의하세요.")
	, TOKEN_IS_VALID("A200", "Token is verified.")
	, TOKEN_IS_NOT_SECURE("A201", "io.jsonwebtoken.security.SecurityException")
	, TOKEN_IS_MALFORMED("A202", "io.jsonwebtoken.MalformedJwtException")
	, TOKEN_IS_EXPIERED("A203", "io.jsonwebtoken.ExpiredJwtException")
	, TOKEN_IS_UNSUPPORTED("A204", "io.jsonwebtoken.UnsupportedJwtException")
	, TOKEN_IS_INVALID("A205", "io.jsonwebtoken.IllegalArgumentException")
	, SIGNUP_FAILED("A300", "회원 등록이 실패하였습니다.")
	, SIGNUP_EMAIL_SEND_FAILED("A301", "회원 등록이 실패하였습니다.")
	, PASSWORD_TOKEN_IS_NOT_VALID("A400", "유효한 토근이 아닙니다.")
	, FORBIDEN("H403", "Forbidden")
	, BAD_SQL("D100", "org.springframework.jdbc.BadSqlGrammarException")
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
