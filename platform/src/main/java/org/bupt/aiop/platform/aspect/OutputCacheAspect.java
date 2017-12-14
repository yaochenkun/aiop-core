package org.bupt.aiop.platform.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.aiop.platform.constant.RedisConsts;
import org.bupt.aiop.platform.constant.ResponseConsts;
import org.bupt.aiop.platform.service.OutputService;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.LogUtil;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 能力执行结果缓存切面
 */
@Aspect
@Order(2)
@Component
public class OutputCacheAspect {

	private Logger logger = LoggerFactory.getLogger(OutputCacheAspect.class);

	@Autowired
	private OutputService outputService;

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
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		logger.debug("进入, 能力执行结果缓存环绕方法");

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
		Integer appId = identity.getId();


		// Before: 缓存试探
		// 获取input JSON串
		String ability = joinPoint.getSignature().getName(); // 能力名称
		String input = JSON.toJSONString(joinPoint.getArgs()[0]); // 输入参数

		// 拼接key
		String key = ability + ":" + input;

		// 缓存试探
		if (!outputService.hasAbilityAndInput(key)) {

			logger.debug("缓存未命中, 放行");
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_CACHE_HIT, "ability", ability, LogConsts.FAILED));
			ResponseResult response = (ResponseResult) joinPoint.proceed();
			String output = (String) response.getContent();

			//发生错误
			if (ResponseConsts.ERROR.equals(response.getCode())) {
				return output;
			}

			// TODO: 后期导向RocketMQ消费者端写入Redis
			// After: 写入缓存
			// 判断是否缓存过，若没有则缓存
			if (outputService.hasAbilityAndInput(key)) {
				logger.debug("已缓存过该结果, 返回之");
				return output;
			}

			outputService.saveOutput(key, output);
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "hash_map", RedisConsts.AIOP_ABILITY_INPUT_OUTPUT, LogConsts.VERB_CACHE_SAVE, "key", key, LogConsts.SUCCESS));
			logger.debug("缓存成功");
			logger.debug("退出, 能力执行结果缓存环绕方法");
			return output;
		}

		//缓存命中, 获取缓存结果
		logger.debug("缓存命中");
		logger.debug("退出, 能力执行结果缓存环绕方法");
		logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_CACHE_HIT, "ability", ability, LogConsts.SUCCESS));
		logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
		return outputService.getOutput(key);
	}
}
