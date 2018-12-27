package com.guohualife.ebiz.bpm.insurance.dto.request;


/**
 * 保全查询DTO
 * 
 * @author wangxulu
 *
 */
public class PolicySurrenderInfoReqDTO {
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 本次操作客户ID
	 */
	private Long customerId;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}


	
}
