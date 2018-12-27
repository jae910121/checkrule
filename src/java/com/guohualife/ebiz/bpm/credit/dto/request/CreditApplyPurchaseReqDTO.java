package com.guohualife.ebiz.bpm.credit.dto.request;

import java.math.BigDecimal;

import com.guohualife.ebiz.bpm.insurance.dto.ActivityDTO;
import com.guohualife.ebiz.bpm.order.dto.ThirdOrderDTO;


/**
 * 债权申购请求信息DTO
 * 
 * @author wangxulu
 *
 */
public class CreditApplyPurchaseReqDTO {
	
	/**
	 * 第三方订单信息DTO
	 */
	private ThirdOrderDTO thirdOrderDTO;
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 产品编码
	 */
	private String productCode;

	/**
	 * 订单渠道
	 */
	private String orderType;

	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 份数
	 */
	private BigDecimal mult;

	/**
	 * 银行编码
	 */
	private String bankCode;
	
	/**
	 * 卡折标志 01-卡 02-折
	 */
	private String cardBookType;
	
	/**
	 * 卡折号
	 */
	private String cardBookCode;
	
	/**
	 * 活动信息
	 */
	private ActivityDTO activityDTO;

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

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getMult() {
		return mult;
	}

	public void setMult(BigDecimal mult) {
		this.mult = mult;
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

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

}
