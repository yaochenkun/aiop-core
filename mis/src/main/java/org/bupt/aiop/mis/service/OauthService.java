package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务类（发短信、发邮件、缓存验证码）
 * Created by ken on 2017/11/1.
 */
@Service
public class OauthService extends BaseService<App> {

	private static final Logger logger = LoggerFactory.getLogger(OauthService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private EnvConsts envConsts;

	/**
	 * 获取用户对应的角色
	 * @param userId 用户ID号
	 */
	public String getUserRole(Integer userId) {
		return (String) redisMapper.opsForHash().get(RedisConsts.AIOP_USER_ROLE, userId.toString());
	}

	/**
	 * 向运营商网关发送手机验证码 + 缓存该验证码
	 * @param mobile 手机
	 * @param captcha 验证码
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public void sendCaptcha(String mobile, String captcha, String type) {

		// todo: 发送验证码给中国移动...
		captcha = "1993";

		// 缓存验证码
		this.setCaptcha(mobile, captcha, type);
	}


	/**
	 * 写入手机验证码
	 * @param mobile 手机
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public void setCaptcha(String mobile, String captcha, String type) {
		redisMapper.opsForValue().set(type + ":" + mobile, captcha, envConsts.SMS_CODE_EXPIRE, TimeUnit.SECONDS);
	}


	/**
	 * 读取手机验证码
	 * @param mobile 手机
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public String getCaptcha(String mobile, String type) {
		return (String) redisMapper.opsForValue().get(type + ":" + mobile);
	}

}
