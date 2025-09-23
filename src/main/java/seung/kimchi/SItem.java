package seung.kimchi;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import seung.kimchi.core.SJson;
import seung.kimchi.core.SSecurity;
import seung.kimchi.core.SText;
import seung.kimchi.core.types.SAlgorithm;
import seung.kimchi.core.types.SException;
import seung.kimchi.core.types.SLinkedHashMap;

public class SItem {

	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param date   기준일시
	 * @param random 랜덤 문자열의 길이 입니다. 랜덤 문자열은 {@link #random(int, boolean, boolean)}를 사용합니다.
	 * @param suffix 접미사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws SException {@code random_size}는 0 보다 커야합니다.
	 * @since 0.0.1
	 */
	public static String item_no(
			final String prefix
			, final Date date
			, final int random_size
			, final String suffix
			) throws SException {
		return new StringBuilder()
				.append(SText.text(prefix, ""))
				.append(date.getTime() / 1000)
				.append(SText.random(random_size, true, true))
				.append(SText.text(suffix, ""))
				.toString();
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @param random 랜덤 문자열의 길이 입니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws SException {@code random_size}는 0 보다 커야합니다.
	 * @since 0.0.1
	 * @see #item_no(String, Date, int, String)
	 */
	public static String item_no(
			final String prefix
			, final int random_size
			) throws SException {
		return item_no(prefix, new Date(), random_size, "");
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 랜덤 문자열 길이: 5</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @param prefix 접두사; {@code null}일 수 있습니다. {@code null}인 경우 공백으로 처리합니다.
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws SException {@code random_size}는 0 보다 커야합니다.
	 * @since 0.0.1
	 * @see #item_no(String, Date, int, String)
	 */
	public static String item_no(
			final String prefix
			) throws SException {
		return item_no(prefix, 5);
	}// end of item_no
	/**
	 * 아이템 번호를 생성합니다.
	 * 
	 * <p>기본값:</p>
	 * <p>- 접두사: I</p>
	 * <p>- 일시: 현재</p>
	 * <p>- 랜덤 문자열 길이: 5</p>
	 * <p>- 접미사: 공백</p>
	 * 
	 * @return 아이템 번호 = 접두사 + 유닉스 시간(10자리) + 랜덤 문자열 + 접미사
	 * @throws SException {@code random_size}는 0 보다 커야합니다.
	 * @since 0.0.1
	 * @see #item_no(String, Date, int, String)
	 */
	public static String item_no() throws SException {
		return item_no("I");
	}// end of item_no
	
	public static String item_hash(
			SLinkedHashMap item
			, List<String> excluded
			) throws SException {
		
		Map<String, Object> i = new TreeMap<>();
		
		for(String key : item.keys()) {
			if(excluded != null && excluded.contains(key)) {
				continue;
			}
			i.put(key, item.get(key));
		}// end of keys
		
		String data = SJson.stringify(
				i//value
				, false//indent
				);
		
		return SFormat.encode_hex(SSecurity.digest(
				SFormat.bytes(data)//data
				, SAlgorithm._S_MD5//algorithm
				));
	}// end of item_hash
	public static String item_hash(
			SLinkedHashMap item
			) throws SException {
		return item_hash(item, null);
	}// end of item_hash
	
}
