package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.common.constant.ResponseConsts;
import org.bupt.common.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


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

		// 写入MySQL
		getMapper().insert(app);

		// 写入Redis
		redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, app.getId().toString(), app.getAbilityScope());

		return ResponseConsts.CRUD_SUCCESS;
	}

	public List<App> listApp(Integer pageNow, Integer pageSize, Integer developerId, String name, String status, Date updateDate) {

		Example example = new Example(App.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo(developerId);
		if (!Validator.checkEmpty(name)) criteria.andLike("name", "%" + name + "%");
		if (!Validator.checkEmpty(status)) criteria.andEqualTo("status", status);
		if (!Validator.checkNull(updateDate)) criteria.andEqualTo("updateDate", updateDate);

		PageHelper.startPage(pageNow, pageSize);
		return this.getMapper().selectByExample(example);
	}
}
