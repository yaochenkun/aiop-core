package org.bupt.aiop.mis.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * 消息队列主题的子消息
 * Created by ken on 2017/10/21.
 */

@Service
public class TagConsts {


	@Value("${rocketmq.sendEmailTag}")
	public String SEND_EMAIL_TAG;

	@Value("${rocketmq.sendSmsTag}")
	public String SEND_SMS_TAG;

}
