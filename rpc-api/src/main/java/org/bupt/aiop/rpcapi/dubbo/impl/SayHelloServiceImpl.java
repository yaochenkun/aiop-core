package org.bupt.aiop.rpcapi.dubbo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.bupt.aiop.rpcapi.dubbo.inter.SayHelloService;


@Service
public class SayHelloServiceImpl implements SayHelloService {

	public String sayHello(String name) {
		return "Hello" + name;
	}
}
