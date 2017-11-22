package org.bupt.common.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用返回结果
 * Created by zlren on 2017/6/6.
 */
public class ErrorResult {

	private static final Logger logger = LoggerFactory.getLogger(ErrorResult.class);

	private Integer error_code;
	private String error_msg;


	public ErrorResult(Integer error_code, String error_msg) {
		this.error_code = error_code;
		this.error_msg = error_msg;

		logger.info("ERROR: code={}, msg={}", error_code, error_msg);
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
}
