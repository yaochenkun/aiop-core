package org.bupt.aiop.platform.rocketmq;

import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.common.message.MessageExt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Email消息的具体消费处理
 * Created by ken on 2017/11/1.
 */
@Component
public class RegisterMessageListener implements MessageListenerOrderly{

	private Logger logger = LoggerFactory.getLogger(RegisterMessageListener.class);

	/*
	** 引入各种Service
	 */


	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

		// 设置自动提交
		context.setAutoCommit(true);

		//获得消息体
		logger.info("线程:{} 收到消息: {}", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

		//业务处理


		return ConsumeOrderlyStatus.SUCCESS;
	}

}
