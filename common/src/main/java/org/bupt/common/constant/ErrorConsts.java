package org.bupt.common.constant;

/**
 * 错误常量类
 */
public class ErrorConsts {


	public static final String KEY_ERROR_CODE = "error_code";
	public static final String KEY_ERROR_MSG = "error_msg";


	/**
	 * Oauth2.0错误
	 */
	//错误码(100-199)
	public static final int OAUTH_CODE_SYSTEM_ERROR = 100;
	public static final int OAUTH_CODE_INVALID_GRANT_TYPE = 101;
	public static final int OAUTH_CODE_INVALID_CLIENT = 102;
	public static final int OAUTH_CODE_ACCESS_TOKEN_INVALID = 103;
	public static final int OAUTH_CODE_TOKEN_INVALID = 103;
	public static final int OAUTH_CODE_PERMISSION_DENIED = 104;
	public static final int OAUTH_CODE_ROLE_DENIED = 105;
	public static final int OAUTH_CODE_UNDEFINED_ERROR = 199;

	//错误描述
	public static final String OAUTH_MSG_SYSTEM_ERROR = "system error";
	public static final String OAUTH_MSG_INVALID_GRANT_TYPE = "invalid grant type";
	public static final String OAUTH_MSG_INVALID_CLIENT_ID = "invalid client id";
	public static final String OAUTH_MSG_INVALID_CLIENT_SECRET = "invalid client secret";
	public static final String OAUTH_MSG_ACCESS_TOKEN_INVALID = "access token invalid or no longer valid";
	public static final String OAUTH_MSG_TOKEN_INVALID = "token invalid or no longer valid";
	public static final String OAUTH_MSG_PERMISSION_DENIED = "no permission to access data";
	public static final String OAUTH_MSG_ROLE_DENIED = "invalid role to access data";
	public static final String OAUTH_MSG_UNDEFINED_ERROR = "undefined oauth error";


	/**
	 * HTTP错误
	 */
	//错误码(400-417，500-505)
	public static final int HTTP_CODE_BAD_REQUEST = 400;
	public static final int HTTP_CODE_UNAUTHORIZED = 401;
	public static final int HTTP_CODE_PAYMENT_REQUIRED = 402;
	public static final int HTTP_CODE_FORBIDDEN = 403;
	public static final int HTTP_CODE_NOT_FOUND = 404;
	public static final int HTTP_CODE_METHOD_NOT_ALLOWED = 405;
	public static final int HTTP_CODE_NOT_ACCEPTABLE = 406;
	public static final int HTTP_CODE_PROXY_AUTHENTICATION_REQUIRED = 407;
	public static final int HTTP_CODE_REQUEST_TIMEOUT = 408;
	public static final int HTTP_CODE_CONFLICT = 409;
	public static final int HTTP_CODE_GONE = 410;
	public static final int HTTP_CODE_LENGTH_REQUIRED = 411;
	public static final int HTTP_CODE_PRECONDITION_FAILED = 412;
	public static final int HTTP_CODE_REQUEST_ENTITY_TOO_LARGE = 413;
	public static final int HTTP_CODE_REQUEST_URI_TOO_LARGE = 414;
	public static final int HTTP_CODE_UNSUPPORTED_MEDIA_TYPE = 415;
	public static final int HTTP_CODE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public static final int HTTP_CODE_EXPECTATION_FAILED = 417;
	public static final int HTTP_CODE_INTERNAL_SERVER_ERROR = 500;
	public static final int HTTP_CODE_NOT_IMPLEMENTED = 501;
	public static final int HTTP_CODE_BAD_GATEWAY = 502;
	public static final int HTTP_CODE_SERVICE_UNAVAILABLE = 503;
	public static final int HTTP_CODE_GATEWAY_TIMEOUT = 504;
	public static final int HTTP_CODE_VERSION_NOT_SUPPORTED = 505;


	//错误描述
	public static final String HTTP_MSG_BAD_REQUEST = "Bad Request";
	public static final String HTTP_MSG_UNAUTHORIZED = "Unauthorized";
	public static final String HTTP_MSG_PAYMENT_REQUIRED = "Payment Required";
	public static final String HTTP_MSG_FORBIDDEN = "Forbidden";
	public static final String HTTP_MSG_NOT_FOUND = "Not Found";
	public static final String HTTP_MSG_METHOD_NOT_ALLOWED = "Method Not Allowed";
	public static final String HTTP_MSG_NOT_ACCEPTABLE = "Not Acceptable";
	public static final String HTTP_MSG_PROXY_AUTHENTICATION_REQUIRED = "Proxy Authentication Required";
	public static final String HTTP_MSG_REQUEST_TIMEOUT = "Request Time-out";
	public static final String HTTP_MSG_CONFLICT = "Conflict";
	public static final String HTTP_MSG_GONE = "Gone";
	public static final String HTTP_MSG_LENGTH_REQUIRED = "Length Required";
	public static final String HTTP_MSG_PRECONDITION_FAILED = "Precondition Failed";
	public static final String HTTP_MSG_REQUEST_ENTITY_TOO_LARGE = "Request Entity Too Large";
	public static final String HTTP_MSG_REQUEST_URI_TOO_LARGE = "Request-URI Too Large";
	public static final String HTTP_MSG_UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";
	public static final String HTTP_MSG_REQUESTED_RANGE_NOT_SATISFIABLE = "Requested range not satisfiable";
	public static final String HTTP_MSG_EXPECTATION_FAILED = "Expectation Failed";
	public static final String HTTP_MSG_INTERNAL_SERVER_ERROR = "Internal Server Error";
	public static final String HTTP_MSG_NOT_IMPLEMENTED = "Not Implemented";
	public static final String HTTP_MSG_BAD_GATEWAY = "Bad Gateway";
	public static final String HTTP_MSG_SERVICE_UNAVAILABLE = "Service Unavailable";
	public static final String HTTP_MSG_GATEWAY_TIMEOUT = "Gateway Time-out";
	public static final String HTTP_MSG_VERSION_NOT_SUPPORTED = "HTTP Version not supported";
	public static final String HTTP_MSG_UNDEFINED_ERROR = "Undefined http error";

}
