package org.bupt.aiop.aialg.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import org.bupt.aiop.aialg.bean.Keyword;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class NlpAlgDubboServiceImpl implements NlpAlgDubboService {

	/**
	 * 提取text的关键词size个
	 *
	 * @param text
	 * @param size
	 * @return
	 */
	@Override
	public String text_keywords(String text, Integer size) {

		//关键字提取
		List<String> keywordList = HanLP.extractKeyword(text, size);

		//构建返回集
		Map<String, Object> result = new HashMap<>();
		result.put("text", text);
		List<Keyword> beanList = new ArrayList<>();
		for (String keyword : keywordList) {
			Keyword bean = new Keyword();
			bean.setItem(keyword);
			bean.setIndex(text.indexOf(keyword));
			beanList.add(bean);
		}
		result.put("items", beanList);

		return JSON.toJSONString(result);
	}
}
