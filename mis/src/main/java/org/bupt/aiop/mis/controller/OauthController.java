package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.Validator;
import org.bupt.aiop.mis.service.RedisService;
import org.bupt.aiop.mis.service.UserService;
import org.bupt.common.util.token.Identity;
import org.bupt.common.util.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 第三方开放认证控制器
 */
@RestController
@RequestMapping("oauth")
public class OauthController {

	private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private EnvConsts envConsts;


	/**
	 * 下发access_token
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "access_token", method = RequestMethod.POST)
	public Object accessToken(@RequestBody Map<String, String> params) {

		//获取数据
		String grantType = params.get(OauthConsts.KEY_GRANT_TYPE);
		String clientId = params.get(OauthConsts.KEY_CLIENT_ID);
		String clientSecret = params.get(OauthConsts.KEY_CLIENT_SECRET);
		logger.info("grantType = {}, clientId = {}, clientSecret = {}", grantType, clientId, clientSecret);

		//数据格式校验
		if (Validator.checkEmpty(grantType)) {
			return new ErrorResult(OauthConsts.ERROR_CODE_INVALID_GRANT_TYPE, OauthConsts.ERROR_MSG_INVALID_GRANT_TYPE);
		}
		if (Validator.checkEmpty(clientId)) {
			return new ErrorResult(OauthConsts.ERROR_CODE_INVALID_CLIENT, OauthConsts.ERROR_MSG_INVALID_CLIENT_ID);
		}
		if (Validator.checkEmpty(clientSecret)) {
			return new ErrorResult(OauthConsts.ERROR_CODE_INVALID_CLIENT, OauthConsts.ERROR_MSG_INVALID_CLIENT_SECRET);
		}

		//判断下发类型: \client_credentials / refresh_token
		if (OauthConsts.CLIENT_CREDENTIALS.equals(grantType)) {

			//进入数据库验证clientId / clientSecret

			//若验证通过，读取数据库中该用户的权限信息

			//生成access_token等相关回复参数
			try {

				//test
				String id = "1"; //应用的id
				String auth = "text_keywords,word_pos,word_seg"; //权限

				// 封装生成access_token需要的身份信息
				Identity identity = new Identity();
				identity.setId(id);
				identity.setIssuer(envConsts.TOKEN_ISSUER);
				identity.setClientId(clientId);
				identity.setPermission(auth);
				identity.setDuration(envConsts.ACCESS_TOKEN_DURATION);
				String accessToken = TokenUtil.createToken(identity, envConsts.ACCESS_TOKEN_API_KEY_SECRET);

				// 封装生成refresh_token需要的身份信息
				identity.setDuration(envConsts.REFRESH_TOKEN_DURATION);
				String refreshToken = TokenUtil.createToken(identity, envConsts.REFRESH_TOKEN_API_KEY_SECRET);

				Map<String, Object> response = new HashMap<>();
				response.put(OauthConsts.KEY_ACCESS_TOKEN, accessToken);
				response.put(OauthConsts.KEY_REFRESH_TOKEN, refreshToken);
				response.put(OauthConsts.KEY_EXPIRES_IN, envConsts.ACCESS_TOKEN_DURATION);
				response.put(OauthConsts.KEY_SCOPE, auth);
				response.put(OauthConsts.KEY_SESSION_KEY, "");
				response.put(OauthConsts.KEY_SESSION_SECRET, "");

				return response;
			} catch (Exception e) {

				e.printStackTrace();
				return new ErrorResult(OauthConsts.ERROR_CODE_SYSTEM_ERROR, OauthConsts.ERROR_MSG_SYSTEM_ERROR);
			}
		} else {
			return new ErrorResult(OauthConsts.ERROR_CODE_INVALID_GRANT_TYPE, OauthConsts.ERROR_MSG_INVALID_GRANT_TYPE);
		}
	}

}
