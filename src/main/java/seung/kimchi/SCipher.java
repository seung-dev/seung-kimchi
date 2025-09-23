package seung.kimchi;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import seung.kimchi.core.SText;
import seung.kimchi.core.types.SAlgorithm;
import seung.kimchi.core.types.SException;
import seung.kimchi.core.types.STransformation;

public class SCipher {

	public static KeyPair keypair(
			final String algorithm
			, final String provider
			, final int key_size
			) throws SException {
		
		try {
			
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
			
		} catch (NoSuchAlgorithmException e) {
			throw new SException(e, "[NoSuchAlgorithmException] Failed to generate keypair.");
		} catch (NoSuchProviderException e) {
			throw new SException(e, "[NoSuchProviderException] Failed to generate keypair.");
		}// end of try
		
	}// end of keypair
	public static KeyPair keypair(
			String algorithm
			, int key_size
			) throws SException {
		return keypair(algorithm, BouncyCastleProvider.PROVIDER_NAME, key_size);
	}// end of keypair
	public static KeyPair keypair(
			int key_size
			) throws SException {
		return keypair(SAlgorithm._S_RSA, BouncyCastleProvider.PROVIDER_NAME, key_size);
	}// end of keypair
	
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
	
	public static byte[] encrypt_aes_gcm(
			final String provider
			, final byte[] key
			, final byte[] nonce
			, final byte[] input
			) throws SException {
		
		STransformation transformation = STransformation.AES_GCM_NOPADDING;
		
		String algorithm = transformation.algorithm();
		int block_size = transformation.block_size();
		
		byte[] _nonce = nonce;
		if(_nonce == null) {
			_nonce = nonce(transformation.nonce_size());
		}
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(input.length + _nonce.length + transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			AlgorithmParameterSpec algorithm_parameter_spec = algorithm_parameter_spec(transformation, _nonce);
			
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
			
			byteBuffer.put(_nonce);
			
			int offset = 0;
			int len = 0;
			byte[] block;
			byte[] encrypted;
			while(offset < input.length) {
				
				len = Math.min(block_size, input.length - offset);
				block = new byte[len];
				System.arraycopy(input, offset, block, 0, len);
				
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
	}// end of encrypt_aes_gcm
	
	public static byte[] encrypt_aes_gcm(
			final byte[] key
			, final byte[] nonce
			, final byte[] input
			) throws SException {
		return encrypt_aes_gcm(
				BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, nonce
				, input
				);
	}// end of encrypt_aes_gcm
	
	public static byte[] encrypt_aes_gcm(
			final byte[] key
			, final byte[] input
			) throws SException {
		return encrypt_aes_gcm(
				BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, null//nonce
				, input
				);
	}// end of encrypt_aes_gcm
	
	public static byte[] decrypt_aes_gcm(
			final String provider
			, final byte[] key
			, final byte[] input
			) throws SException {
		
		STransformation transformation = STransformation.AES_GCM_NOPADDING;
		
		String algorithm = transformation.algorithm();
		int nonce_size = transformation.nonce_size();
		int block_size = transformation.block_size();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(input.length - nonce_size - transformation.tag_size());
		
		try {
			
			Key secret_key = secret_key_spec(key, algorithm);
			
			ByteBuffer encrypted = ByteBuffer.wrap(input);
			
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
	}// end of decrypt_aes_gcm
	
	public static byte[] decrypt_aes_gcm(
			final byte[] key
			, final byte[] input
			) throws SException {
		return decrypt_aes_gcm(
				BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, input
				);
	}// end of decrypt_aes_gcm
	
	public static byte[] encrypt_rsa_oaep_sha256(
			final String provider
			, final PublicKey key
			, final byte[] input
			) throws SException {
		
		if (key == null) {
			throw new SException("Key is empty.");
		}
		if (input == null || input.length == 0) {
			throw new SException("Input is empty.");
		}
		
		STransformation transformation = STransformation.RSA_OAEP_SHA256;
		
		try {
			
			byte[] dek = new byte[32];
			SecureRandom secureRandom = SecureRandom.getInstanceStrong();
			secureRandom.nextBytes(dek);
			
			byte[] encrypted = encrypt_aes_gcm(provider, dek, null, input);
			
			Cipher cipher = null;
			if(SText.is_empty(provider)) {
				cipher = Cipher.getInstance(transformation.transformation());
			} else {
				cipher = Cipher.getInstance(transformation.transformation(), provider);
			}
			
			OAEPParameterSpec algorithm_parameter_spec = new OAEPParameterSpec(
					SAlgorithm._S_SHA256//mdName
					, SAlgorithm._S_MGF1//mgfName
					, MGF1ParameterSpec.SHA256//MGF1ParameterSpec
					, PSource.PSpecified.DEFAULT
					);
			
			cipher.init(Cipher.ENCRYPT_MODE, key, algorithm_parameter_spec);
			
			byte[] _dek = cipher.doFinal(dek);
			
			ByteBuffer byteBuffer = ByteBuffer.allocate(transformation.key_size() + _dek.length + encrypted.length);
			
			byteBuffer.putInt(_dek.length);
			byteBuffer.put(_dek);
			byteBuffer.put(encrypted);
			
			return byteBuffer.array();
			
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
		
	}// end of encrypt_rsa_oaep_sha256
	public static byte[] encrypt_rsa_oaep_sha256(
			final PublicKey key
			, final byte[] input
			) throws SException {
		return encrypt_rsa_oaep_sha256(
				BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, input
				);
	}// end of encrypt_rsa_oaep_sha256
	
	public static byte[] decrypt_rsa_oaep_sha256(
			final String provider
			, final PrivateKey key
			, final byte[] input
			) throws SException {
		
		if (key == null) {
			throw new SException("Key is empty.");
		}
		if (input == null || input.length == 0) {
			throw new SException("Input is empty.");
		}
		
		STransformation transformation = STransformation.RSA_OAEP_SHA256;
		
		try {
			
			ByteBuffer byteBuffer = ByteBuffer.wrap(input);
			
			int dek_length = byteBuffer.getInt();
			
			byte[] dek = new byte[dek_length];
			byteBuffer.get(dek);
			
			byte[] encrypted = new byte[byteBuffer.remaining()];
			byteBuffer.get(encrypted);
			
			Cipher cipher = null;
			if(SText.is_empty(provider)) {
				cipher = Cipher.getInstance(transformation.transformation());
			} else {
				cipher = Cipher.getInstance(transformation.transformation(), provider);
			}
			
			OAEPParameterSpec algorithm_parameter_spec = new OAEPParameterSpec(
					SAlgorithm._S_SHA256//mdName
					, SAlgorithm._S_MGF1//mgfName
					, MGF1ParameterSpec.SHA256//MGF1ParameterSpec
					, PSource.PSpecified.DEFAULT
					);
			
			cipher.init(Cipher.DECRYPT_MODE, key, algorithm_parameter_spec);
			
			byte[] _dek = cipher.doFinal(dek);
			
			return decrypt_aes_gcm(provider, _dek, encrypted);
			
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
		
	}// end of decrypt_rsa_oaep_sha256
	public static byte[] decrypt_rsa_oaep_sha256(
			final PrivateKey key
			, final byte[] input
			) throws SException {
		return decrypt_rsa_oaep_sha256(
				BouncyCastleProvider.PROVIDER_NAME//provider
				, key
				, input
				);
	}// end of decrypt_rsa_oaep_sha256
	
}
