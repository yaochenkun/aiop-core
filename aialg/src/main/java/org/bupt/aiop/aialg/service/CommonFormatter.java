package org.bupt.aiop.aialg.service;

import com.alibaba.fastjson.JSON;
import org.bupt.aiop.aialg.bean.Keyword;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用格式器
 */
@Component
public class CommonFormatter {

    /**
     * {
     *     text: ""
     *     items: []
     * }
     * @param text
     * @param terms
     * @return
     */
    public String extraction(String text, List<String> terms) {
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        List<Keyword> beanList = new ArrayList<>();
        for (String term : terms) {
            Keyword bean = new Keyword();
            bean.setItem(term);
            bean.setIndex(text.indexOf(term));
            beanList.add(bean);
        }
        result.put("items", beanList);
        return JSON.toJSONString(result);
    }
}
