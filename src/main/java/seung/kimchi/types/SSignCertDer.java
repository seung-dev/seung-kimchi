package seung.kimchi.types;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.exceptions.SException;

@Builder
@Accessors(fluent = true)
@Getter
public class SSignCertDer extends SType {

	@NotNull
	private byte[] encoded;
	
	@NotBlank
	@JsonProperty
	private String type;
	
	@NotBlank
	@JsonProperty
	private int version;
	
	@NotBlank
	@JsonProperty
	private String serial_number;
	
	@JsonProperty
	private String signiture_algorithm_oid;
	
	@JsonProperty
	private String signiture_algorithm_name;
	
	@JsonProperty
	private String issuer_dn;
	
	@JsonProperty
	private String subject_dn;
	
	@JsonProperty
	private long not_before;
	
	@JsonProperty
	private long not_after;
	
	@JsonProperty
	private List<String> key_usage;
	
	@JsonProperty
	private String certificate_policy_oid;
	
	@JsonProperty
	private String subject_alternative_name_oid;
	
	@JsonProperty
	private String vid_oid;
	
	@JsonProperty
	private String vid_hash_algorithm_oid;
	
	@JsonProperty
	private String vid;
	
	@JsonProperty
	private String crl_distribution_point;
	
	public X509Certificate x509_certificate() throws SException {
		
		X509Certificate x509_certificate = null;
		
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encoded);
				) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			x509_certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
		} catch (IOException e) {
			throw new SException(e, "Something went wrong.");
		} catch (CertificateException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
		return x509_certificate;
	}// end of x509_certificate
	
}
