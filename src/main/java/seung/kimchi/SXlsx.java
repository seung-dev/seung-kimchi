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
	
	private static String cell_value(Cell cell) {
		
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
	
	private static SCell read_cell(Cell cell) {
		return SCell.builder()
				.row_no(cell.getRowIndex())
				.column_no(cell.getColumnIndex())
				.cell_text(cell_value(cell))
				.build();
	}// end of read_cell
	
	private static SRow read_row(Row row) {
		SRow s_row = SRow.builder()
				.row_no(row.getRowNum())
				.build()
				;
		for(Cell cell : row) {
			s_row.cells().add(read_cell(cell));
		}// end of cell
		return s_row;
	}// end of read_row
	
	private static SSheet read_sheet(Sheet sheet) {
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
	
	public static SExcel read(byte[] file) throws IOException {
		
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
	
	public static short[] row_height(Sheet sheet, int row_no_min, int row_no_max) {
		short[] row_height = new short[row_no_max - row_no_min + 1];
		for(int row_no = row_no_min; row_no <= row_no_max; row_no++) {
			row_height[row_no] = sheet.getRow(row_no).getHeight();
		}
		return row_height;
	}// end of row_height
	
	private static CellStyle[] cell_style(Sheet sheet, int row_no, int cell_no_max) {
		CellStyle[] cell_style = new CellStyle[cell_no_max + 1];
		for(int cell_no = 0; cell_no <= cell_no_max; cell_no++) {
			cell_style[cell_no] = sheet.getRow(row_no).getCell(cell_no).getCellStyle();
		}
		return cell_style;
	}// end of cell_style
	
	private static boolean write_sheet_rows(
			XSSFWorkbook read_workbook
			, XSSFSheet read_sheet
			, SLinkedHashMap sheet_data
			) {
		
		int row_no = sheet_data.get_int("row_no");
		int cell_no_max = read_sheet.getRow(row_no).getPhysicalNumberOfCells() - 1;
		
		// style
		short read_row_height = read_sheet.getRow(row_no).getHeight();
		CellStyle[] read_cell_style = cell_style(read_sheet, row_no, cell_no_max);
		
		// remove
		read_sheet.removeRow(read_sheet.getRow(row_no));
		
		List<SLinkedHashMap> rows = sheet_data.get_list_slinkedhashmap("rows");
		if(rows == null) {
			return false;
		}
		
		XSSFRow write_row = null;
		int cell_no = 0;
		XSSFCell write_cell = null;
		for(SLinkedHashMap row : rows) {
			
			write_row = read_sheet.createRow(row_no++);
			
			write_row.setHeight(read_row_height);
			
			cell_no = 0;
			for(String field_name : row.keys()) {
				
				write_cell = write_row.createCell(cell_no);
				write_cell.setCellValue(row.get_text(field_name));
				write_cell.setCellStyle(read_cell_style[cell_no]);
				cell_no++;
				
			}// end of row
			
		}// end of rows
		
		return true;
	}// end of write_sheet_rows
	
	private static void write_sheet(
			XSSFWorkbook read_workbook
			, XSSFSheet read_sheet
			, SLinkedHashMap sheet_data
			) {
		
		if("1".equals(sheet_data.get_text("is_map", ""))) {
//			write_sheet_cells(read_sheet, sheet_data);
		} else {
			write_sheet_rows(read_workbook, read_sheet, sheet_data);
		}
		
	}// end of write_sheet
	
	public static byte[] write(
			String request_code
			, byte[] read_bytes
			, SLinkedHashMap excel_data
			) throws IOException {
		
		byte[] excel = null;
		
		try (
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(read_bytes);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				XSSFWorkbook read_workbook = new XSSFWorkbook(byteArrayInputStream);
				) {
			
			List<SLinkedHashMap> sheets = excel_data.get_list_slinkedhashmap(_S_SHEETS);
			
			SLinkedHashMap sheet_data = null;
			for(int sheet_no = 0; sheet_no < read_workbook.getNumberOfSheets(); sheet_no++) {
				
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
			
		} catch (IOException e) {
			throw e;
		}// end of try
		
		return excel;
	}// end of write
	
}
