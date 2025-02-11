package seung.kimchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SFileMeta;
import seung.kimchi.types.SFileType;
import seung.kimchi.types.SMediaType;

public class SFile {

	public static boolean is_zip(
			final File file
			) throws SException {
		
		boolean is_zip = false;
		
		try (
				ZipFile zip_file = new ZipFile(file);
				) {
			if(zip_file.size() > 0) {
				is_zip = true;
			}
		} catch (ZipException e) {
			throw new SException(e, "[ZipException] Failed to determine file type.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to determine file type.");
		}// end of try
		
		return is_zip;
	}// end of is_zip
	public static boolean is_zip(final String file_path) throws SException {
		return is_zip(new File(file_path));
	}// end of is_zip
	public static boolean is_zip(final String file_path, String a) throws SException {
		return is_zip(new File(file_path));
	}// end of is_zip
	
	public static void add_zip_entry(
			final ZipOutputStream zipOutputStream
			, final String file_path
			, final byte[] file_data
			) throws SException {
		
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
			throw new SException(e, "[FileNotFoundException] Failed to add file to zip.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to add file to zip.");
		}// end of try
		
	}
	
	public static byte[] zip(
			final List<String> file_path_list
			) throws SException {
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
			throw new SException(e, "[IOException] Failed to zip files.");
		}// end of try
		
		return zip;
	}// end of zip
	
	public static long zip(
			final String zip_path
			, final List<String> file_path_list
			) throws SException {
		
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
			throw new SException(e, "[FileNotFoundException] Failed to zip files.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to zip files.");
		}// end of try
		
		return new File(zip_path).length();
	}// end of zip
	
	public static void write(
			ZipInputStream zipInputStream
			, File file
			) throws SException {
		
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			
			byte[] b = new byte[4096];
			int len = 0;
			
			while((len = zipInputStream.read(b)) > 0) {
				fileOutputStream.write(b, 0, len);
			}// end of while
			
			fileOutputStream.close();
			
		} catch (FileNotFoundException e) {
			throw new SException(e, "[FileNotFoundException] Failed to unzip file.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to unzip file.");
		}// end of try
		
	}// end of write
	
	public static PutObjectResult s3_upload(
			AmazonS3Client amazon_s3_client
			, String bucket_name
			, String key
			, byte[] input
			) throws SException {
		
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
			throw new SException(e, "[IOException] Failed to upload file.");
		}// end of try
		
		return putObjectResult;
	}// end of s3_upload
	
	public static byte[] s3_download(
			AmazonS3Client amazon_s3_client
			, String bucket_name
			, String key
			) throws SException {
		
		try {
			S3Object s3Object = amazon_s3_client.getObject(bucket_name, key);
			return IOUtils.toByteArray(s3Object.getObjectContent());
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to download file.");
		}// end of try
		
	}// end of s3_download
	
	
	public static String extension(String name) {
		String ext = FilenameUtils.getExtension(name);
		if(ext != null) {
			return ext.toLowerCase();
		}
		return null;
	}// end of extension
	public static String extension(File file) {
		return extension(file.getName());
	}// end of extension
	
	public static String content_type(
			byte[] file
			) throws SException {
		
		Metadata metadata = new Metadata();
		
		try (
				InputStream inputStream = new ByteArrayInputStream(file);
				TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
				) {
			
			new Tika().parseToString(tikaInputStream, metadata);
			
		} catch (FileNotFoundException e) {
			throw new SException(e, "[FileNotFoundException] Failed to determine file type.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to determine file type.");
		} catch (TikaException e) {
			throw new SException(e, "[TikaException] Failed to determine file type.");
		}// end of try
		
		return metadata.get(Metadata.CONTENT_TYPE);
	}// end of content_type
	public static String content_type(
			File file
			) throws SException {
		try {
			return content_type(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to determine file type.");
		}
	}// end of content_type
	
	public static String mime_type(
			byte[] file
			, String name
			) throws SException {
		
		Metadata metadata = new Metadata();
		metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, name);
		
		try (
				InputStream inputStream = new ByteArrayInputStream(file);
				TikaInputStream tikaInputStream = TikaInputStream.get(inputStream);
				) {
			
			return new Tika().detect(tikaInputStream, metadata);
			
		} catch (FileNotFoundException e) {
			throw new SException(e, "[FileNotFoundException] Failed to determine mime type.");
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to determine mime type.");
		}// end of try
		
	}// end of mime_type
	public static String mime_type(File file) throws SException {
		try {
			return mime_type(FileUtils.readFileToByteArray(file), file.getName());
		} catch (IOException e) {
			throw new SException(e, "[IOException] Failed to determine mime type.");
		}
	}// end of mime_type
	
	public static SFileMeta metadata(
			File file
			) throws SException {
		if(file == null) {
			return null;
		}
		String name = file.getName();
		String path = file.getParent();
		long size = file.length();
		if(file.isDirectory()) {
			return SFileMeta.builder()
					.name(name)
					.path(path)
					.size(size)
					.type("d")
					.build();
		}
		if(size == 0) {
			return SFileMeta.builder()
					.name(name)
					.path(path)
					.size(size)
					.type("f")
					.extension(extension(file))
					.build();
		}
		return SFileMeta.builder()
				.name(name)
				.path(path)
				.size(size)
				.type(file.isFile() ? "f" : "d")
				.extension(extension(file))
				.mime_type(mime_type(file))
				.content_type(content_type(file))
				.build();
	}// end of metadata
	public static SFileMeta metadata(
			byte[] file
			, String name
			) throws SException {
		if(file == null) {
			return null;
		}
		long size = file.length;
		if(size == 0) {
			return SFileMeta.builder()
					.name(name)
					.size(size)
					.type("f")
					.extension(extension(name))
					.build();
		}
		return SFileMeta.builder()
				.name(name)
				.size(size)
				.type("f")
				.extension(extension(name))
				.mime_type(mime_type(file, name))
				.content_type(content_type(file))
				.build();
	}// end of metadata
	
	public static boolean match(
			File file
			, SFileType[] allowed
			) throws SException {
		if(file == null || allowed == null) {
			return false;
		}
		SFileMeta meta = metadata(file);
		if(meta.size() == 0) {
			return false;
		}
		if(meta.is_directory()) {
			return false;
		}
		for(SFileType t : allowed) {
			if(t.extensions().contains(meta.extension())
					&& t.mime_types().contains(meta.mime_type())
					&& t.content_types().contains(meta.content_type())
					) {
				return true;
			}
		}// end of allowed
		return false;
	}// end of allowed
	public static boolean match(
			byte[] file
			, String name
			, SFileType[] allowed
			) throws SException {
		if(file == null || allowed == null) {
			return false;
		}
		SFileMeta meta = metadata(file, name);
		if(meta.size() == 0) {
			return false;
		}
		if(meta.is_directory()) {
			return false;
		}
		for(SFileType t : allowed) {
			if(t.extensions().contains(meta.extension())
					&& t.mime_types().contains(meta.mime_type())
					&& t.content_types().contains(meta.content_type())
					) {
				return true;
			}
		}// end of allowed
		return false;
	}// end of allowed
	
}
