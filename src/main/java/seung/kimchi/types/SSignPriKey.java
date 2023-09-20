package seung.kimchi.types;

import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public class SSignPriKey extends SType {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private byte[] encoded;
	public byte[] encoded() {
		return this.encoded;
	}
	
	private String private_key_algorythm_oid;
	public String private_key_algorythm_oid() {
		return this.private_key_algorythm_oid;
	}
	
	private String encryption_algorithm_oid;
	public String encryption_algorithm_oid() {
		return this.encryption_algorithm_oid;
	}
	
	private byte[] salt;
	public byte[] salt() {
		return this.salt;
	}
	
	private int iteration_count;
	public int iteration_count() {
		return this.iteration_count;
	}
	
	private int key_length;
	public int key_length() {
		return this.key_length;
	}
	
	private String prf_algorithm_oid;
	public String prf_algorithm_oid() {
		return this.prf_algorithm_oid;
	}
	
	private byte[] iv;
	public byte[] iv() {
		return this.iv;
	}
	
	private byte[] private_key;
	public byte[] private_key() {
		return this.private_key;
	}
	
}
