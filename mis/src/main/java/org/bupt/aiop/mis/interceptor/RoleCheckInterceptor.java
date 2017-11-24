package org.bupt.aiop.mis.interceptor;

import org.bupt.aiop.mis.annotation.RequiredRoles;
import org.bupt.common.constant.ErrorConsts;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色拦截器 1:n
 */
public class RoleCheckInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RoleCheckInterceptor.class);

    // 在调用方法之前执行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("进入RoleCheckInterceptor");

        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的Access注解
        RequiredRoles roleCheck = method.getAnnotation(RequiredRoles.class);
        if (roleCheck == null) {
            // 如果注解为null, 说明不需要拦截, 直接放过
            return true;
        }

        String[] roles = roleCheck.roles();
        if (roles.length > 0) {

            // 如果权限配置不为空, 则取出配置值
            Set<String> roleSet = new HashSet<>();

            // 将权限加入一个set集合中
            roleSet.addAll(Arrays.asList(roles));
            // 这里我为了方便是直接参数传入权限, 在实际操作中应该是从参数中获取用户Id
            // 到数据库权限表中查询用户拥有的权限集合, 与set集合中的权限进行对比完成权限校验

            String role = ((Identity) request.getSession().getAttribute("identity")).getPermission();
            logger.info("用户的角色是 {}", role);
            if (!Validator.checkEmpty(role)) {
                if (roleSet.contains(role)) {

                    // 校验通过返回true, 否则拦截请求
                    logger.info("权限校验通过");
                    return true;
                }
            }
        }

        logger.info("权限拒绝");
        response.sendRedirect("/api/error/oauth/" + ErrorConsts.OAUTH_CODE_PERMISSION_DENIED);
        return false;
    }
}
