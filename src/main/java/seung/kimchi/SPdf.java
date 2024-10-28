package seung.kimchi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import seung.kimchi.exceptions.SException;

public class SPdf {

	public static byte[] encrypt(
			final byte[] data
			, final String key
			, final int key_length
			, final boolean allowPrinting
			, final boolean allowExtraction
			, final boolean allowModifications
			, final boolean readOnly
			) throws SException {
		
		byte[] pdf = null;
		
		try(
				PDDocument pdDocument = Loader.loadPDF(data);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			AccessPermission accessPermission = new AccessPermission();
			accessPermission.setCanExtractContent(allowExtraction);
			accessPermission.setCanModify(allowModifications);
			accessPermission.setCanPrint(allowPrinting);
			if(readOnly) {
				accessPermission.setReadOnly();
			}
			/*
			accessPermission.setCanAssembleDocument(false);
			accessPermission.setCanExtractForAccessibility(false);
			accessPermission.setCanFillInForm(false);
			accessPermission.setCanModifyAnnotations(false);
			accessPermission.setCanPrintDegraded(false);
			*/
			
			StandardProtectionPolicy policy = new StandardProtectionPolicy(
					key
					, key
					, accessPermission
					);
			policy.setEncryptionKeyLength(key_length);
			
			pdDocument.protect(policy);
			pdDocument.save(byteArrayOutputStream);
			
			pdf = byteArrayOutputStream.toByteArray();
			
		} catch (IOException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
		return pdf;
	}// end of encrypt
	public static byte[] encrypt(
			final byte[] data
			, final String key
			, final boolean allowPrinting
			, final boolean allowExtraction
			, final boolean allowModifications
			, final boolean readOnly
			) throws SException {
		return encrypt(
				data
				, key
				, 256//key_length
				, allowPrinting
				, allowExtraction
				, allowModifications
				, readOnly
				);
	}// end of encrypt
	public static byte[] encrypt(
			final byte[] data
			, final String key
			) throws SException {
		return encrypt(data, key, false, false, false, true);
	}// end of encrypt
	public static byte[] encrypt(
			final File file
			, final String key
			) throws SException {
		try {
			return encrypt(FileUtils.readFileToByteArray(file), key);
		} catch (IOException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
	}// end of encrypt
	
	public static byte[] decrypt(
			final byte[] data
			, final String password
			) throws SException {
		
		byte[] pdf = null;
		
		try(
				PDDocument pdDocument = Loader.loadPDF(data);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				) {
			
			if(pdDocument.isEncrypted()) {
				pdDocument.setAllSecurityToBeRemoved(true);
			}
			
			pdDocument.save(byteArrayOutputStream);
			
			pdf = byteArrayOutputStream.toByteArray();
			
		} catch (IOException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
		return pdf;
	}// end of decrypt
	
}
