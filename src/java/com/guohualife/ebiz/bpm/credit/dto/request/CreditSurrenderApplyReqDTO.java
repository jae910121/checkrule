package com.guohualife.ebiz.bpm.credit.dto.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 赎回申请请求DTO
 * 
 * @author wangxulu
 *
 */
public class CreditSurrenderApplyReqDTO {

	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 客户ID
	 */
	private Long customerId;
	
	/**
	 * 申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 赎回申请时间
	 */
	private Date applyDate;
	
	/**
	 * 实际到账金额
	 */
	private BigDecimal availableMoney;
	
	/**
	 * 扣除手续费
	 */
	private BigDecimal surrenderFee;
	
	/**
	 * 申请平台
	 */
	private String platformType;
	
	/**
	 * 赎回本金金额
	 */
	private BigDecimal surrenderInvest;

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

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public BigDecimal getAvailableMoney() {
		return availableMoney;
	}

	public void setAvailableMoney(BigDecimal availableMoney) {
		this.availableMoney = availableMoney;
	}

	public BigDecimal getSurrenderFee() {
		return surrenderFee;
	}

	public void setSurrenderFee(BigDecimal surrenderFee) {
		this.surrenderFee = surrenderFee;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public BigDecimal getSurrenderInvest() {
		return surrenderInvest;
	}

	public void setSurrenderInvest(BigDecimal surrenderInvest) {
		this.surrenderInvest = surrenderInvest;
	}
	
}
