package seung.kimchi.types.excel;

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
public class SExcel extends SType {

	@Builder.Default
	@JsonProperty
	private int number_of_sheets = 0;
	
	@Builder.Default
	@JsonProperty
	private List<SSheet> sheets = new ArrayList<>();
	
}
