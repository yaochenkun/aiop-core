package org.bupt.aiop.mis.constant;

/**
 * 存储Redis中Hash表的key字段
 */
public class RedisConsts {

	public static final String AIOP_USER_ROLE = "aiop:user:role"; // 用户角色信息
	public static final String AIOP_APP_PERMISSION = "aiop:app:permission"; //应用权限信息

	// 验证码
	public static final String AIOP_CAPTCHA_LOGIN = "aiop:captcha:login"; //手机登录验证码
	public static final String AIOP_CAPTCHA_REGISTER = "aiop:captcha:register"; //手机登录验证码
	public static final String AIOP_CAPTCHA_RETRIEVE = "aiop:captcha:retrieve"; //找回密码验证码
}
