package seung.kimchi.types.excel;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import seung.kimchi.types.SType;

@Builder
public class SExcel extends SType {

	private static final long serialVersionUID = 1L;
	
	@Builder.Default
	private int number_of_sheets = 0;
	public int number_of_sheets() {
		return this.number_of_sheets;
	}
	
	@Builder.Default
	private List<SSheet> sheets = new ArrayList<>();
	public List<SSheet> sheets() {
		return this.sheets;
	}
	
}
