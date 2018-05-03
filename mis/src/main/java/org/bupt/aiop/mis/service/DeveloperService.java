package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.RedisConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.mapper.AppAbilityMapper;
import org.bupt.aiop.mis.mapper.UserMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
//import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.User;
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
 * 开发者服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class DeveloperService extends BaseService<User> {

	private static final Logger logger = LoggerFactory.getLogger(DeveloperService.class);

	@Autowired
	private StringRedisTemplate redisMapper;


	/**
	 * 删除开发者
	 * @param id
	 * @return
	 */
	public Integer deleteDeveloper(Integer id) {
		this.getMapper().deleteByPrimaryKey(id);
		redisMapper.opsForHash().delete(RedisConsts.AIOP_USER_ROLE, id.toString()); // 删除该开发者的权限缓存

		return ResponseConsts.CRUD_SUCCESS;
	}

	public List<User> listDeveloper(Integer pageNow, Integer pageSize, String username, String email, String mobile) {

		Example example = new Example(User.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("role", "开发者");
		if (!Validator.checkEmpty(username)) criteria.andLike("username", "%" + username + "%");
		if (!Validator.checkEmpty(email)) criteria.andLike("email", "%" + email + "%");
		if (!Validator.checkEmpty(mobile)) criteria.andLike("mobile", "%" + mobile + "%");

  		PageHelper.startPage(pageNow, pageSize);
		return this.getMapper().selectByExample(example);
	}


}
