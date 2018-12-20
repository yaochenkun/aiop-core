package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.mapper.AppMapper;
import org.bupt.aiop.mis.mapper.ModelMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
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
 * 能力服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class AbilityService extends BaseService<Ability> {

	private static final Logger logger = LoggerFactory.getLogger(AbilityService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private AppMapper appMapper;

	@Autowired
	private AbilityMapper abilityMapper;

	@Autowired
	private ModelMapper modelMapper;


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

	/**
	 * 删除能力
	 * @param id
	 * @return
	 */
	public Integer deleteAbility(Integer id) {

		// 先查询该能力
		Ability ability = getMapper().selectByPrimaryKey(id);
		if (ability == null) {
			logger.debug("删除成功，因为本身就不存在该能力");
			return ResponseConsts.CRUD_SUCCESS;
		}

		// 删除
		getMapper().deleteByPrimaryKey(id);

		// 查出abilityScope字段有被删除的这个能力的应用列表, 需要在它们的权限串中剔除这个权限
		String deletingAbilityPermission = ability.getEnName();
		Example example = new Example(App.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andLike("abilityScope", "%" + deletingAbilityPermission + "%");
		List<App> affectedAppList = appMapper.selectByExample(example);

		// 逐个app剔除abilityPermission
		for (App app : affectedAppList) {
			String[] abilityPermissions = app.getAbilityScope().split(",");
			StringBuilder sb = new StringBuilder();
			for (String ap : abilityPermissions) {
				if (!ap.equals(deletingAbilityPermission)) {
					sb.append(",");
					sb.append(ap);
				}
			}

			// 获取剔除后的新权限串
			String abilityScope = sb.toString();
			if ("".equals(abilityScope)) {
				abilityScope = "none";
			} else {
				abilityScope = abilityScope.substring(1);
			}

			// 更新MySQL
			app.setAbilityScope(abilityScope);
			appMapper.updateByPrimaryKey(app);

			// 更新Redis中appId应用的能力权限串
			redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, app.getId().toString(), abilityScope);
		}

		logger.debug("删除能力成功");
		return ResponseConsts.CRUD_SUCCESS;
	}
}
