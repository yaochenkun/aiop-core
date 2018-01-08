package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.mapper.AppAbilityMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.AppAbility;
import org.bupt.aiop.mis.pojo.vo.AbilityUnderApp;
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

	@Autowired
	private AppAbilityMapper appAbilityMapper;

	/**
	 * 添加应用
	 * @param app
	 * @return
	 */
	public Integer saveApp(App app, List<Ability> selectedAbilityList) {

		getMapper().insert(app);

		// 写入app-ability
		Integer appId = app.getId();
		for (Ability ability : selectedAbilityList) {
			AppAbility appAbility = new AppAbility();
			appAbility.setAppId(appId);
			appAbility.setAbilityId(ability.getId());
			appAbility.setStatus("允许调用");
			appAbility.setInvokeLimit(ability.getInvokeLimit());
			appAbility.setQpsLimit(ability.getQpsLimit());
			appAbilityMapper.insert(appAbility);
		}

		// 将权限串写入Redis
		redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, app.getId().toString(), app.getAbilityScope());

		return ResponseConsts.CRUD_SUCCESS;
	}

	/**
	 * 删除应用
	 * @param id
	 * @return
	 */
	public Integer deleteApp(Integer id) {
		this.getMapper().deleteByPrimaryKey(id);
		redisMapper.opsForHash().delete(RedisConsts.AIOP_APP_PERMISSION, id.toString()); // 删除该应用的权限缓存

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

	public List<AbilityUnderApp> listAbilityUnderApp(Integer appId) {
		return abilityMapper.selectAbilityUnderApp(appId);
	}

	public AppAbility getAbilityUnderApp(AppAbility appAbility) {
		return appAbilityMapper.selectOne(appAbility);
	}

	public Integer saveAbilityUnderApp(AppAbility appAbility, String newAbilityPermission) {

		Integer appId = appAbility.getAppId();

		// 存储记录
		appAbilityMapper.insert(appAbility);

		// 更新能力权限串
		App app = getMapper().selectByPrimaryKey(appId);
		String abilityScope = app.getAbilityScope();
		if ("none".equals(abilityScope)) {
			abilityScope = newAbilityPermission;
		} else {
			abilityScope += "," + newAbilityPermission;
		}
		app.setAbilityScope(abilityScope);
		getMapper().updateByPrimaryKey(app); // 更新

		// 更新Redis中appId的能力权限串
		redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, appId.toString(), abilityScope);

		return ResponseConsts.CRUD_SUCCESS;
	}

	public Integer deleteAbilityUnderApp(AppAbility appAbility, String deletingAbilityPermission) {

		Integer appId = appAbility.getAppId();

		// 删除记录
		appAbilityMapper.deleteByPrimaryKey(appAbility.getId());

		// 更新能力权限串
		App app = getMapper().selectByPrimaryKey(appId);
		String[] abilityPermissions = app.getAbilityScope().split(",");
		StringBuilder sb = new StringBuilder();
		for (String abilityPermission : abilityPermissions) {
			if (!abilityPermission.equals(deletingAbilityPermission)) {
				sb.append(",");
				sb.append(abilityPermission);
			}
		}

		String abilityScope = sb.toString();
		if ("".equals(abilityScope)) {
			abilityScope = "none";
		} else {
			abilityScope = abilityScope.substring(1);
		}

		app.setAbilityScope(abilityScope);
		getMapper().updateByPrimaryKey(app); //更新

		logger.info(abilityScope);

		// 更新Redis中appId应用的能力权限串
		redisMapper.opsForHash().put(RedisConsts.AIOP_APP_PERMISSION, appId.toString(), abilityScope);

		return ResponseConsts.CRUD_SUCCESS;
	}

	public Integer updateAbilityUnderApp(AppAbility appAbility) {
		return appAbilityMapper.updateByPrimaryKey(appAbility);
	}
}
