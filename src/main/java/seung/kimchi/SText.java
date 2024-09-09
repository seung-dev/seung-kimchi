package seung.kimchi;

import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class SText {

	public static final String _S_SPACE = " ";
	public static final String _S_EMPTY = "";
	public static final String _S_LF = "\n";
	public static final String _S_CR = "\r";
	public static final String _S_LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static boolean is_empty(final String value) {
		if(value == null) {
			return true;
		}
		if("".equals(value)) {
			return true;
		}
		return false;
	}
	
	public static String text(final String value, final String default_value) {
		if(is_empty(value)) {
			return default_value;
		}
		return is_empty(value) ? default_value : value;
	}
	
	public static String trim(final String data) {
		if(data == null) {
			return null;
		}
		return data.replaceAll("^\\s+|\\s+$", "");
	}// end of trim
	
	public static String concat(final String... data) {
		return String.join("", data);
	}// end of concat
	
	public static String uuid() {
		return UUID.randomUUID().toString();
	}// end of uuid
	
	public static String item_no(int random_size, String prefix, Date date) {
		return String.format("%s%d%s", prefix, date.getTime() / 1000, RandomStringUtils.random(random_size, true, true));
	}
	public static String item_no(int random_size, String prefix) {
		return item_no(random_size, prefix, new Date());
	}
	public static String item_no(int random_size) {
		return item_no(random_size, "");
	}
	public static String item_no(String prefix) {
		return item_no(5, prefix);
	}
	public static String item_no() {
		return item_no("I");
	}
	
	public static int random(final int min, final int max) {
		return new SecureRandom().nextInt(max - min + 1) + min;
	}// end of random
	
	private static final String _CHAR_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String _CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String _CHAR_DIGITS = "0123456789";
	private static final String _CHAR_SPECIAL = "!@#_";
	
	public static String random_number(final int length) {
		
		if (length < 1) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		StringBuilder random = new StringBuilder();
		
		SecureRandom secureRandom = new SecureRandom();
		
		for(int i = 0; i < length; i++) {
			random.append(_CHAR_DIGITS.charAt(secureRandom.nextInt(_CHAR_DIGITS.length())));
		}
		
		return random.toString();
	}// end of random_number
	
	public static String random_password(final int length, final String char_special) {
		
		if (length < 4) {
			throw new IllegalArgumentException("Unexpected value.");
		}
		
		String char_all = _CHAR_UPPER + _CHAR_LOWER + _CHAR_DIGITS + char_special;
		
		StringBuilder random = new StringBuilder();
		
		SecureRandom secureRandom = new SecureRandom();
		
		random.append(_CHAR_UPPER.charAt(secureRandom.nextInt(_CHAR_UPPER.length())));
		random.append(_CHAR_LOWER.charAt(secureRandom.nextInt(_CHAR_LOWER.length())));
		random.append(_CHAR_DIGITS.charAt(secureRandom.nextInt(_CHAR_DIGITS.length())));
		random.append(char_special.charAt(secureRandom.nextInt(char_special.length())));
		
		for(int i = 4; i < length; i++) {
			random.append(char_all.charAt(secureRandom.nextInt(char_all.length())));
		}
		
		List<String> collection = Arrays.asList(random.toString().split(""));
		
		while(true) {
			
			Collections.shuffle(collection);
			
			if(_CHAR_UPPER.contains(collection.get(0))) {
				break;
			}
			
			if(_CHAR_LOWER.contains(collection.get(0))) {
				break;
			}
			
		}// end of while
		
		return String.join("", collection);
	}// end of random_password
	public static String random_password(final int length) {
		return random_password(length, _CHAR_SPECIAL);
	}// end of random_password
	public static String random_password() {
		return random_password(16);
	}// end of random_password
	
	public static String pad_right(
			final String data
			, final int max_length
			, final String pad_char
			) {
		if(data == null) {
			return null;
		}
		if(data.length() > max_length) {
			return null;
		}
		return String
				.format(concat("%", String.valueOf(max_length), "s"), data)
				.replace(" ", pad_char)
				;
	}// end of pad_right
	public static String pad_right(
			final int data
			, final int max_length
			, final String pad_char
			) {
		return pad_right(String.valueOf(data), max_length, pad_char);
	}// end of pad_right
	
	public static String pad_left(
			final String data
			, final int max_length
			, final String pad_char
			) {
		if(data == null) {
			return null;
		}
		if(data.length() > max_length) {
			return null;
		}
		return String
				.format(concat("%-", String.valueOf(max_length), "s"), data)
				.replace(" ", pad_char)
				;
	}// end of pad_left
	public static String pad_left(
			final int data
			, final int max_length
			, final String pad_char
			) {
		return pad_left(String.valueOf(data), max_length, pad_char);
	}// end of pad_left
	
	public static String to_full_width(final String data) {
		if(is_empty(data)) {
			return null;
		}
		char[] char_array = data.toCharArray();
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
	
	public static String to_half_width(final String data) {
		if(is_empty(data)) {
			return null;
		}
		char[] char_array = data.toCharArray();
		char[] half_width = new char[char_array.length];
		for(int i = 0; i < char_array.length; i++) {
			if(char_array[i] == 12288) {
				half_width[i] = (char) 32;
				continue;
			}
			if(char_array[i] > 65280  && char_array[i] < 65375) {
				half_width[i] = (char) (char_array[i] - 65248);
				continue;
			}
			half_width[i] = char_array[i];
		}// end of characters
		return new String(half_width);
	}// end of to_half_width
	
	public static String byte_array_java_script(final byte[] data) {
		String script = "";
		if(data == null) {
			script = "byte[] byte_array = null;";
		} else if(0 == data.length) {
			script = "byte[] byte_array = null;";
		} else {
			StringBuffer stringBuffer = new StringBuffer();
			for(byte b : data) {
				stringBuffer.append(String.format(", (byte) 0x%02x", b));
			}
			script = String.format("byte[] byte_array = {%s};", stringBuffer.toString().substring(2));
		}
		return script;
	}// end of byte_array_java_script
	
	public static List<String> contains_list(final String value, final boolean contains) {
		List<String> contains_list = new ArrayList<>();
		if(value == null) {
			return contains_list;
		}
		for(String item : value.split(" ")) {
			if("".equals(item)) {
				continue;
			}
			if(contains && !item.startsWith("-")) {
				contains_list.add(item);
				continue;
			}
			if(!contains && item.startsWith("-")) {
				contains_list.add(item.substring(1));
				continue;
			}
		}
		return contains_list;
	}// end of contains_list
	
	public static String number_comma(final String value) {
		if(is_empty(value)) {
			return "";
		}
		try {
			return NumberFormat.getInstance().format(Long.parseLong(value));
		} catch (NumberFormatException e) {
			return "";
		}
	}// end of number_comma
	
	public static String pnu1111000(final String sido) {
		if(sido.contains("서울")) {
			return "1100000000";
		} else if(sido.contains("부산")) {
			return "2600000000";
		} else if(sido.contains("대구")) {
			return "2700000000";
		} else if(sido.contains("인천")) {
			return "2800000000";
		} else if(sido.contains("광주")) {
			return "2900000000";
		} else if(sido.contains("대전")) {
			return "3000000000";
		} else if(sido.contains("울산")) {
			return "3100000000";
		} else if(sido.contains("세종")) {
			return "3611000000";
		} else if(sido.contains("경기")) {
			return "4100000000";
		} else if(sido.contains("충청북도") || sido.contains("충북")) {
			return "4300000000";
		} else if(sido.contains("충청남도") || sido.contains("충남")) {
			return "4400000000";
		} else if(sido.contains("전라남도") || sido.contains("전남")) {
			return "4600000000";
		} else if(sido.contains("경상북도") || sido.contains("경북")) {
			return "4700000000";
		} else if(sido.contains("경상남도") || sido.contains("경남")) {
			return "4800000000";
		} else if(sido.contains("제주")) {
			return "5000000000";
		} else if(sido.contains("강원")) {
			return "5100000000";
		} else if(sido.contains("전라북도") || sido.contains("전북")) {
			return "5200000000";
		}
		return "";
	}// end of sido_no
	
}
