package org.bupt.aiop.aialg.service;

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
import org.bupt.aiop.rpcapi.dubbo.NlpAlgDubboService;
import org.bupt.common.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class NlpAlgDubboServiceImpl implements NlpAlgDubboService {

    @Autowired
    private CommonFormatter commonFormatter;

    @Autowired
    private WordVectorModel wordVectorModel; //词向量

    @Autowired
    private DocVectorModel docVectorModel; //文本相似度

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
     * 基础词性标注功能，不识别日本人名，地名和机构名
     *
     * @param text 目标文本
     * @return
     */
    public String word_pos_normal(String text) {
        return word_pos(text, false, false, false);
    }

    /**
     * 词性标注，识别日本人名
     *
     * @param text 目标文本
     * @return
     */
    public String word_pos_japanese(String text) {
        return word_pos(text, true, false, false);
    }

    /**
     * 词性标注，识别地名
     *
     * @param text 目标文本
     * @return
     */
    public String word_pos_place(String text) {
        return word_pos(text, false, true, false);
    }

    /**
     * 词性标注，识别机构名
     *
     * @param text 目标文本
     * @return
     */
    public String word_pos_organization(String text) {
        return word_pos(text, false, false, true);
    }

    /**
     * 基础词性标注
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

    /**
     * 简体中文转换为繁体中文
     *
     * @param text 转换内容
     * @return
     */
    @Override
    public String simplified_2_traditional(String text) {
        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("converted", HanLP.convertToTraditionalChinese(text));
        return JSON.toJSONString(result);
    }

    /**
     * 繁体中文转换为简体中文
     *
     * @param text 转换内容
     * @return
     */
    @Override
    public String traditional_2_simplified(String text) {
        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("converted", HanLP.convertToSimplifiedChinese(text));
        return JSON.toJSONString(result);
    }

    /**
     * 文本推荐
     * @param sentences 原始内容集合
     * @param text 目标文本
     * @param size 相似文本数量
     * @return
     */
    @Override
    public String text_suggester(List<String> sentences, String text, Integer size) {
        if (size > sentences.size()) size = sentences.size();
        Suggester suggester = new Suggester();
        for (String sentence : sentences) {
            suggester.addSentence(sentence);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("suggester", suggester.suggest(text, size));
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

    @Override
    public String word_sim(String text1, String text2) {
        Map<String, Object> result = new HashMap<>();
        WordSimEntity simEntity = new WordSimEntity();
        simEntity.setWord_1(text1);
        simEntity.setWord_2(text2);
        result.put("score", wordVectorModel.similarity(text1, text2));
        result.put("words", simEntity);
        return JSON.toJSONString(result);
    }

    @Override
    public String document_sim(String doc1, String doc2) {
        Map<String, Object> result = new HashMap<>();
        DocSimEntity simEntity = new DocSimEntity();
        simEntity.setDoc_1(doc1);
        simEntity.setDoc_2(doc2);
        result.put("score", docVectorModel.similarity(doc1, doc2));
        result.put("docs", simEntity);
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
        Sentiment sentiment = new Sentiment();
        result.put("text", text);
        if (pred_result.get("positive") > pred_result.get("negative")) {
            sentiment.setSentiment(1);
        } else {
            sentiment.setSentiment(2);
        }
        sentiment.setPositive_prob(pred_result.get("positive"));
        sentiment.setNegative_prob(pred_result.get("negative"));
        result.put("items", sentiment);
        return JSON.toJSONString(result);
    }

    @Override
    public String category_classify(String text) {
        Map<String, Double> pre_result = categoryClassifierModel.predict(text);
        Map<String, Object> result = new HashMap<>();
        Map<String, Double> items = new HashMap<>();
        result.put("text", text);
        double maxScore = Double.MIN_VALUE;
        String category = null;
        for (Map.Entry<String, Double> item : pre_result.entrySet()) {
            items.put(item.getKey(), item.getValue());
            if (item.getValue() > maxScore) {
                category = item.getKey();
                maxScore = item.getValue();
            }
        }
        result.put("items", items);
        result.put("category", category);
        return JSON.toJSONString(result);
    }

    @Override
    public String dependency_parser(String text){
        CoNLLSentence sentence = HanLP.parseDependency(text);
        Map<String, Object> result = new HashMap<>();
        List<DependencyEntity> items = new ArrayList<>();
        for(CoNLLWord word:sentence){
            DependencyEntity entity = new DependencyEntity(word.ID, word.LEMMA, word.POSTAG, word.HEAD.ID, word.DEPREL);
            items.add(entity);
        }
        result.put("text", text);
        result.put("items", items);
        return JSON.toJSONString(result);
    }
}
