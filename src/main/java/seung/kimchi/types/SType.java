package seung.kimchi.types;

import seung.kimchi.SJson;
import seung.kimchi.exceptions.SException;

/**
 * 데이터 객체의 기본 클래스 입니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SType {

	public String stringify(boolean pretty) throws SException {
		return SJson.stringify(this, pretty);
	}// end of stringify
	
	public String stringify() throws SException {
		return stringify(false);
	}// end of stringify
	
}
