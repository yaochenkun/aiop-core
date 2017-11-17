package org.bupt.common.bean;

import org.bupt.common.constant.ResponseConsts;
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
        return new ResponseResult(ResponseConsts.SUCCESS, null, null);
    }


    /**
     *
     * @param content
     * @return
     */
    public static ResponseResult success(Object content) {
        return new ResponseResult(ResponseConsts.SUCCESS, ResponseConsts.SUCCESS, content);
    }

    /**
     * @param reason
     * @param content
     * @return
     */
    public static ResponseResult success(String reason, Object content) {
        return new ResponseResult(ResponseConsts.SUCCESS, reason, content);
    }


    /**
     * @param reason
     * @return
     */
    public static ResponseResult error(String reason) {
        return new ResponseResult(ResponseConsts.ERROR, reason, null);
    }

    /**
     *
     * @param reason
     * @param content
     * @return
     */
    public static ResponseResult error(String reason, Object content) {
        return new ResponseResult(ResponseConsts.ERROR, reason, content);
    }


    private ResponseResult(String code, String reason, Object content) {
        this.code = code;
        this.reason = reason;
        this.content = content;
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
}
