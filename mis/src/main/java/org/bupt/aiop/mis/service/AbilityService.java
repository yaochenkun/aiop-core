package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.mapper.AbilityModelMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.AbilityModel;
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

	@Autowired
	private AbilityModelMapper abilityModelMapper;

	/**
	 * 添加能力
	 * @param ability
	 * @param modelId
	 * @return
	 */
	public Integer saveAbility(Ability ability, Integer modelId) {

		this.getMapper().insert(ability);

		// 如果是模型算法要继续存储ability-model的关系
		if ("基础算法".equals(ability.getType())) {
			logger.debug("能力添加成功");
			return ResponseConsts.CRUD_SUCCESS;
		}

		AbilityModel abilityModel = new AbilityModel();
		abilityModel.setAbilityId(ability.getId());
		abilityModel.setModelId(modelId); // 选择的模型id

		logger.debug("能力添加成功");
		return abilityModelMapper.insert(abilityModel);
	}

	/**
	 * 查询能力分页列表
	 * @param pageNow
	 * @param pageSize
	 * @param zhName
	 * @param enName
	 * @param type
	 * @param updateDate
	 * @return
	 */
	public List<Ability> listAbility(Integer pageNow, Integer pageSize, String zhName, String enName, String type, Date updateDate) {

		Example example = new Example(Ability.class);
		Example.Criteria criteria = example.createCriteria();

		if (!Validator.checkEmpty(zhName)) criteria.andLike("zhName", "%" + zhName + "%");
		if (!Validator.checkEmpty(enName)) criteria.andLike("enName", "%" + enName + "%");
		if (!Validator.checkEmpty(type)) criteria.andEqualTo("type", type);
		if (!Validator.checkNull(updateDate)) criteria.andEqualTo("updateDate", updateDate);

  		PageHelper.startPage(pageNow, pageSize);
		return this.getMapper().selectByExample(example);
	}
}
