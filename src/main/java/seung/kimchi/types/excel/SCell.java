package seung.kimchi.types.excel;

import lombok.Builder;
import seung.kimchi.types.SType;

@Builder
public class SCell extends SType {

	private static final long serialVersionUID = 1L;
	
	private int row_no;
	public int row_no() {
		return this.row_no;
	}
	
	private int column_no;
	public int column_no() {
		return this.column_no;
	}
	
	private String cell_text;
	public String cell_text() {
		return this.cell_text;
	}
	
}
