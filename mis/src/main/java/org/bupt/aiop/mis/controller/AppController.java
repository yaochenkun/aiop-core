package org.bupt.aiop.mis.controller;

import org.bupt.aiop.mis.annotation.RequiredRoles;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.UUIDUtil;
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
 * 应用控制器
 */
@RestController
@RequestMapping("api/app")
public class AppController {

	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private AppService appService;

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
		String abilityScope = (String) params.get("abilityScope"); //TODO: 将前端获取到的不同权限组装成,分隔的字符串
		String description = (String) params.get("description");

		if (Validator.checkEmpty(name) || Validator.checkEmpty(type) || Validator.checkEmpty(platform)
				|| Validator.checkEmpty(abilityScope)) {
			return ResponseResult.error("添加失败，信息不完整");
		}

		App app = new App();
		app.setDeveloperId(identity.getId());
		app.setName(name);
		app.setType(type);
		app.setPlatform(platform);
		app.setAbilityScope(abilityScope);
		app.setDescription(description);
		app.setClientId(UUIDUtil.generate24UUID());
		app.setClientSecret(UUIDUtil.generate32UUID());
		app.setCreateTime(new Date());
		app.setUpdateTime(new Date());

		appService.saveApp(app);

		logger.debug("应用={}, 添加成功", app.getName());
		return ResponseResult.success("添加成功");
	}
}
