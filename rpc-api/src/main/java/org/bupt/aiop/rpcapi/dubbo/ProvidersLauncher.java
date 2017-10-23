package org.bupt.aiop.rpcapi.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ken on 2017/9/23.
 */
public class ProvidersLauncher {

	public static void main(String[] args) throws Exception {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"classpath:spring/dubbo-provider.xml"});
		context.start();

		System.out.println("Providers has been successfully launched~");
		System.in.read(); // press any key to exit
	}

}
