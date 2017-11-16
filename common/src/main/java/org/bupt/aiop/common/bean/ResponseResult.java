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
    private Object result; // 数据

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
     *
     * @param result
     * @return
     */
    public static ResponseResult success(Object result) {
        return new ResponseResult(CodeConsts.SUCCESS, CodeConsts.SUCCESS, result);
    }

    /**
     * @param reason
     * @param result
     * @return
     */
    public static ResponseResult success(String reason, Object result) {
        return new ResponseResult(CodeConsts.SUCCESS, reason, result);
    }


    /**
     * @param reason
     * @return
     */
    public static ResponseResult error(String reason) {
        return new ResponseResult(CodeConsts.ERROR, reason, null);
    }

    /**
     *
     * @param reason
     * @param result
     * @return
     */
    public static ResponseResult error(String reason, Object result) {
        return new ResponseResult(CodeConsts.ERROR, reason, result);
    }


    private ResponseResult(String code, String reason, Object result) {
        this.code = code;
        this.reason = reason;
        this.result = result;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseResult {" +
                "code = '" + code + '\'' +
                ", reason = '" + reason + '\'' +
                ", result = " + result +
                '}';
    }
}
