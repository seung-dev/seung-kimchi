package seung.kimchi.types;

import com.fasterxml.jackson.core.JsonProcessingException;

import seung.kimchi.core.SJson;
import seung.kimchi.core.types.SException;

/**
 * 데이터 객체의 기본 클래스 입니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SType {

	/**
	 * 요청값을 JSON 포멧 텍스트로 변환합니다.
	 * 
	 * @param ident 들여쓰기 여부
	 * @return JSON 포멧 텍스트
	 * @throws SException {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see SJson#stringify(Object, boolean)
	 */
	public String stringify(
			boolean ident
			) throws SException {
		return SJson.stringify(this, ident);
	}// end of stringify
	
	/**
	 * 요청값을 JSON 포멧 텍스트로 변환합니다.
	 * 
	 * @return JSON 포멧 텍스트
	 * @throws SException {@link JsonProcessingException}
	 * @since 0.0.1
	 * @see SJson#stringify(Object, boolean)
	 */
	public String stringify() throws SException {
		return stringify(false);
	}// end of stringify
	
}
