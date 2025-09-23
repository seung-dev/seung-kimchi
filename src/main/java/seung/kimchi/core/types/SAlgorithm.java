package seung.kimchi.core.types;

import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SAlgorithm {

	// MessageDigest
	public static final String _S_MD5 = "MD5";
	public static final String _S_SHA1 = "SHA-1";
	public static final String _S_SHA256 = "SHA-256";
	public static final String _S_SHA512 = "SHA-512";
	public static final String _S_HMAC_SHA256 = "HmacSHA256";
	
	// MGF(Mask Generation Function)
	public static final String _S_MGF1 = "MGF1";
	
	// RSA
	public static final String _S_RSA = "RSA";
	public static final String _S_RSA_ECB_OAEP_SHA256_MGF1PADDING = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
	
	// AES
	public static final String _S_AES = "AES";
	public static final String _S_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
	public static final String _S_AES_GCM_NOPADDING = "AES/GCM/NoPadding";
	
	// SEED
	public static final String _S_SEED_CBC_PKCS5PADDING = "SEED/CBC/PKCS5Padding";
	
	public static List<String> providers() {
		return Arrays.stream(Security.getProviders())
				.map(provider -> provider.getName())
				.distinct()
				.collect(Collectors.toList())
				;
	}// end of providers
	
	public static List<SLinkedHashMap> available() {
		return Arrays.stream(Security.getProviders())
				.flatMap(provider -> {
					return provider.getServices().stream()
						.map(service -> {
							return new SLinkedHashMap()
									.add("name", service.getAlgorithm())
									.add("type", service.getType())
									.add("class", service.getClassName())
									.add("provider_name", provider.getName())
									.add("provider_version", provider.getVersionStr())
									.add("provider_info", provider.getInfo())
									;
						});
				})
				.collect(Collectors.toList())
				;
	}// end of available
	
	public static List<String> types() {
		return Arrays.stream(Security.getProviders())
				.flatMap(provider -> provider.getServices().stream()
						.map(service -> service.getType())
						)
				.distinct()
				.collect(Collectors.toList())
				;
	}// end of algorithms
	
	/**
	 * providers:
	 *   - BC
	 * types:
	 *   - MessageDigest
	 *   - KeyFactory
	 *   - SSLContext
	 *   - Mac
	 *   - SecretKeyFactory
	 */
	public static List<SLinkedHashMap> algorithms(
			String include_name
			, String exclude_name
			, String include_type
			, String provider_name
			) {
		return Arrays.stream(Security.getProviders())
				.filter(provider -> provider_name == null || "".equals(provider_name) || provider.getName().toLowerCase().contains(provider_name.toLowerCase()))
				.flatMap(provider -> {
					return provider.getServices().stream()
						.filter(service -> include_name == null || "".equals(include_name) || service.getAlgorithm().toLowerCase().contains(include_name.toLowerCase()))
						.filter(service -> exclude_name == null || "".equals(exclude_name) || !service.getAlgorithm().toLowerCase().contains(exclude_name.toLowerCase()))
						.filter(service -> include_type == null || "".equals(include_type) || service.getType().toLowerCase().contains(include_type.toLowerCase()))
						.map(service -> {
							return new SLinkedHashMap()
								.add("name", service.getAlgorithm())
								.add("type", service.getType())
								.add("class", service.getClassName())
								.add("provider_name", provider.getName())
								.add("provider_version", provider.getVersionStr())
								.add("provider_info", provider.getInfo())
								;
					});
				})
				.collect(Collectors.toList())
				;
	}// end of algorithms
	
	public static List<SLinkedHashMap> message_digests() {
		return algorithms(
				""//include_name
				, ""//exclude_name
				, "MessageDigest"//include_type
				, BouncyCastleProvider.PROVIDER_NAME//provider_name
				);
	}// end of message_digests
	
}
