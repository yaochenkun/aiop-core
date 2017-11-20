package org.bupt.aiop.platform.controller;

import org.bupt.common.bean.ErrorResult;
import org.bupt.common.constant.OauthConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 平台账户认证控制器
 */
@RestController
@RequestMapping("api/oauth")
public class OauthController {

    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

    /**
     * access_token失效错误返回
     *
     * @return
     */
    @RequestMapping(value = "access_token_deny")
    public ErrorResult accessTokenDeny() {
        logger.info("进入access_token失效错误返回控制器");
        return new ErrorResult(OauthConsts.ERROR_CODE_ACCESS_TOKEN_INVALID, OauthConsts.ERROR_MSG_ACCESS_TOKEN_INVALID);
    }


    /**
     * 访问能力权限不够错误返回
     *
     * @return
     */
    @RequestMapping(value = "permission_deny")
    public ErrorResult permissionDeny() {
        logger.info("access_token有效，但该用户能力访问权限不够错误返回控制器");
        return new ErrorResult(OauthConsts.ERROR_CODE_PERMISSION_DENIED, OauthConsts.ERROR_MSG_PERMISSION_DENIED);
    }
}
