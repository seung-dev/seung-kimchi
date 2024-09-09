package seung.kimchi.types;

import seung.kimchi.SJson;

public class SType {

	public String stringify(boolean is_pretty) throws SException {
		return SJson.stringify(this, is_pretty);
	}// end of stringify
	
	public String stringify() throws SException {
		return stringify(false);
	}// end of stringify
	
}
