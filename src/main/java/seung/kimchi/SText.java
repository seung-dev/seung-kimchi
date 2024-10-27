package seung.kimchi;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 텍스트 처리와 관련된 기능들을 제공합니다.
 * 다른 타입을 텍스트로 변환하는 기능들도 포함합니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SText {

	/**
	 * 스페이스({@value #_S_SPACE})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_SPACE = " ";
	
	/**
	 * 공백({@value #_S_BLANK})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_BLANK = "";
	
	/**
	 * CR(Carriage Return): 캐리지 리턴({@value #_S_CR})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_CR = "\r";
	
	/**
	 * LF (Line Feed): 라인 피드({@value #_S_LF})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_LF = "\n";
	
	/**
	 * 줄바꿈
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**
	 * 대문자({@value #_S_UPPER_CHARACTERS})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * 소문자({@value #_S_LOWER_CHARACTERS})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
	
	/**
	 * 숫자({@value #_S_NUMBER_CHARACTERS})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_NUMBER_CHARACTERS = "0123456789";
	
	/**
	 * 비밀번호 특수문자({@value #_S_PASSWORD_SYMBOLS})
	 * 
	 * @since 0.0.1
	 */
	public static final String _S_PASSWORD_SYMBOLS = "!@#_";
	
	/**
	 * 널이나 공백인지 확인합니다.
	 * 
	 * @param value 요청값; {@code null}일 수 있습니다.
	 * @return 널이거나 길이가 0이면 {@code true}를 반환합니다.
	 * @since 0.0.1
	 */
	public static boolean is_empty(
			final String value
			) {
		if(value == null) {
			return true;
		}
		if(_S_BLANK.equals(value)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 요청값이 널이거나 길이가 0이면 기본값을 반환합니다.
	 * 
	 * @see #is_empty(String)
	 * @param value 요청값; {@code null}일 수 있습니다.
	 * @param default_value 기본값; {@code null}일 수 있습니다.
	 * @return 널이거나 길이가 0이면 기본값을 반환합니다.
	 * @since 0.0.1
	 */
	public static String text(
			final String value
			, final String default_value
			) {
		return is_empty(value) ? default_value : value;
	}
	
	/**
	 * 앞뒤 공백과 줄바꿈을 제거합니다.
	 * 
	 * @param value 요청값; {@code null}일 수 있습니다.
	 * @return {@code value}가 {@code null}인 경우 공백을 반환합니다.
	 * @since 0.0.1
	 */
	public static String trim(
			final String value
			) {
		return value == null ? _S_BLANK : value.replaceAll("^\\s+|\\s+$", _S_BLANK);
	}// end of trim
	
	/**
	 * 요청값들을 하나로 결합합니다.
	 * 
	 * <pre>
	 * SText.concat("동해물과", " ", "백두산이", " ", "마", "르", "고") = "동해물과 백두산이 마르고"
	 * </pre>
	 * 
	 * @param values 요청값 배열; {@code null}일 수 있습니다.
	 * @return {@code values}에 {@code null}이 포함된 경우 해당 값은 공백으로 처리합니다.
	 * @since 0.0.1
	 */
	public static String concat(final String... values) {
		return Arrays.stream(values)
				.map(value -> text(value, _S_BLANK))
				.collect(Collectors.joining(_S_BLANK));
	}// end of concat
	
	/**
	 * 요청값들 사이에 스페이스를 추가하여 하나로 결합합니다.
	 * 
	 * <pre>
	 * SText.concat_with_space("동해물과", "백두산이", null, "마", "르", "고") = "동해물과 백두산이 마 르 고"
	 * </pre>
	 * 
	 * @param values 요청값 배열; {@code null}일 수 있습니다. {@code null}이 포함된 경우 해당 값은 제외됩니다.
	 * @return {@code values}가 모두 {@code null}인 경우 공백을 반환합니다.
	 * @since 0.0.1
	 */
	public static String concat_with_space(final String... values) {
		return Arrays.stream(values)
				.filter(value -> !is_empty(value))
				.collect(Collectors.joining(_S_SPACE));
	}// end of concat_with_space
	
	/**
	 * 요청값을 요청수 만큼 반복합니다.
	 * 
	 * @see StringUtils#repeat(String, int)
	 * @param value 요청값; {@code null}일 수 있습니다.
	 * @param length 길이
	 * @return {@code value}가 {@code null}인 경우 공백을 반환합니다.
	 * @since 0.0.1
	 */
	public static String repeat(
			final String value
			, int length
			) {
		return is_empty(value) ? _S_BLANK : StringUtils.repeat(value, length);
	}// end of repeat
	
	/**
	 * UUID를 생성합니다.
	 * 
	 * @see UUID#randomUUID()
	 * @return UUID
	 * @since 0.0.1
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}// end of uuid
	
	/**
	 * 랜덤 문자열을 생성합니다.
	 * 
	 * @see {@link RandomStringUtils#random(int, boolean, boolean)}
	 * @param length 길이
	 * @param letters 문자포함여부
	 * @param numbers 숫자포함여부
	 * @return 랜덤 문자열
	 * @throws IllegalArgumentException {@code length}는 0 보다 커야합니다.
	 * @since 0.0.1
	 */
	public static String random(
			final int length
			, final boolean letters
			, final boolean numbers
			) {
		if (length < 1) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		return RandomStringUtils.random(length, letters, numbers);
	}// end of random
	
	/**
	 * 숫자로 이루어진 PIN번호를 생성합니다.
	 * 
	 * @param length 길이
	 * @return PIN번호
	 * @throws IllegalArgumentException {@code length}는 0 보다 커야합니다.
	 * @since 0.0.1
	 */
	public static String pin_number(
			final int length
			) {
		
		if (length < 1) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		StringBuilder random = new StringBuilder();
		
		SecureRandom secureRandom = new SecureRandom();
		
		for(int i = 0; i < length; i++) {
			random.append(_S_NUMBER_CHARACTERS.charAt(secureRandom.nextInt(_S_NUMBER_CHARACTERS.length())));
		}
		
		return random.toString();
	}// end of pin_number
	
	/**
	 * 비밀번호를 생성합니다.
	 * 
	 * @param length 길이
	 * @param symbols 포함할 특수문자; {@link #_S_PASSWORD_SYMBOLS}를 참고하세요.
	 * @return 대문자, 소문자, 숫자, 특수문자가 최소 한개 이상 포함된 비밀번호
	 * @throws IllegalArgumentException {@code length}는 8 이상이어야 합니다. {@code symbols}는 8 {@code null}이나 공백일 수 없습니다.
	 * @since 0.0.1
	 */
	public static String random_password(final int length, final String symbols) {
		
		if(length < 8) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		if(is_empty(symbols)) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		String char_all = _S_UPPER_CHARACTERS + _S_LOWER_CHARACTERS + _S_NUMBER_CHARACTERS + symbols;
		
		StringBuilder random = new StringBuilder();
		
		SecureRandom secureRandom = new SecureRandom();
		
		random.append(_S_UPPER_CHARACTERS.charAt(secureRandom.nextInt(_S_UPPER_CHARACTERS.length())));
		random.append(_S_LOWER_CHARACTERS.charAt(secureRandom.nextInt(_S_LOWER_CHARACTERS.length())));
		random.append(_S_NUMBER_CHARACTERS.charAt(secureRandom.nextInt(_S_NUMBER_CHARACTERS.length())));
		random.append(symbols.charAt(secureRandom.nextInt(symbols.length())));
		
		for(int i = 4; i < length; i++) {
			random.append(char_all.charAt(secureRandom.nextInt(char_all.length())));
		}
		
		List<String> collection = Arrays.asList(random.toString().split(_S_BLANK));
		
		while(true) {
			
			Collections.shuffle(collection);
			
			if(_S_UPPER_CHARACTERS.contains(collection.get(0))) {
				break;
			}
			
			if(_S_LOWER_CHARACTERS.contains(collection.get(0))) {
				break;
			}
			
		}// end of while
		
		return String.join(_S_BLANK, collection);
	}// end of random_password
	/**
	 * 비밀번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 포함할 특수문자: {@link SText#_S_PASSWORD_SYMBOLS}</p>
	 * 
	 * @see {@link #random_password(int, String)}
	 * @param length 길이
	 * @return 대문자, 소문자, 숫자, 특수문자가 최소 한개 이상 포함된 비밀번호
	 * @throws IllegalArgumentException {@code length}는 8 이상이어야 합니다.
	 * @since 0.0.1
	 */
	public static String random_password(final int length) {
		return random_password(length, _S_PASSWORD_SYMBOLS);
	}// end of random_password
	/**
	 * 비밀번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 길이: 16</p>
	 * <p>- 기호: {@link #_S_PASSWORD_SYMBOLS}</p>
	 * 
	 * @see {@link #random_password(int, String)}
	 * @return 대문자, 소문자, 숫자, 특수문자가 최소 한개 이상 포함된 비밀번호
	 * @since 0.0.1
	 */
	public static String random_password() {
		return random_password(16);
	}// end of random_password
	
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param date 기준일시
	 * @param random 랜덤 문자열의 길이 입니다. 랜덤 문자열은 {@link #random(int, boolean, boolean)}를 사용합니다.
	 * @param suffix 접미사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws IllegalArgumentException {@code random}는 0 보다 커야합니다.
	 * @since 0.0.1
	 */
	public static String item_no(
			final String prefix
			, final Date date
			, final int random
			, final String suffix
			) {
		return String.format("%s%d%s%s", text(prefix, ""), date.getTime() / 1000, random(random, true, true), text(suffix, ""));
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @see #item_no(int, String, String, Date)
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param random 랜덤 문자열의 길이 입니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws IllegalArgumentException {@code random}는 0 보다 커야합니다.
	 * @since 0.0.1
	 */
	public static String item_no(
			final String prefix
			, final int random
			) {
		return item_no(prefix, new Date(), random, "");
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 랜덤 문자열 길이: 5</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @see #item_no(int, String, String, Date)
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @since 0.0.1
	 */
	public static String item_no(
			final String prefix
			) {
		return item_no(prefix, 5);
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 접두사: I</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 랜덤 문자열 길이: 5</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @see #item_no(int, String, String, Date)
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws IllegalArgumentException 랜덤 문자열의 길이는 0 이하일 수 없습니다.
	 * @since 0.0.1
	 */
	public static String item_no() {
		return item_no("I");
	}// end of item_no
	
	/**
	 * 왼쪽으로 패딩합니다.
	 * 
	 * <p>사용:</p>
	 * <pre>
	 * SText.pad_left(null, 4, "0")   = "0000"
	 * SText.pad_left("", 4, "0")     = "0000"
	 * SText.pad_left("abc", 4, null) = " abc"
	 * SText.pad_left("abc", 4, "")   = " abc"
	 * SText.pad_left("abc", 4, " ")  = " abc"
	 * SText.pad_left("abc", 2, "0")  = IllegalArgumentException
	 * </pre>
	 * 
	 * @param value 기본값; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param length 길이
	 * @param fill 채울 글자; {@code null}일 수 있습니다. {@code null}인 경우 스페이스으로 처리합니다. 길이는 1을 초과할 수 없습니다.
	 * @return 왼쪽에 채워진 문자열
	 * @throws IllegalArgumentException {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @since 0.0.1
	 */
	public static String pad_left(
			final String value
			, final int length
			, final String fill
			) {
		if(text(value, "").length() > length) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		String fill_safe = text(fill, " ");
		if(text(fill_safe, "").length() > 1) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		return StringUtils.leftPad(value, length, fill_safe);
	}// end of pad_left
	/**
	 * 왼쪽으로 패딩합니다.
	 * 
	 * <p>사용:</p>
	 * <pre>
	 * SText.pad_left(123, 4, null) = " 123"
	 * SText.pad_left(123, 4, "")   = " 123"
	 * SText.pad_left(123, 4, "0")  = "0123"
	 * SText.pad_left(123, 2, "0")  = IllegalArgumentException
	 * </pre>
	 * 
	 * @param value 기본값;
	 * @param length 길이
	 * @param fill 채울 글자; {@code null}일 수 있습니다. {@code null}인 경우 스페이스으로 처리합니다. 길이는 1을 초과할 수 없습니다.
	 * @return 왼쪽에 채워진 문자열
	 * @throws IllegalArgumentException {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @since 0.0.1
	 */
	public static String pad_left(
			final int value
			, final int length
			, final String fill
			) {
		return pad_left(Integer.toString(value), length, fill);
	}// end of pad_left
	
	/**
	 * 오른쪽으로 패딩합니다.
	 * 
	 * <p>사용:</p>
	 * <pre>
	 * SText.pad_right(null, 4, "0")   = "0000"
	 * SText.pad_right("", 4, "0")     = "0000"
	 * SText.pad_right("abc", 4, null) = "abc "
	 * SText.pad_right("abc", 4, "")   = "abc "
	 * SText.pad_right("abc", 4, " ")  =  abc "
	 * SText.pad_right("abc", 2, "0")  = IllegalArgumentException
	 * </pre>
	 * 
	 * @param value 기본값; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param length 길이
	 * @param fill 채울 글자; {@code null}일 수 있습니다. {@code null}인 경우 스페이스으로 처리합니다. 길이는 1을 초과할 수 없습니다.
	 * @return 오른쪽에 채워진 문자열
	 * @throws IllegalArgumentException {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @since 0.0.1
	 */
	public static String pad_right(
			final String value
			, final int length
			, final String fill
			) {
		if(text(value, "").length() > length) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		String fill_safe = text(fill, " ");
		if(text(fill_safe, "").length() > 1) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		return StringUtils.rightPad(value, length, fill_safe);
	}// end of pad_right
	/**
	 * 오른쪽으로 패딩합니다.
	 * 
	 * <p>사용:</p>
	 * <pre>
	 * SText.pad_right(123, 4, null) = " 123"
	 * SText.pad_right(123, 4, "")   = " 123"
	 * SText.pad_right(123, 4, "0")  = "0123"
	 * SText.pad_right(123, 2, "0")  = IllegalArgumentException
	 * </pre>
	 * 
	 * @param value 기본값;
	 * @param length 길이
	 * @param fill 채울 글자; {@code null}일 수 있습니다. {@code null}인 경우 스페이스으로 처리합니다. 길이는 1을 초과할 수 없습니다.
	 * @return 오른쪽에 채워진 문자열
	 * @throws IllegalArgumentException {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @since 0.0.1
	 */
	public static String pad_right(
			final int value
			, final int length
			, final String fill
			) {
		return pad_right(Integer.toString(value), length, fill);
	}// end of pad_right
	
	/**
	 * 전각 문자로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return 전각
	 * @since 0.0.1
	 */
	public static String to_full_width(
			final String value
			) {
		if(is_empty(value)) {
			return _S_BLANK;
		}
		char[] char_array = value.toCharArray();
		char[] full_width = new char[char_array.length];
		for(int i = 0; i < char_array.length; i++) {
			if(char_array[i] == 32) {
				full_width[i] = (char) 12288;
				continue;
			}
			if(char_array[i] < 127) {
				full_width[i] = (char) (char_array[i] + 65248);
				continue;
			}
			full_width[i] = char_array[i];
		}// end of char_array
		return new String(full_width);
	}// end of to_full_width
	
	/**
	 * 반각 문자로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return 반각
	 * @since 0.0.1
	 */
	public static String to_half_width(
			final String value
			) {
		if(is_empty(value)) {
			return _S_BLANK;
		}
		char[] char_array = value.toCharArray();
		char[] half_width = new char[char_array.length];
		for(int i = 0; i < char_array.length; i++) {
			if(char_array[i] == 12288) {
				half_width[i] = (char) 32;
				continue;
			}
			if(char_array[i] > 65280 && char_array[i] < 65375) {
				half_width[i] = (char) (char_array[i] - 65248);
				continue;
			}
			half_width[i] = char_array[i];
		}// end of characters
		return new String(half_width);
	}// end of to_half_width
	
	/**
	 * 바이트 배열을 소스 코드로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return 전각
	 * @since 0.0.1
	 */
	public static String to_code(
			final byte[] bytes
			) {
		if(bytes == null) {
			return "byte[] bytes = null;";
		}
		if(bytes.length == 0) {
			return "byte[] bytes = {};";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for(byte b : bytes) {
			stringBuffer.append(String.format(", (byte) 0x%02x", b));
		}
		return concat("byte[] bytes = {", stringBuffer.toString().substring(2), "};");
	}// end of to_code
	
	/**
	 * 콤마가 포함된 숫자 형식으로 변환합니다.
	 * 
	 * @param value 요청값
	 * @return {@code value}가 숫자 형식이 아닌경우 공백을 반환합니다.
	 * @since 0.0.1
	 */
	public static String number_format(
			final String value
			) {
		if(is_empty(value)) {
			return "";
		}
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		try {
			return numberFormat.format(SNumber.to_long(value));
		} catch (NumberFormatException e) {
			try {
				return numberFormat.format(SNumber.to_double(value));
			} catch (NumberFormatException ee) {
				return "";
			}
		}
	}// end of number_format
	
//	public static List<String> contains_list(
//			final String value
//			, final boolean contains
//			) {
//		List<String> contains_list = new ArrayList<>();
//		if(value == null) {
//			return contains_list;
//		}
//		for(String item : value.split(" ")) {
//			if(_S_BLANK.equals(item)) {
//				continue;
//			}
//			if(contains && !item.startsWith("-")) {
//				contains_list.add(item);
//				continue;
//			}
//			if(!contains && item.startsWith("-")) {
//				contains_list.add(item.substring(1));
//				continue;
//			}
//		}
//		return contains_list;
//	}// end of contains_list
//	
//	public static String pnu1111000(final String sido) {
//		if(sido.contains("서울")) {
//			return "1100000000";
//		} else if(sido.contains("부산")) {
//			return "2600000000";
//		} else if(sido.contains("대구")) {
//			return "2700000000";
//		} else if(sido.contains("인천")) {
//			return "2800000000";
//		} else if(sido.contains("광주")) {
//			return "2900000000";
//		} else if(sido.contains("대전")) {
//			return "3000000000";
//		} else if(sido.contains("울산")) {
//			return "3100000000";
//		} else if(sido.contains("세종")) {
//			return "3611000000";
//		} else if(sido.contains("경기")) {
//			return "4100000000";
//		} else if(sido.contains("충청북도") || sido.contains("충북")) {
//			return "4300000000";
//		} else if(sido.contains("충청남도") || sido.contains("충남")) {
//			return "4400000000";
//		} else if(sido.contains("전라남도") || sido.contains("전남")) {
//			return "4600000000";
//		} else if(sido.contains("경상북도") || sido.contains("경북")) {
//			return "4700000000";
//		} else if(sido.contains("경상남도") || sido.contains("경남")) {
//			return "4800000000";
//		} else if(sido.contains("제주")) {
//			return "5000000000";
//		} else if(sido.contains("강원")) {
//			return "5100000000";
//		} else if(sido.contains("전라북도") || sido.contains("전북")) {
//			return "5200000000";
//		}
//		return _S_BLANK;
//	}// end of sido_no
	
}
