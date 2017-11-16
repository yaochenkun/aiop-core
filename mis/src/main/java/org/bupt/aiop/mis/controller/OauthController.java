package org.bupt.aiop.mis.controller;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.bupt.aiop.common.bean.ResponseResult;
import org.bupt.aiop.common.util.MD5Util;
import org.bupt.aiop.common.util.Validator;
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
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "access_token", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> accessToken(@RequestBody Map<String, String> params) {

		Map<String, Object> oauthResponse = new HashMap<>();

		//获取数据
		String grantType = params.get("grant_type");
		String clientId = params.get("client_id");
		String clientSecret = params.get("client_secret");
		logger.info("grantType = {}, clientId = {}, clientSecret = {}", grantType, clientId, clientSecret);

		//数据格式校验
		if (Validator.checkEmpty(grantType)) {

			oauthResponse.put("error", "invalid_grant_type");
			oauthResponse.put("error_description", "unsupported grant type");
			return oauthResponse;
		}
		if (Validator.checkEmpty(clientId)) {

			oauthResponse.put("error", "invalid_client");
			oauthResponse.put("error_description", "empty client id");
			return oauthResponse;
		}
		if (Validator.checkEmpty(clientSecret)) {

			oauthResponse.put("error", "invalid_client");
			oauthResponse.put("error_description", "empty client secret");
			return oauthResponse;
		}

		//进入数据库验证clientId和clientSecret


		//若验证通过，获取其权限信息



		//生成access_token等相关回复参数
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		try {

			oauthResponse.put("access_token", oauthIssuerImpl.accessToken());
			oauthResponse.put("refresh_token", oauthIssuerImpl.refreshToken());
			oauthResponse.put("expires_in", 259200);
			oauthResponse.put("scope", "");
			oauthResponse.put("session_key", "");
			oauthResponse.put("session_secret", "");

			return oauthResponse;
		} catch (OAuthSystemException e) {

			e.printStackTrace();
			oauthResponse.put("error", "system_error");
			oauthResponse.put("error_description", "system_error");
			return oauthResponse;
		}
	}

}
