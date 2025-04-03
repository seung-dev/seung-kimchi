package seung.kimchi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
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
