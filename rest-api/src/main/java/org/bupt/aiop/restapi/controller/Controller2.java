package org.bupt.aiop.restapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.bupt.aiop.common.service.inter.SayHelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by ken on 2017/9/25.
 */
@Component
public class Controller2 {

	@Reference
	private static SayHelloService sayHelloService;


	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"classpath:spring/consumer.xml"});
		context.start();

		System.out.println(sayHelloService.sayHello(" controller2"));
	}

}
