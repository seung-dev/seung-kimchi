package seung.kimchi;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class SText {

	public static final String _S_SPACE = " ";
	public static final String _S_EMPTY = "";
	public static final String _S_LF = "\n";
	public static final String _S_CR = "\r";
	
	public static boolean is_empty(final String data) {
		return data == null || data.length() == 0;
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
		return item_no(random_size, "", new Date());
	}
	public static String item_no() {
		return item_no(5, "I", new Date());
	}
	
	public static int random(final int min, final int max) {
		return new Random().nextInt(max - min + 1) + min;
	}// end of random
	
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
	
}
