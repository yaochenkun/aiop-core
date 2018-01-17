package org.bupt.aiop.mis.pojo.vo;


/**
 * 能力调用量排名条目
 */
public class AbilityInvokeLogRanking {

	private Integer developerId;

	private Integer invokeTotalCount; // 总调用量

	public Integer getDeveloperId() {
		return developerId;
	}

	public void setDeveloperId(Integer developerId) {
		this.developerId = developerId;
	}

	public Integer getInvokeTotalCount() {
		return invokeTotalCount;
	}

	public void setInvokeTotalCount(Integer invokeTotalCount) {
		this.invokeTotalCount = invokeTotalCount;
	}
}
