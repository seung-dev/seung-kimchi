package seung.kimchi;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import kong.unirest.Headers;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SHttpStatus;

public class SHttp {

	public static String nslookup(
			final String host
			) throws UnknownHostException {
		
		String ipv4 = "";
		
		InetAddress[] inetAddresses = InetAddress.getAllByName(host);
		
		for(InetAddress inetAddress : inetAddresses) {
			ipv4 = inetAddress.getHostAddress();
			break;
		}
		
		return ipv4;
	}// end of nslookup
	
	public static String encode_uri_component(
			final String data
			, final String charset
			) throws UnsupportedEncodingException {
		return URLEncoder
				.encode(data, charset)
				.replaceAll("\\+", "%20")
				.replaceAll("\\%21", "!")
				.replaceAll("\\%27", "'")
				.replaceAll("\\%28", "(")
				.replaceAll("\\%29", ")")
				.replaceAll("\\%7E", "~")
				;
	}// end of encode_uri_component
	public static String encode_uri_component(
			final String data
			, final Charset charset
			) throws UnsupportedEncodingException {
		return encode_uri_component(data, charset.name());
	}// end of encode_uri_component
	
	public static String decode_uri(
			final String data
			, final String charset
			) throws UnsupportedEncodingException {
		return URLDecoder
				.decode(data, charset)
				;
	}// end of decode_uri
	
	public static String content_disposition(
			final String user_agent
			, final String file_name
			) throws UnsupportedEncodingException {
		
		String prefix = "attachment; file_name=";
		String suffix = "";
		
		switch(browser(user_agent)) {
			case "MSIE":
				suffix = URLEncoder.encode(file_name, SCharset._S_UTF_8).replaceAll("\\+", "%20");
				break;
			case "Chrome":
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < file_name.length(); i++) {
					char c = file_name.charAt(i);
					if(c > '~') {
						sb.append(URLEncoder.encode("" + c, SCharset._S_UTF_8));
					} else {
						sb.append(c);
					}
				}
				suffix = sb.toString();
				break;
			case "Opera":
			case "Firefox":
			default:
				suffix = "\"" + new String(file_name.getBytes(SCharset._S_UTF_8), "8859_1") +"\"";
				break;
		}
		
		return prefix.concat(suffix);
	}// end of content_disposition
	
	public static String browser(final String user_agent) {
		if(user_agent.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if(user_agent.indexOf("Trident") > -1) {
			return "MSIE";
		} else if(user_agent.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if(user_agent.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}// end of browser
	
	public static String public_ip(final String url) throws InterruptedException, UnsupportedEncodingException {
		
		HttpResponse<String> httpResponse = Unirest
				.get(url)
				.connectTimeout(1000 * 3)
				.socketTimeout(1000 * 60)
				.asString()
				;
		
		if(SHttpStatus._S_OK == httpResponse.getStatus() && httpResponse.getBody() != null) {
			return httpResponse.getBody();
		}
		
		return null;
	}// end of public_ip
	public static String public_ip() throws InterruptedException, UnsupportedEncodingException {
		return public_ip("http://public.restful.kr/ipv4");
	}// end of public_ip
	
	public static String file_name(
			Headers headers
			) throws UnsupportedEncodingException {
		
		if(headers == null) {
			return null;
		}
		
		if(!headers.containsKey(SHttpHeader._S_CONTENT_DISPOSITION)) {
			return null;
		}
		
		String content_disposition = headers.get(SHttpHeader._S_CONTENT_DISPOSITION).get(0);
		
		if(!content_disposition.contains("filename=\"")) {
			return null;
		}
		
		return URLDecoder.decode(content_disposition.split("filename=\"")[1].split("\"")[0], SCharset._S_UTF_8);
	}// end of file_name
	
}
