package seung.kimchi;

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
import seung.kimchi.types.ncloud.SNcloudMailBody;
import seung.kimchi.types.ncloud.SNcloudMailRecipient;
import seung.kimchi.types.ncloud.SNcloudMailTemplate;
import seung.kimchi.types.ncloud.SNcloudMessage;
import seung.kimchi.types.ncloud.SNcloudMessageBody;

public class SNcloud {

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
	
	public static String message(
			final String method
			, final String path
			, final long timestamp
			, final String access_key
			) {
		return new StringBuilder()
				.append(method)
				.append(" ")
				.append(path)
				.append("\n")
				.append(timestamp)
				.append("\n")
				.append(access_key)
				.toString()
				;
	}// end of meessage
	
	public static String signature(
			final byte[] message
			, final byte[] secret_key
			) throws SException {
		
		return SFormat.encode_base64(
				SSecurity.hmac(
						SAlgorithm._S_HMAC_SHA256
						, BouncyCastleProvider.PROVIDER_NAME
						, secret_key//key
						, message
						)
				, SCharset._S_UTF_8//charset
				);
	}// end of signature
	public static String signature(
			final String message
			, final byte[] secret_key
			) throws SException {
		
		return signature(
				SFormat.bytes(message)
				, secret_key
				);
	}// end of signature
	public static String signature(
			final String message
			, final String secret_key
			) throws SException {
		
		return signature(
				SFormat.bytes(message)//message
				, SFormat.bytes(secret_key)//key
				);
	}// end of signature
	
	public static SLinkedHashMap headers(
			final long timestamp
			, final String access_key
			, final String signature
			, final String lang
			) {
		
		return new SLinkedHashMap()
				.add(SHttpHeader._S_CONTENT_TYPE, SMediaType._S_APPLICATION_JSON)
				.add(_S_NCLOUD_HEADER_TIMESTAMP, Long.toString(timestamp))
				.add(_S_NCLOUD_HEADER_ACCESS_KEY, access_key)
				.add(_S_NCLOUD_HEADER_SIGNATURE_V2, signature)
				.add(_S_NCLOUD_HEADER_LANG, lang)
				;
	}// end of header
	public static SLinkedHashMap headers(
			final long timestamp
			, final String access_key
			, final String signature
			) {
		return headers(
				timestamp
				, access_key
				, signature
				, _S_NCLOUD_LANG_KR//lang
				);
	}// end of header
	public static SLinkedHashMap headers(
			final String method
			, final String path
			, final long timestamp
			, final String access_key
			, final String secret_key
			) throws SException {
		
		String message = message(
				method
				, path
				, timestamp
				, access_key
				);
		
		String signature = signature(
				message
				, secret_key
				);
		
		return headers(
				timestamp
				, access_key
				, signature
				);
	}// end of header
	
	public static HttpResponse<byte[]> send_mail_html(
			final String uri
			, final SLinkedHashMap headers
			, final boolean advertising
			, final String title
			, final String body
			, final String from_address
			, final String from_name
			, final List<SNcloudMailRecipient> recipients
			) throws SException {
		
		String payload = SNcloudMailBody.builder()
				.senderAddress(from_address)
				.senderName(from_name)
				.title(title)
				.body(body)
				.advertising(advertising)
				.recipients(recipients)
				.build()
				.stringify()
				;
		
		return SHttp.post(
				uri
				, headers
				, payload
				);
	}// end of send_mail_html
	public static HttpResponse<byte[]> send_mail_html(
			final String origin
			, final String method
			, final String path
			, final String access_key
			, final String secret_key
			, final boolean advertising
			, final String title
			, final String body
			, final String from_address
			, final String from_name
			, final List<SNcloudMailRecipient> recipients
			) throws SException {
		
		String uri = new StringBuilder()
				.append(origin)
				.append(path)
				.toString();
		
		long timestamp = System.currentTimeMillis();
		
		SLinkedHashMap headers = headers(
				method
				, path
				, timestamp
				, access_key
				, secret_key
				);
		
		return send_mail_html(
				uri
				, headers
				, advertising
				, title
				, body
				, from_address
				, from_name
				, recipients
				);
	}// end of send_mail_html
	public static HttpResponse<byte[]> send_mail_html(
			final String origin
			, final String method
			, final String path
			, final String access_key
			, final String secret_key
			, final boolean advertising
			, final String title
			, final String body
			, final String from_address
			, final String from_name
			, final String to_address
			) throws SException {
		
		String uri = new StringBuilder()
				.append(origin)
				.append(path)
				.toString();
		
		long timestamp = System.currentTimeMillis();
		
		SLinkedHashMap headers = headers(
				method
				, path
				, timestamp
				, access_key
				, secret_key
				);
		
		List<SNcloudMailRecipient> recipients = Arrays.asList(SNcloudMailRecipient.builder()
				.type(SNcloud._S_NCLOUD_TYPE_RECIPIENT)
				.address(to_address)
				.name("")
				.build()
				);
		
		return send_mail_html(
				uri
				, headers
				, advertising
				, title
				, body
				, from_address
				, from_name
				, recipients
				);
	}// end of send_mail_html
	
	public static HttpResponse<byte[]> send_mail_template(
			final String uri
			, final SLinkedHashMap headers
			, final boolean advertising
			, final String title
			, final String template_id
			, final String from_address
			, final String from_name
			, final List<SNcloudMailRecipient> recipients
			) throws SException {
		
		String payload = SNcloudMailTemplate.builder()
				.senderAddress(from_address)
				.senderName(from_name)
				.title(title)
				.templateSid(template_id)
				.advertising(advertising)
				.recipients(recipients)
				.build()
				.stringify()
				;
		
		return SHttp.post(
				uri
				, headers
				, payload
				);
	}// end of send_mail_template
	public static HttpResponse<byte[]> send_mail_template(
			final String origin
			, final String method
			, final String path
			, final String access_key
			, final String secret_key
			, final boolean advertising
			, final String title
			, final String template_id
			, final String from_address
			, final String from_name
			, final String to_address
			, final SLinkedHashMap parameters
			) throws SException {
		
		String uri = new StringBuilder()
				.append(origin)
				.append(path)
				.toString();
		
		long timestamp = System.currentTimeMillis();
		
		SLinkedHashMap headers = headers(
				method
				, path
				, timestamp
				, access_key
				, secret_key
				);
		
		List<SNcloudMailRecipient> recipients = Arrays.asList(SNcloudMailRecipient.builder()
				.type(SNcloud._S_NCLOUD_TYPE_RECIPIENT)
				.address(to_address)
				.name("")
				.parameters(parameters)
				.build()
				);
		
		return send_mail_template(
				uri
				, headers
				, advertising
				, title
				, template_id
				, from_address
				, from_name
				, recipients
				);
	}// end of send_mail_template
	
	public static HttpResponse<byte[]> send_message(
			final String uri
			, final SLinkedHashMap headers
			, final String type
			, final String from
			, final String content
			, final boolean advertising
			, final List<SNcloudMessage> messages
			) throws SException {
		
		SNcloudMessageBody body = SNcloudMessageBody.builder()
				.type(type)
				.contentType(advertising ? _S_NCLOUD_CONTENT_AD : _S_NCLOUD_CONTENT_COMM)
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
			final String origin
			, final String method
			, final String path
			, final String access_key
			, final String secret_key
			, final String type
			, final String from
			, final String content
			, final boolean advertising
			, final List<SNcloudMessage> messages
			) throws SException {
		
		String uri = new StringBuilder()
				.append(origin)
				.append(path)
				.toString();
		
		long timestamp = System.currentTimeMillis();
		
		SLinkedHashMap headers = headers(
				method
				, path
				, timestamp
				, access_key
				, secret_key
				);
		
		return send_message(
				uri
				, headers
				, type
				, from
				, content
				, advertising
				, messages
				);
	}// end of send_message
	public static HttpResponse<byte[]> send_message(
			final String origin
			, final String method
			, final String path
			, final String access_key
			, final String secret_key
			, final String type
			, final String from
			, final String content
			, final boolean advertising
			, final String to
			) throws SException {
		
		String uri = new StringBuilder()
				.append(origin)
				.append(path)
				.toString();
		
		long timestamp = System.currentTimeMillis();
		
		SLinkedHashMap headers = headers(
				method
				, path
				, timestamp
				, access_key
				, secret_key
				);
		
		List<SNcloudMessage> messages = Arrays.asList(SNcloudMessage.builder()
				.to(to)
				.build()
				);
		
		return send_message(
				uri
				, headers
				, type
				, from
				, content
				, advertising
				, messages
				);
	}// end of send_message
	
}
