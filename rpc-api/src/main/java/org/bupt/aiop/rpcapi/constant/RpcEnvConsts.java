package org.bupt.aiop.rpcapi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * 从配置文件中读取的常量
 * Created by ken on 2017/10/21.
 */

@Service
public class RpcEnvConsts {


	public static String ALG_SERVER_IP;
	public static Integer ALG_SERVER_PORT;

	@Value("${alg.serverip}")
	public void setALG_SERVER_IP(String ALG_SERVER_IP) {
		this.ALG_SERVER_IP = ALG_SERVER_IP;
	}

	@Value("${alg.serverport}")
	public void setALG_SERVER_PORT(Integer ALG_SERVER_PORT) {
		this.ALG_SERVER_PORT = ALG_SERVER_PORT;
	}
}
