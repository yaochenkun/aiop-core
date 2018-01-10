package org.bupt.aiop.aialg.util;

import com.alibaba.fastjson.JSON;
import org.bupt.aiop.aialg.bean.ExtractEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用格式器
 */
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
    public static String extraction(String text, List<String> terms) {
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        List<ExtractEntity> beanList = new ArrayList<>();
        int lastIndex = 0;
        for (String term : terms) {
            ExtractEntity bean = new ExtractEntity();
            bean.setItem(term);
            lastIndex = text.indexOf(term, lastIndex);
            bean.setIndex(lastIndex);
            beanList.add(bean);
        }
        result.put("items", beanList);
        return JSON.toJSONString(result);
    }
}
