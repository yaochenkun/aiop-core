package org.bupt.aiop.mis.controller;

import com.github.pagehelper.PageInfo;
import org.apache.commons.fileupload.util.Streams;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.Model;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 算法模型控制器
 */
@RestController
@RequestMapping("api/model")
public class ModelController {

	private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private EnvConsts envConsts;

	/**
	 * 添加模型
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseResult addModel(@RequestBody Map<String, Object> params, HttpSession session) {

		Identity identity = (Identity) session.getAttribute(OauthConsts.KEY_IDENTITY);

		String name = (String) params.get("name");
		String file = (String) params.get("file");

		// 校验数据
		if (Validator.checkEmpty(name) || Validator.checkEmpty(file)) {
			return ResponseResult.error("添加失败，信息不完整");
		}

		// 模型名称不能一样
		Model model = new Model();
		model.setName(name);
		if (modelService.queryOne(model) != null) {
			logger.debug("模型名称={}与已有冲突", name);
			return ResponseResult.error("模型名称重复");
		}

		// 模型路径不能一样
		model.setName(null);
		model.setFile(file);
		if (modelService.queryOne(model) != null) {
			logger.debug("模型文件名={}与已有冲突", name);
			return ResponseResult.error("模型文件名重复");
		}

		model.setName(name);
		model.setFile(file);
		model.setCreateDate(new Date());
		model.setUpdateDate(new Date());
		modelService.save(model);

		logger.debug("模型={}, 新增成功", model.getName());
		return ResponseResult.success("新增成功");
	}

	/**
	 * 查询模型列表
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public ResponseResult listModel(@RequestBody Map<String, Object> params) {

		Integer pageNow = (Integer) params.get("pageNow");
		Integer pageSize = (Integer) params.get("pageSize");

		String name = (String) params.get("name");
		String file = (String) params.get("file");
		Date updateDate = TimeUtil.parseDate((String) params.get("updateDate"));

		List<Model> list = modelService.listModel(pageNow, pageSize, name, file, updateDate);
		PageResult pageResult = new PageResult(new PageInfo<>(list));

		logger.debug("查询模型列表成功, {}, {}, {}", name, file, updateDate);
		return ResponseResult.success("查询成功", pageResult);
	}

	/**
	 * 查询所有模型列表
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ResponseResult listAllModel() {

		List<Model> list = modelService.queryAll();

		logger.debug("查询所有模型列表成功");
		return ResponseResult.success("查询成功", list);
	}

	/**
	 * 删除模型
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "{modelId}" , method = RequestMethod.DELETE)
	public ResponseResult deleteModel(@PathVariable Integer modelId) {

		Model model = modelService.queryById(modelId);
		if (model == null) {
			logger.debug("删除{}模型失败", modelId);
			return ResponseResult.error("删除失败，该模型记录不存在");
		}

		if (modelService.deleteModel(model) == ResponseConsts.CRUD_ERROR) {
			logger.debug("删除{}模型失败", modelId);
			return ResponseResult.error("删除失败");
		}

		logger.debug("删除{}模型成功", modelId);
		return ResponseResult.success("删除成功");
	}

	/**
	 * 获取单个模型
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "{modelId}" , method = RequestMethod.GET)
	public ResponseResult getModel(@PathVariable Integer modelId) {

		Model model = modelService.queryById(modelId);
		if (model == null) {
			logger.debug("获取模型{}失败", modelId);
			return ResponseResult.error("获取失败");
		}

		logger.debug("获取模型{}成功", modelId);
		return ResponseResult.success("获取成功", model);
	}

	/**
	 * 更新模型
	 * @param modelId
	 * @return
	 */
	@RequestMapping(value = "{modelId}" , method = RequestMethod.PUT)
	public ResponseResult modifyModel(@PathVariable Integer modelId, @RequestBody Map<String, Object> params) {

		String name = (String) params.get("name");
		String file = (String) params.get("file");

		Model model = modelService.queryById(modelId);
		if (model == null) {
			logger.debug("模型{}更新失败，不存在该模型", modelId);
			return ResponseResult.error("更新失败，不存在该模型");
		}

		String oldFileName = model.getFile();

		model.setName(name);
		model.setFile(file);
		model.setUpdateDate(new Date());
		if (modelService.update(model) == ResponseConsts.CRUD_ERROR) {
			logger.debug("模型{}更新失败", modelId);
			return ResponseResult.error("更新失败");
		}

		// 模型文件是否存在
		File oldFile = new File(envConsts.FILE_PATH + "model/" + oldFileName);
		if (!oldFile.exists() || !oldFile.isFile()) {
			logger.debug("模型{}更新成功", modelId);
			return ResponseResult.success("更新成功");
		}

		// 重命名模型文件
		oldFile.renameTo(new File(envConsts.FILE_PATH + "model/" + file));

		logger.debug("模型{}更新成功", modelId);
		return ResponseResult.success("更新成功");
	}

	/**
	 * 上传模型文件
	 *
	 * @param file
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "file", method = RequestMethod.POST)
	public ResponseResult uploadModelFile(@RequestParam("file") MultipartFile file, Integer id) {

		logger.debug("文件大小为{}", file.getSize());
		Model model = modelService.queryById(id);
		if (model == null) {
			return ResponseResult.error("上传失败，该模型记录不存在");
		}

		if (file.isEmpty()) {
			return ResponseResult.error("上传失败，文件为空");
		}

		String modelFileName = model.getFile();
		if (modelFileName == null || "".equals(modelFileName)) {
			return ResponseResult.error("上传失败，请检查模型文件名是否配置正确");
		}

		// 获取模型文件名称
		String absolutePath = envConsts.FILE_PATH + "model/" + modelFileName;
		try {
			Streams.copy(file.getInputStream(), new FileOutputStream(absolutePath), true);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseResult.error("上传失败");
		}

		model.setSize((float) (file.getSize() * 1.0) / (1024 * 1024));
		model.setUpdateDate(new Date());
		modelService.update(model);

		return ResponseResult.success("上传成功");
	}

}
