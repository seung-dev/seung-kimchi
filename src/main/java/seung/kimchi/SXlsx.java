package seung.kimchi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import seung.kimchi.types.SLinkedHashMap;
import seung.kimchi.types.excel.SCell;
import seung.kimchi.types.excel.SExcel;
import seung.kimchi.types.excel.SRow;
import seung.kimchi.types.excel.SSheet;

public class SXlsx {

	public final static String _S_SHEETS = "sheets";
	
	public final static String _S_SHEET_NAME = "sheet_name";
	
	public final static String _S_SHEET_NO_BEGIN = "sheet_no_begin";
	
	public final static String _S_ROWS = "rows";
	
	public final static String _S_ROW_NO_BEGIN = "row_no_begin";
	
	private static String cell_value(final Cell cell) {
		
		String cell_value = null;
		if(CellType.STRING == cell.getCellType()) {
			cell_value = cell.getStringCellValue();
		} else if(CellType.NUMERIC == cell.getCellType()) {
			if(DateUtil.isCellDateFormatted(cell)) {
				cell_value = "" + cell.getDateCellValue();
			} else {
				cell_value = NumberToTextConverter.toText(cell.getNumericCellValue());
			}
		} else if(CellType.BOOLEAN == cell.getCellType()) {
			cell_value = Boolean.toString(cell.getBooleanCellValue());
		} else if(CellType.FORMULA == cell.getCellType()) {
			cell_value = cell.getCellFormula();
		} else if(CellType.BLANK == cell.getCellType()) {
			cell_value = "";
		} else if(CellType._NONE == cell.getCellType()) {
			cell_value = "";
		}// end of cell type
		
		return cell_value;
	}// end of cell_value
	
	private static SCell read_cell(final Cell cell) {
		return SCell.builder()
				.row_no(cell.getRowIndex())
				.column_no(cell.getColumnIndex())
				.cell_text(cell_value(cell))
				.build();
	}// end of read_cell
	
	private static SRow read_row(final Row row) {
		SRow s_row = SRow.builder()
				.row_no(row.getRowNum())
				.build()
				;
		for(Cell cell : row) {
			s_row.cells().add(read_cell(cell));
		}// end of cell
		return s_row;
	}// end of read_row
	
	private static SSheet read_sheet(final Sheet sheet) {
		SSheet s_sheet = SSheet.builder()
				.sheet_name(sheet.getSheetName())
				.physical_number_of_rows(sheet.getPhysicalNumberOfRows())
				.build()
				;
		for(Row row : sheet) {
			s_sheet.rows().add(read_row(row));
		}// end of row
		return s_sheet;
	}// end of read_sheet
	
	public static SExcel read(final byte[] file) throws IOException {
		
		SExcel s_excel = null;
		SExcel.builder()
				.build()
				;
		
		try (
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
				XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);
				) {
			
			s_excel = SExcel.builder()
					.number_of_sheets(workbook.getNumberOfSheets())
					.build()
					;
			
			for(Sheet sheet : workbook) {
				
				s_excel.sheets().add(read_sheet(sheet));
				
			}// end of sheet
			
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return s_excel;
	}// end of read
	
	public static short[] row_height(
			final Sheet sheet
			, final int row_no_min
			, final int row_no_max
			) {
		short[] row_height = new short[row_no_max - row_no_min + 1];
		for(int row_no = row_no_min; row_no <= row_no_max; row_no++) {
			row_height[row_no] = sheet.getRow(row_no).getHeight();
		}
		return row_height;
	}// end of row_height
	
	private static CellType[] cell_type(
			final Sheet sheet
			, final int row_no
			, final int cell_no_max
			) {
		CellType[] cell_type = new CellType[cell_no_max + 1];
		Row row = sheet.getRow(row_no);
		for(int cell_no = 0; cell_no <= cell_no_max; cell_no++) {
			cell_type[cell_no] = row.getCell(cell_no).getCellType();
		}
		return cell_type;
	}// end of cell_type
	
	private static CellStyle[] cell_style(
			final Sheet sheet
			, final int row_no
			, final int cell_no_max
			) {
		CellStyle[] cell_style = new CellStyle[cell_no_max + 1];
		for(int cell_no = 0; cell_no <= cell_no_max; cell_no++) {
			cell_style[cell_no] = sheet.getRow(row_no).getCell(cell_no).getCellStyle();
		}
		return cell_style;
	}// end of cell_style
	
	private static void write_cell(
			XSSFRow xssf_row
			, int cell_no
			, CellType cell_type
			, CellStyle cell_style
			, String cell_value
			) {
		
		XSSFCell xssf_cell = xssf_row.createCell(cell_no);
		
		xssf_cell.setCellType(cell_type);
		xssf_cell.setCellStyle(cell_style);
		
		while(true) {
			
			if(CellType.NUMERIC == cell_type) {
				xssf_cell.setCellValue(Double.valueOf(cell_value));
				break;
			}
			
			xssf_cell.setCellValue(cell_value);
			
			break;
		}// end of while
		
	}// end of write_cell
	
	private static boolean write_sheet_rows(
			final XSSFWorkbook read_workbook
			, final XSSFSheet read_sheet
			, SLinkedHashMap sheet_data
			) {
		
		int row_no = sheet_data.get_int(_S_ROW_NO_BEGIN);
		int cell_no_max = read_sheet.getRow(row_no).getPhysicalNumberOfCells() - 1;
		
		// style
		short read_row_height = read_sheet.getRow(row_no).getHeight();
		CellType[] read_cell_type = cell_type(read_sheet, row_no, cell_no_max);
		CellStyle[] read_cell_style = cell_style(read_sheet, row_no, cell_no_max);
		
		// remove
		read_sheet.removeRow(read_sheet.getRow(row_no));
		
		List<SLinkedHashMap> rows = sheet_data.get_list_slinkedhashmap(_S_ROWS);
		if(rows == null) {
			return false;
		}
		
		XSSFRow xssf_row = null;
		int cell_no = 0;
		for(SLinkedHashMap row : rows) {
			
			xssf_row = read_sheet.createRow(row_no++);
			
			xssf_row.setHeight(read_row_height);
			
			cell_no = 0;
			for(String field_name : row.keys()) {
				
				write_cell(
						xssf_row
						, cell_no
						, read_cell_type[cell_no]//cell_type
						, read_cell_style[cell_no]//cell_style
						, row.get_text(field_name, "")//value
						);
				
				cell_no++;
				
			}// end of row
			
		}// end of rows
		
		return true;
	}// end of write_sheet_rows
	
	private static void write_sheet(
			final XSSFWorkbook read_workbook
			, final XSSFSheet read_sheet
			, SLinkedHashMap sheet_data
			) {
		
		if("1".equals(sheet_data.get_text("is_map", ""))) {
//			write_sheet_cells(read_sheet, sheet_data);
		} else {
			write_sheet_rows(read_workbook, read_sheet, sheet_data);
		}
		
	}// end of write_sheet
	
	public static byte[] write(
			final byte[] template
			, final SLinkedHashMap data
			) throws IOException {
		
		byte[] excel = null;
		
		try (
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(template);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				XSSFWorkbook read_workbook = new XSSFWorkbook(byteArrayInputStream);
				) {
			
			List<SLinkedHashMap> sheets = data.get_list_slinkedhashmap(_S_SHEETS);
			int sheet_size = sheets.size();
			int sheet_no_begin = data.get_int(_S_SHEET_NO_BEGIN, 0);
			
			SLinkedHashMap sheet_data = null;
			for(int sheet_no = sheet_no_begin; sheet_no < read_workbook.getNumberOfSheets(); sheet_no++) {
				
				if(sheet_size < sheet_no + 1) {
					break;
				}
				
				if(sheets != null) {
					sheet_data = sheets.get(sheet_no);
					if(!sheet_data.is_empty(_S_SHEET_NAME)) {
						read_workbook.setSheetName(sheet_no, sheet_data.get_text(_S_SHEET_NAME));
					}
				} else {
					sheet_data = new SLinkedHashMap();
				}
				
				write_sheet(read_workbook, read_workbook.getSheetAt(sheet_no), sheet_data);
				
			}// end of sheet
			
			read_workbook.write(byteArrayOutputStream);
			
			excel = byteArrayOutputStream.toByteArray();
			
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return excel;
	}// end of write
	
}
