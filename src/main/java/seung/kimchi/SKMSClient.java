package seung.kimchi;

import java.time.Duration;

import lombok.Builder;
import lombok.NonNull;
import seung.kimchi.exceptions.SException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptResponse;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyResponse;

public class SKMSClient implements AutoCloseable {

	private final KmsClient client;
	
	private final String id;
	
	private byte[] dek;
	
	private byte[] kek;
	
	@Builder
	private SKMSClient(
			@NonNull String accessKey
			, @NonNull String secretKey
			, Region region
			, @NonNull String id
			) {
		this.client = KmsClient.builder()
				.region(region == null ? Region.AP_NORTHEAST_2 : region)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.overrideConfiguration(configuration -> configuration
						.retryStrategy(RetryMode.defaultRetryMode())
						.apiCallTimeout(Duration.ofSeconds(60))
						.apiCallAttemptTimeout(Duration.ofSeconds(60))
						)
				.build();
		this.id = id;
	}
	
	@Override
	public void close() throws SException {
		try {
			client.close();
		} catch (Exception e) {
			throw new SException(e, "Failed to close SS3Client.");
		}// end of try
	}// end of close
	
	private void generate(DataKeySpec keySpec) {
		
		GenerateDataKeyRequest request = GenerateDataKeyRequest.builder()
				.keyId(id)
				.keySpec(keySpec)
				.build();
		
		GenerateDataKeyResponse response = client.generateDataKey(request);
		
		this.dek = response.plaintext().asByteArray();
		this.kek = response.ciphertextBlob().asByteArray();
	}// end of generate
	
	public byte[] dek(DataKeySpec keySpec) {
		if(dek == null) {
			generate(keySpec);
		}
		return dek;
	}// end of dek
	public byte[] dek() {
		return dek(DataKeySpec.AES_256);
	}// end of dek
	
	public byte[] kek() {
		return kek;
	}// kek
	
	public byte[] encrypt(String data) {
		
		EncryptRequest request = EncryptRequest.builder()
				.keyId(id)
				.plaintext(SdkBytes.fromUtf8String(data))
				.build();
		
		EncryptResponse response = client.encrypt(request);
		
		return response.ciphertextBlob().asByteArray();
	}// end of encrypt
	
	public byte[] decrypt(byte[] data) {
		
		DecryptRequest request = DecryptRequest.builder()
				.ciphertextBlob(SdkBytes.fromByteArray(data))
				.build();
		
		DecryptResponse response = client.decrypt(request);
		
		return response.plaintext().asByteArray();
	}// end of decrypt
	
}
