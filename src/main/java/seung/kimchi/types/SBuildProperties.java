package seung.kimchi.types;

import lombok.Builder;

@Builder
public class SBuildProperties extends SType {

	private static final long serialVersionUID = 1L;
	
	private String build_group;
	public String build_group() {
		return this.build_group;
	}
	
	private String build_artifact;
	public String build_artifact() {
		return this.build_artifact;
	}
	
	private String build_name;
	public String build_name() {
		return this.build_name;
	}
	
	private String build_version;
	public String build_version() {
		return this.build_version;
	}
	
	private String build_time;
	public String build_time() {
		return this.build_time;
	}
	
}
