package seung.kimchi;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import kong.unirest.HttpResponse;
import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SAlgorithm;
import seung.kimchi.types.SCharset;
import seung.kimchi.types.SHttpStatus;
import seung.kimchi.types.SLinkedHashMap;

public class SNcloud {

	public static String signature(
			final String method
			, final String path
			, final long timestamp
			, final String access_key
			, final String secret_key
			, final String charset
			) throws SException {
		
		try {
			
			String message = String.format("%s %s\n%d\n%s"
					, method
					, path
					, timestamp
					, access_key
					);
			
			return SFormat.encode_base64(
					SSecurity.hmac(
							SAlgorithm._S_HMAC_SHA256
							, BouncyCastleProvider.PROVIDER_NAME
							, secret_key.getBytes(charset)
							, message.getBytes(charset)
							)
					, charset
					);
			
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
	
}
