package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.constant.EnvConsts;
import org.bupt.aiop.mis.mapper.AbilityMapper;
import org.bupt.aiop.mis.mapper.ModelMapper;
import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.Model;
import org.bupt.common.constant.ResponseConsts;
import org.bupt.common.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * 算法模型服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class ModelService extends BaseService<Model> {

	private static final Logger logger = LoggerFactory.getLogger(ModelService.class);

	@Autowired
	private StringRedisTemplate redisMapper;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EnvConsts envConsts;

	public List<Model> listModel(Integer pageNow, Integer pageSize, String name, String file, Date updateDate) {

		Example example = new Example(Model.class);
		Example.Criteria criteria = example.createCriteria();

		if (!Validator.checkEmpty(name)) criteria.andLike("name", "%" + name + "%");
		if (!Validator.checkEmpty(file)) criteria.andLike("file", "%" + file + "%");
		if (!Validator.checkNull(updateDate)) criteria.andEqualTo("updateDate", updateDate);

  		PageHelper.startPage(pageNow, pageSize);
		return this.getMapper().selectByExample(example);
	}

	/**
	 * 删除模型文件
	 * @param fileName
	 * @return
	 */
	public Integer deleteModelFile(String fileName) {

		//删除模型文件
		String absolutePath = envConsts.FILE_PATH + "model/" + fileName;
		File file = new File(absolutePath);
		if(!file.exists() || !file.isFile()) {
			logger.debug("文件删除成功，因为文件不存在或不是文件");
			return ResponseConsts.CRUD_SUCCESS;
		}

		file.delete();
		logger.debug("文件删除成功");
		return ResponseConsts.CRUD_SUCCESS;
	}
}
