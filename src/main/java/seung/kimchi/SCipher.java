package seung.kimchi;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import seung.kimchi.exceptions.SException;
import seung.kimchi.types.STransformation;

public class SCipher {

	public static SecretKeySpec secret_key_spec(
			final byte[] key
			, final String algorithm
			) {
		return new SecretKeySpec(key, algorithm);
	}// end of secret_key_spec
	
	public static byte[] secure_random_iv(
			final int iv_size
			) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] iv = new byte[iv_size];
		secureRandom.nextBytes(iv);
		return iv;
	}// end of secure_random_iv
	
	public static AlgorithmParameterSpec algorithm_parameter_spec(
			final STransformation transformation
			, final byte[] iv
			) throws SException {
		
		String mode = transformation.mode();
		
		if("GCM".equals(mode)) {
			return new GCMParameterSpec(transformation.tag_size() * 8, iv);
		}
		
		throw new SException("Unsupported encryption mode.");
	}// end of algorithm_parameter_spec
	
	public static byte[] encrypt(
			final STransformation transformation
			, final String provider
			, final byte[] key
			, final byte[] iv
			, final byte[] data
			) throws SException {
		
		String algorithm = transformation.algorithm();
		int block_size = transformation.block_size();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + iv.length + transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			AlgorithmParameterSpec algorithm_parameter_spec = algorithm_parameter_spec(transformation, iv);
			
			Cipher cipher = null;
			if(SText.is_empty(provider)) {
				cipher = Cipher.getInstance(transformation.transformation());
			} else {
				cipher = Cipher.getInstance(transformation.transformation(), provider);
			}
			
			if(algorithm_parameter_spec == null) {
				cipher.init(Cipher.ENCRYPT_MODE, secret_key);
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, secret_key, algorithm_parameter_spec);
			}
			
			byteBuffer.put(iv);
			
			int offset = 0;
			int len = 0;
			byte[] block;
			byte[] encrypted;
			while(offset < data.length) {
				
				len = Math.min(block_size, data.length - offset);
				block = new byte[len];
				System.arraycopy(data, offset, block, 0, len);
				
				encrypted = cipher.update(block);
				if(encrypted != null) {
					byteBuffer.put(encrypted);
				}
				
				offset += len;
				
			}// end of while
			
			encrypted = cipher.doFinal();
			if(encrypted != null) {
				byteBuffer.put(encrypted);
			}
			
		} catch (NoSuchAlgorithmException e) {
			throw new SException("[NoSuchAlgorithmException] Failed to declare cipher.");
		} catch (NoSuchPaddingException e) {
			throw new SException("[NoSuchPaddingException] Failed to declare cipher.");
		} catch (NoSuchProviderException e) {
			throw new SException("[NoSuchProviderException] Failed to declare cipher.");
		} catch (InvalidKeyException e) {
			throw new SException("[InvalidKeyException] Failed to initialize cipher.");
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw new SException("[InvalidAlgorithmParameterException] Failed to initialize cipher.");
		} catch (IllegalBlockSizeException e) {
			throw new SException("[IllegalBlockSizeException] Failed to encrypt data.");
		} catch (BadPaddingException e) {
			throw new SException("[BadPaddingException] Failed to encrypt data.");
		}// end of try
		
		return byteBuffer.array();
	}// end of encrypt
	
	public static byte[] encrypt(
			final STransformation transformation
			, final byte[] key
			, final byte[] iv
			, final byte[] data
			) throws SException {
		return encrypt(
				transformation
				, BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, iv
				, data
				);
	}// end of encrypt
	
	public static byte[] encrypt(
			final STransformation transformation
			, final byte[] key
			, final byte[] data
			) throws SException {
		return encrypt(
				transformation
				, BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, secure_random_iv(transformation.iv_size())//iv
				, data
				);
	}// end of encrypt
	
	public static byte[] decrypt(
			final STransformation transformation
			, final String provider
			, final byte[] key
			, final byte[] data
			) throws SException {
		
		String algorithm = transformation.algorithm();
		int block_size = transformation.block_size();
		int iv_size = transformation.iv_size();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length - iv_size - transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			ByteBuffer encrypted = ByteBuffer.wrap(data);
			
			byte[] iv = new byte[iv_size];
			encrypted.get(iv);
			
			AlgorithmParameterSpec algorithm_parameter_spec = algorithm_parameter_spec(transformation, iv);
			
			Cipher cipher = null;
			if(SText.is_empty(provider)) {
				cipher = Cipher.getInstance(transformation.transformation());
			} else {
				cipher = Cipher.getInstance(transformation.transformation(), provider);
			}
			
			if(algorithm_parameter_spec == null) {
				cipher.init(Cipher.DECRYPT_MODE, secret_key);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secret_key, algorithm_parameter_spec);
			}
			
			int len = 0;
			byte[] block = new byte[block_size];
			byte[] decrypted;
			while(encrypted.hasRemaining()) {
				
				len = Math.min(block_size, encrypted.remaining());
				encrypted.get(block, 0, len);
				
				decrypted = cipher.update(block, 0, len);
				if(decrypted != null) {
					byteBuffer.put(decrypted);
				}
				
			}// end of while
			
			decrypted = cipher.doFinal();
			if(decrypted != null) {
				byteBuffer.put(decrypted);
			}
			
		} catch (NoSuchAlgorithmException e) {
			throw new SException("[NoSuchAlgorithmException] Failed to declare cipher.");
		} catch (NoSuchPaddingException e) {
			throw new SException("[NoSuchPaddingException] Failed to declare cipher.");
		} catch (NoSuchProviderException e) {
			throw new SException("[NoSuchProviderException] Failed to declare cipher.");
		} catch (InvalidKeyException e) {
			throw new SException("[InvalidKeyException] Failed to initialize cipher.");
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw new SException("[InvalidAlgorithmParameterException] Failed to initialize cipher.");
		} catch (IllegalBlockSizeException e) {
			throw new SException("[IllegalBlockSizeException] Failed to encrypt data.");
		} catch (BadPaddingException e) {
			throw new SException("[BadPaddingException] Failed to encrypt data.");
		}// end of try
		
		return byteBuffer.array();
	}// end of decrypt
	
	public static byte[] decrypt(
			final STransformation transformation
			, final byte[] key
			, final byte[] data
			) throws SException {
		return decrypt(
				transformation
				, BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, data
				);
	}// end of decrypt
	
}
