package org.bupt.aiop.mis.mapper;

import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.vo.AbilityUnderApp;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AbilityMapper extends Mapper<Ability> {
	List<AbilityUnderApp> selectAbilityUnderApp(Integer appId);
}