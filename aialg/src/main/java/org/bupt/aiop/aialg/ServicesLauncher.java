package org.bupt.aiop.aialg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ken on 2017/9/23.
 */
public class ServicesLauncher {

    private static final Logger logger = LoggerFactory.getLogger(ServicesLauncher.class);

//	private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        logger.info("dubbo server is loading models, please wait...");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/application-context.xml",
                "classpath:spring/dubbo-server.xml");
        context.start();

        logger.info("dubbo server has been successfully launched on PID = {}", getProcessID());
//		hangup();
        latch.await();
    }

    // 进程挂起
//	public static void hangup() throws InterruptedException {
//		while(true) {
//			synchronized(lock) {
//				lock.wait();
//			}
//		}
//	}

    // 获取进程号
    private static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]);
    }
}
