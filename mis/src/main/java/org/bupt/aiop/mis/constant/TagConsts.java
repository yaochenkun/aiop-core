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


	@Value("${org.bupt.aiop.mis.rocketmq.sendEmailTag}")
	public String SEND_EMAIL_TAG;

	@Value("${org.bupt.aiop.mis.rocketmq.sendSmsTag}")
	public String SEND_SMS_TAG;

}
