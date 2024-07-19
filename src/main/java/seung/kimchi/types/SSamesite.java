package seung.kimchi.types;

public enum SSamesite {

	NONE("None")
	, LAX("Lax")
	, STRICT("Strict")
	;
	
	private String text;
	
	SSamesite(String text) {
		this.text = text;
	}
	
	public String text() {
		return this.text;
	}
	
}
