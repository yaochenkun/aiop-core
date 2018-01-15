package org.bupt.aiop.mis.pojo.vo;

import java.util.Date;

/**
 * 能力调用量统计实体
 */
public class AbilityInvokeLogStatistic {

	private Integer id;

	private Integer invokeCount;

	private Date invokeDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInvokeCount() {
		return invokeCount;
	}

	public void setInvokeCount(Integer invokeCount) {
		this.invokeCount = invokeCount;
	}

	public Date getInvokeDate() {
		return invokeDate;
	}

	public void setInvokeDate(Date invokeDate) {
		this.invokeDate = invokeDate;
	}
}
