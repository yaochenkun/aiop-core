package org.bupt.common.rocketmq.selector;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 生产者选择将消息装入消息队列的方式
 * 取模选择
 * Created by ken on 2017/11/1.
 */
@Component
public class ModMessageQueueSelector implements MessageQueueSelector{

	public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {

		Integer id = (Integer) arg;
		int index = id % mqs.size();
		return mqs.get(index);
	}
}
