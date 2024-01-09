package seung.kimchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import seung.kimchi.types.SMediaType;

public class SFile {

	public static boolean is_zip(final File file) throws IOException {
		boolean is_zip = false;
		try (
				ZipFile zip_file = new ZipFile(file);
				) {
			if(zip_file.size() > 0) {
				is_zip = true;
			}
		}// end of try
		return is_zip;
	}// end of is_zip
	public static boolean is_zip(final String file_path) throws IOException {
		return is_zip(new File(file_path));
	}// end of is_zip
	public static boolean is_zip(final String file_path, String a) throws IOException {
		return is_zip(new File(file_path));
	}// end of is_zip
	
	public static void add_zip_entry(
			final ZipOutputStream zipOutputStream
			, final String file_path
			, final byte[] file_data
			) throws IOException {
		
		try (
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file_data);
				) {
			
			ZipEntry zipEntry = new ZipEntry(file_path);
			zipOutputStream.putNextEntry(zipEntry);
			
			byte[] b = new byte[1024 * 4];
			int off = 0;
			int len = 0;
			while((len = byteArrayInputStream.read(b)) >= 0) {
				zipOutputStream.write(b, off, len);
			}
			
			zipOutputStream.closeEntry();
			
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}// end of try
		
	}
	
	public static byte[] zip(
			final List<String> file_path_list
			) throws IOException {
		byte[] zip = null;
		
		try (
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
				) {
			
			File file = null;
			String file_name = null;
			byte[] file_data = null;
			for(String file_path : file_path_list) {
				file = new File(file_path);
				file_name = file.getName();
				file_data = FileUtils.readFileToByteArray(file);
				add_zip_entry(zipOutputStream, file_name, file_data);
			}
			
			zipOutputStream.flush();
			
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return zip;
	}// end of zip
	
	public static long zip(
			final String zip_path
			, final List<String> file_path_list
			) throws IOException {
		
		File zip = new File(zip_path);
		if(zip.exists()) {
			zip.delete();
		}
		
		try (
				FileOutputStream fileOutputStream = new FileOutputStream(zip_path);
				ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
				) {
			
			File file = null;
			String file_name = null;
			byte[] file_data = null;
			for(String file_path : file_path_list) {
				file = new File(file_path);
				file_name = file.getName();
				file_data = FileUtils.readFileToByteArray(file);
				add_zip_entry(zipOutputStream, file_name, file_data);
			}// end of file_path_list
			
			zipOutputStream.flush();
			
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return new File(zip_path).length();
	}// end of zip
	
	public static void write(
			ZipInputStream zipInputStream
			, File file
			) throws FileNotFoundException, IOException {
		
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			
			byte[] b = new byte[4096];
			int len = 0;
			
			while((len = zipInputStream.read(b)) > 0) {
				fileOutputStream.write(b, 0, len);
			}// end of while
			
			fileOutputStream.close();
			
		}// end of try
		
	}// end of write
	
	public static PutObjectResult s3_upload(
			AmazonS3Client amazon_s3_client
			, String bucket_name
			, String key
			, byte[] input
			) throws IOException {
		
		PutObjectResult putObjectResult = null;
		
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
				) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(SMediaType._S_APPLICATION_OCTET_STREAM);
			objectMetadata.setContentLength(input.length);
			putObjectResult = amazon_s3_client.putObject(
					bucket_name//bucketName
					, key
					, byteArrayInputStream//input
					, objectMetadata//metadata
					);
		} catch (IOException e) {
			throw e;
		}
		
		return putObjectResult;
	}// end of s3_upload
	
}
