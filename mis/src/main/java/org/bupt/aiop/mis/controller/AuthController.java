package org.bupt.aiop.mis.controller;

import org.bupt.aiop.common.bean.ResponseResult;
import org.bupt.aiop.common.util.MD5Util;
import org.bupt.aiop.mis.constant.AuthConsts;
import org.bupt.aiop.mis.constant.RoleConsts;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.aiop.mis.service.RedisService;
import org.bupt.aiop.mis.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;


/**
 * 登录、注册和首页跳转
 * Created by zlren on 2017/6/6.
 */
@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

//    @Reference
//    private static SayHelloService sayHelloService;



    /**
     * 发送短信验证码
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "send_sms", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult sendSms(@RequestBody Map<String, String> params) {

        String phone = params.get("username");

        User record = new User();
        record.setUsername(phone);
        User user = this.userService.queryOne(record);
        String action = params.get("action");
        if (action.equals("注册") || action.equals("修改手机")) {
            if (user != null) {
                return ResponseResult.failure("此号码已经注册");
            }
        } else if (action.equals("找回密码")) {
            if (user == null) {
                return ResponseResult.failure("此号码未注册");
            }
        }

        if (redisService.get(phone) != null) {
            return ResponseResult.failure("请1分钟后再试");
        }

        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < AuthConsts.SMS_CODE_LEN; i++) {
            code.append(String.valueOf(random.nextInt(10)));
        }

        logger.info("验证码，手机号：{}，验证码：{}", phone, code);

        ResponseResult responseResult;
        try {
            responseResult = ResponseResult.success("已发送验证码", record.getId());
            // responseResult = SMSUtil.send(phone, String.valueOf(code));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.failure("短信发送失败，请重试");
        }

//        // 存储在redis中，过期时间为60s
//        redisService.setSmSCode(Constant.REDIS_PRE_CODE + phone, String.valueOf(code));

        return responseResult;
    }

    /**
     * 注册
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult register(@RequestBody Map<String, String> params) {

        // username就是手机号
        String username = params.get("username");
        String password = params.get("password");
        String inputCode = params.get("inputCode");
        String name = params.get("name");
        logger.info("name = {}", name);
        logger.info("inputCode = {}", inputCode);

//        String code = redisService.get(Constant.REDIS_PRE_CODE + phone);
        String code = AuthConsts.AUTH_CODE;
        if (code == null) {
            return ResponseResult.failure("验证码过期");
        } else if (!code.equals(inputCode)) {
            return ResponseResult.failure("验证码错误");
        }

        if (this.userService.isExist(username)) {
            return ResponseResult.failure("用户名已经存在");
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(MD5Util.generate(password));
            user.setRole(RoleConsts.DEVELOPER);
            user.setName(name);
            user.setAvatar("avatar_default.png"); // 默认头像
            this.userService.save(user);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseResult.failure("注册失败");
        }

        return ResponseResult.success("注册成功");
    }


    /**
     * 校验验证码
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "check_code", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult checkSMSCode(@RequestBody Map<String, String> params) {

        String inputCode = params.get("inputCode");
        String username = params.get("username");

        logger.info("传过来的验证码是: {}， 手机是：{}", inputCode, username);

        //用户名是否存在
        if(!this.userService.isExist(username)){
            return ResponseResult.failure("不存在该用户");
        }

        String code = AuthConsts.AUTH_CODE;
        if (code == null) {
            return ResponseResult.failure("验证码过期");
        } else if (!code.equals(inputCode)) {
            return ResponseResult.failure("验证码错误");
        }

        User record = new User();
        record.setUsername(username);

        return ResponseResult.success("验证成功", this.userService.queryOne(record).getId());
    }


    /**
     * 登录
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult login(@RequestBody Map<String, String> params) {

        // 得到用户名和密码，用户名就是phone
        String username = params.get("username");
        String password = params.get("password");

        return this.userService.login(username, password, "null");
    }


    /**
     * 未验证跳转
     *
     * @return
     */
    @RequestMapping(value = "login_denied")
    @ResponseBody
    public ResponseResult loginDenied() {
        logger.info("login_denied");
        return ResponseResult.failure("请先登录");
    }


    /**
     * 权限拒绝
     *
     * @return
     */
    @RequestMapping(value = "role_denied")
    @ResponseBody
    public ResponseResult roleDenied() {
        logger.info("role_denied");
        return ResponseResult.failure("无此权限");
    }
}
