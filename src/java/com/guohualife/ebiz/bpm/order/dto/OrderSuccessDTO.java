package com.guohualife.ebiz.bpm.order.dto;

import java.util.Date;

public class OrderSuccessDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 成交时间
	 */
	private Date successDate;
	
	/**
	 * 封闭期到期日期 
	 * 不需要set 接收返回数据即可
	 */
	private Date closeDate;
	/**
	 * 业务号
	 */
	private String businessNo;
	
	/**
	 * 起息日
	 */
	private Date valueDate;
	
	/**
	 * 到期日期
	 */
	private Date expiryDate;
	
	/**
	 * 产品编码
	 */
	private String productCode;
	
	/**
	 * 订单渠道
	 */
	private String orderType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
}
