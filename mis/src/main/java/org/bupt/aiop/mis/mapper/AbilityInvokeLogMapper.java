package org.bupt.aiop.mis.mapper;

import org.bupt.aiop.mis.pojo.po.AbilityInvokeLog;
import org.bupt.aiop.mis.pojo.vo.AbilityInvokeLogRanking;
import org.bupt.aiop.mis.pojo.vo.AbilityInvokeLogStatistic;
import org.bupt.aiop.mis.pojo.vo.MyAbility;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AbilityInvokeLogMapper extends Mapper<AbilityInvokeLog> {
	List<AbilityInvokeLogStatistic> selectStatistic(Map<String, Object> filters);
	List<MyAbility> selectMyAbilities(Map<String, Object> filters);
	Integer selectTotalCount(Integer developerId);
	List<AbilityInvokeLogRanking> selectRankings();
}