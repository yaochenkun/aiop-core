package org.bupt.aiop.aialg;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ken on 2017/9/23.
 */
public class ServicesLauncher {

	public static void main(String[] args) throws Exception {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"classpath:spring/dubbo-server.xml"});
		context.start();

		System.out.println("dubbo server has been successfully launched~");

		//程序挂起
		while(true) System.in.read();
	}

}
