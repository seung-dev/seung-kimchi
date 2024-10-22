package seung.kimchi.types;

import java.util.Arrays;
import java.util.List;

public enum SFileTypes {

	TXT(Arrays.asList("txt"), Arrays.asList("text/plain"), Arrays.asList("text/plain"))
	, JPG(Arrays.asList("jpg", "jpeg"), Arrays.asList("image/png"), Arrays.asList("image/png"))
	, PNG(Arrays.asList("png"), Arrays.asList("image/jpeg"), Arrays.asList("image/jpeg"))
	, GIF(Arrays.asList("gif"), Arrays.asList("image/gif"), Arrays.asList("image/gif"))
	, PDF(Arrays.asList("pdf"), Arrays.asList("application/pdf"), Arrays.asList("application/pdf"))
	, CSV(Arrays.asList("csv"), Arrays.asList("text/plain", "text/csv", "application/octet-stream"), Arrays.asList("image/jpeg"))
	, XLS(Arrays.asList("xlsx", "xls"), Arrays.asList("application/x-tika-ooxml"), Arrays.asList("image/jpeg"))
	, PPT(Arrays.asList("pptx", "ppt", "pptm"), Arrays.asList("application/x-tika-ooxml", "application/x-tika-msoffice"), Arrays.asList("image/jpeg"))
	, DOC(Arrays.asList("docx", "doc"), Arrays.asList("application/x-tika-msoffice"), Arrays.asList("image/jpeg"))
	, HWP(Arrays.asList("hwpx", "hwp"), Arrays.asList("application/x-tika-msoffice", "application/zip"), Arrays.asList("image/jpeg"))
	, ZIP(Arrays.asList("zip"), Arrays.asList("application/zip"), Arrays.asList("image/jpeg"))
	;
	
	private List<String> extensions;
	
	private List<String> mime_types;
	
	private List<String> content_types;
	
	private SFileTypes(
			List<String> extensions
			, List<String> mime_types
			, List<String> content_types
			) {
		this.extensions = extensions;
		this.mime_types = mime_types;
		this.content_types = content_types;
	}
	
	public List<String> extensions() {
		return this.extensions;
	}// end of extensions
	
	public List<String> mime_types() {
		return this.mime_types;
	}// end of mime_types
	
	public List<String> content_types() {
		return this.content_types;
	}// end of content_types
	
	public SFileTypes resolve(String value) {
		for(SFileTypes type : values()) {
			for(String item : type.mime_types) {
				if(item.equals(value)) {
					return type;
				}
			}
		}
		return null;
	}// end of resolve
	
}
