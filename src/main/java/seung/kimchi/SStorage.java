package seung.kimchi;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class SStorage {

	public static AmazonS3Client s3(
			String provider
			, String endpoint
			, String region
			, String access_key
			, String secret_key
			) {
		if(endpoint != null && !"".equals(endpoint)) {
			return (AmazonS3Client) AmazonS3ClientBuilder.standard()
					.enablePathStyleAccess()
					.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
					.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key)))
					.build()
					;
		}
		return (AmazonS3Client) AmazonS3ClientBuilder.standard()
				.enablePathStyleAccess()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(access_key, secret_key)))
				.build()
				;
	}// end of s3
	public static AmazonS3Client s3(
			String provider
			, String region
			, String access_key
			, String secret_key
			) {
		return s3(
				provider
				, ""//endpoint
				, region
				, access_key
				, secret_key
				);
	}// end of s3
	
}
