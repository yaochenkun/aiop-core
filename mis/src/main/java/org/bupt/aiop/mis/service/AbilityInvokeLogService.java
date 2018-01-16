package org.bupt.aiop.mis.service;

import com.github.pagehelper.PageHelper;
import org.bupt.aiop.mis.mapper.AbilityInvokeLogMapper;
import org.bupt.aiop.mis.pojo.po.AbilityInvokeLog;
import org.bupt.aiop.mis.pojo.vo.AbilityInvokeLogStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 能力调用量服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class AbilityInvokeLogService extends BaseService<AbilityInvokeLog> {

	private static final Logger logger = LoggerFactory.getLogger(AbilityInvokeLogService.class);

	@Autowired
	private AbilityInvokeLogMapper abilityInvokeLogMapper;

	/**
	 * 查询能力调用量（分页）
	 * @param filters
	 * @return
	 */
	public List<AbilityInvokeLogStatistic> listAbilityInvokeLogStatistic(Map<String, Object> filters) {
		return abilityInvokeLogMapper.selectStatistic(filters);
	}
}
