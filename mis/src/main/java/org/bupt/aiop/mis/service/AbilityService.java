package org.bupt.aiop.mis.service;

import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.common.constant.ResponseConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


/**
 * 能力服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class AbilityService extends BaseService<Ability> {

	private static final Logger logger = LoggerFactory.getLogger(AbilityService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private AbilityMapper abilityMapper;


	/**
	 * 添加能力
	 * @param ability
	 * @return
	 */
	public Integer saveAbility(Ability ability) {

		getMapper().insert(ability);

		// 判断ability.type决定要不要写入model


		return ResponseConsts.CRUD_SUCCESS;
	}

//	public List<App> listApp(Integer pageNow, Integer pageSize, Integer developerId, String name, String status, Date updateDate) {
//
//		Example example = new Example(App.class);
//		Example.Criteria criteria = example.createCriteria();
//
//		criteria.andEqualTo(developerId);
//		if (!Validator.checkEmpty(name)) criteria.andLike("name", "%" + name + "%");
//		if (!Validator.checkEmpty(status)) criteria.andEqualTo("status", status);
//		if (!Validator.checkNull(updateDate)) criteria.andEqualTo("updateDate", updateDate);
//
//  		PageHelper.startPage(pageNow, pageSize);
//		return this.getMapper().selectByExample(example);
//	}
}
