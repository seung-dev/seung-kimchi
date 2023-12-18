package seung.kimchi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
public class SSort {

	@JsonProperty
	private String field;
	
	@JsonProperty
	private String order;
	
}
