package org.bupt.aiop.mis.pojo.vo;

import org.bupt.aiop.mis.pojo.po.Ability;

/**
 * 某应用旗下的单个能力信息
 */
public class AbilityUnderApp extends Ability{

	private Integer appAbilityId;

	private Integer appId;

	private Integer abilityId;

	private String status;

	private Integer actualInvokeLimit;

	private Integer actualQpsLimit;

	public Integer getAppAbilityId() {
		return appAbilityId;
	}

	public void setAppAbilityId(Integer appAbilityId) {
		this.appAbilityId = appAbilityId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(Integer abilityId) {
		this.abilityId = abilityId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getActualInvokeLimit() {
		return actualInvokeLimit;
	}

	public void setActualInvokeLimit(Integer actualInvokeLimit) {
		this.actualInvokeLimit = actualInvokeLimit;
	}

	public Integer getActualQpsLimit() {
		return actualQpsLimit;
	}

	public void setActualQpsLimit(Integer actualQpsLimit) {
		this.actualQpsLimit = actualQpsLimit;
	}
}
