package org.bupt.aiop.restapi.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.util.Streams;
import org.apache.thrift.protocol.TProtocol;
import org.bupt.aiop.common.bean.ResponseResult;
import org.bupt.aiop.common.bean.PageResult;
import org.bupt.aiop.common.util.FileUtil;
import org.bupt.aiop.common.util.MD5Util;
import org.bupt.aiop.common.util.Validator;
import org.bupt.aiop.common.util.token.Identity;
import org.bupt.aiop.restapi.annotation.RequiredRoles;
import org.bupt.aiop.restapi.constant.AuthConsts;
import org.bupt.aiop.restapi.constant.EnvConsts;
import org.bupt.aiop.restapi.pojo.po.User;
import org.bupt.aiop.restapi.service.PropertyService;
import org.bupt.aiop.restapi.service.RedisService;
import org.bupt.aiop.restapi.service.TestService;
import org.bupt.aiop.restapi.service.UserService;
import org.bupt.aiop.rpcapi.thrift.inter.ImageAlgService;
import org.bupt.aiop.rpcapi.thrift.inter.NlpAlgService;
import org.bupt.aiop.rpcapi.thrift.inter.SpeechAlgService;
import org.bupt.aiop.rpcapi.thrift.inter.VideoAlgService;
import org.bupt.aiop.rpcapi.thrift.pool.ThriftConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * UserController
 */
@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private EnvConsts envConsts;

    @Autowired
    private ThriftConnectionService thriftNlpConnectionService;

    @Autowired
    private ThriftConnectionService thriftImageConnectionService;

    @Autowired
    private ThriftConnectionService thriftSpeechConnectionService;

    @Autowired
    private ThriftConnectionService thriftVideoConnectionService;

    @Autowired
    private TestService testService;


    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult test() {

//        /*
//        ** RPC测试
//         */
//
//        // 1.自然语言处理技术
//        TProtocol protocol = thriftNlpConnectionService.getConnection();
//        NlpAlgService.Client nlpAlgSerivce = new NlpAlgService.Client(protocol);
//        try {
//            System.out.println(nlpAlgSerivce.hello("yaochenkun"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            thriftNlpConnectionService.returnConnection(protocol);
//        }
//
//        // 2.图像技术
//        protocol = thriftImageConnectionService.getConnection();
//        ImageAlgService.Client imageAlgSerivce = new ImageAlgService.Client(protocol);
//        try {
//            System.out.println(imageAlgSerivce.hello("yaochenkun"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            thriftImageConnectionService.returnConnection(protocol);
//        }
//
//        // 3.语音技术
//        protocol = thriftSpeechConnectionService.getConnection();
//        SpeechAlgService.Client speechAlgSerivce = new SpeechAlgService.Client(protocol);
//        try {
//            System.out.println(speechAlgSerivce.hello("yaochenkun"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            thriftSpeechConnectionService.returnConnection(protocol);
//        }
//
//        // 4.视频技术
//        protocol = thriftVideoConnectionService.getConnection();
//        VideoAlgService.Client videoAlgSerivce = new VideoAlgService.Client(protocol);
//        try {
//            System.out.println(videoAlgSerivce.hello("yaochenkun"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            thriftVideoConnectionService.returnConnection(protocol);
//        }


        /*
        **RocketMQ测试
         */
        testService.registerSuccessNotify(1);


        return ResponseResult.success();
    }




    /**
     * 添加员工
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult addUser(@RequestBody Map<String, Object> params) {

        String name = (String) params.get("name");
        String username = (String) params.get("username");
        String role = (String) params.get("role");

        User user = new User();

        if (Validator.checkEmpty(name) || Validator.checkEmpty(username) || Validator.checkEmpty(role)) {
            return ResponseResult.failure("添加失败，信息不完整");
        } else {
            user.setName(name);
            user.setUsername(username);
            user.setRole(role);
        }

        if (this.userService.isExist(username)) {
            return ResponseResult.failure("该用户名已被注册");
        }

        try {
            user.setPassword(MD5Util.generate(AuthConsts.DEFAULT_PASSWORD));
            user.setAvatar("avatar_default.png"); // 默认头像
            this.userService.save(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseResult.failure("添加失败，md5生成错误");
        }

        return ResponseResult.success("添加成功");
    }


    /**
     * 修改别的用户的信息
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseResult updateOtherUser(@RequestBody Map<String, Object> params) {

        Integer userId = (Integer) params.get("userId");
        // 修改别的用户的时候不能修改name和phone
        String name = (String) params.get("name");
        String role = (String) params.get("role");

        // 未修改的user
        User user = this.userService.queryById(userId);

        if (!Validator.checkEmpty(name)) {
            user.setName(name);
        }

        // role
        if (!Validator.checkEmpty(role)) {
            user.setRole(role);
        }

        this.userService.update(user);

        return ResponseResult.success("修改成功");
    }


    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseResult queryById(@PathVariable("userId") Integer userId) {

        User user = this.userService.queryById(userId);
        if (user == null) {
            return ResponseResult.failure("用户不存在");
        }

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
    @ResponseBody
    @RequiredRoles(roles = {"系统管理员"})
    public ResponseResult deleteById(@PathVariable("userId") Integer userId) {

        User user = this.userService.queryById(userId);
        if (user == null) {
            return ResponseResult.failure("用户不存在");
        }

        // this.userService.deleteById(userId);
        this.userService.delete(user);

        logger.info("删除用户：{}", user.getName());

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
    @ResponseBody
    public ResponseResult updateById(@PathVariable("userId") Integer userId, @RequestBody Map<String, Object> params) {

        // 自己可以修改自己的name和phone
        String name = (String) params.get("name");

        // 未修改的user
        User user = this.userService.queryById(userId);

        if (!Validator.checkEmpty(name)) {
            user.setName(name);
        }

        this.userService.update(user);
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
    @ResponseBody
    public ResponseResult queryUsers(@RequestBody Map<String, Object> params, HttpSession session) {

        Integer pageNow = (Integer) params.get("pageNow");
        Integer pageSize = (Integer) params.get("pageSize");

        String role = (String) params.get("role");
        String username = (String) params.get("username");
        String name = (String) params.get("name");

        Identity identity = (Identity) session.getAttribute(AuthConsts.IDENTITY);

        List<User> userList = this.userService.queryUserList(pageNow, pageSize, role, username, name, identity);
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
    @ResponseBody
    public ResponseResult changePassword(@RequestBody Map<String, Object> params, @PathVariable("userId") Integer
            userId) {

        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");

        User user = this.userService.queryById(userId);

        // 找回密码的时候没有oldPassword
        if (!Validator.checkEmpty(oldPassword)) {
            String oldPasswordMD5;
            try {
                oldPasswordMD5 = MD5Util.generate(oldPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return ResponseResult.failure("md5加密失败！");
            }

            if (!oldPasswordMD5.equals(user.getPassword())) {
                return ResponseResult.failure("修改失败，原密码输入错误");
            }
        }

        String newPasswordMD5;
        try {
            newPasswordMD5 = MD5Util.generate(newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseResult.failure("md5加密失败");
        }

        user.setPassword(newPasswordMD5);
        this.userService.update(user);

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
    @ResponseBody
    public ResponseResult uploadAvatar(@RequestParam("file") MultipartFile file, Integer id) {

        User user = this.userService.queryById(id);
        if (user == null) {
            return ResponseResult.failure("上传失败，用户不存在");
        }

        String fileName;
        if (!file.isEmpty()) {

            fileName = id + "." + FileUtil.getExtensionName(file.getOriginalFilename());

            try {
                Streams.copy(file.getInputStream(), new FileOutputStream(envConsts.FILE_PATH + "avatar/" +
                        fileName), true);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseResult.failure("头像上传失败");
            }

            user.setAvatar(fileName);
            this.userService.update(user);
        } else {
            return ResponseResult.failure("头像上传失败");
        }

        return ResponseResult.success("头像上传成功", "/avatar/" + fileName);
    }
}
