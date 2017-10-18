package org.bupt.aiop.common.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用返回结果
 * Created by zlren on 2017/6/6.
 */
public class CommonResult {

    private static final Logger logger = LoggerFactory.getLogger(CommonResult.class);

    private String code;
    private String reason;
    private Object content; // 数据

    /**
     * @return
     */
    public static CommonResult success() {
        return new CommonResult(Constant.SUCCESS, null, null);
    }

    /**
     * @param reason
     * @return
     */
    public static CommonResult success(String reason) {
        return new CommonResult(Constant.SUCCESS, reason, null);
    }

    /**
     * @param reason
     * @param content
     * @return
     */
    public static CommonResult success(String reason, Object content) {
        return new CommonResult(Constant.SUCCESS, reason, content);
    }


    /**
     * @param reason
     * @return
     */
    public static CommonResult failure(String reason) {
        return new CommonResult(Constant.FAILURE, reason, null);
    }

    private CommonResult(String code, String reason, Object content) {
        this.code = code;
        this.reason = reason;
        this.content = content;

        logger.info(toString());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommonResult {" +
                "code = '" + code + '\'' +
                ", reason = '" + reason + '\'' +
                ", content = " + content +
                '}';
    }
}
