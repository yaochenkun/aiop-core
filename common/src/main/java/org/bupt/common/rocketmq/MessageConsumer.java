package org.bupt.common.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ken on 2017/11/1.
 */
public class MessageConsumer {

	private Logger log = LoggerFactory.getLogger(getClass());

	//消费者对象
	private DefaultMQPushConsumer consumer;

	//消费者处理类
	private MessageListenerOrderly messageListener;

	//消费者群名称
	private String consumerGroup;

	//名字节点IP:Port
	private String namesrvAddr;

	//实例名称
	private String instanceName;

	//业务主题
	private String topic;

	public MessageConsumer(String consumerGroup, String namesrvAddr, String instanceName, String topic, MessageListenerOrderly messageListener) {
		this.consumerGroup = consumerGroup;
		this.namesrvAddr = namesrvAddr;
		this.instanceName = instanceName;
		this.topic = topic;
		this.messageListener = messageListener;
	}

	public void init() throws MQClientException {

		log.info("start init DefaultMQPushConsumer...");
		consumer = new DefaultMQPushConsumer(consumerGroup);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET); //从队列头部开始消费
		consumer.setNamesrvAddr(namesrvAddr);
		consumer.setInstanceName(instanceName);
		consumer.subscribe(topic, "*");
		consumer.registerMessageListener(messageListener);
		consumer.start();
		log.info("DefaultMQPushConsumer init ok.");
	}


	public void destroy() {
		log.info("start destroy DefaultMQPushConsumer...");
		consumer.shutdown();
		log.info("DefaultMQPushConsumer destroy success.");
	}

	public DefaultMQPushConsumer getConsumer() {
		return consumer;
	}
}
