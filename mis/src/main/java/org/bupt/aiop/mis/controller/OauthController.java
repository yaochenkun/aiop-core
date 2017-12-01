package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.constant.RoleConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.ErrorConsts;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.MD5Util;
import org.bupt.common.util.Validator;
import org.bupt.aiop.mis.service.UserService;
import org.bupt.common.util.token.Identity;
import org.bupt.common.util.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * 第三方开放认证控制器
 */
@RestController
@RequestMapping("api/oauth")
public class OauthController {

	private static final Logger logger = LoggerFactory.getLogger(OauthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AppService appService;

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
			return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_GRANT_TYPE, ErrorConsts.OAUTH_MSG_INVALID_GRANT_TYPE);
		}
		if (Validator.checkEmpty(clientId)) {
			return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_CLIENT, ErrorConsts.OAUTH_MSG_INVALID_CLIENT_ID);
		}
		if (Validator.checkEmpty(clientSecret)) {
			return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_CLIENT, ErrorConsts.OAUTH_MSG_INVALID_CLIENT_SECRET);
		}

		//判断下发类型: client_credentials / refresh_token
		if (OauthConsts.CLIENT_CREDENTIALS.equals(grantType)) {

			//进入数据库验证是否存在对应clientId的应用
			App app = new App();
			app.setClientId(clientId);
			app = appService.queryOne(app);
			if (app == null) {
				return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_CLIENT, ErrorConsts.OAUTH_MSG_INVALID_CLIENT_ID);
			}

			//验证clientId与clientSecret是否匹配
			if (!clientSecret.equals(app.getClientSecret())) {
				return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_CLIENT, ErrorConsts.OAUTH_MSG_INVALID_CLIENT_SECRET);
			}

			//生成access_token等相关回复参数
			try {

				String scope = app.getAbilityScope(); //能力权限(注意如果一个权限也没有应是none)

				// 封装生成access_token需要的身份信息
				Identity identity = new Identity();
				identity.setId(app.getId());
				identity.setIssuer(envConsts.ACCESS_TOKEN_ISSUER);
				identity.setClientId(clientId);
				identity.setDuration(envConsts.ACCESS_TOKEN_DURATION);
				String accessToken = TokenUtil.createToken(identity, envConsts.ACCESS_TOKEN_API_KEY_SECRET);

				// 封装生成refresh_token需要的身份信息
				identity.setDuration(envConsts.REFRESH_TOKEN_DURATION);
				String refreshToken = TokenUtil.createToken(identity, envConsts.REFRESH_TOKEN_API_KEY_SECRET);

				Map<String, Object> response = new HashMap<>();
				response.put(OauthConsts.KEY_ACCESS_TOKEN, accessToken);
				response.put(OauthConsts.KEY_REFRESH_TOKEN, refreshToken);
				response.put(OauthConsts.KEY_EXPIRES_IN, envConsts.ACCESS_TOKEN_DURATION);
				response.put(OauthConsts.KEY_SCOPE, scope);
				response.put(OauthConsts.KEY_SESSION_KEY, "");
				response.put(OauthConsts.KEY_SESSION_SECRET, "");

				return response;
			} catch (Exception e) {

				e.printStackTrace();
				return new ErrorResult(ErrorConsts.OAUTH_CODE_SYSTEM_ERROR, ErrorConsts.OAUTH_MSG_SYSTEM_ERROR);
			}
		} else {
			return new ErrorResult(ErrorConsts.OAUTH_CODE_INVALID_GRANT_TYPE, ErrorConsts.OAUTH_MSG_INVALID_GRANT_TYPE);
		}
	}


	/**
	 * 注册
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "register/{roleType}", method = RequestMethod.POST)
	public ResponseResult register(@RequestBody Map<String, String> params,
								   @PathVariable String roleType) {
		
		String username = params.get("username");
		String password = params.get("password");
		String inputCode = params.get("inputCode");

//        String code = redisService.get(Constant.REDIS_PRE_CODE + phone);
		String code = "1993";
		if (code == null) {
			return ResponseResult.error("验证码过期");
		} else if (!code.equals(inputCode)) {
			return ResponseResult.error("验证码错误");
		}

		if (userService.isExist(username)) {
			return ResponseResult.error("用户名已经存在");
		}

		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(MD5Util.generate(password));
			user.setRole(RoleConsts.TYPE_ADMIN.equals(roleType) ? RoleConsts.BUSINESS_ADMIN : RoleConsts.DEVELOPER);
			user.setAvatarFile(envConsts.DEFAULT_AVATAR); // 默认头像
			userService.save(user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.info("注册失败，原因={}", e.getMessage());
			return ResponseResult.error("注册失败");
		}

		logger.info("用户={}，注册成功", username);
		return ResponseResult.success("注册成功");
	}


	/**
	 * 登录
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseResult login(@RequestBody Map<String, String> params) {

		// 得到用户名和密码
		String username = params.get("username");
		String password = params.get("password");

		logger.info("{} 用户请求登录", username);

		// 模拟网络延迟600ms
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 查询用户是否存在
		User record = new User();
		record.setUsername(username);
		User user = userService.queryOne(record);
		if (user == null) {
			return ResponseResult.error("登录失败：用户不存在");
		}

		// 密码加密
		String md5Password;
		try {
			md5Password = MD5Util.generate(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return ResponseResult.error("MD5加密失败");
		}

		// 检验密码
		if (!user.getPassword().equals(md5Password)) {
			return ResponseResult.error("密码错误");
		}

		// 生成token
		Identity identity = new Identity();
		identity.setId(user.getId());
		identity.setIssuer(envConsts.TOKEN_ISSUER);
		identity.setClientId(user.getUsername());
		identity.setDuration(envConsts.ACCESS_TOKEN_DURATION); //Todo
		String token = TokenUtil.createToken(identity, envConsts.TOKEN_API_KEY_SECRET);
		identity.setToken(token);

		logger.info("用户={} 请求登录成功, 身份信息={}", identity);
		return ResponseResult.success("登录成功", identity);
	}


//	/**
//	 * 发送短信验证码
//	 *
//	 * @param params
//	 * @return
//	 */
//	@RequestMapping(value = "send_sms", method = RequestMethod.POST)
//	public ResponseResult sendSms(@RequestBody Map<String, String> params) {
//
//		String phone = params.get("username");
//
//		User record = new User();
//		record.setUsername(phone);
//		User user = userService.queryOne(record);
//		String action = params.get("action");
//		if (action.equals("注册") || action.equals("修改手机")) {
//			if (user != null) {
//				return ResponseResult.error("此号码已经注册");
//			}
//		} else if (action.equals("找回密码")) {
//			if (user == null) {
//				return ResponseResult.error("此号码未注册");
//			}
//		}
//
//		if (redisService.get(phone) != null) {
//			return ResponseResult.error("请1分钟后再试");
//		}
//
//		StringBuilder code = new StringBuilder();
//		Random random = new Random();
//		for (int i = 0; i < envConsts.SMS_CODE_LEN; i++) {
//			code.append(String.valueOf(random.nextInt(10)));
//		}
//
//		logger.info("验证码，手机号：{}，验证码：{}", phone, code);
//
//		ResponseResult responseResult;
//		try {
//			responseResult = ResponseResult.success("已发送验证码", record.getId());
//			// responseResult = SMSUtil.send(phone, String.valueOf(code));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseResult.error("短信发送失败，请重试");
//		}
//
////        // 存储在redis中，过期时间为60s
////        redisService.setSmSCode(Constant.REDIS_PRE_CODE + phone, String.valueOf(code));
//
//		return responseResult;
//	}


	//	/**
//	 * 校验验证码
//	 *
//	 * @param params
//	 * @return
//	 */
//	@RequestMapping(value = "check_code", method = RequestMethod.POST)
//	public ResponseResult checkSMSCode(@RequestBody Map<String, String> params) {
//
//		String inputCode = params.get("inputCode");
//		String username = params.get("username");
//
//		logger.info("传过来的验证码是: {}， 手机是：{}", inputCode, username);
//
//		//用户名是否存在
//		if(!userService.isExist(username)){
//			return ResponseResult.error("不存在该用户");
//		}
//
//		String code = "1993";
//		if (code == null) {
//			return ResponseResult.error("验证码过期");
//		} else if (!code.equals(inputCode)) {
//			return ResponseResult.error("验证码错误");
//		}
//
//		User record = new User();
//		record.setUsername(username);
//
//		return ResponseResult.success("验证成功", userService.queryOne(record).getId());
//	}

}
