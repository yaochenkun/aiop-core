package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.service.AbilityService;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * 能力控制器
 */
@RestController
@RequestMapping("api/ability")
public class AbilityController {

	private static final Logger logger = LoggerFactory.getLogger(AbilityController.class);

	@Autowired
	private AbilityService abilityService;

	@Autowired
	private EnvConsts envConsts;

	/**
	 * 添加能力
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseResult addAbility(@RequestBody Map<String, Object> params, HttpSession session) {

		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

		String zhName = (String) params.get("zhName");
		String enName = (String) params.get("enName");
		String restapiUrl = (String) params.get("restapiUrl");
		String docUrl = (String) params.get("docUrl");
		String type = (String) params.get("type");
		String modelId = (String) params.get("modelId");
		String version = (String) params.get("version");
		Integer invokeLimit = (Integer) params.get("invokeLimit");
		Integer qpsLimit = (Integer) params.get("qpsLimit");

		// 校验数据
		if (Validator.checkEmpty(zhName)
				|| Validator.checkEmpty(enName)
				|| Validator.checkEmpty(restapiUrl)
				|| Validator.checkEmpty(docUrl)
				|| Validator.checkEmpty(type)
				|| Validator.checkEmpty(modelId)
				|| Validator.checkEmpty(version)
				|| Validator.checkNull(invokeLimit)
				|| Validator.checkNull(qpsLimit)) {
			return ResponseResult.error("添加失败，信息不完整");
		}

		// 中文名称不能一样
		Ability ability = new Ability();
		ability.setZhName(zhName);
		if (abilityService.queryOne(ability) != null) {
			logger.debug("能力的中文名称={}与已有冲突", zhName);
			return ResponseResult.error("中文名称重复");
		}

		// 英文名称不能一样
		ability.setZhName(null);
		ability.setEnName(enName);
		if (abilityService.queryOne(ability) != null) {
			logger.debug("能力的英文名称={}与已有冲突", enName);
			return ResponseResult.error("英文名称重复");
		}

		ability.setZhName(zhName);
		ability.setEnName(enName);
		ability.setRestapiUrl(restapiUrl);
		ability.setDocUrl(docUrl);
		ability.setType(type);
		ability.setVersion(version);
		ability.setInvokeLimit(invokeLimit);
		ability.setQpsLimit(qpsLimit);
		ability.setCreateDate(new Date());
		ability.setCreateDate(new Date());

		// 如果type == 模型算法, modelId与ability的关系也要存入
		abilityService.saveAbility(ability);

		logger.debug("能力={}, 创建成功", ability.getZhName());
		return ResponseResult.success("创建成功");
	}

//	/**
//	 * 查询能力列表
//	 *
//	 * @param params
//	 * @return
//	 */
//	@RequestMapping(value = "list", method = RequestMethod.POST)
//	public ResponseResult listAbility(@RequestBody Map<String, Object> params, HttpSession session) {
//
//		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
//
//		Integer pageNow = (Integer) params.get("pageNow");
//		Integer pageSize = (Integer) params.get("pageSize");
//
//		String name = (String) params.get("name");
//		String status = (String) params.get("status");
//		Date updateDate = TimeUtil.parseDate((String) params.get("updateDate"));
//		Integer developerId = identity.getId();
//
//		List<App> list = appService.listApp(pageNow, pageSize, developerId, name, status, updateDate);
//		PageResult pageResult = new PageResult(new PageInfo<>(list));
//
//		logger.debug("查询应用列表成功, {}, {}, {}", name, status, updateDate);
//		return ResponseResult.success("查询成功", pageResult);
//	}

//	/**
//	 * 删除应能力
//	 * @param abilityId
//	 * @return
//	 */
//	@RequestMapping(value = "{abilityId}" , method = RequestMethod.DELETE)
//	public ResponseResult deleteApp(@PathVariable Integer abilityId) {
//
//		if (appService.deleteById(abilityId) == ResponseConsts.CRUD_ERROR) {
//			logger.debug("删除{}应用失败", abilityId);
//			return ResponseResult.error("删除失败");
//		}
//
//		logger.debug("删除{}应用成功", abilityId);
//		return ResponseResult.success("删除成功");
//	}

//	/**
//	 * 获取单个应用
//	 * @param appId
//	 * @return
//	 */
//	@RequestMapping(value = "{abilityId}" , method = RequestMethod.GET)
//	public ResponseResult getAbility(@PathVariable Integer abilityId) {
//
//		App app = appService.queryById(abilityId);
//		if (app == null) {
//			logger.debug("获取应用{}失败", appId);
//			return ResponseResult.error("获取失败");
//		}
//
//		app.setLogoFile("/app_logo/" + app.getLogoFile());
//
//		logger.debug("获取应用{}成功", abilityId);
//		return ResponseResult.success("获取成功", app);
//	}

//	/**
//	 * 更新应用状态
//	 * @param appId
//	 * @return
//	 */
//	@RequestMapping(value = "{appId}/status" , method = RequestMethod.PUT)
//	public ResponseResult updateAppStatus(@PathVariable Integer appId, @RequestBody Map<String, Object> params) {
//
//		String status = (String) params.get("status");
//
//		App app = appService.queryById(appId);
//		if (app == null) {
//			logger.debug("应用{}更新失败，不存在该应用", appId);
//			return ResponseResult.error("更新失败，不存在该应用");
//		}
//
//		app.setStatus(status);
//		if (appService.update(app) == ResponseConsts.CRUD_ERROR) {
//			logger.debug("应用{}更新失败", appId);
//			return ResponseResult.error("更新失败");
//		}
//
//		logger.debug("应用{}更新成功", appId);
//		return ResponseResult.success("更新成功");
//	}

}
