package org.bupt.aiop.platform.interceptor;

import org.bupt.aiop.platform.annotation.RequiredPermission;
import org.bupt.aiop.platform.service.OauthService;
import org.bupt.common.constant.ErrorConsts;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 能力权限拦截器 1:1
 */
public class PermissionCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(PermissionCheckInterceptor.class);

    @Autowired
    private OauthService oauthService;

    // 在调用方法之前执行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("进入PermissionCheckInterceptor");

        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的Access注解
        RequiredPermission permissionCheck = method.getAnnotation(RequiredPermission.class);
        if (permissionCheck == null) {
            logger.info("该方法无需任何权限，放行");
            return true;
        }

        String permission = permissionCheck.permission();
        if (!"".equals(permission)) {

            logger.info("该方法对应能力要求的权限={}", permission);

            // 获取appId对应的权限
            Identity identity = ((Identity) request.getSession().getAttribute(OauthConsts.KEY_IDENTITY));
            Integer appId = identity.getId();
            String userPermissionStr = oauthService.getAppPermission(appId);
            if (userPermissionStr == null) {
                logger.info("权限拒绝");
                response.sendRedirect("/api/oauth/error/" + ErrorConsts.OAUTH_CODE_PERMISSION_DENIED);
            }

            String[] userPermissions = oauthService.getAppPermission(appId).split(",");
            Set<String> userPermissionSet = new HashSet<>();
            userPermissionSet.addAll(Arrays.asList(userPermissions));

            logger.info("应用的能力权限={}", userPermissionSet.toString());

            if (!userPermissionSet.isEmpty()) {
                if (userPermissionSet.contains(permission)) {
                    // 校验通过返回true, 否则拦截请求
                    logger.info("权限校验通过");
                    return true;
                }
            }
        }

        logger.info("权限拒绝");
        response.sendRedirect("/api/oauth/error/" + ErrorConsts.OAUTH_CODE_PERMISSION_DENIED);
        return false;
    }
}
