package org.bupt.aiop.mis.service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import org.bupt.aiop.mis.constant.MQConsts;
import org.bupt.common.bean.ResponseResult;
import org.bupt.aiop.mis.pojo.po.User;
import org.bupt.common.rocketmq.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ken on 2017/11/1.
 */
@Service
public class MqService extends BaseService<User> {

	private static final Logger logger = LoggerFactory.getLogger(MqService.class);

//	@Autowired
//	private MessageQueueSelector modMessageQueueSelector;
//
//	@Autowired
//	private MessageProducer messageProducer;

	/**
	 * 注册成功后分别给邮箱和手机发送邮件、短信通知
	 *
	 * @param userId
	 * @return
	 */
	public ResponseResult registerSuccessNotify(Integer userId) {

//		DefaultMQProducer producer = messageProducer.getProducer();
//
//		//构建依次要发送的Tag
//		String[] tags = {MQConsts.SEND_EMAIL_TAG,
//						 MQConsts.SEND_SMS_TAG};
//
//		//依次通知发送邮件、短信
//		for (int i = 0; i < tags.length; i++) {
//
//			//构造消息体
//			Message msg = new Message(MQConsts.REGISTER_TOPIC,
//									  tags[i % tags.length],
//									  "KEY" + i,
//									  ("第1个用户 " + i).getBytes());
//
//			//发送消息
//			try {
//				SendResult sendResult = producer.send(msg, modMessageQueueSelector, 1);
//				//logger.info("成功发送消息: {}", sendResult);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		//依次通知发送邮件、短信
//		for (int i = 0; i < tags.length; i++) {
//
//			//构造消息体
//			Message msg = new Message(MQConsts.REGISTER_TOPIC,
//					tags[i % tags.length],
//					"KEY" + i,
//					("第2个用户" + i).getBytes());
//
//			//发送消息
//			try {
//				SendResult sendResult = producer.send(msg, modMessageQueueSelector, 2);
//				//logger.info("成功发送消息: {}", sendResult);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		//依次通知发送邮件、短信
//		for (int i = 0; i < tags.length; i++) {
//
//			//构造消息体
//			Message msg = new Message(MQConsts.REGISTER_TOPIC,
//					tags[i % tags.length],
//					"KEY" + i,
//					("第3个用户" + i).getBytes());
//
//			//发送消息
//			try {
//				SendResult sendResult = producer.send(msg, modMessageQueueSelector, 3);
//				//logger.info("成功发送消息: {}", sendResult);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

		return ResponseResult.success();
	}
}
