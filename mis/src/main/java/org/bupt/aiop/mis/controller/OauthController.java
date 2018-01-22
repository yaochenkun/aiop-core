package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.constant.RoleConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.aiop.mis.service.OauthService;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.ErrorConsts;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.CaptchaUtil;
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
	private OauthService oauthService;

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
		logger.debug("grantType = {}, clientId = {}, clientSecret = {}", grantType, clientId, clientSecret);

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
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseResult submitRegister(@RequestBody Map<String, String> params) {
		
		String username = params.get("username");
		String email = params.get("email");
		String password = params.get("password");
		String mobile = params.get("mobile");
		String captcha = params.get("captcha");

		User user = new User();
		user.setUsername(username);
		if (userService.queryOne(user) != null) {
			return ResponseResult.error("该账户名已被注册");
		}

		user.setUsername(null);
		user.setEmail(email);
		if (userService.queryOne(user) != null) {
			return ResponseResult.error("该邮箱已被注册");
		}

		user.setEmail(null);
		user.setMobile(mobile);
		if (userService.queryOne(user) != null) {
			return ResponseResult.error("该手机号已被注册");
		}

		// 检验验证码时效性
		String targetCaptcha = oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_REGISTER);
		if (targetCaptcha == null) {
			return ResponseResult.error("验证码已过期");
		}

		// 检验验证码是否匹配
		if (!targetCaptcha.equals(captcha)) {
			return ResponseResult.error("验证码错误");
		}

		// 进行注册
		try {
			user.setUsername(username);
			user.setEmail(email);
			user.setMobile(mobile);
			user.setPassword(MD5Util.generate(password));
			user.setRole(RoleConsts.DEVELOPER);
			user.setAvatarFile(envConsts.DEFAULT_IMAGE); // 默认头像
			userService.save(user);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.debug("注册失败，原因={}", e.getMessage());
			return ResponseResult.error("注册失败");
		}

		logger.debug("用户={}，注册成功", username);
		return ResponseResult.success("注册成功");
	}

	/**
	 * 发送注册的验证码
	 * @return
	 */
	@RequestMapping(value = "captcha/register", method = RequestMethod.POST)
	public ResponseResult sendRegisterCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号
		String mobile = params.get("mobile");

		logger.debug("{} 用户请求注册时发送验证码", mobile);

		// 先检查是否有之前发送的未失效的验证码
		if (oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_REGISTER) != null) {
			return ResponseResult.error("请勿重复发送验证码");
		}

		// 生成SMS_CODE_LEN位的验证码
		String captcha = CaptchaUtil.generate(envConsts.CAPTCHA_LEN);

		// 发送给mobile手机captcha验证码
		// todo: 后期转为消息队列
		oauthService.sendCaptchaByMobile(mobile, captcha, RedisConsts.AIOP_CAPTCHA_REGISTER);

		logger.debug("已为用户手机 {} 发送验证码 {}", mobile, captcha);
		return ResponseResult.success("已发送验证码");
	}


	/**
	 * 账户登录
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "login/account", method = RequestMethod.POST)
	public ResponseResult verifyAccountLogin(@RequestBody Map<String, String> params) {

		// 得到用户名和密码
		String username = params.get("username");
		String password = params.get("password");

		logger.debug("{} 用户请求账户登录", username);

		// 查询用户是否存在
		User record = new User();
		record.setUsername(username);
		User user = userService.queryOne(record);
		if (user == null) {
			return ResponseResult.error("用户不存在");
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
		identity.setDuration(envConsts.TOKEN_DURATION);
		String token = TokenUtil.createToken(identity, envConsts.TOKEN_API_KEY_SECRET);
		identity.setToken(token);

		logger.debug("用户={} 请求登录成功, 生成token={}", user.getId(), identity.getToken());
		return ResponseResult.success("登录成功", identity);
	}

	/**
	 * 手机登录
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "login/mobile", method = RequestMethod.POST)
	public ResponseResult verifyMobileLogin(@RequestBody Map<String, String> params) {

		// 得到手机号和验证码
		String mobile = params.get("mobile");
		String captcha = params.get("captcha");

		logger.debug("{} 用户请求手机登录", mobile);

		// 查询用户是否存在
		User record = new User();
		record.setMobile(mobile);
		User user = userService.queryOne(record);
		if (user == null) {
			return ResponseResult.error("手机不存在");
		}

		// 检验验证码时效性
		String targetCaptcha = oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_LOGIN);
		if (targetCaptcha == null) {
			return ResponseResult.error("验证码已过期");
		}

		// 检验验证码是否匹配
		if (!targetCaptcha.equals(captcha)) {
			return ResponseResult.error("验证码错误");
		}

		// 生成token
		Identity identity = new Identity();
		identity.setId(user.getId());
		identity.setIssuer(envConsts.TOKEN_ISSUER);
		identity.setClientId(user.getUsername());
		identity.setDuration(envConsts.TOKEN_DURATION);
		String token = TokenUtil.createToken(identity, envConsts.TOKEN_API_KEY_SECRET);
		identity.setToken(token);

		logger.debug("用户={} 请求手机登录成功, 生成token={}", user.getId(), identity.getToken());
		return ResponseResult.success("登录成功", identity);
	}

	/**
	 * 发送手机登录的验证码
	 * @return
	 */
	@RequestMapping(value = "captcha/login", method = RequestMethod.POST)
	public ResponseResult sendMobileLoginCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号
		String mobile = params.get("mobile");

		logger.debug("{} 用户请求手机登录时发送验证码", mobile);

		// 先检查是否有之前发送的未失效的验证码
		if (oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_LOGIN) != null) {
			return ResponseResult.error("请勿重复发送验证码");
		}

		// 生成SMS_CODE_LEN位的验证码
		String captcha = CaptchaUtil.generate(envConsts.CAPTCHA_LEN);

		// 发送给mobile手机captcha验证码
		// todo: 后期转为消息队列
		oauthService.sendCaptchaByMobile(mobile, captcha, RedisConsts.AIOP_CAPTCHA_LOGIN);

		logger.debug("已为用户手机 {} 发送验证码 {}", mobile, captcha);
		return ResponseResult.success("已发送验证码");
	}



	/**
	 * 发送找回密码的验证码
	 * @return
	 */
	@RequestMapping(value = "captcha/retrieve", method = RequestMethod.POST)
	public ResponseResult sendRetrieveCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号
		String mobile = params.get("mobile");

		logger.debug("{} 用户请求找回密码时发送验证码", mobile);

		// 先检查是否有之前发送的未失效的验证码
		if (oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_RETRIEVE) != null) {
			return ResponseResult.error("请勿重复发送验证码");
		}

		// 生成SMS_CODE_LEN位的验证码
		String captcha = CaptchaUtil.generate(envConsts.CAPTCHA_LEN);

		// 发送给mobile手机captcha验证码
		// todo: 后期转为消息队列
		oauthService.sendCaptchaByMobile(mobile, captcha, RedisConsts.AIOP_CAPTCHA_RETRIEVE);

		logger.debug("已为用户手机 {} 发送验证码 {}", mobile, captcha);
		return ResponseResult.success("已发送验证码");
	}


	/**
	 * 找回密码提交验证码进行认证
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "retrieve/verify", method = RequestMethod.POST)
	public ResponseResult verifyRetrieveCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号和验证码
		String mobile = params.get("mobile");
		String captcha = params.get("captcha");

		logger.debug("{} 用户请求检验找回密码的验证码", mobile);

		// 查询用户是否存在
		User record = new User();
		record.setMobile(mobile);
		User user = userService.queryOne(record);
		if (user == null) {
			return ResponseResult.error("手机不存在");
		}

		// 检验验证码时效性
		String targetCaptcha = oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_RETRIEVE);
		if (targetCaptcha == null) {
			return ResponseResult.error("验证码已过期");
		}

		// 检验验证码是否匹配
		if (!targetCaptcha.equals(captcha)) {
			return ResponseResult.error("验证码错误");
		}

		Map<String, Object> content = new HashMap<>();
		content.put("userId", user.getId());

		logger.debug("用户={} 请求检验找回密码的验证码", mobile);
		return ResponseResult.success("验证成功", content);
	}


	/**
	 * 修改密码
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "password/{userId}", method = RequestMethod.PUT)
	public ResponseResult changePassword(@RequestBody Map<String, Object> params, @PathVariable("userId") Integer
			userId) {

		String password = (String) params.get("password");

		User user = userService.queryById(userId);
		if (user == null) {
			return ResponseResult.error("请先返回上一步认证手机");
		}

		String newPasswordMD5;
		try {
			newPasswordMD5 = MD5Util.generate(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return ResponseResult.error("md5加密失败");
		}

		user.setPassword(newPasswordMD5);
		userService.update(user);

		return ResponseResult.success("密码修改成功");
	}

	/**
	 * 发送修改手机号的验证码
	 * @return
	 */
	@RequestMapping(value = "captcha/modify_mobile", method = RequestMethod.POST)
	public ResponseResult sendModifyMobileCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号
		String mobile = params.get("mobile");

		logger.debug("{} 用户请求修改手机时发送验证码", mobile);

		// 先检查是否有之前发送的未失效的验证码
		if (oauthService.getCaptcha(mobile, RedisConsts.AIOP_CAPTCHA_MODIFY_MOBILE) != null) {
			return ResponseResult.error("请勿重复发送验证码");
		}

		// 生成SMS_CODE_LEN位的验证码
		String captcha = CaptchaUtil.generate(envConsts.CAPTCHA_LEN);

		// 发送给mobile手机captcha验证码
		oauthService.sendCaptchaByMobile(mobile, captcha, RedisConsts.AIOP_CAPTCHA_MODIFY_MOBILE);

		logger.debug("已为用户手机 {} 发送验证码 {}", mobile, captcha);
		return ResponseResult.success("已发送验证码");
	}

	/**
	 * 发送修改邮箱的验证码
	 * @return
	 */
	@RequestMapping(value = "captcha/modify_email", method = RequestMethod.POST)
	public ResponseResult sendModifyEmailCaptcha(@RequestBody Map<String, String> params) {

		// 得到手机号
		String email = params.get("email");

		logger.debug("{} 用户请求修改邮箱时发送验证码", email);

		// 先检查是否有之前发送的未失效的验证码
		if (oauthService.getCaptcha(email, RedisConsts.AIOP_CAPTCHA_MODIFY_EMAIL) != null) {
			return ResponseResult.error("请勿重复发送验证码");
		}

		// 生成SMS_CODE_LEN位的验证码
		String captcha = CaptchaUtil.generate(envConsts.CAPTCHA_LEN);

		// 发送给mobile手机captcha验证码
		oauthService.sendCaptchaByEmail(email, captcha, RedisConsts.AIOP_CAPTCHA_MODIFY_EMAIL);

		logger.debug("已为用户邮箱 {} 发送验证码 {}", email, captcha);
		return ResponseResult.success("已发送验证码至邮箱");
	}

	/**
	 * oauth错误
	 *
	 * @return
	 */
	@RequestMapping(value = "error/{code}")
	public ErrorResult oauthError(@PathVariable("code") Integer code) {

		logger.debug("进入oauth错误返回控制器");
		switch (code) {
			case ErrorConsts.OAUTH_CODE_TOKEN_INVALID: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_TOKEN_INVALID);
			case ErrorConsts.OAUTH_CODE_ROLE_DENIED: return new ErrorResult(code, ErrorConsts.OAUTH_MSG_ROLE_DENIED);
			default: return new ErrorResult(ErrorConsts.OAUTH_CODE_UNDEFINED_ERROR, ErrorConsts.OAUTH_MSG_UNDEFINED_ERROR);
		}
	}

}
