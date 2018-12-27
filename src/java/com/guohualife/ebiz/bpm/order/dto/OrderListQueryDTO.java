package com.guohualife.ebiz.bpm.order.dto;

import java.util.Date;
import java.util.List;

public class OrderListQueryDTO {

	/**
	 * 订单状态列表
	 */
	private List<String> statusList;
	
	/**
	 * 产品编码列表
	 */
	private List<String> productCodeList;
	
	/**
	 * 产品类型
	 */
	private String productType;
	
	/**
	 * 订单渠道列表
	 */
	private List<String> orderTypeList;
	
	/**
	 * 成交时间
	 */
	private Date successDate;
	
	/**
	 * 生效时间
	 */
	private Date valueDate;
	
	/**
	 * 到期时间
	 */
	private Date expiryDate;
	
	/**
	 * 客户ID
	 */
	private Long customerId;

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
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

	public List<String> getOrderTypeList() {
		return orderTypeList;
	}

	public void setOrderTypeList(List<String> orderTypeList) {
		this.orderTypeList = orderTypeList;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
