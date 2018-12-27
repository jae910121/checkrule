package com.guohualife.ebiz.bpm.order.dto;

import java.math.BigDecimal;


/**
 * 订单账户信息DTO
 * 
 * @author wangxulu
 *
 */
public class OrderAccountDTO {
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 订单渠道
	 */
	private String orderType;

	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;
	
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
	 * 第三方订单信息
	 */
	private ThirdOrderDTO thirdOrderDTO;

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public ThirdOrderDTO getThirdOrderDTO() {
		return thirdOrderDTO;
	}

	public void setThirdOrderDTO(ThirdOrderDTO thirdOrderDTO) {
		this.thirdOrderDTO = thirdOrderDTO;
	}
	
}
