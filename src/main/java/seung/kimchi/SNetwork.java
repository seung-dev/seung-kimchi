package seung.kimchi;

import java.net.InetAddress;
import java.net.UnknownHostException;

import seung.kimchi.types.SException;

public class SNetwork {

	public static String hostname() throws SException {
		
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
	}// end of hostname
	
	public static String nslookup(
			final String host
			) throws SException {
		
		try {
			InetAddress[] inetAddresses = InetAddress.getAllByName(host);
			for(InetAddress inetAddress : inetAddresses) {
				return inetAddress.getHostAddress();
			}
			return "";
		} catch (UnknownHostException e) {
			throw new SException(e, "Something went wrong.");
		}// end of try
		
	}// end of nslookup
	
}
