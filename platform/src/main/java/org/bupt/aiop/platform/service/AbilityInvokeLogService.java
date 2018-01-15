package org.bupt.aiop.platform.service;

import org.bupt.aiop.platform.mapper.AbilityInvokeLogMapper;
import org.bupt.aiop.platform.pojo.po.AbilityInvokeLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 能力调用日志服务类
 * Created by ken on 2017/11/1.
 */
@Service
public class AbilityInvokeLogService extends BaseService<AbilityInvokeLog> {

	private static final Logger logger = LoggerFactory.getLogger(AbilityInvokeLogService.class);

	@Autowired
	private AbilityInvokeLogMapper abilityInvokeLogMapper;
}
