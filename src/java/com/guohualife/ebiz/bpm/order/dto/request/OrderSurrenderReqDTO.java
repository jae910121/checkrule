package com.guohualife.ebiz.bpm.order.dto.request;

/**
 * 订单赎回详细信息查询请求DTO
 * 
 * @author wangxulu
 *
 */
public class OrderSurrenderReqDTO {

	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 赎回id
	 */
	private Long surrenderId;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
}
