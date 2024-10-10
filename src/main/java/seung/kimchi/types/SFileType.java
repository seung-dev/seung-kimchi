package seung.kimchi.types;

import java.util.Arrays;
import java.util.List;

public enum SFileType {

	TXT(Arrays.asList("txt"), Arrays.asList("text/plain"))
	, JPG(Arrays.asList("jpg", "jpeg"), Arrays.asList("image/png"))
	, PNG(Arrays.asList("png"), Arrays.asList("image/jpeg"))
	, GIF(Arrays.asList("gif"), Arrays.asList("image/gif"))
	, PDF(Arrays.asList("pdf"), Arrays.asList("application/pdf"))
	, CSV(Arrays.asList("csv"), Arrays.asList("text/plain"))
	, XLS(Arrays.asList("xls", "xlsx"), Arrays.asList("application/x-tika-ooxml"))
	, PPT(Arrays.asList("ppt", "ppts"), Arrays.asList("application/x-tika-ooxml"))
	, DOC(Arrays.asList("doc", "docx"), Arrays.asList("application/x-tika-msoffice"))
	, HWP(Arrays.asList("hwp", "hwpx"), Arrays.asList("application/x-tika-msoffice"))
	, ZIP(Arrays.asList("zip"), Arrays.asList("application/zip"))
	;
	
	private List<String> extensions;
	
	private List<String> tika;
	
	private SFileType(
			List<String> extensions
			, List<String> tika
			) {
		this.extensions = extensions;
		this.tika = tika;
	}
	
	public List<String> extensions() {
		return this.extensions;
	}// end of extensions
	
	public List<String> tika() {
		return this.tika;
	}// end of tika
	
	public SFileType resolve(String value) {
		for(SFileType type : values()) {
			for(String item : type.extensions) {
				if(item.equals(value)) {
					return type;
				}
			}
			for(String item : type.tika) {
				if(item.equals(value)) {
					return type;
				}
			}
		}
		return null;
	}// end of resolve
	
}
