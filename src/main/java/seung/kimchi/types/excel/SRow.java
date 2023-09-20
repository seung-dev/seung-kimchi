package seung.kimchi.types.excel;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import seung.kimchi.types.SType;

@Builder
public class SRow extends SType {

	private static final long serialVersionUID = 1L;
	
	private int row_no;
	public int row_no() {
		return this.row_no;
	}
	
	@Builder.Default
	private List<SCell> cells = new ArrayList<>();
	public List<SCell> cells() {
		return this.cells;
	}
	
}
