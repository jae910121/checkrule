package com.guohualife.ebiz.bpm.credit.check.dto;

public class CheckRuleInputDTO {

	/**
	 * 订单渠道
	 */
	private String orderType;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 申请平台
	 */
	private String platformType;
	
	/**
	 * 交易类型
	 */
	private String businessType;
	
	/**
	 * 交易子类型
	 */
	private String businessSubType;
	
	/**
	 * 校验参数
	 */
	private Object paramObject;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getBusinessSubType() {
		return businessSubType;
	}

	public void setBusinessSubType(String businessSubType) {
		this.businessSubType = businessSubType;
	}

	public Object getParamObject() {
		return paramObject;
	}

	public void setParamObject(Object paramObject) {
		this.paramObject = paramObject;
	}
	
}
