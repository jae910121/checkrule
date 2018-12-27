package com.guohualife.ebiz.bpm.asset.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.common.util.lock.dto.LockKeyObject;

public class AssetModifyDTO extends LockKeyObject{
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 资产变更金额
	 * 应等于收益变更金额 + 本金变更金额
	 * 保险产品赎回回调和债权产品赎回处理时传
	 */
	private BigDecimal modifyValue;
	
	/**
	 * 收益变更金额
	 * 同步收益和债权产品赎回处理时传
	 */
	private BigDecimal modifyIncome;
	
	/**
	 * 本金变更金额
	 * 仅债权产品赎回处理时传
	 */
	private BigDecimal modifyPrincipal;
	
	/**
	 * 变更时间
	 */
	private Date modifyDate;
	
	/**
	 * 变更类型
	 */
	private String modifyType;
	
	/**
	 * 变更编号
	 */
	private String operCode;

	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getModifyValue() {
		return modifyValue;
	}

	public void setModifyValue(BigDecimal modifyValue) {
		this.modifyValue = modifyValue;
	}

	public BigDecimal getModifyPrincipal() {
		return modifyPrincipal;
	}

	public void setModifyPrincipal(BigDecimal modifyPrincipal) {
		this.modifyPrincipal = modifyPrincipal;
	}

	public BigDecimal getModifyIncome() {
		return modifyIncome;
	}

	public void setModifyIncome(BigDecimal modifyIncome) {
		this.modifyIncome = modifyIncome;
	}
	
}
