package seung.kimchi.core;

import java.security.SecureRandom;

/**
 * 숫자 처리와 관련된 기능들을 제공합니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SNumber {

	/**
	 * int 타입으로 변환합니다.
	 * 
	 * @param value
	 * @return int 타입 값
	 * @throws NumberFormatException 유효한 형식이 아닌 경우 발생합니다.
	 */
	public static long to_int(
			final String value
			) {
		return Integer.parseInt(value);
	}// end of to_int
	
	/**
	 * long 타입으로 변환합니다.
	 * 
	 * @param value
	 * @return long 타입 값
	 * @throws NumberFormatException 유효한 형식이 아닌 경우 발생합니다.
	 */
	public static long to_long(
			final String value
			) {
		return Long.parseLong(value);
	}// end of to_long
	
	/**
	 * double 타입으로 변환합니다.
	 * 
	 * @param value
	 * @return double 타입 값
	 * @throws NumberFormatException 유효한 형식이 아닌 경우 발생합니다.
	 */
	public static double to_double(
			final String value
			) {
		return Double.parseDouble(value);
	}// end of to_double
	
	/**
	 * 랜덤 숫자를 생성합니다.
	 * 
	 * @param min 최소값
	 * @param max 최대값
	 * @return 랜덤 숫자
	 */
	public static int random(
			final int min
			, final int max
			) {
		return new SecureRandom().nextInt(max - min + 1) + min;
	}// end of random
	
}
