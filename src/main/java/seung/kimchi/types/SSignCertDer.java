package seung.kimchi.types;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public class SSignCertDer extends SType {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private byte[] encoded;
	public byte[] encoded() {
		return this.encoded;
	}
	
	@NotBlank
	private String type;
	public String type() {
		return this.type;
	}
	
	@NotBlank
	private int version;
	public int version() {
		return this.version;
	}
	
	@NotBlank
	private String serial_number;
	public String serial_number() {
		return this.serial_number;
	}
	
	private String signiture_algorithm_oid;
	public String signiture_algorithm_oid() {
		return this.signiture_algorithm_oid;
	}
	
	private String signiture_algorithm_name;
	public String signiture_algorithm_name() {
		return this.signiture_algorithm_name;
	}
	
	private String issuer_dn;
	public String issuer_dn() {
		return this.issuer_dn;
	}
	
	private String subject_dn;
	public String subject_dn() {
		return this.subject_dn;
	}
	
	private long not_before;
	public long not_before() {
		return this.not_before;
	}
	
	private long not_after;
	public long not_after() {
		return this.not_after;
	}
	
	private List<String> key_usage;
	public List<String> key_usage() {
		return this.key_usage;
	}
	
	private String certificate_policy_oid;
	public String certificate_policy_oid() {
		return this.certificate_policy_oid;
	}
	
	private String subject_alternative_name_oid;
	public String subject_alternative_name_oid() {
		return this.subject_alternative_name_oid;
	}
	
	private String vid_oid;
	public String vid_oid() {
		return this.vid_oid;
	}
	
	private String vid_hash_algorithm_oid;
	public String vid_hash_algorithm_oid() {
		return this.vid_hash_algorithm_oid;
	}
	
	private String vid;
	public String vid() {
		return this.vid;
	}
	
	private String crl_distribution_point;
	public String crl_distribution_point() {
		return this.crl_distribution_point;
	}
	
	public X509Certificate x509_certificate() throws IOException, CertificateException {
		X509Certificate x509_certificate = null;
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encoded);
				) {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			x509_certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
		} catch (IOException e) {
			throw e;
		} catch (CertificateException e) {
			throw e;
		}// end of try
		return x509_certificate;
	}// end of x509_certificate
	
}
