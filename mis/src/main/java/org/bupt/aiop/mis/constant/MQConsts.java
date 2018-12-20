package org.bupt.aiop.mis.constant;

import org.springframework.stereotype.Service;

/**
 *
 * 消息队列主题的子消息
 * Created by ken on 2017/10/21.
 */

@Service
public class MQConsts {

	//注册成功Topic
	public static final String REGISTER_TOPIC = "REGISTER_TOPIC";
	public static final String SEND_EMAIL_TAG = "SEND_EMAIL_TAG";
	public static final String SEND_SMS_TAG = "SEND_SMS_TAG";

}
