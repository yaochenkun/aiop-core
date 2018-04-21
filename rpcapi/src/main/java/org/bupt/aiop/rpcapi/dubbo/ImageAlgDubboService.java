package org.bupt.aiop.rpcapi.dubbo;

/**
 * 图像处理算法服务
 * Created by ken on 2017/9/25.
 */
public interface ImageAlgDubboService {
    
    String face_recognition(String text);
}
