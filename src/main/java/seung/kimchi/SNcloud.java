package seung.kimchi;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SAlgorithm;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpHeader;
import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.SMediaType;

public class SNcloud {

	private static final String _NCLOUD_HEADER_TIMESTAMP = "x-ncp-apigw-timestamp";
	private static final String _NCLOUD_HEADER_ACCESSKEY = "x-ncp-iam-access-key";
	private static final String _NCLOUD_HEADER_SIGNATURE = "x-ncp-apigw-signature-v2";
	private static final String _NCLOUD_HEADER_LANG = "x-ncp-lang";
	
	private static final String _NCLOUD_LANG_KO = "ko-KR";
	
	public static SLinkedHashMap request_header(
			long timestamp
			, String access_key
			, byte[] secret_key
			, byte[] message
			) throws SException {
		
		SLinkedHashMap request_header = new SLinkedHashMap();
		
		request_header.add(SHttpHeader._S_CONTENT_TYPE, SMediaType._S_APPLICATION_JSON);
		
		request_header.add(_NCLOUD_HEADER_TIMESTAMP, Long.toString(timestamp));
		request_header.add(_NCLOUD_HEADER_ACCESSKEY, access_key);
		request_header.add(_NCLOUD_HEADER_SIGNATURE
				, SFormat.encode_base64(
						SSecurity.hmac(
								SAlgorithm._S_HMAC_SHA256
								, BouncyCastleProvider.PROVIDER_NAME
								, secret_key
								, message
								)
						, SCharset._S_UTF_8
				));
		request_header.add(_NCLOUD_HEADER_LANG, _NCLOUD_LANG_KO);
		
		return request_header;
	}// end of request_header
	public static SLinkedHashMap request_header(
			long timestamp
			, String access_key
			, String secret_key
			, String charset
			, String method
			, String url
			) throws SException {
		
		try {
			
			return request_header(
					timestamp
					, access_key
					, secret_key.getBytes(charset)//secret_key
					, String.format("%s %s\n%d\n%s"
							, method
							, url
							, timestamp
							, access_key
							)
							.getBytes(charset)
							//message
					);
			
		} catch (UnsupportedEncodingException e) {
			throw new SException("Something went wrong.");
		}// end of try
		
	}// end of request_header
	
}
