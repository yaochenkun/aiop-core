package org.bupt.aiop.platform.controller;

import com.alibaba.fastjson.JSON;
import org.bupt.aiop.platform.annotation.RequiredPermission;
import org.bupt.aiop.platform.constant.EnvConsts;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.aiop.platform.constant.NlpConsts;
import org.bupt.aiop.platform.constant.ResponseConsts;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.thrift.ThriftConnectionService;
import org.bupt.common.util.LogUtil;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自然语言处理服务
 */
@RestController
@RequestMapping("restapi/nlp")
public class NlpController {

	private static final Logger logger = LoggerFactory.getLogger(NlpController.class);


	@Autowired
	private EnvConsts envConsts;

//	@Autowired(required = false)
//	private ThriftConnectionService thriftNlpConnectionService;

	@Autowired(required = false)
	private NlpAlgDubboService nlpAlgDubboService;

	/**
	 * 中文分词
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_seg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_seg")
	public Object word_seg(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理(deepnlp thrift)
//		TProtocol protocol = thriftNlpConnectionService.getConnection();
//		NlpAlgThriftService.Client nlpAlgSerivce = new NlpAlgThriftService.Client(protocol);
//		try {
//		    ResponseResult responseResult = ResponseResult.success("", nlpAlgSerivce.word_seg(text));
//			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
//			return responseResult;
//		} catch (Exception e) {
//			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
//			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
//		} finally {
//			thriftNlpConnectionService.returnConnection(protocol);
//		}

		// 算法处理(hanlp)
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_seg(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}


	/**
	 * 词性标注-默认
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_pos")
	public Object word_pos(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_pos_normal(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 词性标注-地名
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos_place", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_pos_place")
	public Object word_pos_place(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_pos_place(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 词性标注-机构名
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos_organization", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_pos_organization")
	public Object word_pos_organization(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_pos_organization(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 词性标注-日本人名
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos_japanese", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_pos_japanese")
	public Object word_pos_japanese(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_pos_japanese(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 命名实体识别
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_ner", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_ner")
	public Object word_ner(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_ner(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 依存句法分析
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/dependency_parse", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "dependency_parse")
	public Object dependency_parse(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.dependency_parse(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 关键词提取
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_keywords", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "text_keywords")
	public Object text_keywords(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(size) || size <= 0) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.text_keywords(text, size));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 摘要提取
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_summaries", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "text_summaries")
	public Object text_summaries(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(size) || size <= 0) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.text_summaries(text, size));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 获取句子短语
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_phrases", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "text_phrases")
	public Object text_phrases(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(size) || size <= 0) {
			size = NlpConsts.DEFAULT_SIZE;
		}


		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.text_phrases(text, size));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 生成词向量
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_2_vec", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_2_vec")
	public Object word_2_vec(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_2_vec(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 汉字转拼音
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_2_pinyin", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_2_pinyin")
	public Object word_2_pinyin(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_2_pinyin(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 简体转繁体
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/simplified_2_traditional", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "simplified_2_traditional")
	public Object simplified_2_traditional(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.simplified_2_traditional(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 繁体转简体
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/traditional_2_simplified", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "traditional_2_simplified")
	public Object traditional_2_simplified(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.traditional_2_simplified(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 短语相似度
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_sim", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "word_sim")
	public Object word_sim(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text1 = null;
		String text2 = null;
		try {
			text1 = (String) params.get("text1");
			text2 = (String) params.get("text2");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text1) || Validator.checkEmpty(text2)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.word_sim(text1, text2));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}


	/**
	 * 文档相似度
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/document_sim", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "document_sim")
	public Object document_sim(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String doc1 = null;
		String doc2 = null;
		try {
			doc1 = (String) params.get("doc1");
			doc2 = (String) params.get("doc2");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(doc1) || Validator.checkEmpty(doc2)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.document_sim(doc1, doc2));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 词库中与text最相似的前size个单词
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/nearest_words", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "nearest_words")
	public Object nearest_words(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(size) || size <= 0) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.nearest_words(text, size));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}


	/**
	 * 情感分类
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/motion_classify", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "motion_classify")
	public Object motion_classify(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		Integer classNum = null; // 分类数
		try {
			text = (String) params.get("text");
			classNum = (Integer) params.get("class_num");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(classNum) || classNum <= 0) {
			classNum = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		try {
			ResponseResult responseResult = classNum == 5 ? ResponseResult.success("", nlpAlgDubboService.motion_classify_5(text)) : ResponseResult.success("", nlpAlgDubboService.motion_classify_2(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 文本分类
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/category_classify", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "category_classify")
	public Object category_classify(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}

		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.category_classify(text));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}

	/**
	 * 文本推荐
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_suggester", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@RequiredPermission(value = "text_suggester")
	public Object text_suggester(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		List<String> sentences = null;
		String text = null;
		Integer size = null;
		try {
			sentences = (ArrayList<String>) params.get("sentences");
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(text) || Validator.checkNull(sentences)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (Validator.checkNull(size) || size <= 0) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		try {
			ResponseResult responseResult = ResponseResult.success("", nlpAlgDubboService.text_suggester(sentences, text, size));
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_NLP_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		}
	}
}
