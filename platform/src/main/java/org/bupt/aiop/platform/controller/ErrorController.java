package org.bupt.aiop.platform.controller;


import org.bupt.common.bean.ErrorResult;
import org.bupt.common.constant.ErrorConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 错误返回Controller
 */
@RestController
@RequestMapping("api/error")
public class ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	/**
	 * HTTP 错误
	 * @return
	 */
	@RequestMapping(value = "http/{code}")
	public ErrorResult httpError(@PathVariable("code") Integer code) {

		logger.info("进入http错误返回控制器");
		switch (code) {
			case ErrorConsts.HTTP_CODE_BAD_REQUEST: return new ErrorResult(code, ErrorConsts.HTTP_MSG_BAD_REQUEST);
			case ErrorConsts.HTTP_CODE_UNAUTHORIZED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_UNAUTHORIZED);
			case ErrorConsts.HTTP_CODE_PAYMENT_REQUIRED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_PAYMENT_REQUIRED);
			case ErrorConsts.HTTP_CODE_FORBIDDEN: return new ErrorResult(code, ErrorConsts.HTTP_MSG_FORBIDDEN);
			case ErrorConsts.HTTP_CODE_NOT_FOUND: return new ErrorResult(code, ErrorConsts.HTTP_MSG_NOT_FOUND);
			case ErrorConsts.HTTP_CODE_METHOD_NOT_ALLOWED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_METHOD_NOT_ALLOWED);
			case ErrorConsts.HTTP_CODE_NOT_ACCEPTABLE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_NOT_ACCEPTABLE);
			case ErrorConsts.HTTP_CODE_PROXY_AUTHENTICATION_REQUIRED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_PROXY_AUTHENTICATION_REQUIRED);
			case ErrorConsts.HTTP_CODE_REQUEST_TIMEOUT: return new ErrorResult(code, ErrorConsts.HTTP_MSG_REQUEST_TIMEOUT);
			case ErrorConsts.HTTP_CODE_CONFLICT: return new ErrorResult(code, ErrorConsts.HTTP_MSG_CONFLICT);
			case ErrorConsts.HTTP_CODE_GONE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_GONE);
			case ErrorConsts.HTTP_CODE_LENGTH_REQUIRED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_LENGTH_REQUIRED);
			case ErrorConsts.HTTP_CODE_PRECONDITION_FAILED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_PRECONDITION_FAILED);
			case ErrorConsts.HTTP_CODE_REQUEST_ENTITY_TOO_LARGE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_REQUEST_ENTITY_TOO_LARGE);
			case ErrorConsts.HTTP_CODE_REQUEST_URI_TOO_LARGE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_REQUEST_URI_TOO_LARGE);
			case ErrorConsts.HTTP_CODE_UNSUPPORTED_MEDIA_TYPE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_UNSUPPORTED_MEDIA_TYPE);
			case ErrorConsts.HTTP_CODE_REQUESTED_RANGE_NOT_SATISFIABLE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_REQUESTED_RANGE_NOT_SATISFIABLE);
			case ErrorConsts.HTTP_CODE_EXPECTATION_FAILED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_EXPECTATION_FAILED);
			case ErrorConsts.HTTP_CODE_INTERNAL_SERVER_ERROR: return new ErrorResult(code, ErrorConsts.HTTP_MSG_INTERNAL_SERVER_ERROR);
			case ErrorConsts.HTTP_CODE_NOT_IMPLEMENTED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_NOT_IMPLEMENTED);
			case ErrorConsts.HTTP_CODE_BAD_GATEWAY: return new ErrorResult(code, ErrorConsts.HTTP_MSG_BAD_GATEWAY);
			case ErrorConsts.HTTP_CODE_SERVICE_UNAVAILABLE: return new ErrorResult(code, ErrorConsts.HTTP_MSG_SERVICE_UNAVAILABLE);
			case ErrorConsts.HTTP_CODE_GATEWAY_TIMEOUT: return new ErrorResult(code, ErrorConsts.HTTP_MSG_GATEWAY_TIMEOUT);
			case ErrorConsts.HTTP_CODE_VERSION_NOT_SUPPORTED: return new ErrorResult(code, ErrorConsts.HTTP_MSG_VERSION_NOT_SUPPORTED);
			default: return new ErrorResult(code, ErrorConsts.HTTP_MSG_UNDEFINED_ERROR);
		}
	}

	/**
	 * oauth错误
	 *
	 * @return
	 */
	@RequestMapping(value = "oauth/{code}")
	public ErrorResult oauthError(@PathVariable("code") Integer code) {

		logger.info("进入oauth错误返回控制器");
		switch (code) {
			case ErrorConsts.OAUTH_CODE_ACCESS_TOKEN_INVALID: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_ACCESS_TOKEN_INVALID);
			case ErrorConsts.OAUTH_CODE_PERMISSION_DENIED: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_PERMISSION_DENIED);
			default: return new ErrorResult(ErrorConsts.OAUTH_CODE_UNDEFINED_ERROR, ErrorConsts.OAUTH_MSG_UNDEFINED_ERROR);
		}
	}
}
