package seung.kimchi;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SAlgorithm;
import seung.kimchi.types.aws.SS3Object;
import seung.kimchi.types.aws.SS3ObjectVersion;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
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
			, final boolean diff
			, final boolean closeable
			) throws SException {
		
		try {
			
			String md5 = SFormat.encode_hex(SSecurity.digest(bytes, SAlgorithm._S_MD5));
			
			if(diff) {
				SS3Object uploaded = metadata(s3, bucket, key, false);
				if(uploaded != null && md5.equals(uploaded.md5())) {
					return uploaded;
				}
			}
			
			Map<String, String> misc = new HashMap<>();
			misc.put("md5", md5);
			if(metadata != null) {
				misc.putAll(metadata);
			}
			
			PutObjectRequest request = PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.metadata(misc)
					.build()
					;
			
			s3.putObject(request, RequestBody.fromBytes(bytes));
			
			return metadata(s3, bucket, key, closeable);
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to put object.");
		} catch (S3Exception e) {
			if(e.statusCode() == 412) {
				throw new SException(e, "Duplicate key is not allowed.");
			} else {
				throw new SException(e, "Failed to put object.");
			}
		}// end of try
		
	}// end of put
	public static SS3Object put(
			final S3Client s3
			, final String bucket
			, final String key
			, final Map<String, String> metadata
			, final byte[] bytes
			, final boolean diff
			) throws SException {
		return put(s3, bucket, key, metadata, bytes, diff, true);
	}// end of put
	public static SS3Object put(
			final S3Client s3
			, final String bucket
			, final String key
			, final Map<String, String> metadata
			, final byte[] bytes
			) throws SException {
		return put(s3, bucket, key, metadata, bytes, true, true);
	}// end of put
	
	public static List<SS3ObjectVersion> versions(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean closeable
			) throws SException {
		
		try {
			
			ListObjectVersionsRequest request = ListObjectVersionsRequest.builder()
					.bucket(bucket)
					.prefix(key)
					.build()
					;
			
			ListObjectVersionsResponse response = s3.listObjectVersions(request);
			
			List<SS3ObjectVersion> items = new ArrayList<>();
			for(ObjectVersion item : response.versions()) {
				items.add(SS3ObjectVersion.builder()
						.version(item.versionId())
						.lastest(item.isLatest())
						.content_length(item.size())
						.last_modified(item.lastModified().toEpochMilli())
						.md5(item.eTag())
						.build()
						);
			}
			
			return items;
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to get versions.");
		} catch (S3Exception e) {
			throw new SException(e, "Failed to get versions.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of versions
	public static List<SS3ObjectVersion> versions(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		return versions(s3, bucket, key, true);
	}// end of versions
	
	public static SS3Object metadata(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean closeable
			) throws SException {
		
		try {
			
			HeadObjectRequest request = HeadObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.build()
					;
			
			HeadObjectResponse response = s3.headObject(request);
			
			String content_type = response.contentType();
			Map<String, String> metadata = response.metadata();
			
			return SS3Object.builder()
					.key(key)
					.directory(content_type.toLowerCase().contains("directory"))
					.content_length(response.contentLength())
					.content_type(content_type)
					.last_modified(response.lastModified().toEpochMilli())
					.md5(metadata.get("md5"))
					.version(response.versionId())
					.misc(metadata)
					.build();
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to get metadata.");
		} catch (S3Exception e) {
			if(e.statusCode() == 404) {
				return null;
			}
			throw new SException(e, "Failed to get metadata.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of metadata
	public static SS3Object metadata(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		return metadata(s3, bucket, key, true);
	}// end of metadata
	
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
					.key(key)
					.bytes(bytes.asByteArray())
					.content_length(response.contentLength())
					.content_type(response.contentType())
					.last_modified(response.lastModified().toEpochMilli())
					.md5(response.eTag())
					.version(response.versionId())
					.misc(response.metadata())
					.build();
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to get object.");
		} catch (S3Exception e) {
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
	
	public static List<SS3Object> ls(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean all
			, final boolean closeable
			) throws SException {
		
		try {
			
			ListObjectsV2Request request = ListObjectsV2Request.builder()
					.bucket(bucket)
					.prefix(key)
					.build();
			
			ListObjectsV2Iterable iterable = s3.listObjectsV2Paginator(request);
			
			List<SS3Object> items = new ArrayList<>();
			for(ListObjectsV2Response response : iterable) {
				for(S3Object item : response.contents()) {
					if(all) {
						items.add(metadata(
								s3
								, bucket
								, item.key()//key
								, false//closeable
								));
						continue;
					}
					items.add(SS3Object.builder()
							.key(item.key())
							.last_modified(item.lastModified().toEpochMilli())
							.md5(item.eTag().replaceAll("\"", ""))
							.build()
							);
				}
			}
			
			return items;
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to list objects.");
		} catch (S3Exception e) {
			throw new SException(e, "Failed to list objects.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of ls
	public static List<SS3Object> ls(
			final S3Client s3
			, final String bucket
			, final String key
			) throws SException {
		return ls(s3, bucket, key, false, true);
	}// end of ls
	
	public static List<SS3Object> rm(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean recursive
			, final boolean closeable
			) throws SException {
		
		try {
			
			List<SS3Object> items = ls(s3, bucket, key, false, false);
			
			if(items.size() == 0) {
				return new ArrayList<>();
			}
			
			if(!recursive && items.size() > 1) {
				return new ArrayList<>();
			}
			
//			Delete delete = Delete.builder()
//					.objects(items.stream().map(item -> ObjectIdentifier.builder().key(item.key()).build()).toList())
//					.build();
//			
//			DeleteObjectsRequest request = DeleteObjectsRequest.builder()
//					.bucket(bucket)
//					.delete(delete)
//					.build();
			
			DeleteObjectsRequest request = DeleteObjectsRequest.builder()
					.bucket(bucket)
					.delete(builder -> builder.objects(items.stream().map(item -> ObjectIdentifier.builder().key(item.key()).build()).toList()))
					.build();
			
			DeleteObjectsResponse response = s3.deleteObjects(request);
			
			List<SS3Object> deleted = new ArrayList<>();
			for(DeletedObject item : response.deleted()) {
				deleted.add(SS3Object.builder()
					.key(item.key())
					.version(item.deleteMarkerVersionId())
					.build()
					);
			}
			
			return deleted;
			
		} catch (SdkClientException e) {
			throw new SException(e, "Failed to remove objects.");
		} catch (S3Exception e) {
			throw new SException(e, "Failed to remove objects.");
		} finally {
			if(closeable) {
				s3.close();
			}
		}// end of try
		
	}// end of rm
	public static List<SS3Object> rm(
			final S3Client s3
			, final String bucket
			, final String key
			, final boolean recursive
			) throws SException {
		return rm(s3, bucket, key, recursive, true);
	}// end of rm
	
}
