package seung.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import seung.kimchi.exceptions.SException;

public class SFormat {

	public static long milliseconds(
			String value
			, long default_value
			) {
		if(SText.is_empty(value)) {
			return default_value;
		}
		if(value.length() < 2) {
			return default_value;
		}
		String unit = value.replaceAll("[\\d]", "");
		long number = Long.parseLong(value.replaceAll("[^\\d]", ""));
		switch(unit) {
			case "ms":
				return number;
			case "s":
				return number * 1000;
			case "m":
				return number * 1000 * 60;
			case "h":
				return number * 1000 * 60 * 60;
			case "d":
				return number * 1000 * 60 * 60 * 24;
			case "w":
				return number * 1000 * 60 * 60 * 24 * 7;
			default:
				throw new IllegalArgumentException("Unexpected value.");
		}
	}// end of ms
	
	public static byte[] encode_base64(final byte[] data) {
		return Base64.getEncoder().encode(data);
	}// end of encode_base64
	public static String encode_base64(final byte[] data, final Charset charset) {
		return new String(encode_base64(data), charset);
	}// end of encode_base64
	public static String encode_base64(final byte[] data, final String charset) {
		return encode_base64(data, Charset.forName(charset));
	}// end of encode_base64
	
	public static byte[] decode_base64(final byte[] data) {
		return Base64.getDecoder().decode(data);
	}// end of decode_base64
	public static String decode_base64(final byte[] data, final Charset charset) {
		return new String(decode_base64(data), charset);
	}// end of decode_base64
	public static String decode_base64(final byte[] data, final String charset) {
		return decode_base64(data, Charset.forName(charset));
	}// end of decode_base64
	
	public static byte[] decode_base64(final String data) {
		return Base64.getDecoder().decode(data);
	}// end of decode_base64
	
	public static String encode_hex(final byte[] data, final boolean to_lower_case) {
		return Hex.encodeHexString(data, to_lower_case);
	}// end of encode_hex
	public static String encode_hex(final byte[] data) {
		return encode_hex(data, true);
	}// end of encode_hex
	public static String encode_hex(final BigInteger data, final boolean to_lower_case) {
		return encode_hex(data.toByteArray(), to_lower_case);
	}// end of encode_hex
	public static String encode_hex(final BigInteger data) {
		return encode_hex(data, true);
	}// end of encode_hex
	
	public static byte[] decode_hex(final String data) throws SException {
		
		try {
			return Hex.decodeHex(data);
		} catch (DecoderException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
	}// end of decode_hex
	
	public static byte[] compress(
			final byte[] data
			, final int level
			, final boolean nowrap
			) throws IOException {
		
		byte[] deflated = null;
		
		Deflater deflater = new Deflater(level, nowrap);
		
		deflater.setInput(data);
		deflater.finish();
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!deflater.finished()) {
				len = deflater.deflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}// end of while
			
			deflated = byteArrayOutputStream.toByteArray();
			deflater.end();
			
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return deflated;
	}// end of compress
	public static byte[] compress(final byte[] data) throws IOException {
		return compress(data, Deflater.BEST_COMPRESSION, true);
	}// end of compress
	
	public static byte[] decompress(
			final byte[] data
			, final boolean nowrap
			) throws IOException, DataFormatException {
		
		byte[] inflated = null;
		
		Inflater inflater = new Inflater(nowrap);
		
		inflater.setInput(data);
		
		byte[] b = new byte[1024];
		int len;
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			while(!inflater.finished()) {
				len = inflater.inflate(b);
				byteArrayOutputStream.write(b, 0, len);
			}// end of while
			
			inflated = byteArrayOutputStream.toByteArray();
			inflater.end();
			
		} catch (IOException e) {
			throw e;
		} catch (DataFormatException e) {
			throw e;
		}// end of try
		
		return inflated;
	}// end of decompress
	public static byte[] decompress(final byte[] data) throws IOException, DataFormatException {
		return decompress(data, true);
	}// end of decompress
	
	public static boolean regex(
			String expression
			, String value
			) {
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}// end of regex
	
}
