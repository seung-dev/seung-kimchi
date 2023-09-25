package seung.kimchi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1IA5String;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLTaggedObject;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSAttributeTableGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.DefaultAuthenticatedAttributeTableGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.encoders.Hex;

import seung.kimchi.types.SSignCertDer;
import seung.kimchi.types.SSignPriKey;

public class SCertificate {

	public final static String _S_RSA = "RSA";
	public final static String _S_RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";
	public final static String _S_X509 = "X.509";
	public final static String _S_DER = "DER";
	
	public static PublicKey public_key(
			final byte[] encoded
			, final String algorithm
			, final String provider
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encoded);
		if(provider != null) {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			return keyFactory.generatePublic(encodedKeySpec);
		} else {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			return keyFactory.generatePublic(encodedKeySpec);
		}
	}// end of public_key
	public static PublicKey public_key(
			final byte[] encoded
			, final String algorithm
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		return public_key(
				encoded
				, algorithm
				, BouncyCastleProvider.PROVIDER_NAME//provider
				);
	}// end of public_key
	public static PublicKey public_key(
			final byte[] encoded
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		return public_key(
				encoded
				, _S_RSA//algorithm
				);
	}// end of public_key
	public static PublicKey public_key(
			final String encoded_hex
			, final String algorithm
			, final String provider
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return public_key(
				SConvert.decode_hex(encoded_hex)
				, algorithm
				, provider
				);
	}// end of public_key
	public static PublicKey public_key(
			final String encoded_hex
			, final String algorithm
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return public_key(
				SConvert.decode_hex(encoded_hex)
				, algorithm
				, BouncyCastleProvider.PROVIDER_NAME//provider
				);
	}// end of public_key
	public static PublicKey public_key(
			final String encoded_hex
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return public_key(
				SConvert.decode_hex(encoded_hex)
				, _S_RSA//algorithm
				);
	}// end of public_key
	
	public static PrivateKey private_key(
			final byte[] encoded
			, final String algorithm
			, final String provider
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encoded);
		if(provider != null) {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, provider);
			return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} else {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		}
	}// end of private_key
	public static PrivateKey private_key(
			final byte[] encoded
			, final String algorithm
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		return private_key(
				encoded
				, algorithm
				, BouncyCastleProvider.PROVIDER_NAME//provider
				);
	}// end of private_key
	public static PrivateKey private_key(
			final byte[] encoded
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		return private_key(
				encoded
				, _S_RSA//algorithm
				);
	}// end of private_key
	public static PrivateKey private_key(
			final String encoded_hex
			, final String algorithm
			, final String provider
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return private_key(
				SConvert.decode_hex(encoded_hex)
				, algorithm
				, provider
				);
	}// end of private_key
	public static PrivateKey private_key(
			final String encoded_hex
			, final String algorithm
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return private_key(
				SConvert.decode_hex(encoded_hex)
				, algorithm
				, BouncyCastleProvider.PROVIDER_NAME//provider
				);
	}// end of private_key
	public static PrivateKey private_key(
			final String encoded_hex
			) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, DecoderException {
		return private_key(
				SConvert.decode_hex(encoded_hex)
				, _S_RSA//algorithm
				);
	}// end of private_key
	
	public static X509Certificate x509_certificate(
			final byte[] encoded
			) throws IOException, CertificateException {
		X509Certificate x509_certificate = null;
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encoded);
				) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(_S_X509);
			x509_certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
		} catch (IOException e) {
			throw e;
		} catch (CertificateException e) {
			throw e;
		}
		return x509_certificate;
	}// end of x509_certificate
	
	public static List<String> key_usage(
			final X509Certificate x509_certificate
			) {
		
		List<String> key_usage = new ArrayList<>();
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.keyUsage.getId()));
		
		if(asn1OctetString == null) {
			return key_usage;
		}
		
		KeyUsage keyUsage = KeyUsage.getInstance(asn1OctetString.getOctets());
		
		if(keyUsage.hasUsages(KeyUsage.digitalSignature)) {
			key_usage.add("digitalSignature");
		} else if(keyUsage.hasUsages(KeyUsage.nonRepudiation)) {
			key_usage.add("nonRepudiation");
		} else if(keyUsage.hasUsages(KeyUsage.keyEncipherment)) {
			key_usage.add("keyEncipherment");
		} else if(keyUsage.hasUsages(KeyUsage.dataEncipherment)) {
			key_usage.add("dataEncipherment");
		} else if(keyUsage.hasUsages(KeyUsage.keyAgreement)) {
			key_usage.add("keyAgreement");
		} else if(keyUsage.hasUsages(KeyUsage.keyCertSign)) {
			key_usage.add("keyCertSign");
		} else if(keyUsage.hasUsages(KeyUsage.cRLSign)) {
			key_usage.add("cRLSign");
		} else if(keyUsage.hasUsages(KeyUsage.encipherOnly)) {
			key_usage.add("encipherOnly");
		} else if(keyUsage.hasUsages(KeyUsage.decipherOnly)) {
			key_usage.add("decipherOnly");
		}
		
		return key_usage;
	}// end of key_usage
	
	public static String certificate_policy_oid(
			final X509Certificate x509_certificate
			) {
		
		String certificate_policy = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.certificatePolicies.getId()));
		
		if(asn1OctetString == null) {
			return certificate_policy;
		}
		
		CertificatePolicies certificatePolicies = CertificatePolicies.getInstance(asn1OctetString.getOctets());
		for(PolicyInformation policyInformation : certificatePolicies.getPolicyInformation()) {
			if(policyInformation == null) {
				continue;
			}
			certificate_policy = policyInformation.getPolicyIdentifier().getId();
			break;
		}// end of policy
		
		return certificate_policy;
	}// end of certificate_policy
	
	public static String subject_alternative_name_oid(
			final X509Certificate x509_certificate
			) {
		
		String subject_alternative_name_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		if(asn1OctetString == null) {
			return subject_alternative_name_oid;
		}
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			// 0: [1.2.410.200004.10.1.1, [0][박종승, [[1.2.410.200004.10.1.1.1, [[2.16.840.1.101.3.4.2.1], [0]#ab0525126c906e01bcdebd2ac2ae5196f4635575fa7c5eebfd395e073a7cd0fc]]]]]
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			subject_alternative_name_oid = general_name_split[1].split(",")[0];
			break;
		}// end of names
		
		return subject_alternative_name_oid;
	}// end of subject_alternative_name_oid
	
	public static String vid_oid(
			final X509Certificate x509_certificate
			) {
		
		String vid_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid_oid = general_name_split[5].split(",")[0];
			break;
		}// end of names
		
		return vid_oid;
	}// end of vid_oid
	
	public static String vid_hash_algorithm_oid(
			final X509Certificate x509_certificate
			) {
		
		String vid_hash_algorithm_oid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid_hash_algorithm_oid = general_name_split[7].split("\\]")[0];
			break;
		}// end of names
		
		return vid_hash_algorithm_oid;
	}// end of vid_hash_algorithm_oid
	
	public static String vid(
			final X509Certificate x509_certificate
			) {
		
		String vid = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.subjectAlternativeName.getId()));
		
		GeneralNames generalNames = GeneralNames.getInstance(asn1OctetString.getOctets());
		
		String general_name_text = "";
		String[] general_name_split = null;
		for(GeneralName generalName : generalNames.getNames()) {
			
			if(generalName == null) {
				continue;
			}
			if(GeneralName.otherName != generalName.getTagNo()) {
				continue;
			}
			
			general_name_text = generalName.toString();
			if(general_name_text == null || !general_name_text.contains("[") || !general_name_text.contains("#")) {
				continue;
			}
			
			general_name_split = general_name_text.split("\\[");
			if(general_name_split.length != 9) {
				continue;
			}
			
			vid = general_name_text.split("#")[1].split("\\]")[0];
			break;
		}// end of names
		
		return vid;
	}// end of vid
	
	public static String crl_distribution_point(
			final X509Certificate x509_certificate
			) {
		
		String crl_distribution_point = "";
		
		ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(x509_certificate.getExtensionValue(Extension.cRLDistributionPoints.getId()));
		
		if(asn1OctetString == null) {
			return crl_distribution_point;
		}
		
		CRLDistPoint crlDistPoint = CRLDistPoint.getInstance(asn1OctetString.getOctets());
		
		for(DistributionPoint distributionPoint : crlDistPoint.getDistributionPoints()) {
			
			DistributionPointName distributionPointName = distributionPoint.getDistributionPoint();
			
			if(distributionPointName == null) {
				continue;
			}
			if(DistributionPointName.FULL_NAME != distributionPointName.getType()) {
				continue;
			}
			
			for(GeneralName general_name : GeneralNames.getInstance(distributionPointName.getName()).getNames()) {
				if(GeneralName.uniformResourceIdentifier != general_name.getTagNo()) {
					continue;
				}
				crl_distribution_point = ASN1IA5String.getInstance(general_name.getName()).getString();
				break;
			}
			
			if(!"".equals(crl_distribution_point)) {
				break;
			}
		}// end of distribution points
		
		return crl_distribution_point;
	}// end of crl_distribution_point
	
	public static SSignCertDer s_sign_cert_der(
			final byte[] encoded
			) throws CertificateException, IOException {
		
		X509Certificate x509_certificate = x509_certificate(encoded);
		if(x509_certificate == null) {
			return null;
		}
		
		String subject_alternative_name_oid = subject_alternative_name_oid(x509_certificate);
		String vid_oid = "";
		String vid_hash_algorithm_oid = "";
		String vid = "";
		if(!"".equals(subject_alternative_name_oid)) {
			vid_oid = vid_oid(x509_certificate);
			vid_hash_algorithm_oid = vid_hash_algorithm_oid(x509_certificate);
			vid = vid(x509_certificate);
		}
		
		return SSignCertDer.builder()
				.encoded(encoded)
				.type(x509_certificate.getType())
				.version(x509_certificate.getVersion())
				.serial_number(x509_certificate.getSerialNumber().toString())
				.signiture_algorithm_oid(x509_certificate.getSigAlgOID())
				.signiture_algorithm_name(x509_certificate.getSigAlgName())
				.issuer_dn(x509_certificate.getIssuerDN().getName())
				.subject_dn(x509_certificate.getSubjectDN().getName())
				.not_before(x509_certificate.getNotBefore().getTime())
				.not_after(x509_certificate.getNotAfter().getTime())
				.key_usage(key_usage(x509_certificate))
				.certificate_policy_oid(certificate_policy_oid(x509_certificate))
				.subject_alternative_name_oid(subject_alternative_name_oid)
				.vid_oid(vid_oid)
				.vid_hash_algorithm_oid(vid_hash_algorithm_oid)
				.vid(vid)
				.crl_distribution_point(crl_distribution_point(x509_certificate))
				.build()
				;
	}// end of s_sign_cert_der
	public static SSignCertDer s_sign_cert_der(
			final File file
			) throws CertificateException, IOException {
		return s_sign_cert_der(FileUtils.readFileToByteArray(file));
	}// end of s_sign_cert_der
	
	public static SSignPriKey s_sign_pri_key(
			final byte[] encoded
			) {
		
		ASN1Sequence seq = ASN1Sequence.getInstance(encoded);
		
		ASN1Sequence seq_0 = ASN1Sequence.getInstance(seq.getObjectAt(0));
		ASN1OctetString seq_1 = ASN1OctetString.getInstance(seq.getObjectAt(1));
		byte[] private_key = seq_1.getOctets();
		
		ASN1ObjectIdentifier seq_0_0 = ASN1ObjectIdentifier.getInstance(seq_0.getObjectAt(0));
		
		String private_key_algorythm_oid = seq_0_0.getId();
		
		String encryption_algorithm_oid = "";
		byte[] salt = null;
		int iteration_count = 0;
		int key_length = 0;
		String prf_algorithm_oid = "";
		byte[] iv = null;
		
		if("1.2.840.113549.1.5.13".equals(private_key_algorythm_oid)) {
			// pkcs5PBES2
			// https://www.rfc-editor.org/rfc/rfc8018
			
			ASN1Sequence seq_0_1 = ASN1Sequence.getInstance(seq_0.getObjectAt(1));
			
			ASN1Sequence seq_0_1_0 = ASN1Sequence.getInstance(seq_0_1.getObjectAt(0));
			ASN1Sequence seq_0_1_1 = ASN1Sequence.getInstance(seq_0_1.getObjectAt(1));
			
			ASN1ObjectIdentifier seq_0_1_0_0 = ASN1ObjectIdentifier.getInstance(seq_0_1_0.getObjectAt(0));
			encryption_algorithm_oid = seq_0_1_0_0.getId();
			ASN1Sequence seq_0_1_0_1 = ASN1Sequence.getInstance(seq_0_1_0.getObjectAt(1));
			
			ASN1OctetString seq_0_1_0_1_0 = ASN1OctetString.getInstance(seq_0_1_0_1.getObjectAt(0));
			salt = seq_0_1_0_1_0.getOctets();
			ASN1Integer seq_0_1_0_1_1 = ASN1Integer.getInstance(seq_0_1_0_1.getObjectAt(1));
			iteration_count = seq_0_1_0_1_1.intValueExact();
			if(seq_0_1_0_1.size() > 2) {
				ASN1Integer seq_0_1_0_0_1_2 = ASN1Integer.getInstance(seq_0_1_0_1.getObjectAt(2));
				key_length = seq_0_1_0_0_1_2.intValueExact();
			}
			
			ASN1ObjectIdentifier seq_0_1_1_0 = ASN1ObjectIdentifier.getInstance(seq_0_1_1.getObjectAt(0));
			prf_algorithm_oid = seq_0_1_1_0.getId();
			ASN1OctetString seq_0_1_1_1 = ASN1OctetString.getInstance(seq_0_1_1.getObjectAt(1));
			iv = seq_0_1_1_1.getOctets();
			
		} else if("1.2.410.200004.1.15".equals(private_key_algorythm_oid)) {
			// seedCBCWithSHA1
			// https://www.rfc-editor.org/rfc/rfc4269
			
			ASN1Sequence seq_0_1 = ASN1Sequence.getInstance(seq_0.getObjectAt(1));
			
			ASN1OctetString seq_0_1_0 = ASN1OctetString.getInstance(seq_0_1.getObjectAt(0));
			salt = seq_0_1_0.getOctets();
			ASN1Integer seq_0_1_1 = ASN1Integer.getInstance(seq_0_1.getObjectAt(1));
			iteration_count = seq_0_1_1.intValueExact();
			
		}// end of private_key_algorythm_oid
		
		return SSignPriKey.builder()
				.encoded(encoded)
				.private_key_algorythm_oid(private_key_algorythm_oid)
				.encryption_algorithm_oid(encryption_algorithm_oid)
				.salt(salt)
				.iteration_count(iteration_count)
				.key_length(key_length)
				.prf_algorithm_oid(prf_algorithm_oid)
				.iv(iv)
				.private_key(private_key)
				.build()
				;
	}// end of s_sign_pri_key
	public static SSignPriKey s_sign_pri_key(
			final File file
			) throws IOException {
		return s_sign_pri_key(FileUtils.readFileToByteArray(file));
	}// end of s_sign_pri_key
	
	public static byte[] decrypt_private_key(
			final byte[] encoded
			, final String secret
			) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] decrypted = null;
		
		SSignPriKey s_sign_pri_key = s_sign_pri_key(encoded);
		
		SecretKeySpec secretKeySpec = null;
		IvParameterSpec ivParameterSpec = null;
		while(true) {
			
			// pkcs5PBES2
			// https://www.rfc-editor.org/rfc/rfc8018
			if("1.2.840.113549.1.5.13".equals(s_sign_pri_key.private_key_algorythm_oid())) {
				
				PBEParametersGenerator pbeParametersGenerator = new PKCS5S2ParametersGenerator();
				pbeParametersGenerator.init(
						PBEParametersGenerator.PKCS5PasswordToBytes(secret.toCharArray())//password
						, s_sign_pri_key.salt()//salt
						, s_sign_pri_key.iteration_count()//iterationCount
						);
				
				int key_size = s_sign_pri_key.key_length();
				if(key_size == 0) {
					key_size = 256;
				}
				KeyParameter keyParameter = (KeyParameter) pbeParametersGenerator.generateDerivedParameters(
						key_size//keySize
						);
				
				secretKeySpec = new SecretKeySpec(keyParameter.getKey(), "SEED");
				ivParameterSpec = new IvParameterSpec(s_sign_pri_key.iv());
				
				break;
			}// end of 1.2.840.113549.1.5.13
			
			// seedCBCWithSHA1
			// https://www.rfc-editor.org/rfc/rfc4269
			if("1.2.410.200004.1.15".equals(s_sign_pri_key.private_key_algorythm_oid())) {
				
				MessageDigest message_digest_0 = MessageDigest.getInstance("SHA-1");
				message_digest_0.update(secret.getBytes("UTF-8"));
				message_digest_0.update(s_sign_pri_key.salt());
				
				byte[] digested_0 = message_digest_0.digest();
				for(int i = 1; i < s_sign_pri_key.iteration_count(); i++) {
					digested_0 = message_digest_0.digest(digested_0);
				}
				
				byte[] key = new byte[16];
				System.arraycopy(digested_0, 0, key, 0, 16);
				
				secretKeySpec = new SecretKeySpec(key, "SEED");
				
				byte[] iv = null;
				if("1.2.410.200004.1.4".equals(s_sign_pri_key.prf_algorithm_oid())) {
					iv = "0123456789012345".getBytes("UTF-8");
					ivParameterSpec = new IvParameterSpec(iv);
					break;
				}
				
				byte[] digested_1 = new byte[4];
				System.arraycopy(digested_0, 16, digested_1, 0, 4);
				
				MessageDigest message_digest_1 = MessageDigest.getInstance("SHA-1");
				message_digest_1.reset();
				message_digest_1.update(digested_1);
				
				byte[] digested_2 = message_digest_1.digest();
				
				iv = new byte[16];
				System.arraycopy(digested_2, 0, iv, 0, 16);
				
				ivParameterSpec = new IvParameterSpec(iv);
				
				break;
			}// end of 1.2.410.200004.1.15
			
			break;
		}// end of while
		
		Cipher cipher = Cipher.getInstance(SSecurity._S_SEED_CBC_PKCS5PADDING, BouncyCastleProvider.PROVIDER_NAME);
		cipher.init(
				Cipher.DECRYPT_MODE//opmode
				, secretKeySpec//key
				, ivParameterSpec//params
				);
		decrypted = cipher.doFinal(s_sign_pri_key.private_key());
		
		return decrypted;
	}// end of decrypt_private_key
	
	public static byte[] sign(
			final byte[] sign_cert_der
			, final byte[] sign_pri_key
			, final String secret
			, final byte[] message
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, OperatorCreationException, CertificateException, IOException, CMSException {
		
		SSignCertDer s_sign_cert_der = s_sign_cert_der(sign_cert_der);
		
		byte[] decrypted = decrypt_private_key(sign_pri_key, secret);
		
		KeyFactory keyFactory = KeyFactory.getInstance(_S_RSA);
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decrypted));
		
		ContentSigner contentSigner = new JcaContentSignerBuilder(s_sign_cert_der.signiture_algorithm_name())
				.setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.build(privateKey);
		
		JcaSignerInfoGeneratorBuilder jcaSignerInfoGeneratorBuilder = new JcaSignerInfoGeneratorBuilder(
				new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build()
				);
		SignerInfoGenerator infoGen = jcaSignerInfoGeneratorBuilder.build(contentSigner, s_sign_cert_der.x509_certificate());
		final CMSAttributeTableGenerator cmsAttributeTableGenerator = infoGen.getSignedAttributeTableGenerator();
		infoGen = new SignerInfoGenerator(
				infoGen
				, new DefaultAuthenticatedAttributeTableGenerator() {
					@SuppressWarnings("rawtypes")
					@Override
					public AttributeTable getAttributes(Map parameters) {
//							return super.getAttributes(parameters);
						AttributeTable attributeTable = cmsAttributeTableGenerator.getAttributes(parameters);
						return attributeTable.remove(CMSAttributes.cmsAlgorithmProtect);
					}
				}
				, infoGen.getUnsignedAttributeTableGenerator()
				);
		
		CMSSignedDataGenerator cmsSignedDataGenerator = new CMSSignedDataGenerator();
		cmsSignedDataGenerator.addCertificate(new X509CertificateHolder(s_sign_cert_der.encoded()));
		cmsSignedDataGenerator.addSignerInfoGenerator(infoGen);
		
		CMSTypedData cmsTypedData = new CMSProcessableByteArray(message);
		
		CMSSignedData cmsSignedData = cmsSignedDataGenerator.generate(cmsTypedData, true);
		
		return cmsSignedData.getEncoded(_S_DER);
	}// end of sign
	public static byte[] sign(
			final File sign_cert_der
			, final File sign_pri_key
			, final String secret
			, final byte[] message
			) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, OperatorCreationException, CertificateException, IOException, CMSException {
		return sign(
				FileUtils.readFileToByteArray(sign_cert_der)//s_sign_cert_der
				, FileUtils.readFileToByteArray(sign_pri_key)//s_sign_pri_key
				, secret
				, message
				);
	}// end of sign
	
	public static byte[] random_number(
			final byte[] encoded
			) throws IOException {
		
		byte[] random_number = null;
		
		ASN1TaggedObject asn1TaggedObject = null;
		ASN1Sequence asn1Sequence = null;
		for(ASN1Encodable asn1Encodable : ASN1Sequence.getInstance(encoded)) {
			
			if(!(asn1Encodable instanceof DLTaggedObject)) {
				continue;
			}
			
			asn1TaggedObject = ASN1TaggedObject.getInstance(asn1Encodable);
			if(asn1TaggedObject.getTagNo() != 0) {
				continue;
			}
			
			asn1Sequence = ASN1Sequence.getInstance(asn1TaggedObject.getBaseObject());
			for(int i = 0; i < asn1Sequence.size(); i++) {
				
				ASN1Encodable asn1Encodable1 = asn1Sequence.getObjectAt(i);
				if(!(asn1Encodable1 instanceof ASN1ObjectIdentifier)) {
					continue;
				}
				
				if(!"1.2.410.200004.10.1.1.3".equals(ASN1ObjectIdentifier.getInstance(asn1Encodable1).getId())) {
					continue;
				}
				
				ASN1Set asn1Set = ASN1Set.getInstance(asn1Sequence.getObjectAt(i + 1));
				ASN1BitString asn1BitString = ASN1BitString.getInstance(asn1Set.getObjectAt(0));
				random_number = asn1BitString.getOctets();
				
				break;
			}
			
			if(random_number != null) {
				break;
			}
			
		}
		
		return random_number;
	}// end of random_number
	public static byte[] random_number(
			final SSignPriKey s_sign_pri_key
			, final String secret
			) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return random_number(decrypt_private_key(s_sign_pri_key.encoded(), secret));
	}// end of random_number
	
	public static byte[] generate_vid(
			String rrn
			, byte[] random_number
			, String algorithm
			) throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
		
		DERSequence derSequence = new DERSequence(new ASN1Encodable[] {
				new DERPrintableString(rrn)
				, new DERBitString(random_number)
		});
		
		byte[] digested = SSecurity.digest(derSequence.getEncoded(), algorithm);
		return SSecurity.digest(digested, algorithm);
	}// end of generate_vid
	
	public static int verify_vid(
			final byte[] sign_cert_der
			, final byte[] sign_pri_key
			, final String secret
			, final String rrn
			) throws CertificateException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		int verify_vid = 0;
		
		SSignCertDer s_sign_cert_der = s_sign_cert_der(sign_cert_der);
		SSignPriKey s_sign_pri_key = s_sign_pri_key(sign_pri_key);
		byte[] random_number = random_number(s_sign_pri_key, secret);
		
		String sign_cert_der_vid = s_sign_cert_der.vid();
		String generate_vid = Hex.toHexString(
				generate_vid(
						rrn
						, random_number
						, s_sign_cert_der.vid_hash_algorithm_oid()//algorithm
						)
				);
		
		if(sign_cert_der_vid.equals(generate_vid)) {
			verify_vid = 1;
		}
		
		return verify_vid;
	}// end of verify_vid
	public static int verify_vid(
			final File sign_cert_der
			, final File sign_pri_key
			, final String secret
			, final String rrn
			) throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return verify_vid(
				FileUtils.readFileToByteArray(sign_cert_der)//sign_cert_der
				, FileUtils.readFileToByteArray(sign_pri_key)//sign_pri_key
				, secret
				, rrn
				);
	}// end of verify_vid
	
}
