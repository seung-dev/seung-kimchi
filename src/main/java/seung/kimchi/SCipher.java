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
	
	public static byte[] nonce(
			final int nonce_size
			) {
		byte[] nonce = new byte[nonce_size];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(nonce);
		return nonce;
	}// end of nonce
	
	public static AlgorithmParameterSpec algorithm_parameter_spec(
			final STransformation transformation
			, final byte[] nonce
			) throws SException {
		
		String mode = transformation.mode();
		
		if("GCM".equals(mode)) {
			return new GCMParameterSpec(transformation.tag_size() * 8, nonce);
		}
		
		throw new SException("Unsupported encryption mode.");
	}// end of algorithm_parameter_spec
	
	public static byte[] encrypt(
			final STransformation transformation
			, final String provider
			, final byte[] key
			, final byte[] nonce
			, final byte[] data
			) throws SException {
		
		String algorithm = transformation.algorithm();
		int block_size = transformation.block_size();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length + nonce.length + transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			AlgorithmParameterSpec algorithm_parameter_spec = algorithm_parameter_spec(transformation, nonce);
			
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
			
			byteBuffer.put(nonce);
			
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
			throw new SException(e, "[NoSuchAlgorithmException] Failed to encrypt value.");
		} catch (NoSuchPaddingException e) {
			throw new SException(e, "[NoSuchPaddingException] Failed to encrypt value.");
		} catch (NoSuchProviderException e) {
			throw new SException(e, "[NoSuchProviderException] Failed to encrypt value.");
		} catch (InvalidKeyException e) {
			throw new SException(e, "[InvalidKeyException] Failed to encrypt value.");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SException(e, "[InvalidAlgorithmParameterException] Failed to encrypt value.");
		} catch (IllegalBlockSizeException e) {
			throw new SException(e, "[IllegalBlockSizeException] Failed to encrypt value.");
		} catch (BadPaddingException e) {
			throw new SException(e, "[BadPaddingException] Failed to encrypt value.");
		}// end of try
		
		return byteBuffer.array();
	}// end of encrypt
	
	public static byte[] encrypt(
			final STransformation transformation
			, final byte[] key
			, final byte[] nonce
			, final byte[] data
			) throws SException {
		return encrypt(
				transformation
				, BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, nonce
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
				, nonce(transformation.nonce_size())//nonce
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
		int nonce_size = transformation.nonce_size();
		int block_size = transformation.block_size();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(data.length - nonce_size - transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			ByteBuffer encrypted = ByteBuffer.wrap(data);
			
			byte[] nonce = new byte[nonce_size];
			encrypted.get(nonce);
			
			AlgorithmParameterSpec algorithm_parameter_spec = algorithm_parameter_spec(transformation, nonce);
			
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
			throw new SException(e, "[NoSuchAlgorithmException] Failed to decrypt value.");
		} catch (NoSuchPaddingException e) {
			throw new SException(e, "[NoSuchPaddingException] Failed to decrypt value.");
		} catch (NoSuchProviderException e) {
			throw new SException(e, "[NoSuchProviderException] Failed to decrypt value.");
		} catch (InvalidKeyException e) {
			throw new SException(e, "[InvalidKeyException] Failed to decrypt value.");
		} catch (InvalidAlgorithmParameterException e) {
			throw new SException(e, "[InvalidAlgorithmParameterException] Failed to decrypt value.");
		} catch (IllegalBlockSizeException e) {
			throw new SException(e, "[IllegalBlockSizeException] Failed to decrypt value.");
		} catch (BadPaddingException e) {
			throw new SException(e, "[BadPaddingException] Failed to decrypt value.");
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
