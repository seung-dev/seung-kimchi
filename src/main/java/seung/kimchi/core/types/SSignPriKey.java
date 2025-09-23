package seung.kimchi.core.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SSignPriKey extends SType {

	@NotNull
	@JsonProperty
	private byte[] encoded;
	
	@JsonProperty
	private String private_key_algorythm_oid;
	
	@JsonProperty
	private String encryption_algorithm_oid;
	
	@JsonProperty
	private byte[] salt;
	
	@JsonProperty
	private int iteration_count;
	
	@JsonProperty
	private int key_length;
	
	@JsonProperty
	private String prf_algorithm_oid;
	
	@JsonProperty
	private byte[] iv;
	
	@JsonProperty
	private byte[] private_key;
	
}
