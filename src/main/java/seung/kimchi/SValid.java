package seung.kimchi;

import org.apache.commons.lang3.tuple.Pair;

import seung.kimchi.types.SRegex;

public class SValid {

	public static Pair<Boolean, String> password(
			String value
			, SRegex[] expressions
			, String email
			, String phone_number
			) {
		
		if(SText.is_empty(value)) {
			return Pair.of(false, "비밀번호가 올바르지 않습니다.");
		}
		
		if(expressions != null) {
			for(SRegex regex : expressions) {
				if(!SFormat.regex(regex.expression(), value)) {
					return Pair.of(false, regex.message());
				}
			}
		}
		
		if(!SText.is_empty(email)) {
			if(!email.contains("@")) {
				return Pair.of(false, "이메일 형식이 올바르지 않습니다.");
			}
			String domain = email.split("@")[1];
			if(!domain.contains(".")) {
				return Pair.of(false, "이메일 형식이 올바르지 않습니다.");
			}
			if(value.contains(domain.split("\\.")[0])) {
				return Pair.of(false, "이메일 주소는 비밀번호에 포함될 수 없습니다.");
			}
			String address = email.split("@")[0];
			for(String unit : address.replaceAll("[-.]", " ").split(" ")) {
				if(value.contains(unit)) {
					return Pair.of(false, "이메일 주소는 비밀번호에 포함될 수 없습니다.");
				}
			}
		}
		
		if(!SText.is_empty(phone_number)) {
			for(String no : phone_number.split("-")) {
				if(value.contains(no)) {
					return Pair.of(false, "전화번호는 비밀번호에 포함될 수 없습니다");
				}
			}
		}
		
		return Pair.of(true, "사용할 수 있는 비밀번호 입니다.");
	}// end of password
	
}
