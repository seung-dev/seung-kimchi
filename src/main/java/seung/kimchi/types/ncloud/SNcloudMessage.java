package seung.kimchi.types.ncloud;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SNcloudMessage {

	@NotBlank
	@JsonProperty
	private String to;
	
	@JsonProperty
	private String subject;
	
	@JsonProperty
	private String content;
	
}
