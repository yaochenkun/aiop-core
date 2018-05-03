package org.bupt.aiop.platform.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.aiop.platform.constant.ResponseConsts;
import org.bupt.aiop.platform.pojo.po.Ability;
import org.bupt.aiop.platform.pojo.po.AbilityInvokeLog;
import org.bupt.aiop.platform.service.AbilityInvokeLogService;
import org.bupt.aiop.platform.service.AbilityService;
import org.bupt.aiop.platform.service.OauthService;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.LogUtil;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 日志切面
 */
@Aspect
@Order(1)
@Component
public class LogAspect {

	private Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Autowired
	private AbilityService abilityService;

	@Autowired
	private AbilityInvokeLogService abilityInvokeLogService;

	/**
	 * Controller每个方法均作为切点
	 */
	@Pointcut("execution(* org.bupt.aiop.platform.controller..*.*(..))")
	private void pointcut(){}


	/**
	 * 日志输出 缓存环绕方法
	 * @param joinPoint
	 */
	@Around(value = "pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		long enterTimestamp = System.currentTimeMillis();

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
		Integer appId = identity.getId();
		String abilityName = joinPoint.getSignature().getName(); // 能力名称

		logger.info(LogUtil.body(LogConsts.DOMAIN_ABILITY_REST, "app_id", appId, LogConsts.VERB_ENTER, "ability", abilityName));
		ResponseResult response = (ResponseResult) joinPoint.proceed();

		// 根据ability名称获取abilityId
		Ability ability = new Ability();
		ability.setEnName(abilityName);
		ability = abilityService.queryOne(ability);
		if (ability == null) {
			logger.debug("能力={}不存在", abilityName);
			logger.info(LogUtil.body(LogConsts.DOMAIN_ABILITY_REST, "app_id", appId, LogConsts.VERB_EXIT, "ability", abilityName));
			return response.getContent();
		}

		// 写入能力调用日志数据库
		AbilityInvokeLog abilityInvokeLog = new AbilityInvokeLog();
		abilityInvokeLog.setAppId(appId);
		abilityInvokeLog.setAbilityId(ability.getId());
		abilityInvokeLog.setInvokeResult(ResponseConsts.ERROR.equals(response.getCode()) ? "调用失败" : "调用成功");
		abilityInvokeLog.setInvokeResultReason(response.getReason());
		abilityInvokeLog.setInvokeTime(new Date());
		abilityInvokeLogService.save(abilityInvokeLog);

		long exitTimestamp = System.currentTimeMillis();

		logger.info(LogUtil.body(LogConsts.DOMAIN_ABILITY_REST, "app_id", appId, LogConsts.VERB_EXIT, "ability", abilityName, Long.toString(exitTimestamp - enterTimestamp)));
		return response.getContent();
	}
}
