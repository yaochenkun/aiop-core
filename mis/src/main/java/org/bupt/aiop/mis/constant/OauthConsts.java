package org.bupt.aiop.mis.constant;

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


	//值变量
	//错误码(100-199)
	public static final Integer ERROR_CODE_SYSTEM_ERROR = 100;
	public static final Integer ERROR_CODE_INVALID_GRANT_TYPE = 101;
	public static final Integer ERROR_CODE_INVALID_CLIENT = 102;

	//错误描述
	public static final String ERROR_MSG_SYSTEM_ERROR = "system error";
	public static final String ERROR_MSG_INVALID_GRANT_TYPE = "invalid grant type";
	public static final String ERROR_MSG_INVALID_CLIENT_ID = "invalid client";
	public static final String ERROR_MSG_INVALID_CLIENT_SECRET = "invalid client";
}
