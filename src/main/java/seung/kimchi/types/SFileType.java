package seung.kimchi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Accessors(fluent = true)
@Getter
public class SFileType extends SType {

	@JsonProperty
	private String mime_type;
	
	@JsonProperty
	private String extension;
	
	@JsonProperty
	private String content_type;
	
}
