package com.guohualife.ebiz.bpm.order.dto.request;
/**
 * 赎回回调请求DTO
 * @author zhangyl_ebiz
 *
 */
public class PolicySurrenderCallbackReqDTO {
	
	/**
	 * 赎回Id(退保订单号)
	 */
	private Long surrenderId;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 赎回受理号
	 */
	private String acceptNo;
	
	/**
	 * 是否成功
	 */
	private String isSuccess;
	
	/**
	 * 失败原因
	 */
	private String failReason;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
	
}
