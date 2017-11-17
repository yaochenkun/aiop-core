package org.bupt.aiop.mis.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * 从配置文件中读取的常量
 * Created by ken on 2017/10/21.
 */

@Service
public class EnvConsts {


	@Value("${file.path}")
	public String FILE_PATH;

	@Value("${default.password}")
	public String DEFAULT_PASSWORD;

	//Token
	@Value("${token.issuer}")
	public String TOKEN_ISSUER;

	@Value("${token.duration}")
	public Long TOKEN_DURATION;

	@Value("${token.apiKeySecret}")
	public String TOKEN_API_KEY_SECRET;

	//SMS
	@Value("${sms.code.expire}")
	public Integer SMS_CODE_EXPIRE;

	@Value("${sms.code.len}")
	public Integer SMS_CODE_LEN;
}
