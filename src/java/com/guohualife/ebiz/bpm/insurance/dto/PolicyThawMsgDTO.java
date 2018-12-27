package com.guohualife.ebiz.bpm.insurance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.ebiz.bpm.order.dto.OrderMsgDTO;

/**
 * 订单犹豫期解冻消息DTO
 * 
 * @author wangxulu
 *
 */
public class PolicyThawMsgDTO extends OrderMsgDTO{

	/**
	 * 份数
	 */
	private BigDecimal mult;
	
	/**
	 * 生效时间
	 */
	private Date cvalidate;
	
	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 犹豫期结束日期
	 */
	private Date lastHesitateDate;
	
	/**
	 * 保费
	 */
	private BigDecimal prem;
	
	/**
	 * 保额
	 */
	private BigDecimal amt;

	public BigDecimal getMult() {
		return mult;
	}

	public void setMult(BigDecimal mult) {
		this.mult = mult;
	}

	public Date getCvalidate() {
		return cvalidate;
	}

	public void setCvalidate(Date cvalidate) {
		this.cvalidate = cvalidate;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getLastHesitateDate() {
		return lastHesitateDate;
	}

	public void setLastHesitateDate(Date lastHesitateDate) {
		this.lastHesitateDate = lastHesitateDate;
	}

	public BigDecimal getPrem() {
		return prem;
	}

	public void setPrem(BigDecimal prem) {
		this.prem = prem;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	
}
