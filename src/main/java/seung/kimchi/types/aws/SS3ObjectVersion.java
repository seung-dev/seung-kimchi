package seung.kimchi.types.aws;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.SFormat;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SS3ObjectVersion extends SType {

	@JsonProperty
	private String version;
	
	@JsonProperty
	private boolean lastest;
	
	@JsonProperty
	private long content_length;
	
	@JsonProperty
	private long last_modified;
	
	@JsonProperty
	private String md5;
	
	public String date(
			String format
			) {
		return SFormat.date(format, this.last_modified);
	}// end of date
	
}
