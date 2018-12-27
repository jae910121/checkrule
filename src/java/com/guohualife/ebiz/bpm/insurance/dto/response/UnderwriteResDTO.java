package com.guohualife.ebiz.bpm.insurance.dto.response;


/**
 * 核保结果DTO
 * 
 * @author wangxulu
 *
 */
public class UnderwriteResDTO {

	/**
	 * 核保是否成功(1-是 0-否)
	 */
	private String underwriteFlag;
	
	/**
	 * 失败原因
	 */
	private String failReason;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 投保单号
	 */
	private String proposalNo;

	public String getUnderwriteFlag() {
		return underwriteFlag;
	}

	public void setUnderwriteFlag(String underwriteFlag) {
		this.underwriteFlag = underwriteFlag;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProposalNo() {
		return proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}
	
}
