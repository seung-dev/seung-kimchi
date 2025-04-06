package seung.kimchi.types.ncloud;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SNcloudMessageFile {

	@JsonProperty
	private String fileId;
	
}
