package org.bupt.aiop.aialg;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by ken on 2017/9/23.
 */
public class ServicesLauncher {

	private static Object lock = new Object();

	public static void main(String[] args) throws InterruptedException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"classpath:spring/application-context.xml",
						 	 "classpath:spring/dubbo-server.xml"});
		context.start();

		System.out.println("dubbo server has been successfully launched on PID = " + getProcessID());

		hangup();
	}

	public static void hangup() throws InterruptedException {
		while(true) {
			synchronized(lock) {
				lock.wait();
			}
		}
	}

	public static final int getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		System.out.println(runtimeMXBean.getName());
		return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
				.intValue();
	}

}
