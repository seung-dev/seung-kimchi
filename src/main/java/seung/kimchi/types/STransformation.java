package seung.kimchi.types;

public enum STransformation {

	AES_GCM_NOPADDING_A("AES", "GCM", "NoPadding", 32, 16, 12, 16)
	;
	
	private String algorithm;
	private String mode;
	private String padding;
	private int key_size;
	private int tag_size;
	private int nonce_size;
	private int block_size;
	
	STransformation(
			String algorithm
			, String mode
			, String padding
			, int key_size
			, int tag_size
			, int nonce_size
			, int block_size
			) {
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
		this.key_size = key_size;
		this.tag_size = tag_size;
		this.nonce_size = nonce_size;
		this.block_size = block_size;
	}
	
	public String algorithm() {
		return this.algorithm;
	}
	
	public String mode() {
		return this.mode;
	}
	
	public String padding() {
		return this.padding;
	}
	
	public String transformation() {
		return String.format("%s/%s/%s", this.algorithm, this.mode, this.padding);
	}
	
	public int key_size() {
		return this.key_size;
	}
	
	public int tag_size() {
		return this.tag_size;
	}
	
	public int nonce_size() {
		return this.nonce_size;
	}
	
	public int block_size() {
		return this.block_size;
	}
	
}
