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


	//Access_Token
	@Value("${accesstoken.issuer}")
	public String ACCESS_TOKEN_ISSUER;

	@Value("${accesstoken.duration}")
	public Long ACCESS_TOKEN_DURATION;

	@Value("${refreshtoken.duration}")
	public Long REFRESH_TOKEN_DURATION;

	@Value("${accesstoken.apiKeySecret}")
	public String ACCESS_TOKEN_API_KEY_SECRET;

	@Value("${refreshtoken.apiKeySecret}")
	public String REFRESH_TOKEN_API_KEY_SECRET;

	//SMS
	@Value("${sms.code.expire}")
	public Integer SMS_CODE_EXPIRE;

	@Value("${sms.code.len}")
	public Integer SMS_CODE_LEN;

	//默认图片名字（头像、LOGO、商标、图案）
	@Value("${default.image}")
	public String DEFAULT_IMAGE;
}
