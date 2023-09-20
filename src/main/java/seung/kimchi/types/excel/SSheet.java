package seung.kimchi.types.excel;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import seung.kimchi.types.SType;

@Builder
public class SSheet extends SType {

	private static final long serialVersionUID = 1L;
	
	private String sheet_name;
	public String sheet_name() {
		return this.sheet_name;
	}
	
	@Builder.Default
	private int physical_number_of_rows = 0;
	public int physical_number_of_rows() {
		return this.physical_number_of_rows;
	}
	
	@Builder.Default
	private List<SRow> rows = new ArrayList<>();
	public List<SRow> rows() {
		return this.rows;
	}
	
}
