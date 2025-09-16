package seung.kimchi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import kong.unirest.UnirestException;
import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SHttpStatus;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SSamesite;

public class SHttp {

	public static int _S_DEFAULT_CONNECTION_TIMEOUT = 1000 * 3;
	public static int _S_DEFAULT_SOCKET_TIMEOUT = 1000 * 60;
	
	public static String encode_uri_component(
			final String data
			, final String charset
			) throws SException {
		
		try {
			return URLEncoder
					.encode(data, charset)
					.replaceAll("\\+", "%20")
					.replaceAll("\\%21", "!")
					.replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(")
					.replaceAll("\\%29", ")")
					.replaceAll("\\%7E", "~")
					;
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to encode uri.");
		}// end of try
		
	}// end of encode_uri_component
	public static String encode_uri_component(
			final String data
			, final Charset charset
			) throws SException {
		return encode_uri_component(data, charset.name());
	}// end of encode_uri_component
	
	public static String decode_uri(
			final String data
			, final String charset
			) throws SException {
		
		try {
			return URLDecoder
					.decode(data, charset)
					;
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to decode uri.");
		}// end of try
		
	}// end of decode_uri
	
	public static String content_disposition(
			final String user_agent
			, final String file_name
			) throws SException {
		
		try {
			
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
			
			return new StringBuilder()
					.append("attachment; filename=\"")
					.append(filename)
					.append("\"")
					.toString();
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to encode content disposition.");
		}// end of try
		
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
	
	public static String public_ip(
			final String url
			) throws SException {
		
		try {
			
			HttpResponse<String> httpResponse = Unirest
					.get(url)
					.connectTimeout(_S_DEFAULT_CONNECTION_TIMEOUT)
					.socketTimeout(_S_DEFAULT_SOCKET_TIMEOUT)
					.asString()
					;
			
			if(SHttpStatus._S_OK == httpResponse.getStatus() && httpResponse.getBody() != null) {
				return httpResponse.getBody();
			}
			
		} catch (UnirestException e) {
			throw new SException(e, "[UnirestException] Failed to get public ip.");
		}// end of try
		
		return "";
	}// end of public_ip
	public static String public_ip() throws SException {
		return public_ip("http://public.restful.kr/ipv4");
	}// end of public_ip
	
	public static String filename(
			Headers headers
			) throws SException {
		
		try {
			
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
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to get file name.");
		}// end of try
		
	}// end of filename
	
	public static String filename(
			Headers headers
			, String encoded_charset
			, String decoded_charset
			) throws SException {
		
		try {
			return new String(filename(headers).getBytes(encoded_charset), decoded_charset);
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to get file name.");
		}// end of try
		
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
			) throws SException {
		
		try {
			
			StringBuilder cookie = new StringBuilder();
			
			cookie
				.append(name)
				.append("=")
				.append(URLEncoder.encode(value, charset))
				;
			
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
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to build cookie.");
		}// end of try
		
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
			) throws SException {
		return cookie(name, value, SCharset._S_UTF_8, domain, path, timezone, locale, max_age, http_only, secure, same_site);
	}// end of cookie
	
	@SuppressWarnings("unchecked")
	public static HttpResponse<byte[]> get(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final Integer connection_timeout
			, final Integer socket_timeout
			, final SLinkedHashMap headers
			, final SLinkedHashMap query
			) throws SException {
		
		try {
			
			GetRequest getRequest = Unirest.get(url);
			
			if(!SText.is_empty(proxy_host)) {
				getRequest = getRequest.proxy(proxy_host, proxy_port);
			}
			
			if(connection_timeout != null) {
				getRequest = getRequest.connectTimeout(connection_timeout);
			} else {
				getRequest = getRequest.connectTimeout(_S_DEFAULT_CONNECTION_TIMEOUT);
			}
			
			if(socket_timeout != null) {
				getRequest = getRequest.socketTimeout(socket_timeout);
			} else {
				getRequest = getRequest.socketTimeout(_S_DEFAULT_SOCKET_TIMEOUT);
			}
			
			if(headers != null) {
				getRequest = getRequest.headers(headers);
			}
			
			if(query != null) {
				getRequest = getRequest.queryString(query);
			}
			
			return getRequest.asBytes();
			
		} catch (UnirestException e) {
			throw new SException(e, "[UnirestException] Failed to request.");
		}// end of try
		
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			, final String proxy_host
			, final int proxy_port
			) throws SException {
		return get(
				url
				, proxy_host
				, proxy_port
				, _S_DEFAULT_CONNECTION_TIMEOUT//connection_timeout
				, _S_DEFAULT_SOCKET_TIMEOUT//socket_timeout
				, null//headers
				, null//query
				);
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			, final SLinkedHashMap headers
			, final SLinkedHashMap query
			) throws SException {
		return get(
				url
				, ""//proxy_host
				, 0//proxy_port
				, _S_DEFAULT_CONNECTION_TIMEOUT//connection_timeout
				, _S_DEFAULT_SOCKET_TIMEOUT//socket_timeout
				, headers
				, query
				);
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			, final SLinkedHashMap headers
			) throws SException {
		return get(
				url
				, headers
				, null//query
				);
	}// end of get
	public static HttpResponse<byte[]> get(
			final String url
			) throws SException {
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
			, final Integer socket_timeout
			, final SLinkedHashMap headers
			, final String payload
			) throws SException {
		
		try {
			
			HttpRequestWithBody httpRequestWithBody = Unirest.post(url);
			
			if(!SText.is_empty(proxy_host)) {
				httpRequestWithBody = httpRequestWithBody.proxy(proxy_host, proxy_port);
			}
			
			if(connection_timeout != null) {
				httpRequestWithBody = httpRequestWithBody.connectTimeout(connection_timeout);
			} else {
				httpRequestWithBody = httpRequestWithBody.connectTimeout(_S_DEFAULT_CONNECTION_TIMEOUT);
			}
			
			if(socket_timeout != null) {
				httpRequestWithBody = httpRequestWithBody.socketTimeout(socket_timeout);
			} else {
				httpRequestWithBody = httpRequestWithBody.socketTimeout(_S_DEFAULT_SOCKET_TIMEOUT);
			}
			
			if(headers != null) {
				httpRequestWithBody = httpRequestWithBody.headers(headers);
			}
			
			if(payload == null) {
				return httpRequestWithBody.asBytes();
			}
			
			return httpRequestWithBody.body(payload).asBytes();
			
		} catch (UnirestException e) {
			throw new SException(e, "[UnirestException] Failed to request.");
		}// end of try
		
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final Integer connection_timeout
			, final Integer socket_timeout
			, final SLinkedHashMap headers
			, final SLinkedHashMap payload
			) throws JsonProcessingException, SException {
		return post(
				url
				, proxy_host
				, proxy_port
				, connection_timeout
				, socket_timeout
				, headers
				, payload == null ? "" : payload.stringify()//payload
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final String proxy_host
			, final int proxy_port
			, final SLinkedHashMap headers
			, final String payload
			) throws SException {
		return post(
				url
				, proxy_host
				, proxy_port
				, _S_DEFAULT_CONNECTION_TIMEOUT//connection_timeout
				, _S_DEFAULT_SOCKET_TIMEOUT//socket_timeout
				, headers
				, payload
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap headers
			, final String payload
			) throws SException {
		return post(
				url
				, ""//proxy_host
				, 0//proxy_port
				, _S_DEFAULT_CONNECTION_TIMEOUT//connection_timeout
				, _S_DEFAULT_SOCKET_TIMEOUT//socket_timeout
				, headers
				, payload
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap headers
			, final SLinkedHashMap payload
			) throws SException {
		return post(
				url
				, headers
				, payload == null ? "" : payload.stringify()//payload
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final String payload
			) throws SException {
		return post(
				url
				, null//headers
				, payload
				);
	}// end of post
	public static HttpResponse<byte[]> post(
			final String url
			, final SLinkedHashMap payload
			) throws SException {
		return post(
				url
				, payload == null ? "" : payload.stringify()//payload
				);
	}// end of post
	
}
