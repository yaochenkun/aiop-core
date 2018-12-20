package org.bupt.common.util;

import java.util.Random;

public class CaptchaUtil {

	/**
	 * 生成len位验证码
	 * @param len
	 * @return
	 */
	public static String generate(int len) {

		// 生成SMS_CODE_LEN位验证码
		StringBuilder captcha = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			captcha.append(String.valueOf(random.nextInt(10)));
		}

		return captcha.toString();
	}
}
