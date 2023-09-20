package seung.kimchi;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Provider.Service;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import seung.kimchi.types.SLinkedHashMap;

public class SSecurity {

	public static final String _S_MD2 = "MD2";
	public static final String _S_MD4 = "MD4";
	public static final String _S_MD5 = "MD5";
	public static final String _S_SHA1 = "SHA-1";
	public static final String _S_SHA256 = "SHA-256";
	public static final String _S_SHA512 = "SHA-512";
	
	public final static String _S_AES = "AES";
	public final static String _S_RSA = "AES";
	
	public final static String _S_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
	public final static String _S_RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
	public final static String _S_SEED_CBC_PKCS5PADDING = "SEED/CBC/PKCS5Padding";
	
	private static final int _S_XXTEA_DELTA = 0x9E3779B9;
	private static final int _S_XXTEA_BLOCK_SIZE = 8;
	
	public static List<SLinkedHashMap> providers() {
		List<SLinkedHashMap> providers = new ArrayList<>();
		List<SLinkedHashMap> services = new ArrayList<>();
		for(Provider provider : Security.getProviders()) {
			services.clear();
			for(Service service : provider.getServices()) {
				services.add(new SLinkedHashMap()
						.add("type", service.getType())
						.add("algorithm", service.getAlgorithm())
						.add("class", service.getClassName())
						);
			}// end of getServices
			providers.add(new SLinkedHashMap()
					.add("name", provider.getName())
					.add("version", provider.getVersion())
					.add("info", provider.getInfo())
					.add("services", services)
					);
		}// end of getProviders
		return providers;
	}// end of providers
	
	public static byte[] digest(
			byte[] data
			, String algorithm
			, String provider
			, int iteration
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		
		byte[] digest = null;
		
		MessageDigest messageDigest = null;
		if(provider == null) {
			messageDigest = MessageDigest.getInstance(algorithm);
		} else {
			messageDigest = MessageDigest.getInstance(algorithm, provider);
		}
		
		for(int i = 0; i < iteration; i++) {
			if(digest != null) {
				messageDigest.update(digest);
			}
			digest = messageDigest.digest();
		}
		return digest;
	}// end of digest
	public static byte[] digest(
			byte[] data
			, String algorithm
			, String provider
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		return digest(
				data
				, algorithm
				, provider
				, 1//iteration
				);
	}// end of digest
	public static byte[] digest(
			byte[] data
			, String algorithm
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		return digest(
				data
				, algorithm
				, null//provider
				);
	}// end of digest
	public static byte[] digest(
			byte[] data
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		return digest(
				data
				, _S_SHA256//algorithm
				);
	}// end of digest
	
	public static byte[] hmac(
			String algorithm
			, String provider
			, byte[] key
			, byte[] message
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
		
		Mac mac = null;
		
		if(provider != null) {
			mac = Mac.getInstance(algorithm, provider);
		} else {
			mac = Mac.getInstance(algorithm);
		}
		
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
		
		mac.init(secretKeySpec);
		
		return mac.doFinal(message);
	}// end of hmac
	public static byte[] hmac(
			String algorithm
			, String provider
			, String key
			, String message
			, Charset charset
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
		return hmac(algorithm, provider, key.getBytes(charset), message.getBytes(charset));
	}// end of hmac
	
	public static byte[] encrypt(
			byte[] data
			, Key key
			, String transformation
			, AlgorithmParameterSpec algorithm_parameter_spec
			, String provider
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = null;
		if(provider == null) {
			cipher = Cipher.getInstance(transformation);
		} else {
			cipher = Cipher.getInstance(transformation, provider);
		}
		
		if(algorithm_parameter_spec == null) {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, key, algorithm_parameter_spec);
		}
		
		return cipher.doFinal(data);
	}// end of encrypt
	public static byte[] encrypt(
			byte[] data
			, Key key
			, String transformation
			, AlgorithmParameterSpec algorithm_parameter_spec
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return encrypt(
				data
				, key
				, transformation
				, algorithm_parameter_spec
				, null//provider
				);
	}// end of encrypt
	public static byte[] encrypt(
			byte[] data
			, Key key
			, String transformation
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return encrypt(
				data
				, key
				, transformation
				, null//algorithm_parameter_spec
				, null//provider
				);
	}// end of encrypt
	public static byte[] encrypt(
			byte[] data
			, Key key
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return encrypt(
				data
				, key
				, _S_AES_CBC_PKCS5PADDING//transformation
				, null//algorithm_parameter_spec
				, null//provider
				);
	}// end of encrypt
	
	public static byte[] decrypt(
			byte[] data
			, Key key
			, String transformation
			, AlgorithmParameterSpec algorithm_parameter_spec
			, String provider
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = null;
		if(provider == null) {
			cipher = Cipher.getInstance(transformation);
		} else {
			cipher = Cipher.getInstance(transformation, provider);
		}
		
		if(algorithm_parameter_spec == null) {
			cipher.init(Cipher.DECRYPT_MODE, key);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key, algorithm_parameter_spec);
		}
		
		return cipher.doFinal(data);
	}// end of decrypt
	public static byte[] decrypt(
			byte[] data
			, Key key
			, String transformation
			, AlgorithmParameterSpec algorithm_parameter_spec
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return decrypt(
				data
				, key
				, transformation
				, algorithm_parameter_spec
				, null//provider
				);
	}// end of decrypt
	public static byte[] decrypt(
			byte[] data
			, Key key
			, String transformation
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return decrypt(
				data
				, key
				, transformation
				, null//algorithm_parameter_spec
				, null//provider
				);
	}// end of decrypt
	public static byte[] decrypt(
			byte[] data
			, Key key
			) throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return decrypt(
				data
				, key
				, _S_AES_CBC_PKCS5PADDING//transformation
				, null//algorithm_parameter_spec
				, null//provider
				);
	}// end of decrypt
	
	public static SecretKeySpec secret_key_spec(
			byte[] key
			, String algorithm
			) {
		return new SecretKeySpec(key, algorithm);
	}// end of secret_key_spec
	public static SecretKeySpec secret_key_spec(
			byte[] key
			) {
		return new SecretKeySpec(key, _S_AES);
	}// end of secret_key_spec
	
	public static byte[] secure_random_iv(
			int iv_size
			) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] iv = new byte[iv_size];
		secureRandom.nextBytes(iv);
		return iv;
	}// end of secure_random_iv
	
	public static AlgorithmParameterSpec iv_parameter_spec(
			byte[] iv
			) {
		if(iv == null) {
			return null;
		}
		return new IvParameterSpec(iv);
	}// end of iv_parameter_spec
	
	public static KeyPair keypair(
			String algorithm
			, String provider
			, int key_size
			) throws NoSuchAlgorithmException, NoSuchProviderException {
		
		KeyPairGenerator keyPairGenerator = null;
		
		if(provider == null) {
			keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		} else {
			keyPairGenerator = KeyPairGenerator.getInstance(
					algorithm
					, provider
					);
		}
		
		keyPairGenerator.initialize(
				key_size
				, new SecureRandom()
				);
		
		return keyPairGenerator.generateKeyPair();
	}// end of keypair
	
	public static int[] xxtea_int_array_bak(String data) {
		int data_length = data.length();
		int[] int_array = new int[(int) (Math.ceil(data_length / 4d))];
		for(int i = 0; i < int_array.length; i++) {
			int_array[i] = data.charAt(i * 4);
			for(int j = 1; j < 4; j++) {
				if(i * 4 + j < data_length) {
					int_array[i] += (data.charAt(i * 4 + j) << (j * _S_XXTEA_BLOCK_SIZE));
				}
			}
		}
		return int_array;
	}// end of xxtea_int_array_bak
	
	public static int[] xxtea_int_array(byte[] data) {
		int data_length = data.length;
		int[] int_array = new int[(data_length & 3) == 0 ? data_length >>> 2 : (data_length >>> 2) + 1];
		for(int i = 0; i < data_length; i++) {
			int_array[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return int_array;
	}// end of xxtea_int_array
	
	public static byte[] xxtea_byte_array_bak(int[] int_array) {
		int int_array_length = int_array.length;
		byte[] byte_array = new byte[int_array.length * 4];
		for(int i = 0; i < int_array_length; i++) {
			byte_array[i * 4] = Long.valueOf((int_array[i] & 0xff)).byteValue();
			for(int j = 1; j < 4; j++) {
				byte_array[i * 4 + j] = Long.valueOf((int_array[i] >>> (j * _S_XXTEA_BLOCK_SIZE) & 0xff)).byteValue();
			}
		}
		return byte_array;
	}// end of xxtea_byte_array_bak
	
	public static byte[] xxtea_byte_array(int[] int_array) {
		int byte_size = int_array.length << 2;
		byte[] byte_array = new byte[int_array.length << 2];
		for(int i = 0; i < byte_size; i++) {
			byte_array[i] = (byte) (int_array[i >>> 2] >>> ((i & 3) << 3));
		}
		return byte_array;
	}// end of xxtea_byte_array
	
	public static int[] xxtea_encrypt(int[] v, int[] k) {
		int n = v.length;
		int q = (int) Math.floor(6 + 52 / n);
		int z = v[n - 1];
		int y = v[0];
		int mx = 0;
		int e = 0;
		int sum = 0;
		while(q-- > 0) {
			sum += _S_XXTEA_DELTA;
			e = (int) sum >>> 2 & 3;
			for(int p = 0; p < n; p++) {
				y = v[(p + 1) % n];
				mx = ((int) z >>> 5 ^ (int) y << 2) + ((int) y >>> 3 ^ (int) z << 4) ^ ((int) sum ^ (int) y) + ((int) k[p & 3 ^ e] ^ (int) z);
				z = v[p] += mx;
			}
		}
		return v;
	}// end of xxtea_encrypt
	public static byte[] xxtea_encrypt(byte[] data, byte[] key) {
		if(key == null || key.length < 16) {
			return null;
		}
		if(data == null) {
			return null;
		}
		if(key.length > 16) {
			key = Arrays.copyOfRange(key, 0, 16);
		}
		return xxtea_byte_array(
				xxtea_encrypt(
						xxtea_int_array(key)//k
						, xxtea_int_array(data)//v
						)
				);
	}// end of xxtea_encrypt
	public static byte[] xxtea_encrypt(String plain_text, String secret) {
		return xxtea_encrypt(
				plain_text.getBytes()//data
				, secret.getBytes()//key
				);
	}// end of xxtea_encrypt
	
	public static int[] xxtea_decrypt(int[] v, int[] k) {
		int n = v.length;
		int q = (int) Math.floor(6 + 52 / n);
		int z = v[n - 1], y = v[0];
		int mx, e, sum = Long.valueOf(q * _S_XXTEA_DELTA).intValue();
		while(sum != 0) {
			e = sum >>> 2 & 3;
			for(int p = n - 1; p >= 0; p--) {
				z = v[p > 0 ? p - 1 : n - 1];
				mx = ((int) z >>> 5 ^ (int) y << 2) + ((int) y >>> 3 ^ (int) z << 4) ^ (sum ^ (int) y) + ((int) k[p & 3 ^ e] ^ (int) z);
				y = v[p] -= mx;
			}
			sum -= _S_XXTEA_DELTA;
		}
		return v;
	}// end of xxtea_decrypt
	
	public static byte[] xxtea_decrypt(byte[] encrypted, byte[] key) {
		if(key == null || key.length < 16) {
			return null;
		}
		if(encrypted == null) {
			return null;
		}
		if(key.length > 16) {
			key = Arrays.copyOfRange(key, 0, 16);
		}
		return xxtea_byte_array(
				xxtea_decrypt(
						xxtea_int_array(key)//k
						, xxtea_int_array(encrypted)//v
						)
				);
	}// end of xxtea_decrypt
	
}
