package org.bupt.common.util;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ElasticSearch工具类
 * Created by ken on 2017/6/9.
 */
public class ESUtil {

	/**
	 * 将命中的原始记录+高亮记录合并
	 * 进行格式化处理后，以列表返回
	 * @param hits 命中的ES原始记录
	 * @return
	 */
	public static List<Map<String, Object>> unionWithHighlights(SearchHits hits) {

		List<Map<String, Object>> resList = new ArrayList<>();
		for (SearchHit sh : hits) {
			// 合并高亮结果，若highlightFieldsMap中无无则使用source中的原始数据
			Map<String, Object> sourceFieldsMap = sh.getSourceAsMap(); // 合并到此Map
			Map<String, HighlightField> highlightFieldsMap = sh.getHighlightFields();

			// 开始合并
			for (Map.Entry<String, HighlightField> entry : highlightFieldsMap.entrySet()) {
				HighlightField highlightField = entry.getValue();
				if (!Validator.checkNull(highlightField)) {
					StringBuilder sb = new StringBuilder();
					for (Text text : highlightField.getFragments()) {
						sb.append(text.string());
						sb.append(" ");
					}
					sourceFieldsMap.put(entry.getKey(), sb.toString());
				}
			}

			// 添加
			resList.add(sourceFieldsMap);
		}

		return resList;
	}
}
