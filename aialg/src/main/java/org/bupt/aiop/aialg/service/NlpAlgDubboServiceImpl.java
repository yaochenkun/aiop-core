package org.bupt.aiop.aialg.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.bupt.aiop.aialg.Utils.ReflectUtils;
import org.bupt.aiop.aialg.bean.Keyword;
import org.bupt.aiop.aialg.bean.WordVector;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@org.springframework.stereotype.Service
public class NlpAlgDubboServiceImpl implements NlpAlgDubboService {

    @Autowired
    private WordVectorModel wordVectorModel;

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

	@Override
	public String word_pos(String text) {
		return null;
	}

	@Override
    public String word_2_vec(String text) {
        Vector vector = wordVectorModel.vector(text);
        WordVector result = new WordVector();
        result.setWord(text);
        result.setElementArray(ReflectUtils.getObjectByFieldName(vector, "elementArray", float[].class));
        return JSON.toJSONString(result);
    }


}
