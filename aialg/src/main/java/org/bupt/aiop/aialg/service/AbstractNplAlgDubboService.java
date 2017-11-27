package org.bupt.aiop.aialg.service;

import com.alibaba.fastjson.JSON;
import org.bupt.aiop.aialg.bean.Keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractNplAlgDubboService {

    protected String extractCommon(String text, List<String> terms) {
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
