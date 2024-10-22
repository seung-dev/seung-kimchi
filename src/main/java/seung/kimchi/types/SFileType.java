package seung.kimchi.types;

import java.util.Arrays;
import java.util.List;

public enum SFileType {
	
	TXT(Arrays.asList("txt")
			, Arrays.asList(
					"text/plain"
					, "text/x-robots"
					)
			, Arrays.asList(
					"text/plain"
					, "text/x-robots"
					)
			)
	, JPG(Arrays.asList("jpeg", "jpg")
			, Arrays.asList(
					"image/jpeg"
					)
			, Arrays.asList(
					"image/jpeg"
					)
			)
	, PNG(Arrays.asList("png")
			, Arrays.asList(
					"image/png"
					)
			, Arrays.asList(
					"image/png"
					)
			)
	, GIF(Arrays.asList("gif")
			, Arrays.asList(
					"image/gif"
					)
			, Arrays.asList(
					"image/gif"
					)
			)
	, SVG(Arrays.asList("svg")
			, Arrays.asList(
					"image/svg+xml"
					)
			, Arrays.asList(
					"image/svg+xml"
					, "text/plain"
					)
			)
	, ICO(Arrays.asList("ico")
			, Arrays.asList(
					"image/vnd.microsoft.icon"
					, "text/html"
					)
			, Arrays.asList(
					"image/vnd.microsoft.icon"
					, "text/html"
					)
			)
	, WEBP(Arrays.asList("webp")
			, Arrays.asList(
					"image/webp"
					)
			, Arrays.asList(
					"image/webp"
					)
			)
	, PDF(Arrays.asList("pdf")
			, Arrays.asList(
					"application/pdf"
					)
			, Arrays.asList(
					"application/pdf"
					)
			)
	, CSV(Arrays.asList("csv")
			, Arrays.asList(
					"text/csv"
					)
			, Arrays.asList(
					"text/plain"
					, "application/octet-stream"
					)
			)
	, XLSX(Arrays.asList("xlsx", "xls", "xlsm")
			, Arrays.asList(
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
					, "application/vnd.ms-excel"
					, "application/vnd.ms-excel.sheet.macroenabled.12"
					, "application/x-tika-msoffice"
					)
			, Arrays.asList(
					"application/zip"
					, "application/x-tika-msoffice"
					, "application/x-tika-ooxml"
					)
			)
	, DOCX(Arrays.asList("docx")
			, Arrays.asList(
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
					, "application/x-ms-owner"
					)
			, Arrays.asList(
					"application/x-ms-owner"
					, "application/x-tika-ooxml"
					)
			)
	, PPTX(Arrays.asList("pptx", "ppt", "pptm")
			, Arrays.asList(
					"application/vnd.openxmlformats-officedocument.presentationml.presentation"
					, "application/vnd.ms-powerpoint"
					, "application/vnd.ms-powerpoint.presentation.macroenabled.12"
					)
			, Arrays.asList(
					"application/zip"
					, "application/x-tika-msoffice"
					, "application/x-tika-ooxml"
					)
			)
	, HWPX(Arrays.asList("hwpx", "hwp")
			, Arrays.asList(
					"application/hwp+zip"
					, "application/x-tika-msoffice"
					)
			, Arrays.asList(
					"application/zip"
					, "application/x-tika-msoffice"
					)
			)
	, ZIP(Arrays.asList("zip")
			, Arrays.asList(
					"application/zip"
					)
			, Arrays.asList(
					"application/zip"
					)
			)
	, GZIP(Arrays.asList("gz")
			, Arrays.asList(
					"application/gzip"
					)
			, Arrays.asList(
					"application/gzip"
					)
			)
	, JSON(Arrays.asList("json")
			, Arrays.asList(
					"application/json"
					)
			, Arrays.asList(
					"text/plain"
					)
			)
	, HTML(Arrays.asList("html")
			, Arrays.asList(
					"text/html"
					)
			, Arrays.asList(
					"text/html"
					, "text/plain"
					)
			)
	, XML(Arrays.asList("xml")
			, Arrays.asList(
					"application/xml"
					)
			, Arrays.asList(
					"application/xml"
					)
			)
	, DTD(Arrays.asList("dtd")
			, Arrays.asList(
					"application/xml-dtd"
					)
			, Arrays.asList(
					"text/plain"
					)
			)
	, TTF(Arrays.asList("ttf")
			, Arrays.asList(
					"application/x-font-ttf"
					)
			, Arrays.asList(
					"application/x-font-ttf"
					)
			)
	, SH(Arrays.asList("sh")
			, Arrays.asList(
					"application/x-sh"
					)
			, Arrays.asList(
					"application/x-sh"
					)
			)
	, INI(Arrays.asList("ini")
			, Arrays.asList(
					"text/x-ini"
					)
			, Arrays.asList(
					"text/plain"
					)
			)
	, PEM(Arrays.asList("pem")
			, Arrays.asList(
					"application/x-x509-key; format=pem"
					)
			, Arrays.asList(
					"application/x-x509-key; format=pem"
					)
			)
	, EXE(Arrays.asList("exe")
			, Arrays.asList(
					"application/x-dosexec"
					)
			, Arrays.asList(
					"application/x-msdownload"
					)
			)
	, PKG(Arrays.asList("pkg")
			, Arrays.asList(
					"application/vnd.xara"
					)
			, Arrays.asList(
					"application/vnd.xara"
					)
			)
	, DMG(Arrays.asList("dmg")
			, Arrays.asList(
					"application/x-apple-diskimage"
					, "application/zlib"
					, "application/x-xz"
					, "application/x-bzip2"
					)
			, Arrays.asList(
					"application/zlib"
					, "application/x-xz"
					, "application/x-bzip2"
					, "application/octet-stream"
					)
			)
	, RDP(Arrays.asList("rdp")
			, Arrays.asList(
					"text/plain"
					)
			, Arrays.asList(
					"text/plain"
					)
			)
	;
	
	private List<String> extensions;
	
	private List<String> mime_types;
	
	private List<String> content_types;
	
	private SFileType(
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
	
	public static SFileType resolve(String extension) {
		for(SFileType type : values()) {
			for(String ext : type.extensions) {
				if(ext.equals(extension)) {
					return type;
				}
			}
		}
		return null;
	}// end of resolve
	
}
