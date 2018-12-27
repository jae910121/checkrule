package com.guohualife.ebiz.bpm.insurance.dto;

import java.util.Date;
import java.util.List;

public class PolicyListQueryDTO {
	
	/**
	 * 保单状态
	 */
	private List<String> processStatusList;
	
	/**
	 * 保单状态
	 */
	private List<String> confirmStatusList;
	
	/**
	 * 是否过犹豫期
	 */
	private String isThaw;
	
	/**
	 * 犹豫期结束时间
	 */
	private Date lastHesitateDate;

	public String getIsThaw() {
		return isThaw;
	}

	public void setIsThaw(String isThaw) {
		this.isThaw = isThaw;
	}

	public Date getLastHesitateDate() {
		return lastHesitateDate;
	}

	public void setLastHesitateDate(Date lastHesitateDate) {
		this.lastHesitateDate = lastHesitateDate;
	}

	public List<String> getProcessStatusList() {
		return processStatusList;
	}

	public void setProcessStatusList(List<String> processStatusList) {
		this.processStatusList = processStatusList;
	}

	public List<String> getConfirmStatusList() {
		return confirmStatusList;
	}

	public void setConfirmStatusList(List<String> confirmStatusList) {
		this.confirmStatusList = confirmStatusList;
	}
	
}
