package org.bupt.aiop.rpcapi.dubbo;

/**
 * 自然语言处理算法服务
 * Created by ken on 2017/9/25.
 */
public interface NlpAlgDubboService {

	String text_keywords(String text, Integer size);

	String word_pos(String text);

	String word_2_vec(String text);
}
