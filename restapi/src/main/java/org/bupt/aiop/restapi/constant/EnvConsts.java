package org.bupt.aiop.restapi.constant;

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


}
