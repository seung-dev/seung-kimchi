package seung.kimchi.types;

import com.fasterxml.jackson.core.JsonProcessingException;

import seung.kimchi.SJson;

public class SType {

	public String stringify(boolean is_pretty) throws JsonProcessingException {
		return SJson.stringify(this, is_pretty);
	}// end of stringify
	
	public String stringify() throws JsonProcessingException {
		return stringify(false);
	}// end of stringify
	
}
