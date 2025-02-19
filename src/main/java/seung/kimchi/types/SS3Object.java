package seung.kimchi.types;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Accessors(fluent = true)
@Getter
public class SS3Object extends SType {

	@NotNull
	@JsonProperty
	private byte[] bytes;
	
	@JsonProperty
	private long content_length;
	
	@JsonProperty
	private String content_disposition;
	
	@JsonProperty
	private String content_type;
	
	@JsonProperty
	private Map<String, String> metadata;
	
}
