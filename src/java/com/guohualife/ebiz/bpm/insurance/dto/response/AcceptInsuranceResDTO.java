package com.guohualife.ebiz.bpm.insurance.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import com.guohualife.platform.base.api.dto.BaseResultDTO;

/**
 * 承保结果DTO
 * 
 * @author wangxulu
 *
 */
public class AcceptInsuranceResDTO extends BaseResultDTO {
	
	/**
	 * 出单状态(0-成功 1-支付中 2-失败)
	 */
	private String status;
	
	/**
	 * 失败原因
	 */
	private String failReason;
	
	/**
	 * 保单号
	 */
	private String policyNo;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 出单时间
	 */
	private Date issuedTime;
	
	/**
	 * 生效时间
	 */
	private Date cvalidate;
	
	/**
	 * 总保费
	 */
	private BigDecimal totalPremium;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

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

	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
	}

	public BigDecimal getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(BigDecimal totalPremium) {
		this.totalPremium = totalPremium;
	}

	public Date getCvalidate() {
		return cvalidate;
	}

	public void setCvalidate(Date cvalidate) {
		this.cvalidate = cvalidate;
	}
}
