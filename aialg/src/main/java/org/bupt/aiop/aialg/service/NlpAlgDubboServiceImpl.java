package org.bupt.aiop.aialg.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.classification.classifiers.NaiveBayesClassifier;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.bupt.aiop.aialg.bean.*;
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.common.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@org.springframework.stereotype.Service
public class NlpAlgDubboServiceImpl implements NlpAlgDubboService {

    @Autowired
    private CommonFormatter commonFormatter;


    @Autowired
    private WordVectorModel wordVectorModel; //词向量

    @Autowired
    private NaiveBayesClassifier motionClassifierModel; //情感分类

    @Autowired
    private NaiveBayesClassifier categoryClassifierModel; //文本分类


    /**
     * 提取给定文本的关键词
     *
     * @param text 提取关键词的文本
     * @param size 要提取的关键词的个数
     * @return
     */
    @Override
    public String text_keywords(String text, Integer size) {
        List<String> keywordList = HanLP.extractKeyword(text, size);
        return commonFormatter.extraction(text, keywordList);
    }

    /**
     * 获取摘要
     *
     * @param text
     * @param size 摘要个数
     * @return
     */
    @Override
    public String text_summaries(String text, Integer size) {
        List<String> summaries = HanLP.extractSummary(text, size);
        return commonFormatter.extraction(text, summaries);
    }


    /**
     * 获取句子短语
     *
     * @param text
     * @param size 短语个数
     * @return
     */
    @Override
    public String text_phrases(String text, Integer size) {
        List<String> phrases = HanLP.extractPhrase(text, size);
        return commonFormatter.extraction(text, phrases);
    }

    /**
     * 默认不开启日本人名，地名和机构名识别
     *
     * @param text 被分词的句子
     * @return
     */
    @Override
    public String word_pos(String text) {
        return word_pos(text, false, false, false);
    }

    /**
     * 基础分词接口
     *
     * @param text      被分词的句子
     * @param japName   是否开启日本人名识别
     * @param placeName 是否开启地名识别
     * @param orgName   是否开启机构名识别
     * @return
     */
    @Override
    public String word_pos(String text, Boolean japName, Boolean placeName, Boolean orgName) {
        Segment segment = HanLP.newSegment()
                .enableJapaneseNameRecognize(japName)
                .enablePlaceRecognize(placeName)
                .enableOrganizationRecognize(orgName);

        List<Term> segments = segment.seg(text);
        Map<String, Object> result = new HashMap<>();
        List<WordPos> items = new ArrayList<>();
        int offset = 0;
        for (Term term : segments) {
            WordPos item = new WordPos();
            item.setPos(term.word);
            item.setByteOffset(offset);
            item.setByteLen(term.length());
            item.setNature(term.nature);
            items.add(item);
            offset += term.length();
        }
        result.put("text", text);
        result.put("items", items);
        return JSON.toJSONString(result);
    }

    /**
     * 词向量接口
     *
     * @param text
     * @return
     */
    @Override
    public String word_2_vec(String text) {
        Vector vector = wordVectorModel.vector(text);
        WordVector result = new WordVector();
        result.setWord(text);
        result.setVector(ReflectUtil.getObjectByFieldName(vector, "elementArray", float[].class));
        return JSON.toJSONString(result);
    }

    /**
     * 汉字转换为拼音
     *
     * @param text
     * @return
     */
    @Override
    public String word_2_pinyin(String text) {
        List<Pinyin> pinyins = HanLP.convertToPinyinList(text);
        Map<String, Object> result = new HashMap<>();
        List<LocalPinyin> items = new ArrayList<>();
        for (int i = 0; i < pinyins.size(); ++i) {
            Pinyin pinyin = pinyins.get(i);
            LocalPinyin localPinyin = new LocalPinyin();
            localPinyin.setPinyin(pinyin.getPinyinWithoutTone());
            localPinyin.setWord(text.charAt(i));
            localPinyin.setTone(pinyin.getTone());
            localPinyin.setShenMu(pinyin.getShengmu().toString());
            localPinyin.setYunMu(pinyin.getYunmu().toString());
            items.add(localPinyin);
        }
        result.put("text", text);
        result.put("items", items);
        return JSON.toJSONString(result);
    }

    @Override
    public String simplified_2_traditional(String text) {
        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("converted", HanLP.convertToTraditionalChinese(text));
        return JSON.toJSONString(result);
    }

    @Override
    public String traditional_2_simplified(String text) {
        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("converted", HanLP.convertToSimplifiedChinese(text));
        return JSON.toJSONString(result);
    }

    @Override
    public String word_sim(String text1, String text2) {
        Map<String, Object> result = new HashMap<>();
        result.put("text1", text1);
        result.put("text2", text2);
        result.put("score", wordVectorModel.similarity(text1, text2));
        return JSON.toJSONString(result);
    }

    @Override
    public String document_sim(String doc1, String doc2) {
        Map<String, Object> result = new HashMap<>();
        result.put("doc1", doc1);
        result.put("doc2", doc2);
        result.put("score", wordVectorModel.similarity(doc1, doc2));
        return JSON.toJSONString(result);
    }

    @Override
    public String nearest_words(String text, Integer size) {
        List<Map.Entry<String, Float>> nearests = wordVectorModel.nearest(text, size);
        Map<String, Object> result = new HashMap<>();
        List<NearestWord> items = new ArrayList<>();
        for (Map.Entry<String, Float> nearest : nearests) {
            NearestWord nearestWord = new NearestWord();
            nearestWord.setScore(nearest.getValue());
            nearestWord.setWord(nearest.getKey());
            items.add(nearestWord);
        }
        result.put("text", text);
        result.put("items", items);
        return JSON.toJSONString(result);
    }

    @Override
    public String motion_classify(String text) {
        Map<String, Double> pred_result = motionClassifierModel.predict(text);
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        if (pred_result.get("正面") > pred_result.get("负面")) {
            result.put("sentiment", 1);
        } else {
            result.put("sentiment", 2);
        }
        result.put("positive_prob", pred_result.get("正面"));
        result.put("negative_prob", pred_result.get("负面"));
        return JSON.toJSONString(result);
    }

    @Override
    public String category_classify(String text) {
        Map<String, Double> pred_result = categoryClassifierModel.predict(text);
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        double maxScore = Double.MIN_VALUE;
        String category = null;
        for (Map.Entry<String, Double> item : pred_result.entrySet()) {
            result.put(item.getKey(), item.getValue());
            if (item.getValue() > maxScore) {
                category = item.getKey();
                maxScore = item.getValue();
            }
        }
        result.put("category", category);
        return JSON.toJSONString(result);
    }
}
