package seung.kimchi;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 네트워크와 관련된 기능들을 제공합니다.
 * 다른 타입을 텍스트로 변환하는 기능들도 포함합니다.
 * 
 * @author seung
 * @since 0.0.1
 */
public class SNetwork {

	/**
	 * 호스트 이름을 확인합니다.
	 * 
	 * @return 호스트 이름을 가져오는데 실패하면 공백을 반환합니다.
	 * @since 0.0.1
	 */
	public static String hostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "";
		}// end of try
	}// end of hostname
	
	/**
	 * IP를 확인합니다.
	 * 
	 * @param host 호스트
	 * @return 호스트 이름을 찾을 수 없으면 {@code unknown}을 반환합니다.
	 * @since 0.0.1
	 */
	public static String nslookup(
			final String host
			) {
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName(host);
			for(InetAddress inetAddress : inetAddresses) {
				return inetAddress.getHostAddress();
			}
			return "";
		} catch (UnknownHostException e) {
			return "";
		}// end of try
	}// end of nslookup
	
}
