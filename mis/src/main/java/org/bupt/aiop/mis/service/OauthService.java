package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.constant.KafkaConsts;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.common.kafka.KafkaMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
	private KafkaMessageProducer kafkaMessageProducer;

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
	public void sendCaptchaByMobile(String mobile, String captcha, String type) {

		// todo: 目前写死，到时候直接去掉
		captcha = "1993";

		/**
		 * 生产Kafka消息：通过运营商发送验证码到手机
		 */

		// 1.构造消息体内容
		Map<String, Object> params = new HashMap<>();
		params.put("to", mobile);
		params.put("content", "验证码：" + captcha + "。来自");
		params.put("footer", KafkaConsts.FOOTER);

		// 2.生产消息
		kafkaMessageProducer.send(KafkaConsts.TOPIC_SEND_SMS_TO_SINGLE, params);

		// 3.缓存验证码
		this.setCaptcha(mobile, captcha, type);
	}

	/**
	 * 向邮箱地址发送验证码 + 缓存该验证码
	 * @param email 邮箱
	 * @param captcha 验证码
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public void sendCaptchaByEmail(String email, String captcha, String type) {

		// todo: 目前写死，到时候直接去掉
		captcha = "1993";

		/**
		 * 生产Kafka消息：通过SMTP服务发送验证码到邮箱
		 */

		// 1.构造消息体内容
		Map<String, Object> params = new HashMap<>();
		params.put("fromName", KafkaConsts.FROM_NAME);
		params.put("to", email);
		params.put("subject", "邮箱验证");
		params.put("content", "验证码：<strong>" + captcha + "</strong><br>请在修改页面输入该验证码后进行验证更换");
		params.put("footer", KafkaConsts.FOOTER);

		// 2.生产消息
		kafkaMessageProducer.send(KafkaConsts.TOPIC_SEND_EMAIL_TO_SINGLE, params);

		// 3.缓存验证码
		this.setCaptcha(email, captcha, type);
	}


	/**
	 * 写入验证码
	 * @param mobileOrEmail 手机/邮箱
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public void setCaptcha(String mobileOrEmail, String captcha, String type) {
		redisMapper.opsForValue().set(type + ":" + mobileOrEmail, captcha, envConsts.CAPTCHA_EXPIRE, TimeUnit.SECONDS);
	}


	/**
	 * 读取验证码
	 * @param mobileOrEmail 手机/邮箱
	 * @param type 验证码种类(手机登录、注册、找回密码)
	 * @return
	 */
	public String getCaptcha(String mobileOrEmail, String type) {
		return (String) redisMapper.opsForValue().get(type + ":" + mobileOrEmail);
	}

	/**
	 * 向邮箱地址发送注册成功邮件
	 * @param user 用户信息
	 * @return
	 */
	public void sendRegisterSuccessByEmail(User user) {

		/**
		 * 生产Kafka消息：通过SMTP服务发送注册成功邮件到邮箱
		 */

		// 1.构造消息体内容
		Map<String, Object> params = new HashMap<>();
		params.put("fromName", KafkaConsts.FROM_NAME);
		params.put("to", user.getEmail());
		params.put("subject", "注册成功");
		params.put("content", "恭喜您成功在本平台注册账号，您的账号信息如下：" +
				"<br>账户名：<strong>" + user.getUsername() + "</strong>" +
				"<br>手机：<strong>" + user.getMobile() + "</strong>" +
				"<br>邮箱：<strong>" + user.getEmail() + "</strong>" +
				"<br>角色：<strong>" + user.getRole() + "</strong>" +
				"<br><br>请妥善保管您的账号信息，切勿泄露给他人。");
		params.put("footer", KafkaConsts.FOOTER);

		// 2.生产消息
		kafkaMessageProducer.send(KafkaConsts.TOPIC_SEND_EMAIL_TO_SINGLE, params);
	}

}
