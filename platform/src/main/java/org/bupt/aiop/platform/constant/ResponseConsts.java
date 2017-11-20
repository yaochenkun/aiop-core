package org.bupt.aiop.platform.constant;

/**
 * Created by ken on 2017/10/21.
 */
public class ResponseConsts {

	public static final String SUCCESS = "success";
	public static final String ERROR = "error";

	public static final Integer CRUD_SUCCESS = 1;
	public static final Integer CRUD_ERROR = 0;


	//调用能力后返回的错误状态码+描述
	//错误码(200-299)
	public static final Integer ERROR_CODE_EMPTY_PARAM = 200;
	public static final Integer ERROR_CODE_LONG_PARAM = 201;
	public static final Integer ERROR_CODE_INTERNAL_ERROR = 202;
	public static final Integer ERROR_CODE_ALGORITHMN_ERROR = 203;
	public static final Integer ERROR_CODE_INVALID_PARAM = 204;

	//错误描述
	public static final String ERROR_MSG_EMPTY_PARAM = "empty param";
	public static final String ERROR_MSG_LONG_PARAM = "long param";
	public static final String ERROR_MSG_INTERNAL_ERROR = "internal error";
	public static final String ERROR_MSG_ALGORITHMN_ERROR = "algorithmn error";
	public static final String ERROR_MSG_INVALID_PARAM = "invalid param";
}
