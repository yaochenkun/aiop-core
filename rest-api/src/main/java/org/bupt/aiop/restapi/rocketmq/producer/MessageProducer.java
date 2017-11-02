package org.bupt.aiop.restapi.rocketmq.producer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.apache.log4j.Logger;

/**
 * RocketMQ消息队列生产者
 * Created by ken on 2017/11/1.
 */
public class MessageProducer {

	private Logger log = Logger.getLogger(getClass());

	//生产者
	private DefaultMQProducer producer;

	//生产者群名称
	private String producerGroup;

	//名字节点IP:Port
	private String namesrvAddr;

	//实例名称
	private String instanceName;

	public MessageProducer(String producerGroup, String namesrvAddr, String instanceName) {

		this.producerGroup = producerGroup;
		this.namesrvAddr = namesrvAddr;
		this.instanceName = instanceName;
	}

	//构造完成后调用的初始化方法
	public void init() throws MQClientException {

		log.info("start init DefaultMQProducer...");
		producer = new DefaultMQProducer(producerGroup);
		producer.setNamesrvAddr(this.namesrvAddr);
		producer.setInstanceName(this.instanceName);
		producer.start();
		log.info("DefaultMQProducer init success.");
	}

	public void destroy() {

		log.info("start destroy DefaultMQProducer...");
		producer.shutdown();
		log.info("DefaultMQProducer destroy success.");
	}

	public DefaultMQProducer getProducer() {

		return producer;
	}


}
