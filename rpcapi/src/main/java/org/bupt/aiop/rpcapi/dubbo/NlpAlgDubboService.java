package org.bupt.aiop.rpcapi.dubbo;

/**
 * 自然语言处理算法服务
 * Created by ken on 2017/9/25.
 */
public interface NlpAlgDubboService {

	String text_keywords(String text, Integer size);
}
