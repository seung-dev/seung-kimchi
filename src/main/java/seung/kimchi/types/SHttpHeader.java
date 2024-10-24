package seung.kimchi.types;

/**
 * HTTP headers
 * 
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers">MDN Web Docs</a>
 */
public class SHttpHeader {

	// Authentication
	/**
	 * Contains the credentials to authenticate a user-agent with a server.
	 */
	public static final String _S_AUTHORIZATION = "Authorization";
	
	// Caching
	/**
	 * The time, in seconds, that the object has been in a proxy cache.
	 */
	public static final String _S_AGE = "Age";
	/**
	 * Directives for caching mechanisms in both requests and responses.
	 */
	public static final String _S_CACHE_CONTROL = "Cache-Control";
	
	// User agent client hints;
	
	// Conditionals
	/**
	 * The last modification date of the resource, used to compare several versions of the same resource.
	 *  It is less accurate than ETag, but easier to calculate in some environments.
	 *  Conditional requests using If-Modified-Since and If-Unmodified-Since use this value to change the behavior of the request.
	 */
	public static final String _S_LAST_MODIFIED = "Last-Modified";
	
	// Connection management
	/**
	 * Controls whether the network connection stays open after the current transaction finishes.
	 */
	public static final String _S_CONNECTION = "Connection";
	/**
	 * Controls how long a persistent connection should stay open.
	 */
	public static final String _S_KEEP_ALIVE = "Keep-Alive";
	
	// Content negotiation
	/**
	 * Informs the server about the types of data that can be sent back.
	 */
	public static final String _S_ACCEPT = "Accept";
	/**
	 * The encoding algorithm, usually a compression algorithm, that can be used on the resource sent back.
	 */
	public static final String _S_ACCEPT_ENCODING = "Accept-Encoding";
	/**
	 * Informs the server about the human language the server is expected to send back.
	 *  This is a hint and is not necessarily under the full control of the user: the server should always pay attention
	 *  not to override an explicit user choice (like selecting a language from a dropdown).
	 */
	public static final String _S_ACCEPT_LANGUAGE = "Accept-Language";
	
	// Cookies
	/**
	 * Contains stored HTTP cookies previously sent by the server with the Set-Cookie header.
	 */
	public static final String _S_COOKIE = "Cookie";
	/**
	 * Send cookies from the server to the user-agent.
	 */
	public static final String _S_SET_COOKIE = "Set-Cookie";
	
	// CORS
	/**
	 * Indicates whether the response can be shared.
	 */
	public static final String _S_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	/**
	 * Indicates where a fetch originates from.
	 */
	public static final String _S_ORIGIN = "Origin";
	
	// Downloads
	/**
	 * Indicates if the resource transmitted should be displayed inline (default behavior without the header),
	 *  or if it should be handled like a download and the browser should present a "Save As" dialog.
	 */
	public static final String _S_CONTENT_DISPOSITION = "Content-Disposition";
	
	// Message body information
	/**
	 * The size of the resource, in decimal number of bytes.
	 */
	public static final String _S_CONTENT_LENGTH = "Content-Length";
	/**
	 * Indicates the media type of the resource.
	 */
	public static final String _S_CONTENT_TYPE = "Content-Type";
	/**
	 * Used to specify the compression algorithm.
	 */
	public static final String _S_CONTENT_ENCODING = "Content-Encoding";
	/**
	 * Describes the human language(s) intended for the audience,
	 *  so that it allows a user to differentiate according to the users' own preferred language.
	 */
	public static final String _S_CONTENT_LANGUAGE = "Content-Language";
	
	// Proxies
	/**
	 * Identifies the originating IP addresses of a client connecting to a web server through an HTTP proxy or a load balancer.
	 */
	public static final String _S_X_FORWARDED_FOR = "X-Forwarded-For";
	/**
	 * Identifies the original host requested that a client used to connect to your proxy or load balancer.
	 */
	public static final String _S_X_FORWARDED_HOST = "X-Forwarded-Host";
	/**
	 * Identifies the protocol (HTTP or HTTPS) that a client used to connect to your proxy or load balancer.
	 */
	public static final String _S_X_FORWARDED_PROTO = "X-Forwarded-Proto";
	
	// Request context
	/**
	 * Specifies the domain name of the server (for virtual hosting),
	 *  and (optionally) the TCP port number on which the server is listening.
	 */
	public static final String _S_HOST = "Host";
	/**
	 * The address of the previous web page from which a link to the currently requested page was followed.
	 */
	public static final String _S_REFERER = "Referer";
	/**
	 * Contains a characteristic string that allows the network protocol peers to identify the application type,
	 *  operating system, software vendor or software version of the requesting software user agent.
	 *   See also the Firefox user agent string reference.
	 */
	public static final String _S_USER_AGENT = "User-Agent";
	
	// Response context
	/**
	 * Lists the set of HTTP request methods supported by a resource.
	 */
	public static final String _S_ALLOW = "Allow";
	
	// Fetch metadata request headers
	/**
	 * Indicates the relationship between a request initiator's origin and its target's origin.
	 *  It is a Structured Header whose value is a token with possible values cross-site, same-origin, same-site, and none.
	 */
	public static final String _S_SEC_FETCH_SITE = "Sec-Fetch-Site";
	/**
	 * Indicates the request's mode to a server. It is a Structured Header whose value is a token
	 *  with possible values cors, navigate, no-cors, same-origin, and websocket.
	 */
	public static final String _S_SEC_FETCH_MODE = "Sec-Fetch-Mode";
	/**
	 * Indicates whether or not a navigation request was triggered by user activation.
	 *  It is a Structured Header whose value is a boolean so possible values are ?0 for false and ?1 for true.
	 */
	public static final String _S_SEC_FETCH_USER = "Sec-Fetch-User";
	/**
	 * Indicates the request's destination. It is a Structured Header whose value is a token
	 *  with possible values audio, audioworklet, document, embed, empty, font, image, manifest,
	 *  object, paintworklet, report, script, serviceworker, sharedworker, style, track, video, worker, and xslt.
	 */
	public static final String _S_SEC_FETCH_DEST = "Sec-Fetch-Dest";
	
}
