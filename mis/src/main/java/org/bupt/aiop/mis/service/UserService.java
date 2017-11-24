package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.ResponseConsts;
import org.bupt.common.redis.JedisClient;
import org.bupt.common.util.MD5Util;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.bupt.common.util.token.TokenUtil;
import org.bupt.aiop.mis.pojo.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends BaseService<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private EnvConsts envConsts;

    @Autowired(required = false)
    private JedisClient jedisClient;

    /**
     * 检查用户名是否重复
     *
     * @param username
     * @return true 重复（数据库中存在）
     */
    public boolean isExist(String username) {
        User record = new User();
        record.setUsername(username);
        return super.queryOne(record) != null;
    }


    /**
     * 查询所有会员
     *
     * @return
     */
    public List<User> queryAllMembers() {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andLike("role", "%会员%");
        return this.getMapper().selectByExample(example);
    }


    /**
     * 条件查询会员
     *
     * @param pageNow
     * @param pageSize
     * @param role
     * @param username
     * @param name
     * @param identity
     * @return
     */
    public List<User> queryUserList(Integer pageNow, Integer pageSize, String role, String username, String name, Identity identity) {

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();


        if (!Validator.checkEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }

        if (!Validator.checkEmpty(username)) {
            criteria.andLike("username", "%" + username + "%");
        }

        if (!Validator.checkEmpty(role)) {
            criteria.andLike("role", "%" + role + "%");
        }

        PageHelper.startPage(pageNow, pageSize);
        return this.getMapper().selectByExample(example);
    }


    /**
     * 模糊匹配姓名，查询会员
     *
     * @param userName
     * @return
     */
    public Set<Integer> getMemberIdSetByUserNameLike(String userName) {
        return getIdSetByUserNameLikeAndRole(userName, "会员");
    }


    /**
     * 模糊匹配姓名，查询职员
     *
     * @param userName
     * @return
     */
    public Set<Integer> getEmployeeIdSetByUserNameLike(String userName) {
        return this.getIdSetByUserNameLikeAndRole(userName, "职员");
    }

    /**
     * 根据姓名和角色模糊匹配，将匹配的结果的id组成set返回
     *
     * @param name
     * @param role
     * @return
     */
    public Set<Integer> getIdSetByUserNameLikeAndRole(String name, String role) {

        Example userExample = new Example(User.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andLike("name", "%" + name + "%");

        if (role.equals("职员")) {
            userCriteria.andNotLike("role", "%会员%");
        } else {
            userCriteria.andLike("role", "%" + role + "%");
        }

        List<User> userList = this.getMapper().selectByExample(userExample);

        Set<Integer> userIdSet = new HashSet<>();
        for(User user : userList)
            userIdSet.add(user.getId());

        // 结果为空的话查询会出错
        if (userIdSet.size() == 0) {
            userIdSet.add(-1);
        }

        return userIdSet;
    }


    /**
     * 删除用户，并级联删除医师
     * @param user
     * @return
     */
    public Integer delete(User user) {

        this.getMapper().delete(user);

        return ResponseConsts.CRUD_SUCCESS;
    }


}
