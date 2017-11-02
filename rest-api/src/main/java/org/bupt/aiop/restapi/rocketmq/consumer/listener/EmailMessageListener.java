package org.bupt.aiop.restapi.rocketmq.consumer.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Email消息的具体消费处理
 * Created by ken on 2017/11/1.
 */

@Component
public class EmailMessageListener implements MessageListenerConcurrently{

	private Logger log = Logger.getLogger(getClass());

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {


		for (MessageExt msg : msgs) {
			log.info("email msg : " + new String(msg.getBody()));
		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
}
