package seung.kimchi.core.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SFileMeta extends SType {

	@JsonProperty
	private String name;
	
	@JsonProperty
	private String path;
	
	@JsonProperty
	private Long size;
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private String extension;
	
	@JsonProperty
	private String mime_type;
	
	@JsonProperty
	private String content_type;
	
	public boolean is_file() {
		return "f".equals(type);
	}// end of is_file
	
	public boolean is_directory() {
		return "d".equals(type);
	}// end of is_file
	
}
