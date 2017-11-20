package org.bupt.common.constant;

/**
 * Created by ken on 2017/10/21.
 */
public class OauthConsts {


	//键常量
	public static final String KEY_GRANT_TYPE = "grant_type";
	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_CLIENT_SECRET = "client_secret";
	public static final String KEY_ACCESS_TOKEN = "access_token";
	public static final String KEY_REFRESH_TOKEN = "refresh_token";
	public static final String KEY_EXPIRES_IN = "expires_in";
	public static final String KEY_SCOPE = "scope";
	public static final String KEY_SESSION_KEY = "session_key";
	public static final String KEY_SESSION_SECRET = "session_secret";

	public static final String KEY_ERROR_CODE = "error_code";
	public static final String KEY_ERROR_MSG = "error_msg";


	//值常量
	public static final String CLIENT_CREDENTIALS = "client_credentials";
	public static final String REFRESH_TOKEN = "refresh_token";

	//值变量
	//错误码(100-199)
	public static final Integer ERROR_CODE_SYSTEM_ERROR = 100;
	public static final Integer ERROR_CODE_INVALID_GRANT_TYPE = 101;
	public static final Integer ERROR_CODE_INVALID_CLIENT = 102;
	public static final Integer ERROR_CODE_ACCESS_TOKEN_INVALID = 103;
	public static final Integer ERROR_CODE_PERMISSION_DENIED = 104;
	public static final Integer ERROR_CODE_UNSUPPORTED_OPENAPI_METHOD = 105;

	//错误描述
	public static final String ERROR_MSG_SYSTEM_ERROR = "system error";
	public static final String ERROR_MSG_INVALID_GRANT_TYPE = "invalid grant type";
	public static final String ERROR_MSG_INVALID_CLIENT_ID = "invalid client";
	public static final String ERROR_MSG_INVALID_CLIENT_SECRET = "invalid client";
	public static final String ERROR_MSG_ACCESS_TOKEN_INVALID = "access token invalid or no longer valid";
	public static final String ERROR_MSG_PERMISSION_DENIED = "No permission to access data";
	public static final String ERROR_MSG_UNSUPPORTED_OPENAPI_METHOD = "unsupported openapi method";
}
