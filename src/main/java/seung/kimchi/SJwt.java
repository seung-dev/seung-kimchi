package seung.kimchi;

import java.security.Key;
import java.security.PublicKey;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.SecretKey;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.WeakKeyException;
import seung.kimchi.exceptions.SException;
import seung.kimchi.types.SError;
import seung.kimchi.types.SSamesite;

public class SJwt {

	public static final String _JWT_HEADER_TYPE = "JWT";
	
	public static final String _JWT_COOKIE_PREFIX = "Bearer ";
	
	public static SecretKey hmac_key(
			final String hex
			) throws SException {
		
		try {
			return Keys.hmacShaKeyFor(Hex.decodeHex(hex));
		} catch (WeakKeyException e) {
			throw new SException(e, "[WeakKeyException] Failed to create key.");
		} catch (DecoderException e) {
			throw new SException(e, "[DecoderException] Failed to create key.");
		}// end of try
		
	}// end of hmac_key
	
	public static String jws(
			Key key
			, String typ
//			, String jti
			, String iss
			, String sub
			, Date iat
			, Date nbf
			, Date exp
			, Map<String, ?> claims
			) throws SException {
		
		try {
			
			return Jwts.builder()
					.signWith(key)
					.header()
					.type(typ)
					.and()
//				.id(jti)
					.issuer(iss)
					.subject(sub)
					.issuedAt(iat)
					.notBefore(nbf)
					.expiration(exp)
					.claims(claims)
					.compact()
					;
			
		} catch (JwtException e) {
			throw new SException(e, "[JwtException] Failed to build jws.");
		}// end of try
		
	}// end of jwt_token
	public static String jws(
			Key key
			, String iss
			, String sub
			, long iat
			, long nbf
			, long exp
			, Map<String, ?> claims
			) throws SException {
		return jws(
				key
				, _JWT_HEADER_TYPE
				, iss
				, sub
				, new Date(iat)
				, new Date(nbf)
				, new Date(exp)
				, claims
				);
	}// end of jwt_token
	public static String jws(
			Key key
			, String iss
			, String sub
			, Map<String, ?> claims
			, long duration
			) throws SException {
		long now = System.currentTimeMillis();
		return jws(
				key
				, iss
				, sub
				, now
				, now
				, now + duration
				, claims
				);
	}// end of jwt_token
	
	public static String cookie(
			String name
			, String jws
			, String domain
			, String path
			, long max_age
			, boolean http_only
			, boolean secure
			, SSamesite same_site
			) throws SException {
		return SHttp.cookie(
				name
				, SText.is_empty(jws) ? "" : _JWT_COOKIE_PREFIX.concat(jws)//value
				, domain
				, path
				, TimeZone.getTimeZone(ZoneId.of("GMT"))
				, Locale.US
				, max_age
				, http_only
				, secure
				, same_site
				);
	}// end of jwt_cookie
	
	public static SError verify(
			Key key
			, String jws
			) {
		SError s_error = SError.TOKEN_IS_INVALID;
		try {
			Jwts.parser().verifyWith((PublicKey) key).build().parseSignedClaims(jws.replaceAll(_JWT_COOKIE_PREFIX, ""));
			s_error = SError.SUCCESS;
		} catch (SecurityException e) {
			s_error = SError.TOKEN_IS_NOT_SECURE;
		} catch (MalformedJwtException e) {
			s_error = SError.TOKEN_IS_MALFORMED;
		} catch (ExpiredJwtException e) {
			s_error = SError.TOKEN_IS_EXPIERED;
		} catch (UnsupportedJwtException e) {
			s_error = SError.TOKEN_IS_UNSUPPORTED;
		} catch (IllegalArgumentException e) {
			s_error = SError.TOKEN_IS_INVALID;
		}// end of try
		return s_error;
	}// end of verify
	
	public static Claims payload(
			PublicKey key
			, String jws
			) throws SException {
		
		try {
			return Jwts.parser().verifyWith(key).build()
					.parseSignedClaims(jws.replaceAll(_JWT_COOKIE_PREFIX, ""))
					.getPayload();
		} catch (JwtException e) {
			throw new SException(e, "[JwtException] Failed to get payload.");
		}// end of try
		
	}// end of claims
	
}
