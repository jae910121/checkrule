package com.guohualife.ebiz.bpm.insurance.dto.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保全核算DTO
 * 
 * @author wangxulu
 *
 */
public class PolicySurrenderChargeReqDTO extends PolicySurrenderInfoReqDTO {

	/**
	 * 申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 赎回类型
	 */
	private String surrenderType;

	/**
	 * 赎回申请时间
	 */
	private Date applyDate;
	
	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public String getSurrenderType() {
		return surrenderType;
	}

	public void setSurrenderType(String surrenderType) {
		this.surrenderType = surrenderType;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
}
