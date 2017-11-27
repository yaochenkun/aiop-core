package org.bupt.aiop.platform.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.protocol.TProtocol;
import org.bupt.aiop.platform.constant.NlpConsts;
import org.bupt.aiop.platform.constant.ResponseConsts;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.aiop.rpcapi.thrift.NlpAlgThriftService;
import org.bupt.common.bean.ErrorResult;
import org.bupt.common.thrift.ThriftConnectionService;
import org.bupt.common.util.Validator;
import org.bupt.aiop.platform.constant.EnvConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * NlpController
 */
@RestController
@RequestMapping("restapi/nlp")
public class NlpController {

	private static final Logger logger = LoggerFactory.getLogger(NlpController.class);


	@Autowired
	private EnvConsts envConsts;

	@Autowired
	private ThriftConnectionService thriftNlpConnectionService;

	@Reference
	private NlpAlgDubboService nlpAlgDubboService;

	/**
	 * 中文分词
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_seg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_seg(@RequestBody Map<String, Object> params) throws UnsupportedEncodingException {


		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}


		// 算法处理
		TProtocol protocol = thriftNlpConnectionService.getConnection();
		NlpAlgThriftService.Client nlpAlgSerivce = new NlpAlgThriftService.Client(protocol);
		try {
			response = nlpAlgSerivce.word_seg(text);
		} catch (Exception e) {
			e.printStackTrace();
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
		} finally {
			thriftNlpConnectionService.returnConnection(protocol);
		}

		return response;
	}

	/**
	 * 词性标注(deepnlp)
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_pos", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_pos_v1(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}


		// 算法处理
		TProtocol protocol = thriftNlpConnectionService.getConnection();
		NlpAlgThriftService.Client nlpAlgSerivce = new NlpAlgThriftService.Client(protocol);
		try {
			response = nlpAlgSerivce.word_pos(text);
		} catch (Exception e) {
			e.printStackTrace();
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
		} finally {
			thriftNlpConnectionService.returnConnection(protocol);
		}

		return response;
	}

	/**
	 * 词性标注(hanlp)
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v2/word_pos", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_pos_v2(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}


		// 算法处理
		response = nlpAlgDubboService.word_pos(text);
		return response;
	}

	/**
	 * 关键词提取
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_keywords", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
//	@RequiredPermission(permission = "text_keywords")
	public String text_keywords(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}
		if (Validator.checkNull(size)) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		response = nlpAlgDubboService.text_keywords(text, size);
		return response;
	}

	/**
	 * 摘要提取
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_summaries", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String text_summaries(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}
		if (Validator.checkNull(size)) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		response = nlpAlgDubboService.text_summaries(text, size);
		return response;
	}

	/**
	 * 获取句子短语
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/text_phrases", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String text_phrases(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}
		if (Validator.checkNull(size)) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		response = nlpAlgDubboService.text_phrases(text, size);
		return response;
	}

	/**
	 * 生成词向量
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_2_vec", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_2_vec(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.word_2_vec(text);
		return response;
	}

	/**
	 * 汉字转拼音
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_2_pinyin", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_2_pinyin(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.word_2_pinyin(text);
		return response;
	}

	/**
	 * 简体转繁体
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/simplified_2_traditional", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String simplified_2_traditional(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.simplified_2_traditional(text);
		return response;
	}

	/**
	 * 繁体转简体
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/traditional_2_simplified", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String traditional_2_simplified(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		try {
			text = (String) params.get("text");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.traditional_2_simplified(text);
		return response;
	}

	/**
	 * 短语相似度
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/word_sim", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String word_sim(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text1 = null;
		String text2 = null;
		try {
			text1 = (String) params.get("text1");
			text2 = (String) params.get("text2");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text1) || Validator.checkEmpty(text2)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.word_sim(text1, text2);
		return response;
	}


	/**
	 * 文档相似度
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/document_sim", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String document_sim(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String doc1 = null;
		String doc2 = null;
		try {
			doc1 = (String) params.get("doc1");
			doc2 = (String) params.get("doc2");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(doc1) || Validator.checkEmpty(doc2)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}

		// 算法处理
		response = nlpAlgDubboService.document_sim(doc1, doc2);
		return response;
	}

	/**
	 * 最相似的前size个单词
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/nearest_words", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String nearest_words(@RequestBody Map<String, Object> params) {

		String response = null;

		// 获取初始输入
		String text = null;
		Integer size = null;
		try {
			text = (String) params.get("text");
			size = (Integer) params.get("size");
		} catch (Exception e) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return response;
		}


		// 校验初始输入
		if (Validator.checkEmpty(text)) {
			response = JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return response;
		}
		if (Validator.checkNull(size)) {
			size = NlpConsts.DEFAULT_SIZE;
		}

		// 算法处理
		response = nlpAlgDubboService.nearest_words(text, size);
		return response;
	}


}
