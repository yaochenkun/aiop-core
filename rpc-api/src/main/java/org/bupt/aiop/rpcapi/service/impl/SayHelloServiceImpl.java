package org.bupt.aiop.rpcapi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.bupt.aiop.common.rpcapi.service.inter.SayHelloService;


@Service
public class SayHelloServiceImpl implements SayHelloService {

	public String sayHello(String name) {
		return "Hello" + name;
	}
}
