package seung.kimchi.types;

public enum STransformation {

	AES_TYPE_A("AES", "GCM", "NoPadding", 4096, 16, 16)
	;
	
	private String algorithm;
	private String mode;
	private String padding;
	private int block_size;
	private int iv_size;
	private int tag_size;
	
	STransformation(
			String algorithm
			, String mode
			, String padding
			, int block_size
			, int iv_size
			, int tag_size
			) {
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
		this.block_size = block_size;
		this.iv_size = iv_size;
		this.tag_size = tag_size;
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
	
	public int block_size() {
		return this.block_size;
	}
	
	public int iv_size() {
		return this.iv_size;
	}
	
	public int tag_size() {
		return this.tag_size;
	}
	
}
