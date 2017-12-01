package org.bupt.aiop.platform.controller;


import org.bupt.common.bean.ErrorResult;
import org.bupt.common.constant.ErrorConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Oauth2.0 Controller
 */
@RestController
@RequestMapping("api/oauth")
public class ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	/**
	 * oauth错误
	 *
	 * @return
	 */
	@RequestMapping(value = "error/{code}")
	public ErrorResult oauthError(@PathVariable("code") Integer code) {

		logger.info("进入oauth错误返回控制器");
		switch (code) {
			case ErrorConsts.OAUTH_CODE_ACCESS_TOKEN_INVALID: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_ACCESS_TOKEN_INVALID);
			case ErrorConsts.OAUTH_CODE_PERMISSION_DENIED: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_PERMISSION_DENIED);
			default: return new ErrorResult(ErrorConsts.OAUTH_CODE_UNDEFINED_ERROR, ErrorConsts.OAUTH_MSG_UNDEFINED_ERROR);
		}
	}
}
