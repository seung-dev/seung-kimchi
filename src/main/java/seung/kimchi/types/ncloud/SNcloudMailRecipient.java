package seung.kimchi.types.ncloud;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import seung.kimchi.types.SLinkedHashMap;

@Builder
@AllArgsConstructor
@Getter
public class SNcloudMailRecipient {

	@NotBlank
	@JsonProperty
	private String type;
	
	@NotBlank
	@JsonProperty
	private String address;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private SLinkedHashMap parameters;
	
}
