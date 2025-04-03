package seung.kimchi;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import kong.unirest.HttpResponse;
import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SAlgorithm;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SHttpStatus;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SMediaType;
import seung.kimchi.types.SNcloudMessage;
import seung.kimchi.types.SNcloudMessageType;

public class SNcloud {

	public final static String _S_MESSAGE_SMS = "SMS";
	
	public final static String _S_MESSAGE_LMS = "LMS";
	
	public final static String _S_MESSAGE_MMS = "MMS";
	
	public final static String _S_MESSAGE_NCLOUD_COMM = "COMM";
	
	public final static String _S_MESSAGE_NCLOUD_AD = "AD";
	
	public static SLinkedHashMap headers(
			final long timestamp
			, final String method
			, final String endpoint
			, final String access_key
			, final String secret_key
			, final String charset
			) throws SException {
		
		try {
			
			String message = String.format("%s %s\n%d\n%s"
					, method
					, endpoint
					, timestamp
					, access_key
					);
			
			String signature = SFormat.encode_base64(
					SSecurity.hmac(
							SAlgorithm._S_HMAC_SHA256
							, BouncyCastleProvider.PROVIDER_NAME
							, secret_key.getBytes(charset)
							, message.getBytes(charset)
							)
					, charset
					);
			
			return new SLinkedHashMap()
					.add(SHttpHeader._S_CONTENT_TYPE, SMediaType._S_APPLICATION_JSON)
					.add(SHttpHeader._S_NCLOUD_TIMESTAMP, Long.toString(timestamp))
					.add(SHttpHeader._S_NCLOUD_ACCESS_KEY, access_key)
					.add(SHttpHeader._S_NCLOUD_SIGNATURE_V2, signature)
					;
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to create signature.");
		}// end of try
		
	}// end of signature
	
	public static String request(
			final String url
			, final SLinkedHashMap request_header
			, final SLinkedHashMap request_body
			) throws SException {
		
		String response_text = null;
		
		try {
			
			while(true) {
				
				HttpResponse<byte[]> httpResponse = SHttp.post(
						url//url
						, request_header//headers
						, request_body.stringify()//payload
						);
				
				int status_code = httpResponse.getStatus();
				if(SHttpStatus._S_OK != status_code) {
					throw new SException("Failed to request ncloud.");
				}
				
				byte[] response_body = httpResponse.getBody();
				
				if(response_body == null) {
					throw new SException("Failed to request ncloud.");
				}
				
				response_text = new String(response_body, SCharset._S_UTF_8);
				
				break;
			}// end of while
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to request.");
		}// end of try
		
		return response_text;
	}// end of request
	
	public static SLinkedHashMap build_message(
			final SNcloudMessageType type
			, final String country_code
			, final String from
			, final List<SNcloudMessage> messages
			, final String reserve_time
			, final String reserve_timezone
			) throws SException {
		
		SLinkedHashMap message = new SLinkedHashMap();
		
		message.add("type", type.id());
		message.add("contentType", type.type());
		message.add("countryCode", country_code);
		message.add("from", from);
		message.add("subject", "");
		message.add("content", messages.get(0).content());
		message.add("messages", messages);
		if(!SText.is_empty(reserve_time)) {
			message.add("reserveTime", reserve_time);
		}
		if(!SText.is_empty(reserve_timezone)) {
			message.add("reserveTimeZone", reserve_timezone);
		}
		
		return message;
	}// end of build_message
	public static SLinkedHashMap build_message(
			final SNcloudMessageType type
			, final String from
			, final String to
			, final String subject
			, final String content
			, final String reserve_time
			, final String reserve_timezone
			) throws SException {
		return build_message(
				type
				, "82"//country_code
				, from
				, List.of(SNcloudMessage.builder()
						.to(to)
						.subject(subject)
						.content(content)
						.build()
						)
				, reserve_time
				, reserve_timezone
				);
	}// end of build_message
	public static SLinkedHashMap build_message(
			final SNcloudMessageType type
			, final String from
			, final String to
			, final String content
			) throws SException {
		return build_message(
				type
				, from
				, to
				, ""//subject
				, content
				, ""//reserve_time
				, ""//reserve_timezone
				);
	}// end of build_message
	
	public static String send_message(
			final String service_id
			, final String access_key
			, final String secret_key
			, final SLinkedHashMap body
			) throws SException {
		
		String response_text = null;
		
		try {
			
			long timestamp = System.currentTimeMillis();
			String method = "POST";
			String endpoint = String.format("/sms/v2/services/%s/messages", service_id);
			String url = String.format("https://sens.apigw.ntruss.com%s", endpoint);
			
			SLinkedHashMap headers = SNcloud.headers(
					timestamp
					, method
					, endpoint
					, access_key
					, secret_key
					, SCharset._S_UTF_8//charset
					);
			
			System.out.println(headers.stringify(true));
			
			while(true) {
				
				HttpResponse<byte[]> httpResponse = SHttp.post(
						url
						, headers
						, body.stringify()//payload
						);
				
				int status_code = httpResponse.getStatus();
				System.out.println(status_code);
				System.out.println(httpResponse.getStatusText());
				if(SHttpStatus._S_ACCEPTED != status_code) {
					throw new SException("Failed to request ncloud.");
				}
				
				byte[] response_body = httpResponse.getBody();
				
				if(response_body == null) {
					throw new SException("Failed to request ncloud.");
				}
				
				response_text = new String(response_body, SCharset._S_UTF_8);
				
				break;
			}// end of while
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to request.");
		}// end of try
		
		return response_text;
	}// end of request
	
}
