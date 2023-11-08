package seung.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class SConvert {

	public static final String _S_BIG5 = "Big5";
	public static final String _S_BIG5_HKSCS = "Big5-HKSCS";
	public static final String _S_CESU_8 = "CESU-8";
	public static final String _S_EUC_JP = "EUC-JP";
	public static final String _S_EUC_KR = "EUC-KR";
	public static final String _S_GB18030 = "GB18030";
	public static final String _S_GB2312 = "GB2312";
	public static final String _S_GBK = "GBK";
	public static final String _S_IBM_THAI = "IBM-Thai";
	public static final String _S_IBM00858 = "IBM00858";
	public static final String _S_IBM01140 = "IBM01140";
	public static final String _S_IBM01141 = "IBM01141";
	public static final String _S_IBM01142 = "IBM01142";
	public static final String _S_IBM01143 = "IBM01143";
	public static final String _S_IBM01144 = "IBM01144";
	public static final String _S_IBM01145 = "IBM01145";
	public static final String _S_IBM01146 = "IBM01146";
	public static final String _S_IBM01147 = "IBM01147";
	public static final String _S_IBM01148 = "IBM01148";
	public static final String _S_IBM01149 = "IBM01149";
	public static final String _S_IBM037 = "IBM037";
	public static final String _S_IBM1026 = "IBM1026";
	public static final String _S_IBM1047 = "IBM1047";
	public static final String _S_IBM273 = "IBM273";
	public static final String _S_IBM277 = "IBM277";
	public static final String _S_IBM278 = "IBM278";
	public static final String _S_IBM280 = "IBM280";
	public static final String _S_IBM284 = "IBM284";
	public static final String _S_IBM285 = "IBM285";
	public static final String _S_IBM290 = "IBM290";
	public static final String _S_IBM297 = "IBM297";
	public static final String _S_IBM420 = "IBM420";
	public static final String _S_IBM424 = "IBM424";
	public static final String _S_IBM437 = "IBM437";
	public static final String _S_IBM500 = "IBM500";
	public static final String _S_IBM775 = "IBM775";
	public static final String _S_IBM850 = "IBM850";
	public static final String _S_IBM852 = "IBM852";
	public static final String _S_IBM855 = "IBM855";
	public static final String _S_IBM857 = "IBM857";
	public static final String _S_IBM860 = "IBM860";
	public static final String _S_IBM861 = "IBM861";
	public static final String _S_IBM862 = "IBM862";
	public static final String _S_IBM863 = "IBM863";
	public static final String _S_IBM864 = "IBM864";
	public static final String _S_IBM865 = "IBM865";
	public static final String _S_IBM866 = "IBM866";
	public static final String _S_IBM868 = "IBM868";
	public static final String _S_IBM869 = "IBM869";
	public static final String _S_IBM870 = "IBM870";
	public static final String _S_IBM871 = "IBM871";
	public static final String _S_IBM918 = "IBM918";
	public static final String _S_ISO_2022_CN = "ISO-2022-CN";
	public static final String _S_ISO_2022_JP = "ISO-2022-JP";
	public static final String _S_ISO_2022_JP_2 = "ISO-2022-JP-2";
	public static final String _S_ISO_2022_KR = "ISO-2022-KR";
	public static final String _S_ISO_8859_1 = "ISO-8859-1";
	public static final String _S_ISO_8859_13 = "ISO-8859-13";
	public static final String _S_ISO_8859_15 = "ISO-8859-15";
	public static final String _S_ISO_8859_2 = "ISO-8859-2";
	public static final String _S_ISO_8859_3 = "ISO-8859-3";
	public static final String _S_ISO_8859_4 = "ISO-8859-4";
	public static final String _S_ISO_8859_5 = "ISO-8859-5";
	public static final String _S_ISO_8859_6 = "ISO-8859-6";
	public static final String _S_ISO_8859_7 = "ISO-8859-7";
	public static final String _S_ISO_8859_8 = "ISO-8859-8";
	public static final String _S_ISO_8859_9 = "ISO-8859-9";
	public static final String _S_JIS_X0201 = "JIS_X0201";
	public static final String _S_JIS_X0212_1990 = "JIS_X0212-1990";
	public static final String _S_KOI8_R = "KOI8-R";
	public static final String _S_KOI8_U = "KOI8-U";
	public static final String _S_SHIFT_JIS = "Shift_JIS";
	public static final String _S_TIS_620 = "TIS-620";
	public static final String _S_US_ASCII = "US-ASCII";
	public static final String _S_UTF_16 = "UTF-16";
	public static final String _S_UTF_16BE = "UTF-16BE";
	public static final String _S_UTF_16LE = "UTF-16LE";
	public static final String _S_UTF_32 = "UTF-32";
	public static final String _S_UTF_32BE = "UTF-32BE";
	public static final String _S_UTF_32LE = "UTF-32LE";
	public static final String _S_UTF_8 = "UTF-8";
	public static final String _S_WINDOWS_1250 = "windows-1250";
	public static final String _S_WINDOWS_1251 = "windows-1251";
	public static final String _S_WINDOWS_1252 = "windows-1252";
	public static final String _S_WINDOWS_1253 = "windows-1253";
	public static final String _S_WINDOWS_1254 = "windows-1254";
	public static final String _S_WINDOWS_1255 = "windows-1255";
	public static final String _S_WINDOWS_1256 = "windows-1256";
	public static final String _S_WINDOWS_1257 = "windows-1257";
	public static final String _S_WINDOWS_1258 = "windows-1258";
	public static final String _S_WINDOWS_31J = "windows-31j";
	public static final String _S_X_BIG5_HKSCS_2001 = "x-Big5-HKSCS-2001";
	public static final String _S_X_BIG5_SOLARIS = "x-Big5-Solaris";
	public static final String _S_X_EUC_JP_LINUX = "x-euc-jp-linux";
	public static final String _S_X_EUC_TW = "x-EUC-TW";
	public static final String _S_X_EUCJP_OPEN = "x-eucJP-Open";
	public static final String _S_X_IBM1006 = "x-IBM1006";
	public static final String _S_X_IBM1025 = "x-IBM1025";
	public static final String _S_X_IBM1046 = "x-IBM1046";
	public static final String _S_X_IBM1097 = "x-IBM1097";
	public static final String _S_X_IBM1098 = "x-IBM1098";
	public static final String _S_X_IBM1112 = "x-IBM1112";
	public static final String _S_X_IBM1122 = "x-IBM1122";
	public static final String _S_X_IBM1123 = "x-IBM1123";
	public static final String _S_X_IBM1124 = "x-IBM1124";
	public static final String _S_X_IBM1166 = "x-IBM1166";
	public static final String _S_X_IBM1364 = "x-IBM1364";
	public static final String _S_X_IBM1381 = "x-IBM1381";
	public static final String _S_X_IBM1383 = "x-IBM1383";
	public static final String _S_X_IBM300 = "x-IBM300";
	public static final String _S_X_IBM33722 = "x-IBM33722";
	public static final String _S_X_IBM737 = "x-IBM737";
	public static final String _S_X_IBM833 = "x-IBM833";
	public static final String _S_X_IBM834 = "x-IBM834";
	public static final String _S_X_IBM856 = "x-IBM856";
	public static final String _S_X_IBM874 = "x-IBM874";
	public static final String _S_X_IBM875 = "x-IBM875";
	public static final String _S_X_IBM921 = "x-IBM921";
	public static final String _S_X_IBM922 = "x-IBM922";
	public static final String _S_X_IBM930 = "x-IBM930";
	public static final String _S_X_IBM933 = "x-IBM933";
	public static final String _S_X_IBM935 = "x-IBM935";
	public static final String _S_X_IBM937 = "x-IBM937";
	public static final String _S_X_IBM939 = "x-IBM939";
	public static final String _S_X_IBM942 = "x-IBM942";
	public static final String _S_X_IBM942C = "x-IBM942C";
	public static final String _S_X_IBM943 = "x-IBM943";
	public static final String _S_X_IBM943C = "x-IBM943C";
	public static final String _S_X_IBM948 = "x-IBM948";
	public static final String _S_X_IBM949 = "x-IBM949";
	public static final String _S_X_IBM949C = "x-IBM949C";
	public static final String _S_X_IBM950 = "x-IBM950";
	public static final String _S_X_IBM964 = "x-IBM964";
	public static final String _S_X_IBM970 = "x-IBM970";
	public static final String _S_X_ISCII91 = "x-ISCII91";
	public static final String _S_X_ISO_2022_CN_CNS = "x-ISO-2022-CN-CNS";
	public static final String _S_X_ISO_2022_CN_GB = "x-ISO-2022-CN-GB";
	public static final String _S_X_ISO_8859_11 = "x-iso-8859-11";
	public static final String _S_X_JIS0208 = "x-JIS0208";
	public static final String _S_X_JISAUTODETECT = "x-JISAutoDetect";
	public static final String _S_X_JOHAB = "x-Johab";
	public static final String _S_X_MACARABIC = "x-MacArabic";
	public static final String _S_X_MACCENTRALEUROPE = "x-MacCentralEurope";
	public static final String _S_X_MACCROATIAN = "x-MacCroatian";
	public static final String _S_X_MACCYRILLIC = "x-MacCyrillic";
	public static final String _S_X_MACDINGBAT = "x-MacDingbat";
	public static final String _S_X_MACGREEK = "x-MacGreek";
	public static final String _S_X_MACHEBREW = "x-MacHebrew";
	public static final String _S_X_MACICELAND = "x-MacIceland";
	public static final String _S_X_MACROMAN = "x-MacRoman";
	public static final String _S_X_MACROMANIA = "x-MacRomania";
	public static final String _S_X_MACSYMBOL = "x-MacSymbol";
	public static final String _S_X_MACTHAI = "x-MacThai";
	public static final String _S_X_MACTURKISH = "x-MacTurkish";
	public static final String _S_X_MACUKRAINE = "x-MacUkraine";
	public static final String _S_X_MS932_0213 = "x-MS932_0213";
	public static final String _S_X_MS950_HKSCS = "x-MS950-HKSCS";
	public static final String _S_X_MS950_HKSCS_XP = "x-MS950-HKSCS-XP";
	public static final String _S_X_MSWIN_936 = "x-mswin-936";
	public static final String _S_X_PCK = "x-PCK";
	public static final String _S_X_SJIS_0213 = "x-SJIS_0213";
	public static final String _S_X_UTF_16LE_BOM = "x-UTF-16LE-BOM";
	public static final String _S_X_UTF_32BE_BOM = "X-UTF-32BE-BOM";
	public static final String _S_X_UTF_32LE_BOM = "X-UTF-32LE-BOM";
	public static final String _S_X_WINDOWS_50220 = "x-windows-50220";
	public static final String _S_X_WINDOWS_50221 = "x-windows-50221";
	public static final String _S_X_WINDOWS_874 = "x-windows-874";
	public static final String _S_X_WINDOWS_949 = "x-windows-949";
	public static final String _S_X_WINDOWS_950 = "x-windows-950";
	public static final String _S_X_WINDOWS_ISO2022JP = "x-windows-iso2022jp";
	
	public static String[] charsets() {
		return Charset.availableCharsets().keySet().toArray(new String[0]);
	}// end of charsets
	
	public static String throwable(final Throwable throwable) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter, true);
		throwable.printStackTrace(printWriter);
		return stringWriter.getBuffer().toString();
	}// end of throwable
	
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
	}// end of encode_base64
	public static String decode_base64(final byte[] data, final Charset charset) {
		return new String(decode_base64(data), charset);
	}// end of encode_base64
	public static String decode_base64(final byte[] data, final String charset) {
		return decode_base64(data, Charset.forName(charset));
	}// end of encode_base64
	
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
	
	public static byte[] decode_hex(final String data) throws DecoderException {
		return Hex.decodeHex(data);
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
	
}
