package org.bupt.aiop.mis.controller;

import com.github.pagehelper.PageInfo;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.vo.AbilityInvokeLogRanking;
import org.bupt.aiop.mis.pojo.vo.AbilityInvokeLogStatistic;
import org.bupt.aiop.mis.pojo.vo.AbilityWithModel;
import org.bupt.aiop.mis.service.AbilityInvokeLogService;
import org.bupt.aiop.mis.service.AbilityService;
import org.bupt.aiop.mis.service.ModelService;
import org.bupt.common.bean.PageResult;
import org.bupt.common.bean.ResponseResult;
import org.bupt.common.constant.OauthConsts;
import org.bupt.common.constant.ResponseConsts;
import org.bupt.common.util.TimeUtil;
import org.bupt.common.util.Validator;
import org.bupt.common.util.token.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private AbilityInvokeLogService abilityInvokeLogService;

	@Autowired
	private ModelService modelService;

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

		String zhName = (String) params.get("zhName");
		String enName = (String) params.get("enName");
		String description = (String) params.get("description");
		String restapiUrl = (String) params.get("restapiUrl");
		String docUrl = (String) params.get("docUrl");
		String type = (String) params.get("type");
		Integer modelId = (Integer) params.get("modelId");
		String version = (String) params.get("version");
		Integer invokeLimit = (Integer) params.get("invokeLimit");
		Integer qpsLimit = (Integer) params.get("qpsLimit");

		// 校验数据
		if (Validator.checkEmpty(zhName)
				|| Validator.checkEmpty(enName)
				|| Validator.checkEmpty(restapiUrl)
				|| Validator.checkEmpty(docUrl)
				|| Validator.checkEmpty(type)
				|| Validator.checkNull(modelId)
				|| Validator.checkEmpty(version)
				|| Validator.checkEmpty(description)
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
		ability.setModelId("基础算法".equals(type) ? null : modelId);
		ability.setVersion(version);
		ability.setDescription(description);
		ability.setInvokeLimit(invokeLimit);
		ability.setQpsLimit(qpsLimit);
		ability.setCreateDate(new Date());
		ability.setUpdateDate(new Date());

		abilityService.save(ability);

		logger.debug("能力={}, 新增成功", ability.getZhName());
		return ResponseResult.success("新增成功");
	}

	/**
	 * 查询能力列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ResponseResult listAbility(@RequestBody Map<String, Object> params) {

		Integer pageNow = (Integer) params.get("pageNow");
		Integer pageSize = (Integer) params.get("pageSize");

		String zhName = (String) params.get("zhName");
		String enName = (String) params.get("enName");
		String type = (String) params.get("type");
		Date updateDate = TimeUtil.parseDate((String) params.get("updateDate"));

		List<Ability> list = abilityService.listAbility(pageNow, pageSize, zhName, enName, type, updateDate);
		PageResult pageResult = new PageResult(new PageInfo<>(list));

		logger.debug("查询能力列表成功, {}, {}, {}, {}", zhName, enName, type, updateDate);
		return ResponseResult.success("查询成功", pageResult);
	}

	/**
	 * 查询所有能力列表
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ResponseResult listAllAbility() {

		List<Ability> list = abilityService.queryAll();

		logger.debug("查询所有能力列表成功");
		return ResponseResult.success("查询成功", list);
	}

	/**
	 * 删除能力
	 * @param abilityId
	 * @return
	 */
	@RequestMapping(value = "{abilityId}" , method = RequestMethod.DELETE)
	public ResponseResult deleteAbility(@PathVariable Integer abilityId) {

		if (abilityService.deleteAbility(abilityId) == ResponseConsts.CRUD_ERROR) {
			logger.debug("删除{}能力失败", abilityId);
			return ResponseResult.error("删除失败");
		}

		logger.debug("删除{}能力成功", abilityId);
		return ResponseResult.success("删除成功");
	}

	/**
	 * 获取单个能力
	 * @param abilityId
	 * @return
	 */
	@RequestMapping(value = "{abilityId}" , method = RequestMethod.GET)
	public ResponseResult getAbility(@PathVariable Integer abilityId) {

		Ability ability = abilityService.queryById(abilityId);
		if (ability == null) {
			logger.debug("获取能力{}失败", abilityId);
			return ResponseResult.error("获取失败");
		}

		// 查询ability对应的model, 方便前端显示
		AbilityWithModel abilityWithModel = new AbilityWithModel(ability);
		abilityWithModel.setModel(modelService.queryById(ability.getModelId()));

		logger.debug("获取能力{}成功", abilityId);
		return ResponseResult.success("获取成功", abilityWithModel);
	}

	/**
	 * 更新能力状态
	 * @param abilityId
	 * @return
	 */
	@RequestMapping(value = "{abilityId}" , method = RequestMethod.PUT)
	public ResponseResult updateAbility(@PathVariable Integer abilityId, @RequestBody Map<String, Object> params) {

		String restapiUrl = (String) params.get("restapiUrl");
		String docUrl = (String) params.get("docUrl");
		String type = (String) params.get("type");
		Integer modelId = (Integer) params.get("modelId");
		String version = (String) params.get("version");
		Integer invokeLimit = (Integer) params.get("invokeLimit");
		Integer qpsLimit = (Integer) params.get("qpsLimit");

		// 校验数据
		if (Validator.checkEmpty(restapiUrl)
				|| Validator.checkEmpty(docUrl)
				|| Validator.checkEmpty(type)
				|| Validator.checkNull(modelId)
				|| Validator.checkEmpty(version)
				|| Validator.checkNull(invokeLimit)
				|| Validator.checkNull(qpsLimit)) {
			return ResponseResult.error("更新失败，信息不完整");
		}


		Ability ability = abilityService.queryById(abilityId);
		if (ability == null) {
			logger.debug("能力{}更新失败，不存在该能力", ability);
			return ResponseResult.error("更新失败，不存在该能力");
		}

		ability.setRestapiUrl(restapiUrl);
		ability.setDocUrl(docUrl);
		ability.setType(type);
		ability.setModelId("基础算法".equals(type) ? null : modelId);
		ability.setVersion(version);
		ability.setInvokeLimit(invokeLimit);
		ability.setQpsLimit(qpsLimit);
		ability.setUpdateDate(new Date());

		abilityService.update(ability);

		logger.debug("能力{}更新成功", abilityId);
		return ResponseResult.success("更新成功");
	}

	/**
	 * 查询能力调用量的统计信息
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "invoke_log/list", method = RequestMethod.POST)
	public ResponseResult listAbilityInvokeLogStatistic(@RequestBody Map<String, Object> params, HttpSession session) {

		// 获取开发者ID
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

		params.put("developerId", identity.getId()); // 方便查找developerId旗下所有应用使用
		List<AbilityInvokeLogStatistic> abilityInvokeLogStatisticList = abilityInvokeLogService.listAbilityInvokeLogStatistic(params);

		logger.debug("查询能力调用量的统计信息成功");
		return ResponseResult.success("查询成功", abilityInvokeLogStatisticList);
	}

	/**
	 * 查询能力调用总量
	 *
	 * @return
	 */
	@RequestMapping(value = "invoke_log/count", method = RequestMethod.GET)
	public ResponseResult countAbilityInvokeLogTotal(HttpSession session) {

		// 获取开发者ID
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
		Integer totalCount = abilityInvokeLogService.countAbilityInvokeLogTotal(identity.getId());

		logger.debug("查询能力调用总量成功");
		return ResponseResult.success("查询成功", totalCount);
	}

	/**
	 * 查询能力调用量列表中的排名(index)
	 *
	 * @return
	 */
	@RequestMapping(value = "invoke_log/ranking/index", method = RequestMethod.GET)
	public ResponseResult indexAbilityInvokeLogRanking(HttpSession session) {

		// 获取开发者ID
		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);
		Integer developerId = identity.getId();

		List<AbilityInvokeLogRanking> rankingList = abilityInvokeLogService.listAbilityInvokeLogRanking();
		Integer ranking = 0;
		for (AbilityInvokeLogRanking abilityInvokeLogRanking : rankingList) {
			ranking++;
			if (abilityInvokeLogRanking.getDeveloperId() == developerId) {
				break;
			}
		}

		logger.debug("查询能力调用总量排名成功");
		return ResponseResult.success("查询成功", ranking);
	}

}
