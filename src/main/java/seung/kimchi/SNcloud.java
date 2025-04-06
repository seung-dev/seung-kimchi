package seung.kimchi;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import kong.unirest.HttpResponse;
import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SAlgorithm;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SMediaType;
import seung.kimchi.types.ncloud.SNcloudMailRecipient;
import seung.kimchi.types.ncloud.SNcloudMailTemplate;
import seung.kimchi.types.ncloud.SNcloudMessage;
import seung.kimchi.types.ncloud.SNcloudMessageBody;

public class SNcloud {

	public final static String _S_NCLOUD_METHOD = "POST";
	
	public final static String _S_NCLOUD_MAIL_HOST = "https://mail.apigw.ntruss.com";
	
	public final static String _S_NCLOUD_MAIL_ENDPOINT = "/api/v1/mails";
	
	public final static String _S_NCLOUD_MESSAGE_HOST = "https://sens.apigw.ntruss.com";
	
	public final static String _S_NCLOUD_MESSAGE_ENDPOINT = "/sms/v2/services/%s/messages";
	
	public static final String _S_NCLOUD_HEADER_TIMESTAMP = "x-ncp-apigw-timestamp";
	
	public static final String _S_NCLOUD_HEADER_ACCESS_KEY = "x-ncp-iam-access-key";
	
	public static final String _S_NCLOUD_HEADER_SIGNATURE_V2 = "x-ncp-apigw-signature-v2";
	
	public static final String _S_NCLOUD_HEADER_LANG = "x-ncp-lang";
	
	public static final String _S_NCLOUD_LANG_KR = "ko-KR";
	
	public final static String _S_NCLOUD_TYPE_RECIPIENT = "R";
	
	public final static String _S_NCLOUD_TYPE_CC = "C";
	
	public final static String _S_NCLOUD_TYPE_BCC = "B";
	
	public final static String _S_NCLOUD_TYPE_SMS = "SMS";
	
	public final static String _S_NCLOUD_TYPE_LMS = "LMS";
	
	public final static String _S_NCLOUD_TYPE_MMS = "MMS";
	
	public final static String _S_NCLOUD_CONTENT_COMM = "COMM";
	
	public final static String _S_NCLOUD_CONTENT_AD = "AD";
	
	public final static String _S_NCLOUD_COUNTRY_KR = "82";
	
	public static String mail_endpoint(
			) {
		return _S_NCLOUD_MAIL_ENDPOINT;
	}//end of mail_endpoint
	
	public static String mail_uri(
			final String endpoint
			) {
		return String.format("%s%s"
				, _S_NCLOUD_MAIL_HOST
				, endpoint
				);
	}//end of mail_uri
	
	public static String message_endpoint(
			final String service_id
			) {
		return String.format(_S_NCLOUD_MESSAGE_ENDPOINT, service_id);
	}//end of message_endpoint
	
	public static String message_uri(
			final String endpoint
			) {
		return String.format("%s%s"
				, _S_NCLOUD_MESSAGE_HOST
				, endpoint
				);
	}//end of message_uri
	
	public static SLinkedHashMap header(
			final long timestamp
			, final String method
			, final String endpoint
			, final String access_key
			, final String secret_key
			, final String lang
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
					.add(_S_NCLOUD_HEADER_TIMESTAMP, Long.toString(timestamp))
					.add(_S_NCLOUD_HEADER_ACCESS_KEY, access_key)
					.add(_S_NCLOUD_HEADER_SIGNATURE_V2, signature)
					.add(_S_NCLOUD_HEADER_LANG, lang)
					;
			
		} catch (UnsupportedEncodingException e) {
			throw new SException(e, "[UnsupportedEncodingException] Failed to create signature.");
		}// end of try
		
	}// end of header
	public static SLinkedHashMap header(
			final long timestamp
			, final String method
			, final String endpoint
			, final String access_key
			, final String secret_key
			) throws SException {
		return header(
				timestamp
				, method
				, endpoint
				, access_key
				, secret_key
				, _S_NCLOUD_LANG_KR
				, SCharset._S_UTF_8
				);
	}// end of header
	
	public static HttpResponse<byte[]> send_mail(
			final String access_key
			, final String secret_key
			, boolean ad
			, final String template_id
			, final String from_address
			, final String from_name
			, final String title
			, final List<SNcloudMailRecipient> recipients
			) throws SException {
		
		String endpoint = mail_endpoint();
		String uri = mail_uri(endpoint);
		
		long timestamp = System.currentTimeMillis();
		String method = "POST";
		
		SLinkedHashMap headers = SNcloud.header(
				timestamp
				, method
				, endpoint
				, access_key
				, secret_key
				);
		
		SNcloudMailTemplate body = SNcloudMailTemplate.builder()
				.advertising(ad)
				.templateSid(template_id)
				.senderAddress(from_address)
				.senderName(from_name)
				.title(title)
				.recipients(recipients)
				.build()
				;
		
		return SHttp.post(
				uri
				, headers
				, body.stringify()//payload
				);
	}// end of send_mail
	public static HttpResponse<byte[]> send_mail(
			final String access_key
			, final String secret_key
			, boolean ad
			, final String template_id
			, final String from_address
			, final String from_name
			, final String title
			, final String to_address
			, final SLinkedHashMap parameters
			) throws SException {
		
		return send_mail(
				access_key
				, secret_key
				, ad
				, template_id
				, from_address
				, from_name
				, title
				, Arrays.asList(SNcloudMailRecipient.builder()
						.type(_S_NCLOUD_TYPE_RECIPIENT)
						.address(to_address)
						.parameters(parameters)
						.build()
						)
				);
	}// end of send_mail
	
	public static HttpResponse<byte[]> send_message(
			final String access_key
			, final String secret_key
			, boolean ad
			, final String service_id
			, final String type
			, final String from
			, final String content
			, final List<SNcloudMessage> messages
			) throws SException {
		
		String endpoint = message_endpoint(service_id);
		String uri = message_uri(endpoint);
		
		long timestamp = System.currentTimeMillis();
		String method = "POST";
		
		SLinkedHashMap headers = SNcloud.header(
				timestamp
				, method
				, endpoint
				, access_key
				, secret_key
				);
		
		SNcloudMessageBody body = SNcloudMessageBody.builder()
				.type(type)
				.contentType(ad ? _S_NCLOUD_CONTENT_AD : _S_NCLOUD_CONTENT_COMM)
				.countryCode(_S_NCLOUD_COUNTRY_KR)
				.from(from)
				.content(content)
				.messages(messages)
				.build()
				;
		
		return SHttp.post(
				uri
				, headers
				, body.stringify()//payload
				);
	}// end of send_message
	public static HttpResponse<byte[]> send_message(
			final String access_key
			, final String secret_key
			, boolean ad
			, final String service_id
			, final String type
			, final String from
			, final String content
			, final String to
			) throws SException {
		
		return send_message(
				access_key
				, secret_key
				, ad
				, service_id
				, type
				, from
				, content
				, Arrays.asList(SNcloudMessage.builder()
						.to(to)
						.build()
						)
				);
	}// end of send_message
	
}
