package com.guohualife.ebiz.bpm.insurance.dto.response;

import java.math.BigDecimal;

import com.guohualife.platform.base.api.dto.BaseResultDTO;


/**
 * 保单核算结果DTO
 * 
 * @author wangxulu
 *
 */
public class PolicySurrenderChargeResDTO extends BaseResultDTO {

	/**
	 * 实际金额
	 */
	private BigDecimal availableMoney;
	
	/**
	 * 扣除手续费
	 */
	private BigDecimal surrenderFee;
	
	/**
	 * 退保提示语句
	 */
	private String confirmMessage;

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

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}
	
}
