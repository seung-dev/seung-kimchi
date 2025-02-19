package seung.kimchi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

public class SS3Client {

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
	
	public static SS3Object put(
			final S3Client s3
			, final String bucket
			, final String key
			, final Map<String, String> metadata
			, final byte[] bytes
			, final boolean closeable
			) throws SException {
		
		try {
			
			PutObjectRequest request = PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.metadata(metadata)
					.build()
					;
			
			PutObjectResponse response = s3.putObject(request, RequestBody.fromBytes(bytes));
			
			return SS3Object.builder()
					.e_tag(response.eTag())
					.build();
			
		} catch (SdkClientException | AwsServiceException e) {
			throw new SException(e, "Failed to put object.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of upload
	public static SS3Object put(
			final S3Client s3
			, final String bucket
			, final String key
			, final Map<String, String> metadata
			, final byte[] bytes
			) throws SException {
		return put(s3, bucket, key, metadata, bytes, true);
	}// end of upload
	
	public static SS3Object get(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean closeable
			) throws SException {
		
		try {
			
			GetObjectRequest request = GetObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.build();
			
			ResponseBytes<GetObjectResponse> bytes = s3.getObjectAsBytes(request);
			
			GetObjectResponse response = bytes.response();
			
			return SS3Object.builder()
					.e_tag(response.eTag())
					.bytes(bytes.asByteArray())
					.content_length(response.contentLength())
					.content_disposition(response.contentDisposition())
					.content_type(response.contentType())
					.build();
			
		} catch (SdkClientException | AwsServiceException e) {
			throw new SException(e, "Failed to get object.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of download
	public static SS3Object get(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		return get(s3, bucket, key, true);
	}// end of download
	
	public static List<S3Object> ls(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean closeable
			) throws SException {
		
		try {
			
			ListObjectsV2Request request = ListObjectsV2Request.builder()
					.bucket(bucket)
					.prefix(key)
					.build();
			
			
			ListObjectsV2Iterable iterable = s3.listObjectsV2Paginator(request);
			
			List<S3Object> items = new ArrayList<>();
			for(ListObjectsV2Response response : iterable) {
				items.addAll(response.contents());
			}
			
			return items;
			
		} catch (SdkClientException | AwsServiceException e) {
			throw new SException(e, "Failed to list objects.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of ls
	public static List<S3Object> ls(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		return ls(s3, bucket, key, true);
	}// end of ls
	
	public static int rm(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean rf
			, final boolean closeable
			) throws SException {
		
		try {
			
			List<S3Object> items = ls(s3, bucket, key, false);
			
			if(items.size() == 0) {
				return 0;
			}
			
			if(rf && items.size() > 1) {
				return 0;
			}
			
//			Delete delete = Delete.builder()
//					.objects(items.stream().map(item -> ObjectIdentifier.builder().key(item.key()).build()).toList())
//					.build();
			
			DeleteObjectsRequest request = DeleteObjectsRequest.builder()
					.bucket(bucket)
//					.delete(delete)
					.delete(builder -> builder.objects(items.stream().map(item -> ObjectIdentifier.builder().key(item.key()).build()).toList()))
					.build();
			
			DeleteObjectsResponse response = s3.deleteObjects(request);
			
			return response.deleted().size();
			
		} catch (SdkClientException | AwsServiceException e) {
			throw new SException(e, "Failed to remove objects.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of rm
	public static int rm(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean rf
			) throws SException {
		return rm(s3, bucket, key, rf, true);
	}// end of rm
	
}
