package org.bupt.aiop.restapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.protocol.TProtocol;
import org.bupt.aiop.restapi.constant.ResponseConsts;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.aiop.rpcapi.thrift.NlpAlgThriftService;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.thrift.ThriftConnectionService;
import org.bupt.common.util.Validator;
import org.bupt.aiop.restapi.constant.EnvConsts;
import org.bupt.aiop.restapi.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * UserController
 */
@RestController
@RequestMapping("nlp")
public class NlpController {

	private static final Logger logger = LoggerFactory.getLogger(NlpController.class);


	@Autowired
	private RedisService redisService;

	@Autowired
	private EnvConsts envConsts;

	@Autowired
	private ThriftConnectionService thriftNlpConnectionService;

	@Reference
    private static NlpAlgDubboService nlpAlgDubboService;

	/**
	 * 中文分词
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_seg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_seg(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException {


		String response = null;

		/**
		 * 获取初始输入
		 */
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_INVALID_PARAM,
					ResponseConsts.ERROR_MSG_INVALID_PARAM));

			logger.info(response);
			return response;
		}


		/**
		 * 校验初始输入
		 */
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_EMPTY_PARAM,
					ResponseConsts.ERROR_MSG_EMPTY_PARAM));

			logger.info(response);
			return response;
		}


		/**
		 * 算法处理
		 */
		TProtocol protocol = thriftNlpConnectionService.getConnection();
		NlpAlgThriftService.Client nlpAlgSerivce = new NlpAlgThriftService.Client(protocol);
		try {
			response = nlpAlgSerivce.word_seg(text);
		} catch (Exception e) {
			e.printStackTrace();
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_INTERNAL_ERROR,
					ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
		} finally {
			thriftNlpConnectionService.returnConnection(protocol);
		}

		logger.info(response);
		return response;
	}

	/**
	 * 词性标注
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_pos(@RequestBody Map<String, Object> params) {

		String response = null;

		/**
		 * 获取初始输入
		 */
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_INVALID_PARAM,
					ResponseConsts.ERROR_MSG_INVALID_PARAM));

			logger.info(response);
			return response;
		}


		/**
		 * 校验初始输入
		 */
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_EMPTY_PARAM,
					ResponseConsts.ERROR_MSG_EMPTY_PARAM));

			logger.info(response);
			return response;
		}


		/**
		 * 算法处理
		 */
		TProtocol protocol = thriftNlpConnectionService.getConnection();
		NlpAlgThriftService.Client nlpAlgSerivce = new NlpAlgThriftService.Client(protocol);
		try {
			response = nlpAlgSerivce.word_pos(text);
		} catch (Exception e) {
			e.printStackTrace();
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_INTERNAL_ERROR,
					ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
		} finally {
			thriftNlpConnectionService.returnConnection(protocol);
		}

		logger.info(response);
		return response;
	}

	/**
	 * 关键词提取
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_keywords", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String text_keywords(@RequestBody Map<String, Object> params) {

		String response = null;

		/**
		 * 获取初始输入
		 */
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_INVALID_PARAM,
					ResponseConsts.ERROR_MSG_INVALID_PARAM));

			logger.info(response);
			return response;
		}


		/**
		 * 校验初始输入
		 */
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(ErrorResult.build(ResponseConsts.ERROR_CODE_EMPTY_PARAM,
					ResponseConsts.ERROR_MSG_EMPTY_PARAM));

			logger.info(response);
			return response;
		}

		/**
		 * 算法处理
		 */
		response = nlpAlgDubboService.text_keywords(text, size);


		logger.info(response);
		return response;
	}


}
