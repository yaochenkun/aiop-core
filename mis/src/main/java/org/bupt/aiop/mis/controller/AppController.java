package org.bupt.aiop.mis.controller;

import com.github.pagehelper.PageInfo;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.App;
import org.bupt.aiop.mis.service.AppService;
import org.bupt.common.bean.PageResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.util.TimeUtil;
import org.bupt.common.util.UUIDUtil;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

		// 获取开发者勾选的能力
		List<String> abilityList = new ArrayList<>();
		abilityList.add((Boolean) params.get("word_seg") == true ? "word_seg" : "");
		abilityList.add((Boolean) params.get("word_pos") == true ? "word_pos" : "");
		abilityList.add((Boolean) params.get("word_ner") == true ? "word_ner" : "");
		abilityList.add((Boolean) params.get("dependency_parse") == true ? "dependency_parse" : "");
		abilityList.add((Boolean) params.get("text_keywords") == true ? "text_keywords" : "");
		abilityList.add((Boolean) params.get("text_summaries") == true ? "text_summaries" : "");
		abilityList.add((Boolean) params.get("text_phrases") == true ? "text_phrases" : "");
		abilityList.add((Boolean) params.get("word_2_pinyin") == true ? "word_2_pinyin" : "");
		abilityList.add((Boolean) params.get("simplified_2_traditional") == true ? "simplified_2_traditional" : "");
		abilityList.add((Boolean) params.get("traditional_2_simplified") == true ? "traditional_2_simplified" : "");
		abilityList.add((Boolean) params.get("word_2_vec") == true ? "word_2_vec" : "");
		abilityList.add((Boolean) params.get("word_sim") == true ? "word_sim" : "");
		abilityList.add((Boolean) params.get("doc_sim") == true ? "doc_sim" : "");
		abilityList.add((Boolean) params.get("nearest_words") == true ? "nearest_words" : "");
		abilityList.add((Boolean) params.get("motion_classify") == true ? "motion_classify" : "");
		abilityList.add((Boolean) params.get("category_classify") == true ? "category_classify" : "");

		// 拼接权限字符串
		StringBuilder sb = new StringBuilder();
		for (String ability : abilityList) {
			if (!"".equals(ability)) {
				sb.append(",");
				sb.append(ability);
			}
		}

		String abilityScope = sb.toString();
		abilityScope = "".equals(abilityScope) ? "none" : abilityScope.substring(1);

		// 校验数据
		if (Validator.checkEmpty(name) || Validator.checkEmpty(type) || Validator.checkEmpty(platform) || Validator.checkEmpty(description)) {
			return ResponseResult.error("添加失败，信息不完整");
		}

		App app = new App();
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

		appService.saveApp(app);

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

}
