package org.bupt.aiop.platform.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.aiop.platform.service.OauthService;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.LogUtil;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 日志切面
 */
@Aspect
@Order(1)
@Component
public class LogAspect {

	private Logger logger = LoggerFactory.getLogger(LogAspect.class);

	/**
	 * NlpController每个方法均作为切点
	 */
	@Pointcut("execution(* org.bupt.aiop.platform.controller.NlpController.*(..))")
	private void pointcut(){}


	/**
	 * 能力执行结果缓存环绕方法
	 * @param joinPoint
	 */
	@Around(value = "pointcut()")
	public String around(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
		Integer appId = identity.getId();
		String ability = joinPoint.getSignature().getName(); // 能力名称

		logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_ENTER, "ability", ability, LogConsts.SUCCESS));
		String output = (String) joinPoint.proceed();

		//todo: 根据output的状态码决定SUCCESS ERROR, output可以改成ResponseResult, 然后提取content字段返回json字符串
		logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_EXIT, "ability", ability, LogConsts.SUCCESS));

		return output;
	}
}
