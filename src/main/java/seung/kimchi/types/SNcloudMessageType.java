package seung.kimchi.types;

public enum SNcloudMessageType {

	SMS("SMS", "COMM")
	, LMS("LMS", "COMM")
	, MMS("LMS", "COMM")
	, SMS_AD("SMS", "AD")
	, LMS_AD("LMS", "AD")
	, MMS_AD("LMS", "AD")
	;
	
	private final String id;
	
	private final String type;
	
	private SNcloudMessageType(
			String id
			, String type
			) {
		this.id = id;
		this.type = type;
	}
	
	public String id() {
		return this.id;
	}
	
	public String type() {
		return this.type;
	}
	
}
