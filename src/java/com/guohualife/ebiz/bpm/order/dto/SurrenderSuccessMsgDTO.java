package com.guohualife.ebiz.bpm.order.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单赎回消息DTO
 * 
 * @author wangxulu
 *
 */
public class SurrenderSuccessMsgDTO extends OrderMsgDTO{
	
	/**
	 * 赎回ID
	 */
	private Long surrenderId;
	
	/**
	 * 赎回时间
	 */
	private Date surrenderDate;
	
	/**
	 * 赎回受理号
	 */
	private String acceptNo;
	
	/**
	 * 赎回类型
	 */
	private String surrenderType;
	
	/**
	 * 赎回申请金额
	 */
	private BigDecimal applyMoney;
	
	/**
	 * 赎回手续费
	 */
	private BigDecimal surrenderFee;
	
	/**
	 * 赎回实际金额
	 */
	private BigDecimal actualMoney;
	
	/**
	 * 赎回申请平台
	 */
	private String platformType;

	public Date getSurrenderDate() {
		return surrenderDate;
	}

	public void setSurrenderDate(Date surrenderDate) {
		this.surrenderDate = surrenderDate;
	}

	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}

	public String getSurrenderType() {
		return surrenderType;
	}

	public void setSurrenderType(String surrenderType) {
		this.surrenderType = surrenderType;
	}

	public BigDecimal getApplyMoney() {
		return applyMoney;
	}

	public void setApplyMoney(BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}

	public BigDecimal getSurrenderFee() {
		return surrenderFee;
	}

	public void setSurrenderFee(BigDecimal surrenderFee) {
		this.surrenderFee = surrenderFee;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public Long getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(Long surrenderId) {
		this.surrenderId = surrenderId;
	}
	
}
