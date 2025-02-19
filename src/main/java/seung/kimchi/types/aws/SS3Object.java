package seung.kimchi.types.aws;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.SFormat;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SS3Object extends SType {

	@NotBlank
	@JsonProperty
	private String key;
	
	@Builder.Default
	@JsonProperty
	private boolean directory = false;
	
	@JsonProperty
	private byte[] bytes;
	
	@JsonProperty
	private long content_length;
	
	@JsonProperty
	private String content_type;
	
	@JsonProperty
	private long last_modified;
	
	@JsonProperty
	private String md5;
	
	@JsonProperty
	private String version;
	
	@JsonProperty
	private Map<String, String> misc;
	
	public String date(
			String format
			) {
		return SFormat.date(format, this.last_modified);
	}// end of date
	
}
