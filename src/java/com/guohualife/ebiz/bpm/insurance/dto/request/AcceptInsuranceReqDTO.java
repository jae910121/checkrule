package com.guohualife.ebiz.bpm.insurance.dto.request;

import com.guohualife.ebiz.bpm.order.dto.ThirdOrderDTO;

/**
 * 承保请求DTO
 * 
 * @author wangxulu
 *
 */
public class AcceptInsuranceReqDTO {
	
	/**
	 * 客户ID
	 */
	private Long customerId;

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 投保单号
	 */
	private String proposalNo;
	
	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 卡折标志
	 */
	private String cardBookType;
	
	/**
	 * 卡折号
	 */
	private String cardBookCode;
	
	/**
	 * 第三方订单信息DTO
	 */
	private ThirdOrderDTO thirdOrderDTO;

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

	public ThirdOrderDTO getThirdOrderDTO() {
		return thirdOrderDTO;
	}

	public void setThirdOrderDTO(ThirdOrderDTO thirdOrderDTO) {
		this.thirdOrderDTO = thirdOrderDTO;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getCardBookType() {
		return cardBookType;
	}

	public void setCardBookType(String cardBookType) {
		this.cardBookType = cardBookType;
	}

	public String getCardBookCode() {
		return cardBookCode;
	}

	public void setCardBookCode(String cardBookCode) {
		this.cardBookCode = cardBookCode;
	}
}
