package org.bupt.aiop.aialg.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import org.bupt.aiop.aialg.bean.*;
import org.bupt.aiop.aialg.util.CommonFormatter;
import org.bupt.aiop.aialg.util.ReflectUtil;
import org.bupt.aiop.rpcapi.dubbo.ImageAlgDubboService;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ImageAlgDubboServiceImpl implements ImageAlgDubboService {


    @Override
    public String face_recognition(String text) {
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        return JSON.toJSONString(result);
    }
}
