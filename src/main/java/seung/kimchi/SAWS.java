package seung.kimchi;

import java.net.URI;
import java.util.Map;

import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SS3Object;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.EncryptionTypeMismatchException;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.InvalidObjectStateException;
import software.amazon.awssdk.services.s3.model.InvalidRequestException;
import software.amazon.awssdk.services.s3.model.InvalidWriteOffsetException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.TooManyPartsException;

public class SAWS {

	public static S3Client s3(
			final String access_key
			, final String secret_key
			, final Region region
			, final String endpoint
			) {
		S3ClientBuilder builder = S3Client.builder()
				.region(region)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(access_key, secret_key)))
				;
		if(!SText.is_empty(endpoint)) {
			builder.endpointOverride(URI.create(endpoint));
		}
		return builder.build();
	}// end of s3
	public static S3Client s3(
			final String access_key
			, final String secret_key
			, final Region region
			) {
		return s3(access_key, secret_key, region, "");
	}// end of s3
	public static S3Client s3(
			final String access_key
			, final String secret_key
			, final String region
			, final String endpoint
			) {
		return s3(access_key, secret_key, Region.of(region), "");
	}// end of s3
	public static S3Client s3(
			final String access_key
			, final String secret_key
			, final String region
			) {
		return s3(access_key, secret_key, region, "");
	}// end of s3
	public static S3Client s3(
			final String access_key
			, final String secret_key
			) {
		return s3(access_key, secret_key, Region.AP_NORTHEAST_2, "");
	}// end of s3
	
	public PutObjectResponse s3_upload(
			final S3Client s3
			, final String bucket
			, final String key
			, final Map<String, String> metadata
			, final byte[] value
			) throws SException {
		
		try {
			
			PutObjectRequest request = PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.metadata(metadata)
					.build()
					;
			
			return s3.putObject(request, RequestBody.fromBytes(value));
			
		} catch (InvalidRequestException e) {
			throw new SException(e, "Failed to put object.");
		} catch (InvalidWriteOffsetException e) {
			throw new SException(e, "Failed to put object.");
		} catch (TooManyPartsException e) {
			throw new SException(e, "Failed to put object.");
		} catch (EncryptionTypeMismatchException e) {
			throw new SException(e, "Failed to put object.");
		} catch (S3Exception e) {
			throw new SException(e, "Failed to put object.");
		} catch (AwsServiceException e) {
			throw new SException(e, "Failed to put object.");
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to put object.");
		} finally {
			s3.close();
		}// end of try
		
	}// end of s3_upload
	
	public SS3Object s3_download(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		
		try {
			
			GetObjectRequest request = GetObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.build();
			
			ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(request);
			
			GetObjectResponse response = bytes.response();
			
			return SS3Object.builder()
					.bytes(bytes.asByteArray())
					.content_length(response.contentLength())
					.content_disposition(response.contentDisposition())
					.content_type(response.contentType())
					.build();
			
		} catch (NoSuchKeyException e) {
			throw new SException(e, "Failed to get object.");
		} catch (InvalidObjectStateException e) {
			throw new SException(e, "Failed to get object.");
		} catch (S3Exception e) {
			throw new SException(e, "Failed to get object.");
		} catch (AwsServiceException e) {
			throw new SException(e, "Failed to get object.");
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to get object.");
		} finally {
			s3.close();
		}// end of try
		
	}// end of s3_download
	
}
