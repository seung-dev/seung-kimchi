package seung.kimchi.core.types.excel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SSheet extends SType {

	@JsonProperty
	private String sheet_name;
	
	@Builder.Default
	@JsonProperty
	private int physical_number_of_rows = 0;
	
	@Builder.Default
	@JsonProperty
	private List<SRow> rows = new ArrayList<>();
	
}
