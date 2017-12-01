package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.common.constant.ResponseConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 应用服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class AppService extends BaseService<App> {

	private static final Logger logger = LoggerFactory.getLogger(AppService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private AbilityMapper abilityMapper;

	/**
	 * 添加应用
	 * @param app
	 * @return
	 */
	public Integer saveApp(App app) {
		getMapper().insert(app);
		redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, app.getId().toString(), app.getAbilityScope()); //将权限信息写入Redis

		return ResponseConsts.CRUD_SUCCESS;
	}
}
