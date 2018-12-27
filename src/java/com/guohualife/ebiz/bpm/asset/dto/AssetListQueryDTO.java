package com.guohualife.ebiz.bpm.asset.dto;

import java.util.Date;
import java.util.List;

public class AssetListQueryDTO {

	/**
	 * 最近收益日期
	 */
	private Date lastIncomeDate;
	
	/**
	 * 起息日期
	 */
	private Date valueDate;
	
	/**
	 * 到期日期
	 */
	private Date expiryDate;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 产品列表
	 */
	private List<String> productCodeList;
	
	/**
	 * 产品类型
	 */
	private String productType;

	public Date getLastIncomeDate() {
		return lastIncomeDate;
	}

	public void setLastIncomeDate(Date lastIncomeDate) {
		this.lastIncomeDate = lastIncomeDate;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getProductCodeList() {
		return productCodeList;
	}

	public void setProductCodeList(List<String> productCodeList) {
		this.productCodeList = productCodeList;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
