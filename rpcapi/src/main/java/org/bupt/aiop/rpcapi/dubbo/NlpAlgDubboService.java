package org.bupt.aiop.rpcapi.dubbo;

/**
 * 自然语言处理算法服务
 * Created by ken on 2017/9/25.
 */
public interface NlpAlgDubboService {

    String text_keywords(String text, Integer size);

    String text_summaries(String text, Integer size);

    String text_phrases(String text, Integer size);

    String word_pos(String text);

    String word_pos_normal(String text);

    String word_pos_japanese(String text);

    String word_pos_place(String text);

    String word_pos_organization(String text);

    String word_pos(String text, Boolean japName, Boolean placeName, Boolean orgName);

    String word_2_vec(String text);

    String word_2_pinyin(String text);

    String simplified_2_traditional(String text);

    String traditional_2_simplified(String text);

    String word_sim(String text1, String text2);

    String document_sim(String doc1, String doc2);

    String nearest_words(String text, Integer size);

    String motion_classify(String text);

    String category_classify(String text);
}
