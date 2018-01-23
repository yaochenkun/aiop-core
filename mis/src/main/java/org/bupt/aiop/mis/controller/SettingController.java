package org.bupt.aiop.mis.controller;

import org.bupt.common.bean.ResponseResult;
import org.bupt.common.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("api/setting")
public class SettingController {

	private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

	/**
	 * 查询系统配置
	 */
	@RequestMapping(value="list", method = RequestMethod.GET)
	public ResponseResult listSetting() {
		String filePath = "env.properties";
		logger.debug("查询配置文件={}成功", filePath);
		return ResponseResult.success("创建成功", PropertyUtil.readStrings(filePath));
	}
}
