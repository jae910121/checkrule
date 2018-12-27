package com.guohualife.ebiz.bpm.insurance.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.platform.base.api.dto.BaseResultDTO;


/**
 * 保单保全信息DTO
 * 
 * @author wangxulu
 *
 */
public class PolicySurrenderInfoResDTO extends BaseResultDTO{

	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 保单价值
	 */
	private BigDecimal contValue;
	
	/**
	 * 保单生效日期
	 */
	private Date cvaliDate;
	
	/**
	 * 保单投保方式
	 */
	private String orderType;
	
	/**
	 * 保单投保方式文字描述
	 */
	private String orderTypeLabel;
	
	/**
	 * 是否过犹豫期
	 */
	private String isAfterThaw;
	
	/**
	 *是否支持领取
	 *从产品属性配置中获取1-是 0-否
	 */
	private String isDraw;
	
	/**
	 * 是否支持退保
	 * 从产品属性配置中获取1-是 0-否
	 */
	private String isRefund;
	
	/**
	 * 是否支持犹豫期退保
	 * 从产品属性配置中获取1-是 0-否
	 */
	private String isThawRefund;
	
	/**
	 * 赎回金额倍数(支持赎回前提)
	 */
	private BigDecimal surrenderUnit;
	
	/**
	 * 赎回最小金额(支持赎回前提)
	 */
	private BigDecimal surrenderMin;
	
	/**
	 * 赎回账户剩余最低金额(支持赎回前提)
	 */
	private BigDecimal surrenderRestMin;
	
	/**
	 * 赎回最大金额(支持赎回前提)
	 */
	private BigDecimal surrenderMax;
	
	/**
	 * 赎回账户号
	 */
	private String recAccountNo;
	
	/**
	 * 赎回账户名
	 */
	private String recAccountName;
	
	/**
	 * 赎回账户类型
	 */
	private String recAccountType;
	
	/**
	 * 银行名称
	 */
	private String bankName;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getContValue() {
		return contValue;
	}

	public void setContValue(BigDecimal contValue) {
		this.contValue = contValue;
	}

	public Date getCvaliDate() {
		return cvaliDate;
	}

	public void setCvaliDate(Date cvaliDate) {
		this.cvaliDate = cvaliDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeLabel() {
		return orderTypeLabel;
	}

	public void setOrderTypeLabel(String orderTypeLabel) {
		this.orderTypeLabel = orderTypeLabel;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(String recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public String getRecAccountName() {
		return recAccountName;
	}

	public void setRecAccountName(String recAccountName) {
		this.recAccountName = recAccountName;
	}

	public String getRecAccountType() {
		return recAccountType;
	}

	public void setRecAccountType(String recAccountType) {
		this.recAccountType = recAccountType;
	}

	public String getIsAfterThaw() {
		return isAfterThaw;
	}

	public void setIsAfterThaw(String isAfterThaw) {
		this.isAfterThaw = isAfterThaw;
	}

	public String getIsDraw() {
		return isDraw;
	}

	public void setIsDraw(String isDraw) {
		this.isDraw = isDraw;
	}

	public String getIsThawRefund() {
		return isThawRefund;
	}

	public void setIsThawRefund(String isThawRefund) {
		this.isThawRefund = isThawRefund;
	}

	public BigDecimal getSurrenderUnit() {
		return surrenderUnit;
	}

	public void setSurrenderUnit(BigDecimal surrenderUnit) {
		this.surrenderUnit = surrenderUnit;
	}

	public BigDecimal getSurrenderMin() {
		return surrenderMin;
	}

	public void setSurrenderMin(BigDecimal surrenderMin) {
		this.surrenderMin = surrenderMin;
	}

	public BigDecimal getSurrenderRestMin() {
		return surrenderRestMin;
	}

	public void setSurrenderRestMin(BigDecimal surrenderRestMin) {
		this.surrenderRestMin = surrenderRestMin;
	}

	public BigDecimal getSurrenderMax() {
		return surrenderMax;
	}

	public void setSurrenderMax(BigDecimal surrenderMax) {
		this.surrenderMax = surrenderMax;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
