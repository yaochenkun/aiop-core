package org.bupt.aiop.restapi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * 消息队列主题
 * Created by ken on 2017/10/21.
 */

@Service
public class TopicConsts {


	@Value("${rocketmq.registerTopic}")
	public String REGISTER_TOPIC;


}
