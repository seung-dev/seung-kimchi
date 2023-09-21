package seung.kimchi.types.excel;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import seung.kimchi.types.SType;

@Builder
@Accessors(fluent = true)
@Getter
public class SRow extends SType {

	@NotNull
	@JsonProperty
	private Integer row_no;
	
	@Builder.Default
	@JsonProperty
	private List<SCell> cells = new ArrayList<>();
	
}
