package com.guohualife.ebiz.bpm.insurance.dto;

import java.util.Date;

public class PolicySuccessDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 订单渠道
	 */
	private String orderType;
	
	/**
	 * 出单时间
	 */
	private Date successDate;
	
	/**
	 * 生效时间
	 * 使用时不需要set，可在调用后get
	 */
	private Date cvalidate;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getCvalidate() {
		return cvalidate;
	}

	public void setCvalidate(Date cvalidate) {
		this.cvalidate = cvalidate;
	}
	
}
