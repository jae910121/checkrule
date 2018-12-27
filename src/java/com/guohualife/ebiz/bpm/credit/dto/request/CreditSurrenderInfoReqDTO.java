package com.guohualife.ebiz.bpm.credit.dto.request;


/**
 * 查询赎回信息请求DTO
 * 
 * @author wangxulu
 *
 */
public class CreditSurrenderInfoReqDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 客户ID
	 */
	private  Long customerId;

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
