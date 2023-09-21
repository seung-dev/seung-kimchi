package seung.kimchi.types.excel;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SCell extends SType {

	@NotNull
	@JsonProperty
	private Integer row_no;
	
	@NotNull
	@JsonProperty
	private int column_no;
	
	@JsonProperty
	private String cell_text;
	
}
