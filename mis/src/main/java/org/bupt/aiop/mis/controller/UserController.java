package org.bupt.aiop.mis.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.util.Streams;
import org.bupt.aiop.mis.annotation.RequiredRoles;
import org.bupt.aiop.mis.constant.RoleConsts;
import org.bupt.common.bean.PageResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.FileUtil;
import org.bupt.common.util.MD5Util;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.aiop.mis.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 用户相关控制器
 */
@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private EnvConsts envConsts;

    /**
     * 添加员工
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseResult addUser(@RequestBody Map<String, Object> params) {

        String name = (String) params.get("name");
        String username = (String) params.get("username");
        String role = (String) params.get("role");

        User user = new User();

        if (Validator.checkEmpty(name) || Validator.checkEmpty(username) || Validator.checkEmpty(role)) {
            return ResponseResult.error("添加失败，信息不完整");
        } else {
//            user.setName(name);
            user.setUsername(username);
            user.setRole(role);
        }

        if (userService.queryOne(user) != null) {
            return ResponseResult.error("该用户名已被注册");
        }

        try {
            user.setPassword(MD5Util.generate(envConsts.DEFAULT_PASSWORD));
            user.setAvatarFile(envConsts.DEFAULT_IMAGE); // 默认头像
            userService.save(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseResult.error("添加失败，md5生成错误");
        }

        logger.debug("用户={}, 添加成功", user.getUsername());
        return ResponseResult.success("添加成功");
    }


    /**
     * 修改别的用户的信息
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseResult updateOtherUser(@RequestBody Map<String, Object> params) {

        Integer userId = (Integer) params.get("userId");
        // 修改别的用户的时候不能修改name和phone
        String name = (String) params.get("name");
        String role = (String) params.get("role");

        // 未修改的user
        User user = userService.queryById(userId);

        if (!Validator.checkEmpty(name)) {
//            user.setName(name);
        }

        // role
        if (!Validator.checkEmpty(role)) {
            user.setRole(role);
        }

        userService.update(user);

        logger.debug("用户={}, 修改成功", user.getUsername());
        return ResponseResult.success("修改成功");
    }


    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseResult queryById(@PathVariable("userId") Integer userId) {

        User user = userService.queryById(userId);
        if (user == null) {
            return ResponseResult.error("用户不存在");
        }

        // 清除密码、添加头像文件的文件夹目录
        user.setPassword(null);
        user.setAvatarFile("/" + envConsts.FILE_AVATAR_DIC + "/" + user.getAvatarFile());

        logger.debug("用户={}, 查询成功", user.getUsername());
        return ResponseResult.success("查询成功", user);
    }


    /**
     * 删除用户
     * role改为已删除，username加上_deleted的后缀
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
    @RequiredRoles(value = {"系统管理员"})
    public ResponseResult deleteById(@PathVariable("userId") Integer userId) {

        User user = userService.queryById(userId);
        if (user == null) {
            return ResponseResult.error("用户不存在");
        }

        // userService.deleteById(userId);
        userService.delete(user);

        logger.debug("用户={}, 删除成功", user.getUsername());
        return ResponseResult.success("删除成功");
    }


    /**
     * 用户自己修改自己
     *
     * @param userId
     * @param params
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.PUT)
    public ResponseResult updateById(@PathVariable("userId") Integer userId, @RequestBody Map<String, Object> params) {

        // 自己可以修改自己的name和phone
        String name = (String) params.get("name");

        // 未修改的user
        User user = userService.queryById(userId);

        if (!Validator.checkEmpty(name)) {
//            user.setName(name);
        }

        userService.update(user);
        return ResponseResult.success("修改成功");
    }



    /**
     * 条件分页查询用户
     * 会员member、职员employee
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseResult queryUsers(@RequestBody Map<String, Object> params, HttpSession session) {

        Integer pageNow = (Integer) params.get("pageNow");
        Integer pageSize = (Integer) params.get("pageSize");

        String role = (String) params.get("role");
        String username = (String) params.get("username");
        String name = (String) params.get("name");

        Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

        List<User> userList = userService.queryUserList(pageNow, pageSize, role, username, name, identity);
        PageResult pageResult = new PageResult(new PageInfo<>(userList));

        return ResponseResult.success("查询成功", pageResult);
    }


    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "password/{userId}", method = RequestMethod.PUT)
    public ResponseResult changePassword(@RequestBody Map<String, Object> params, @PathVariable("userId") Integer
            userId) {

        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");

        User user = userService.queryById(userId);

        // 找回密码的时候没有oldPassword
        if (!Validator.checkEmpty(oldPassword)) {
            String oldPasswordMD5;
            try {
                oldPasswordMD5 = MD5Util.generate(oldPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return ResponseResult.error("md5加密失败！");
            }

            if (!oldPasswordMD5.equals(user.getPassword())) {
                return ResponseResult.error("修改失败，原密码输入错误");
            }
        }

        String newPasswordMD5;
        try {
            newPasswordMD5 = MD5Util.generate(newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseResult.error("md5加密失败");
        }

        user.setPassword(newPasswordMD5);
        userService.update(user);

        return ResponseResult.success("密码修改成功");
    }


    /**
     * 修改用户头像
     *
     * @param file
     * @param id
     * @return
     */
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseResult uploadAvatar(@RequestParam("file") MultipartFile file, Integer id) {

        User user = userService.queryById(id);
        if (user == null) {
            return ResponseResult.error("上传失败，用户不存在");
        }

        String fileName;
        if (!file.isEmpty()) {

            fileName = id + "." + FileUtil.getExtensionName(file.getOriginalFilename());

            try {
                Streams.copy(file.getInputStream(), new FileOutputStream(envConsts.FILE_PATH + envConsts.FILE_AVATAR_DIC + "/" +
                        fileName), true);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResult.error("头像上传失败");
            }

            user.setAvatarFile(fileName);
            userService.update(user);
        } else {
            return ResponseResult.error("头像上传失败");
        }

        return ResponseResult.success("头像上传成功", "/" + envConsts.FILE_AVATAR_DIC + "/" + fileName);
    }

    /**
     * 查询用户总量
     * @return
     */
    @RequestMapping(value = "developer/count", method = RequestMethod.GET)
    public ResponseResult countDevelopers() {

        // 查询条件（必须是开发者）
        User record = new User();
        record.setRole(RoleConsts.DEVELOPER);

        return ResponseResult.success("查询成功", userService.queryListByWhere(record).size());
    }
}
