package org.bupt.aiop.platform.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.thrift.protocol.TProtocol;
import org.bupt.aiop.platform.annotation.RequiredPermission;
import org.bupt.aiop.platform.constant.EnvConsts;
import org.bupt.aiop.platform.constant.LogConsts;
import org.bupt.aiop.platform.constant.NlpConsts;
import org.bupt.aiop.platform.constant.ResponseConsts;
import org.bupt.aiop.rpcapi.dubbo.ImageAlgDubboService;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.aiop.rpcapi.thrift.ImageAlgThriftService;
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
 * 图像处理服务
 */
@RestController
@RequestMapping("restapi/image")
public class ImageController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);


	@Autowired
	private EnvConsts envConsts;

	@Autowired
	private ThriftConnectionService thriftImageConnectionService;

	@Reference
	private ImageAlgDubboService imageAlgDubboService;

	/**
	 * 人脸相似度
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "v1/face_sim", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
//	@RequiredPermission(value = "face_sim")
	public Object face_sim(@RequestBody Map<String, Object> params, HttpSession session) {

		//日志参数
		String ability = Thread.currentThread().getStackTrace()[1].getMethodName();
		Integer appId = ((Identity) session.getAttribute(OauthConsts.KEY_IDENTITY)).getId();

		// 获取初始输入
		String face1 = null;
		String face2 = null;
		try {
			face1 = (String) params.get("face1");
			face2 = (String) params.get("face2");
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_IMAGE_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_PARAM, ResponseConsts.ERROR_MSG_INVALID_PARAM)));
		}


		// 校验初始输入
		if (Validator.checkEmpty(face1) || Validator.checkEmpty(face2)) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_IMAGE_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_EMPTY_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_EMPTY_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_EMPTY_PARAM, ResponseConsts.ERROR_MSG_EMPTY_PARAM)));
		}
		if (!Validator.checkBase64(face1) || !Validator.checkBase64(face2)) { // 校验两张图片是否为合法的base64编码格式
			logger.info(LogUtil.body(LogConsts.DOMAIN_IMAGE_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INVALID_BASE64_PARAM));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INVALID_BASE64_PARAM, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INVALID_BASE64_PARAM, ResponseConsts.ERROR_MSG_INVALID_BASE64_PARAM)));
		}


		// 算法处理(thrift)
		TProtocol protocol = thriftImageConnectionService.getConnection();
		ImageAlgThriftService.Client imageAlgSerivce = new ImageAlgThriftService.Client(protocol);
		try {
		    ResponseResult responseResult = ResponseResult.success("", imageAlgSerivce.face_sim(face1, face2));
			logger.info(LogUtil.body(LogConsts.DOMAIN_IMAGE_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.SUCCESS));
			return responseResult;
		} catch (Exception e) {
			logger.info(LogUtil.body(LogConsts.DOMAIN_IMAGE_REST, "app_id", appId, LogConsts.VERB_INVOKE, "ability", ability, LogConsts.ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR));
			return ResponseResult.error(ResponseConsts.ERROR_MSG_INTERNAL_ERROR, JSON.toJSONString(new ErrorResult(ResponseConsts.ERROR_CODE_INTERNAL_ERROR, ResponseConsts.ERROR_MSG_INTERNAL_ERROR)));
		} finally {
			thriftImageConnectionService.returnConnection(protocol);
		}
	}
}
