package com.guohualife.ebiz.bpm.credit.dto;

import java.util.Date;
import java.util.List;

public class CreditListQueryDTO {

	/**
	 * 起息日
	 */
	private Date valueDate;
	
	/**
	 * 到期日
	 */
	private Date expiryDate;
	
	/**
	 * 募集结束日期
	 */
	private Date raiseDate;
	
	/**
	 * 处理状态
	 */
	private List<String> processStatusList;

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getRaiseDate() {
		return raiseDate;
	}

	public void setRaiseDate(Date raiseDate) {
		this.raiseDate = raiseDate;
	}

	public List<String> getProcessStatusList() {
		return processStatusList;
	}

	public void setProcessStatusList(List<String> processStatusList) {
		this.processStatusList = processStatusList;
	}
	
}
