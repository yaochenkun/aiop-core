package org.bupt.aiop.mis.mapper;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.common.redis.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RedisMapper {

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private EnvConsts envConsts;

	/**
	 * 保存验证码到redis，过期时间为1分钟
	 *
	 * @param key
	 * @param value
	 */
	public void setSmSCode(String key, String value) {
		jedisClient.set(key, value);
		jedisClient.expire(key, envConsts.SMS_CODE_EXPIRE);
	}

}
