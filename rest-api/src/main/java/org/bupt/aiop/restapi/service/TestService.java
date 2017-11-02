package org.bupt.aiop.restapi.service;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.bupt.aiop.common.bean.ResponseResult;
import org.bupt.aiop.restapi.pojo.po.User;
import org.bupt.aiop.restapi.rocketmq.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ken on 2017/11/1.
 */
@Service
public class TestService extends BaseService<User> {

	private static final Logger logger = LoggerFactory.getLogger(TestService.class);

	@Autowired
	private MessageProducer emailMessageProducer;

	@Autowired
	private MessageProducer smsMessageProducer;

	/**
	 * 注册成功后分别给邮箱和手机发送邮件、短信通知
	 * @param content
	 * @return
	 */
	public ResponseResult registerSuccessNotify(String content) {


		//发送邮件
		DefaultMQProducer producer = emailMessageProducer.getProducer();
		Message msg = new Message("emailTopic", content.getBytes());
		try {
			producer.send(msg, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					logger.info(sendResult.getSendStatus().name());
					logger.info("onSuccess~~~~~");
				}

				@Override
				public void onException(Throwable throwable) {

				}
			});
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		//发送通知

		return ResponseResult.success();
	}
}
