package org.bupt.aiop.aialg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by ken on 2017/9/23.
 */
public class ServicesLauncher {

	private static final Logger logger = LoggerFactory.getLogger(ServicesLauncher.class);

	private static Object lock = new Object();

	public static void main(String[] args) throws InterruptedException {

		logger.info("dubbo server is loading models, please wait...");

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[]{"classpath:spring/application-context.xml",
						 	 "classpath:spring/dubbo-server.xml"});
		context.start();

		hangup();
		logger.info("dubbo server has been successfully launched on PID = {}", getProcessID());
	}

	// 进程挂起
	public static void hangup() throws InterruptedException {
		while(true) {
			synchronized(lock) {
				lock.wait();
			}
		}
	}

	// 获取进程号
	public static final int getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
	}
}
