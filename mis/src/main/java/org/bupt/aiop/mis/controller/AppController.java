package org.bupt.aiop.mis.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.util.Streams;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.pojo.po.AppAbility;
import org.bupt.aiop.mis.pojo.vo.AbilityUnderApp;
import org.bupt.aiop.mis.service.AbilityService;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.common.bean.PageResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.constant.ResponseConsts;
import org.bupt.common.util.FileUtil;
import org.bupt.common.util.TimeUtil;
import org.bupt.common.util.UUIDUtil;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 应用控制器
 */
@RestController
@RequestMapping("api/app")
public class AppController {

	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private AppService appService;

	@Autowired
	private AbilityService abilityService;

	@Autowired
	private EnvConsts envConsts;

	/**
	 * 添加应用
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseResult addApp(@RequestBody Map<String, Object> params, HttpSession session) {

		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

		String name = (String) params.get("name");
		String type = (String) params.get("type");
		String platform = (String) params.get("platform");
		String description = (String) params.get("description");

		// 校验数据
		if (Validator.checkEmpty(name) || Validator.checkEmpty(type) || Validator.checkEmpty(platform) || Validator.checkEmpty(description)) {
			return ResponseResult.error("添加失败，信息不完整");
		}

		// 名称不可重复
		App app = new App();
		app.setName(name);
		if (appService.queryOne(app) != null) {
			return ResponseResult.error("添加失败，应用名称重复");
		}

		// 查询目前平台的所有能力
		// 将用户勾选的能力拼接成以,分隔的字符串作为权限串
		List<Ability> allAbilityList = abilityService.queryAll();
		List<Ability> selectedAbilityList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (Ability ability : allAbilityList) {
			String enName = ability.getEnName();
			Boolean isSelected = (Boolean) params.get(enName);
			if (isSelected == true) {
				selectedAbilityList.add(ability);
				sb.append(","); // 拼接权限串
				sb.append(enName);
			}
		}

		// 获取权限串
		String abilityScope = sb.toString();
		abilityScope = "".equals(abilityScope) ? "none" : abilityScope.substring(1);

		// 装配数据
		app.setDeveloperId(identity.getId());
		app.setName(name);
		app.setType(type);
		app.setPlatform(platform);
		app.setAbilityScope(abilityScope);
		app.setDescription(description);
		app.setStatus("关闭");
		app.setLogoFile(envConsts.DEFAULT_IMAGE);
		app.setClientId(UUIDUtil.generate24UUID());
		app.setClientSecret(UUIDUtil.generate32UUID());
		app.setCreateDate(new Date());
		app.setUpdateDate(new Date());

		appService.saveApp(app, selectedAbilityList);

		logger.debug("应用={}, 创建成功", app.getName());
		return ResponseResult.success("创建成功");
	}

	/**
	 * 查询应用列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ResponseResult listApp(@RequestBody Map<String, Object> params, HttpSession session) {

		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

		Integer pageNow = (Integer) params.get("pageNow");
		Integer pageSize = (Integer) params.get("pageSize");

		String name = (String) params.get("name");
		String status = (String) params.get("status");
		Date updateDate = TimeUtil.parseDate((String) params.get("updateDate"));
		Integer developerId = identity.getId();

		List<App> list = appService.listApp(pageNow, pageSize, developerId, name, status, updateDate);
		PageResult pageResult = new PageResult(new PageInfo<>(list));

		logger.debug("查询应用列表成功, {}, {}, {}", name, status, updateDate);
		return ResponseResult.success("查询成功", pageResult);
	}

	/**
	 * 删除应用
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}" , method = RequestMethod.DELETE)
	public ResponseResult deleteApp(@PathVariable Integer appId) {

		if (appService.deleteApp(appId) == ResponseConsts.CRUD_ERROR) {
			logger.debug("删除{}应用失败", appId);
			return ResponseResult.error("删除失败");
		}

		logger.debug("删除{}应用成功", appId);
		return ResponseResult.success("删除成功");
	}

	/**
	 * 获取单个应用
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}" , method = RequestMethod.GET)
	public ResponseResult getApp(@PathVariable Integer appId) {

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("获取应用{}失败", appId);
			return ResponseResult.error("获取失败");
		}

		app.setLogoFile("/app_logo/" + app.getLogoFile());

		logger.debug("获取应用{}成功", appId);
		return ResponseResult.success("获取成功", app);
	}

	/**
	 * 获取应用旗下的所有能力
	 * 会列出平台的所有能力，只是会以abilityId是否为null标记该应用是否勾选了该能力
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}/ability" , method = RequestMethod.GET)
	public ResponseResult listAbilityUnderApp(@PathVariable Integer appId) {

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("获取应用{}失败", appId);
			return ResponseResult.error("获取失败");
		}

		List<AbilityUnderApp> abilityUnderAppList = appService.listAbilityUnderApp(appId);

		logger.debug("获取应用{}旗下的能力列表成功", appId);
		return ResponseResult.success("获取成功", abilityUnderAppList);
	}

	/**
	 * 上传应用LOGO
	 *
	 * @param file
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "logo", method = RequestMethod.POST)
	public ResponseResult uploadLogo(@RequestParam("file") MultipartFile file, Integer id) {

		App app = appService.queryById(id);
		if (app == null) {
			return ResponseResult.error("不存在该应用");
		}

		String fileName;
		if (!file.isEmpty()) {

			fileName = id + "." + FileUtil.getExtensionName(file.getOriginalFilename());
			try {
				Streams.copy(file.getInputStream(), new FileOutputStream(envConsts.FILE_PATH + "app_logo/" +
						fileName), true);
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseResult.error("上传失败");
			}

			app.setLogoFile(fileName);
			app.setUpdateDate(new Date());
			appService.update(app);
		} else {
			return ResponseResult.error("上传失败");
		}

		return ResponseResult.success("上传成功", "/logo/" + fileName);
	}

	/**
	 * 更新应用状态
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}/status" , method = RequestMethod.PUT)
	public ResponseResult updateAppStatus(@PathVariable Integer appId, @RequestBody Map<String, Object> params) {

		String status = (String) params.get("status");

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("应用{}更新失败，不存在该应用", appId);
			return ResponseResult.error("更新失败，不存在该应用");
		}

		app.setStatus(status);
		app.setUpdateDate(new Date());
		if (appService.update(app) == ResponseConsts.CRUD_ERROR) {
			logger.debug("应用{}更新失败", appId);
			return ResponseResult.error("更新失败");
		}

		logger.debug("应用{}更新成功", appId);
		return ResponseResult.success("更新成功");
	}

	/**
	 * 申请能力
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}/ability/{abilityId}" , method = RequestMethod.POST)
	public ResponseResult occupyAbility(@PathVariable Integer appId, @PathVariable Integer abilityId) {

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("为应用{}申请能力失败，不存在该应用", appId);
			return ResponseResult.error("申请失败，不存在该应用");
		}

		Ability ability = abilityService.queryById(abilityId);
		if (ability == null) {
			logger.debug("为应用{}申请能力{}失败，不存在该能力", appId, abilityId);
			return ResponseResult.error("申请失败，不存在该能力");
		}

		AppAbility appAbility = new AppAbility();
		appAbility.setAppId(appId);
		appAbility.setAbilityId(abilityId);
		if (appService.getAbilityUnderApp(appAbility) != null) {
			logger.debug("为应用{}申请能力{}失败，因为已存在记录", appId, abilityId);
			return ResponseResult.error("申请失败，该能力已被申请过");
		}

		appAbility.setStatus("允许调用");
		appAbility.setInvokeLimit(ability.getInvokeLimit());
		appAbility.setQpsLimit(ability.getQpsLimit());
		appService.saveAbilityUnderApp(appAbility, ability.getEnName());

		logger.debug("为应用{}申请能力{}更新成功", appId, abilityId);
		return ResponseResult.success("申请成功");
	}

	/**
	 * 注销能力
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}/ability/{abilityId}" , method = RequestMethod.DELETE)
	public ResponseResult cancelAbility(@PathVariable Integer appId, @PathVariable Integer abilityId) {

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("为应用{}注销能力失败，不存在该应用", appId);
			return ResponseResult.error("申请失败，不存在该应用");
		}

		Ability ability = abilityService.queryById(abilityId);
		if (ability == null) {
			logger.debug("为应用{}注销能力{}失败，不存在该能力", appId, abilityId);
			return ResponseResult.error("申请失败，不存在该能力");
		}

		AppAbility appAbility = new AppAbility();
		appAbility.setAppId(appId);
		appAbility.setAbilityId(abilityId);
		appAbility = appService.getAbilityUnderApp(appAbility);
		if (appAbility == null) {
			logger.debug("为应用{}注销能力{}成功，因为本身就不存在该记录", appId, abilityId);
			return ResponseResult.success("注销成功");
		}

		appService.deleteAbilityUnderApp(appAbility, ability.getEnName());

		logger.debug("为应用{}注销能力{}更新成功", appId, abilityId);
		return ResponseResult.success("注销成功");
	}

	/**
	 * 提升能力配额
	 * @param appId
	 * @return
	 */
	@RequestMapping(value = "{appId}/ability/{abilityId}" , method = RequestMethod.PUT)
	public ResponseResult updateAbilityLimit(@PathVariable Integer appId, @PathVariable Integer abilityId, @RequestBody Map<String, Object> params) {

		Integer invokeLimit = (Integer) params.get("invokeLimit");
		Integer qpsLimit = (Integer) params.get("qpsLimit");

		// 校验数据
		if (Validator.checkNull(invokeLimit) || Validator.checkNull(qpsLimit)) {
			return ResponseResult.error("提升能力失败，信息不完整");
		}

		App app = appService.queryById(appId);
		if (app == null) {
			logger.debug("为应用{}提升能力配额失败，不存在该应用", appId);
			return ResponseResult.error("提升配额失败，不存在该应用");
		}

		Ability ability = abilityService.queryById(abilityId);
		if (ability == null) {
			logger.debug("为应用{}提升能力{}配额失败，不存在该能力", appId, abilityId);
			return ResponseResult.error("提升配额失败，不存在该能力");
		}

		AppAbility appAbility = new AppAbility();
		appAbility.setAppId(appId);
		appAbility.setAbilityId(abilityId);
		appAbility = appService.getAbilityUnderApp(appAbility);
		if (appAbility == null) {
			logger.debug("为应用{}提升能力{}配额失败，因为不存在该记录", appId, abilityId);
			return ResponseResult.error("提升配额失败，不存在该记录");
		}

		appAbility.setInvokeLimit(invokeLimit);
		appAbility.setQpsLimit(qpsLimit);
		appService.updateAbilityUnderApp(appAbility);

		logger.debug("为应用{}提升能力{}配额成功", appId, abilityId);
		return ResponseResult.success("提升配额成功");
	}

}
