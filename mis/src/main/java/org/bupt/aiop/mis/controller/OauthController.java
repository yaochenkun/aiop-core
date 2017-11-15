package org.bupt.aiop.mis.controller;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.bupt.aiop.common.bean.ResponseResult;
import org.bupt.aiop.common.util.MD5Util;
import org.bupt.aiop.mis.constant.AuthConsts;
import org.bupt.aiop.mis.constant.RoleConsts;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.aiop.mis.service.RedisService;
import org.bupt.aiop.mis.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * 第三方开放认证控制器
 */
@Controller
@RequestMapping("oauth")
public class OauthController {

    private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

//    @Reference
//    private static SayHelloService sayHelloService;


    /**
     * 下发access_token
     * @param request
     * @return
     */
    //@RequestBody Map<String, String> params

    @RequestMapping(value = "access_token", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> accessToken(HttpServletRequest request) {

        Map<String, Object> responseResult = new HashMap<>();

        OAuthTokenRequest oAuthTokenRequest = null;
        try {
            oAuthTokenRequest = new OAuthTokenRequest(request);

            //获取数据
            String grantType = oAuthTokenRequest.getGrantType();
            String clientId = oAuthTokenRequest.getClientId();
            String clientSecret = oAuthTokenRequest.getClientSecret();

            //认证clientId和clientSecret


            //随机序列生成器
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

            logger.info("grantType = {}, clientId = {}, clientSecret = {}", grantType, clientId, clientSecret);


        } catch (OAuthSystemException e) {

            responseResult.put("error", "system error");
            responseResult.put("error_description", "system error");
            e.printStackTrace();

        } catch (OAuthProblemException e) {

            responseResult.put("error", e.getError());
            responseResult.put("error_description", e.getDescription());
            e.printStackTrace();
        }

        return responseResult;
    }

}
