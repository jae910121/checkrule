package com.guohualife.ebiz.bpm.order.dto.request;

/**
 * 订单查询请求DTO 
 * 
 * @author wangxulu
 *
 */
public class OrderReqDTO {
	
	/**
	 * 客户ID
	 */
	private Long customerId;

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 查询信息类型
	 */
	private String queryDetailType;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getQueryDetailType() {
		return queryDetailType;
	}

	public void setQueryDetailType(String queryDetailType) {
		this.queryDetailType = queryDetailType;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
