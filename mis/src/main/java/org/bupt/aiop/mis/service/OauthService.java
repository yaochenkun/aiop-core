package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
	 * 保存验证码到redis，过期时间为1分钟
	 *
	 * @param key
	 * @param value
	 */
	public void saveSmSCode(String key, String value) {
		redisMapper.opsForValue().set(key, value, envConsts.SMS_CODE_EXPIRE, TimeUnit.SECONDS);
	}

}
