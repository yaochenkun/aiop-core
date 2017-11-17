package org.bupt.aiop.mis.controller;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.bupt.aiop.mis.constant.OauthConsts;
import org.bupt.common.util.Validator;
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

import java.util.HashMap;
import java.util.Map;


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
		String grantType = params.get(OauthConsts.KEY_GRANT_TYPE);
		String clientId = params.get(OauthConsts.KEY_CLIENT_ID);
		String clientSecret = params.get(OauthConsts.KEY_CLIENT_SECRET);
		logger.info("grantType = {}, clientId = {}, clientSecret = {}", grantType, clientId, clientSecret);

		//数据格式校验
		if (Validator.checkEmpty(grantType) || !OauthConsts.CLIENT_CREDENTIALS.equals(grantType)) {

			oauthResponse.put(OauthConsts.KEY_ERROR_CODE, OauthConsts.ERROR_CODE_INVALID_GRANT_TYPE);
			oauthResponse.put(OauthConsts.KEY_ERROR_MSG, OauthConsts.ERROR_MSG_INVALID_GRANT_TYPE);
			return oauthResponse;
		}
		if (Validator.checkEmpty(clientId)) {

			oauthResponse.put(OauthConsts.KEY_ERROR_CODE, OauthConsts.ERROR_CODE_INVALID_CLIENT);
			oauthResponse.put(OauthConsts.KEY_ERROR_MSG, OauthConsts.ERROR_MSG_INVALID_CLIENT_ID);
			return oauthResponse;
		}
		if (Validator.checkEmpty(clientSecret)) {

			oauthResponse.put(OauthConsts.KEY_ERROR_CODE, OauthConsts.ERROR_CODE_INVALID_CLIENT);
			oauthResponse.put(OauthConsts.KEY_ERROR_MSG, OauthConsts.ERROR_MSG_INVALID_CLIENT_SECRET);
			return oauthResponse;
		}

		//进入数据库验证clientId / clientSecret
		


		//若验证通过，读取数据库中该用户的权限信息



		//生成access_token等相关回复参数
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		try {

			oauthResponse.put(OauthConsts.KEY_ACCESS_TOKEN, oauthIssuerImpl.accessToken());
			oauthResponse.put(OauthConsts.KEY_REFRESH_TOKEN, oauthIssuerImpl.refreshToken());
			oauthResponse.put(OauthConsts.KEY_EXPIRES_IN, 259200);
			oauthResponse.put(OauthConsts.KEY_SCOPE, "");
			oauthResponse.put(OauthConsts.KEY_SESSION_KEY, "");
			oauthResponse.put(OauthConsts.KEY_SESSION_SECRET, "");

			return oauthResponse;
		} catch (OAuthSystemException e) {

			e.printStackTrace();
			oauthResponse.put(OauthConsts.KEY_ERROR_CODE, OauthConsts.ERROR_CODE_SYSTEM_ERROR);
			oauthResponse.put(OauthConsts.KEY_ERROR_MSG, OauthConsts.ERROR_MSG_SYSTEM_ERROR);
			return oauthResponse;
		}
	}

}
