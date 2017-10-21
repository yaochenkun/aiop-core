package org.bupt.aiop.common.bean;

import org.bupt.aiop.common.constant.CodeConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用返回结果
 * Created by zlren on 2017/6/6.
 */
public class ResponseResult {

    private static final Logger logger = LoggerFactory.getLogger(ResponseResult.class);

    private String code;
    private String reason;
    private Object content; // 数据

    /**
     * @return
     */
    public static ResponseResult success() {
        return new ResponseResult(CodeConsts.SUCCESS, null, null);
    }

    /**
     * @param reason
     * @return
     */
    public static ResponseResult success(String reason) {
        return new ResponseResult(CodeConsts.SUCCESS, reason, null);
    }

    /**
     * @param reason
     * @param content
     * @return
     */
    public static ResponseResult success(String reason, Object content) {
        return new ResponseResult(CodeConsts.SUCCESS, reason, content);
    }


    /**
     * @param reason
     * @return
     */
    public static ResponseResult failure(String reason) {
        return new ResponseResult(CodeConsts.FAILURE, reason, null);
    }

    private ResponseResult(String code, String reason, Object content) {
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
        return "ResponseResult {" +
                "code = '" + code + '\'' +
                ", reason = '" + reason + '\'' +
                ", content = " + content +
                '}';
    }
}
