package seung.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import seung.kimchi.core.SText;
import seung.kimchi.core.types.SCharset;
import seung.kimchi.core.types.SException;

public class SFormat {

	public static BigDecimal decimal(double value) {
		return new BigDecimal(value);
	}// end of decimal
	
	public static String date(
			final String format
			, final Instant datetime
			, final ZoneId zone
			) {
		return DateTimeFormatter
				.ofPattern(format)
				.withZone(zone)
				.format(datetime)
				;
	}// end of date
	public static String date(
			final String format
			, final long epoch
			, final ZoneId zone
			) {
		return DateTimeFormatter
				.ofPattern(format)
				.withZone(zone)
				.format(Instant.ofEpochMilli(epoch))
				;
	}// end of date
	public static String date(
			final String format
			, final long epoch
			) {
		return date(format, epoch, ZoneId.systemDefault());
	}// end of date
	public static String date(
			final String format
			) {
		return date(format, Instant.now(), ZoneId.systemDefault());
	}// end of date
	public static String date(
			final long epoch
			) {
		return date("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", epoch, ZoneId.systemDefault());
	}// end of date
	
	public static long milliseconds(
			String value
			, long default_value
			) throws SException {
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
				throw new SException("[IllegalArgumentException] Invalid argument.");
		}
	}// end of ms
	
	public static byte[] bytes(final String value, final String charset) throws SException {
		if(SText.is_empty(value)) {
			throw new SException("[IllegalArgumentException] Invalid argument.");
		}
		try {
			return value.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to convert to byte array.");
		}
	}// end of bytes
	public static byte[] bytes(final String value) throws SException {
		return bytes(value, SCharset._S_UTF_8);
	}// end of bytes
	
	public static String text(final byte[] value, final String charset) throws SException {
		try {
			return new String(value, charset);
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to convert to text.");
		}
	}// end of text
	public static String text(final byte[] value) throws SException {
		return text(value, SCharset._S_UTF_8);
	}// end of text
	
	public static String underscore(final String value) {
		if(value == null || value.isBlank()) {
			return value;
		}
		return value
				.replaceAll("([a-z])([A-Z])", "$1_$2")
				.replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
				.toLowerCase();
	}// end of underscore
	
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
			throw new SException(e, "[DecoderException] Failed to decode hex.");
		}// end of try
		
	}// end of decode_hex
	
	public static String encode_32(final byte[] data) {
		BigInteger bigInteger = new BigInteger(1, data);
		return bigInteger.toString(32);
	}// end of encode_hex
	
	public static byte[] compress(
			final byte[] data
			, final int level
			, final boolean nowrap
			) throws SException {
		
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
			throw new SException(e, "[IOException] Failed to compress value.");
		}// end of try
		
		return deflated;
	}// end of compress
	public static byte[] compress(final byte[] data) throws SException {
		return compress(data, Deflater.BEST_COMPRESSION, true);
	}// end of compress
	
	public static byte[] decompress(
			final byte[] data
			, final boolean nowrap
			) throws SException {
		
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
			throw new SException(e, "[IOException] Failed to decompress value.");
		} catch (DataFormatException e) {
			throw new SException(e, "[DataFormatException] Failed to decompress value.");
		}// end of try
		
		return inflated;
	}// end of decompress
	public static byte[] decompress(final byte[] data) throws SException {
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
