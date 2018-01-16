package org.bupt.aiop.mis.pojo.vo;

import java.util.Date;

/**
 * 能力调用量统计实体
 */
public class AbilityInvokeLogStatistic {

	private Integer invokeSuccessCount;

	private Integer invokeFailureCount;

	private Date invokeDate;

	public Integer getInvokeSuccessCount() {
		return invokeSuccessCount;
	}

	public void setInvokeSuccessCount(Integer invokeSuccessCount) {
		this.invokeSuccessCount = invokeSuccessCount;
	}

	public Integer getInvokeFailureCount() {
		return invokeFailureCount;
	}

	public void setInvokeFailureCount(Integer invokeFailureCount) {
		this.invokeFailureCount = invokeFailureCount;
	}

	public Date getInvokeDate() {
		return invokeDate;
	}

	public void setInvokeDate(Date invokeDate) {
		this.invokeDate = invokeDate;
	}


}
