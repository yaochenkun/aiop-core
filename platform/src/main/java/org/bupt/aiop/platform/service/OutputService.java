package org.bupt.aiop.platform.service;


import org.bupt.aiop.platform.constant.EnvConsts;
import org.bupt.aiop.platform.constant.RedisConsts;
import org.bupt.aiop.platform.pojo.po.Ability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 能力输出结果缓存+能力调用日志服务类
 */
@Service
public class OutputService extends BaseService<Ability> {

	private static final Logger logger = LoggerFactory.getLogger(OutputService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private EnvConsts envConsts;


	/**
	 * 是否已有缓存的能力输出
	 * @param abilityAndInput ability:input
	 */
	public Boolean hasAbilityAndInput(String abilityAndInput) {
		return redisMapper.opsForHash().hasKey(RedisConsts.AIOP_ABILITY_INPUT_OUTPUT, abilityAndInput);
	}

	/**
	 * 保存能力输出
	 * @param abilityAndInput ability:input
	 * @param output
	 */
	public void saveOutput(String abilityAndInput, String output) {
		redisMapper.opsForHash().put(RedisConsts.AIOP_ABILITY_INPUT_OUTPUT, abilityAndInput, output);
	}

	/**
	 * 获取能力输出
	 * @param abilityAndInput ability:input
	 */
	public String getOutput(String abilityAndInput) {
		return (String) redisMapper.opsForHash().get(RedisConsts.AIOP_ABILITY_INPUT_OUTPUT, abilityAndInput);
	}



}
