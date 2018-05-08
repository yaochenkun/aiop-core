package org.bupt.aiop.platform.interceptor;

import com.alibaba.fastjson.JSON;
import org.bupt.aiop.platform.constant.EnvConsts;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.ErrorConsts;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.LogUtil;
import org.bupt.common.util.token.Identity;
import org.bupt.common.util.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 能力使用权限认证拦截器（判断access_token有效性）
 * Created by ken on 2017/6/8.
 */
public class AccessTokenCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenCheckInterceptor.class);

    @Autowired
    private EnvConsts envConsts;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {

        logger.debug("进入AccessTokenCheckInterceptor");

        // 验证token的有效性
        Integer appId = null;
        String accessToken = "";
        try {

            accessToken = request.getHeader(OauthConsts.KEY_ACCESS_TOKEN);
            Identity identity = TokenUtil.parseToken(accessToken, envConsts.ACCESS_TOKEN_API_KEY_SECRET);
            appId = identity.getId();

            //把identity存入session中(其中包含用户名、角色、过期时间戳等)
            request.getSession().setAttribute(OauthConsts.KEY_IDENTITY, identity);

            logger.debug("app_id={}, client_id={}, access_token通过认证", appId, identity.getClientId());
            logger.info(LogUtil.body(LogConsts.DOMAIN_OAUTH, "app_id", appId, LogConsts.VERB_AUTH, "access_token", "", LogConsts.SUCCESS));
            return true;

        } catch (Exception e) {
            logger.debug("access_token无效, 原因为: {}", e.getMessage());
            logger.debug("正转向认证失败控制器");
            logger.info(LogUtil.body(LogConsts.DOMAIN_OAUTH, "app_id", appId, LogConsts.VERB_AUTH, "access_token", "", LogConsts.ERROR, ErrorConsts.OAUTH_MSG_ACCESS_TOKEN_INVALID));
            response.sendRedirect("/restapi/common/error/oauth/" + ErrorConsts.OAUTH_CODE_ACCESS_TOKEN_INVALID);

            return false;
        }
    }

}
