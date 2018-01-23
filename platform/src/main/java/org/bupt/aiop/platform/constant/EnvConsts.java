package org.bupt.aiop.platform.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * 从配置文件中读取的常量
 * Created by ken on 2017/10/21.
 */

@Service
public class EnvConsts {

	@Value("${accesstoken.apiKeySecret}")
	public String ACCESS_TOKEN_API_KEY_SECRET;

	@Value("${refreshtoken.apiKeySecret}")
	public String REFRESH_TOKEN_API_KEY_SECRET;

}
