package com.guohualife.ebiz.bpm.credit.dto.response;

import java.math.BigDecimal;

import com.guohualife.platform.base.api.dto.BaseResultDTO;

/**
 * 赎回核算返回DTO
 * 
 * @author wangxulu
 * 
 */
public class CreditSurrenderChargeResDTO extends BaseResultDTO {

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 申请赎回金额
	 */
	private BigDecimal applyMoney;

	/**
	 * 实际赎回金额
	 */
	private BigDecimal availableMoney;

	/**
	 * 扣除手续费
	 */
	private BigDecimal surrenderFee;

	/**
	 * 赎回本金金额
	 */
	private BigDecimal surrenderInvest;

	/**
	 * 赎回收益金额
	 */
	private BigDecimal surrenderIncome;

	/**
	 * 赎回确认信息
	 */
	private String confirmMessage;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public BigDecimal getSurrenderInvest() {
		return surrenderInvest;
	}

	public void setSurrenderInvest(BigDecimal surrenderInvest) {
		this.surrenderInvest = surrenderInvest;
	}

	public BigDecimal getSurrenderIncome() {
		return surrenderIncome;
	}

	public void setSurrenderIncome(BigDecimal surrenderIncome) {
		this.surrenderIncome = surrenderIncome;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

}
