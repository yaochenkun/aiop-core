package org.bupt.aiop.restapi.controller;

import org.bupt.common.bean.ResponseResult;
import org.bupt.aiop.restapi.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 平台账户认证控制器
 */
@RestController
@RequestMapping("oauth")
public class OauthController {

    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);


    @Autowired
    private RedisService redisService;


    /**
     * 权限拒绝
     *
     * @return
     */
    @RequestMapping(value = "deny")
    public ResponseResult authDeny() {
        logger.info("该用户能力权限不够");
        return ResponseResult.error("能力权限不够");
    }
}
