package seung.kimchi.types;

public enum SRegex {

	PASSWORD_BASIC("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,}", "비밀번호는 영어 대소문자, 숫자, 특수기호(!@#$%^&*)가 포함된 8자리 이상이어야 합니다.")
	, PASSWORD_DUPLICATION("^(?!.*(.)\\1{2,}).*$", "3회 이상 반복되는 문자나 숫자는 사용할 수 없습니다.")
	, PASSWORD_INCREASING_NUMBER("^(?!.*(?:012|123|234|345|456|567|678|789|890)).*$", "3개 이상의 연속된 숫자는 사용할 수 없습니다.")
	, PASSWORD_DECREASING_NUMBER("^(?!.*(?:210|321|432|543|654|765|876|987|098)).*$", "3개 이상의 연속된 숫자는 사용할 수 없습니다.")
	, PASSWORD_INCREASING_LOWER("^(?!.*(?:abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)).*$", "3개 이상의 연속된 문자는 사용할 수 없습니다.")
	, PASSWORD_DECREASING_LOWER("^(?!.*(?:cba|dcb|edc|fed|gfe|hgf|ihg|jhg|kji|lkj|mlk|nml|onm|pon|qpo|rqp|srq|tsr|uts|vut|wvu|xwv|yxy|zyx)).*$", "3개 이상의 연속된 문자는 사용할 수 없습니다.")
	, PASSWORD_INCREASING_UPPER("^(?!.*(?:ABC|BCD|CDE|DEF|EFG|FGH|GHI|HIJ|IJK|JKL|KLM|LMN|MNO|NOP|OPQ|PQR|QRS|RST|STU|TUV|UVW|VWX|WXY|XYZ)).*$", "3개 이상의 연속된 문자는 사용할 수 없습니다.")
	, PASSWORD_DECREASING_UPPER("^(?!.*(?:CBA|DCB|EDC|FED|GFE|HGF|IHG|JHG|KJI|LKJ|MLK|NML|ONM|PON|QPO|RQP|SRQ|TSR|UTS|VUT|WVU|XWV|YXY|ZYX)).*$", "3개 이상의 연속된 문자는 사용할 수 없습니다.")
	;
	
	private String expression;
	
	private String message;
	
	private SRegex(
			String expression
			, String message
			) {
		this.expression = expression;
		this.message = message;
	}
	
	public String expression() {
		return this.expression;
	}// end of extensions
	
	public String message() {
		return this.message;
	}// end of message
	
}
