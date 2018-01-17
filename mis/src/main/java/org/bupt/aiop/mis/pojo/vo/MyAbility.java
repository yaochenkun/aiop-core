package org.bupt.aiop.mis.pojo.vo;

/**
 * 能力调用量统计实体（首页列表项）
 */
public class MyAbility {

	private Integer id; //abilityId

	private String name;

	private Integer invokeTotalCount;

	private Integer invokeSuccessCount;

	private Float invokeSuccessRate;

	private Integer invokeFailureCount;

	private Float invokeFailureRate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getInvokeTotalCount() {
		return invokeTotalCount;
	}

	public void setInvokeTotalCount(Integer invokeTotalCount) {
		this.invokeTotalCount = invokeTotalCount;
	}

	public Float getInvokeSuccessRate() {
		return invokeSuccessRate;
	}

	public void setInvokeSuccessRate(Float invokeSuccessRate) {
		this.invokeSuccessRate = invokeSuccessRate;
	}

	public Float getInvokeFailureRate() {
		return invokeFailureRate;
	}

	public void setInvokeFailureRate(Float invokeFailureRate) {
		this.invokeFailureRate = invokeFailureRate;
	}

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


}
