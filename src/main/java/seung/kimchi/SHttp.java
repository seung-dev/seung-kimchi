package seung.kimchi;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import kong.unirest.GetRequest;
import kong.unirest.Headers;
import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SHttpStatus;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SSamesite;

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
		
		String filename = "";
		
		switch(browser(user_agent)) {
			case "MSIE":
				filename = URLEncoder.encode(file_name, SCharset._S_UTF_8).replaceAll("\\+", "%20");
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
				filename = sb.toString();
				break;
			case "Opera":
			case "Firefox":
			default:
				filename = new String(file_name.getBytes(SCharset._S_UTF_8), "8859_1");
				break;
		}
		
		return String.format("attachment; filename=\"%s\"", filename);
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
	
	public static String filename(
			Headers headers
			) throws UnsupportedEncodingException {
		
		if(headers == null) {
			return null;
		}
		
		if(!headers.containsKey(SHttpHeader._S_CONTENT_DISPOSITION)) {
			return null;
		}
		
		String content_disposition = headers.get(SHttpHeader._S_CONTENT_DISPOSITION).get(0);
		
		if(!content_disposition.contains("filename") || !content_disposition.contains("=")) {
			return null;
		}
		
		String filename = content_disposition.split("filename")[1].split("=")[1];
		if(filename.contains("\"")) {
			filename = filename.split("\"")[1];
		} else if(filename.contains("'")) {
			filename = filename.split("'")[1];
		}
		
		return URLDecoder.decode(SText.trim(filename), SCharset._S_UTF_8);
	}// end of filename
	
	/**
	 * 
	 * <h1>Usage</h1>
	 * <pre>
	 * SHttp.filename(headers, SCharset._S_ISO_8859_1, SCharset._S_EUC_KR);
	 * SHttp.filename(headers, SCharset._S_WINDOWS_1252, SCharset._S_EUC_KR);
	 * </pre>
	 * 
	 * @param headers
	 * @param encoded_charset
	 * @param decoded_charset
	 */
	public static String filename(
			Headers headers
			, String encoded_charset
			, String decoded_charset
			) throws UnsupportedEncodingException {
		return new String(filename(headers).getBytes(encoded_charset), decoded_charset);
	}// end of filename
	
	public static String cookie(
			String name
			, String value
			, String charset
			, String domain
			, String path
			, TimeZone timezone
			, Locale locale
			, long max_age
			, boolean http_only
			, boolean secure
			, SSamesite same_site
			) throws UnsupportedEncodingException {
		
		StringBuilder cookie = new StringBuilder();
		
		cookie.append(name).append("=").append(URLEncoder.encode(value, charset));
		
		if(!StringUtils.isEmpty(domain)) {
			cookie.append("; Domain=").append(domain);
		}
		
		if(!StringUtils.isEmpty(path)) {
			cookie.append("; Path=").append(path);
		}
		
		if(max_age > 0) {
			cookie.append("; Max-Age=").append(max_age / 1000);
			long expires = System.currentTimeMillis() + max_age;
			cookie.append("; Expires=").append(SDate.format("EEE, dd MMM yyyy HH:mm:ss zzz", new Date(expires), timezone, locale));
		}
		
		if(http_only) {
			cookie.append("; HttpOnly");
		}
		
		if(secure) {
			cookie.append("; Secure");
		}
		
		if(same_site != null) {
			cookie.append("; SameSite=").append(same_site.text());
		}
		
		return cookie.toString();
	}// end of cookie
	public static String cookie(
			String name
			, String value
			, String domain
			, String path
			, TimeZone timezone
			, Locale locale
			, long max_age
			, boolean http_only
			, boolean secure
			, SSamesite same_site
			) throws UnsupportedEncodingException {
		return cookie(name, value, SCharset._S_UTF_8, domain, path, timezone, locale, max_age, http_only, secure, same_site);
	}// end of cookie
	
	@SuppressWarnings("unchecked")
	public static HttpResponse<byte[]> get(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final int connection_timeout
			, final int socker_timeout
			, final SLinkedHashMap headers
			, final SLinkedHashMap query
			) {
		
		GetRequest getRequest = Unirest.get(url);
		
		if(!SText.is_empty(proxy_host)) {
			getRequest = getRequest.proxy(proxy_host, proxy_port);
		}
		
		getRequest = getRequest.connectTimeout(connection_timeout);
		
		getRequest = getRequest.socketTimeout(socker_timeout);
		
		if(headers != null) {
			getRequest = getRequest.headers(headers);
		}
		
		if(query != null) {
			getRequest = getRequest.queryString(query);
		}
		
		return getRequest.asBytes();
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			, final SLinkedHashMap headers
			, final SLinkedHashMap query
			) {
		return get(
				url
				, ""//proxy_host
				, 0//proxy_port
				, 1000 * 3//connection_timeout
				, 1000 * 60//socker_timeout
				, headers
				, query
				);
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			, final SLinkedHashMap headers
			) {
		return get(
				url
				, headers
				, null//query
				);
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			) {
		return get(
				url
				, null//headers
				, null//query
				);
	}// end of get
	
	@SuppressWarnings("unchecked")
	public static HttpResponse<byte[]> post(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final Integer connection_timeout
			, final Integer socker_timeout
			, final SLinkedHashMap headers
			, final String body
			) {
		
		HttpRequestWithBody httpRequestWithBody = Unirest.post(url);
		
		if(!SText.is_empty(proxy_host)) {
			httpRequestWithBody = httpRequestWithBody.proxy(proxy_host, proxy_port);
		}
		
		if(connection_timeout != null) {
			httpRequestWithBody = httpRequestWithBody.connectTimeout(connection_timeout);
		} else {
			httpRequestWithBody = httpRequestWithBody.connectTimeout(1000 * 3);
		}
		
		if(socker_timeout != null) {
			httpRequestWithBody = httpRequestWithBody.socketTimeout(socker_timeout);
		} else {
			httpRequestWithBody = httpRequestWithBody.socketTimeout(0);
		}
		
		if(headers != null) {
			httpRequestWithBody = httpRequestWithBody.headers(headers);
		}
		
		return httpRequestWithBody
				.body(body)
				.asBytes();
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final Integer connection_timeout
			, final Integer socker_timeout
			, final SLinkedHashMap headers
			, final SLinkedHashMap body
			) throws JsonProcessingException {
		return post(
				url
				, proxy_host
				, proxy_port
				, connection_timeout
				, socker_timeout
				, headers
				, body == null ? "" : body.stringify()//body
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap headers
			, final String body
			) throws JsonProcessingException {
		return post(
				url
				, ""//proxy_host
				, 0//proxy_port
				, 1000 * 3//connection_timeout
				, 1000 * 60//socker_timeout
				, headers
				, body
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap headers
			, final SLinkedHashMap body
			) throws JsonProcessingException {
		return post(
				url
				, headers
				, body == null ? "" : body.stringify()//body
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final String body
			) throws JsonProcessingException {
		return post(
				url
				, null//headers
				, body
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap body
			) throws JsonProcessingException {
		return post(
				url
				, body == null ? "" : body.stringify()//body
				);
	}// end of post
	
}
