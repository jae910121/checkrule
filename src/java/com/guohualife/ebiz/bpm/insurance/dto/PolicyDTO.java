package com.guohualife.ebiz.bpm.insurance.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保单信息DTO(核保)
 * 
 * @author wangxulu
 *
 */
public class PolicyDTO {
	
	/**
	 * 保费
	 */
	private BigDecimal prem;
	
	/**
	 * 份数
	 */
	private BigDecimal mult;
	
	/**
	 * 被保人是否为本人
	 * 1-本人
	 * 2-非本人
	 */
	private String insuredType;
	
	/**
	 * 受益人类型
	 * 1-法定
	 * 2-指定
	 */
	private String bnfType;
	
	/**
	 * 投保人信息DTO
	 */
	private AppntDTO appntDTO;
	
	/**
	 * 被保人信息
	 */
	private List<InsuredDTO> insuredDTOList;
	
	/**
	 * 受益人信息
	 */
	private List<BnfDTO> bnfDTOList;

	public BigDecimal getPrem() {
		return prem;
	}

	public void setPrem(BigDecimal prem) {
		this.prem = prem;
	}

	public BigDecimal getMult() {
		return mult;
	}

	public void setMult(BigDecimal mult) {
		this.mult = mult;
	}

	public AppntDTO getAppntDTO() {
		return appntDTO;
	}

	public void setAppntDTO(AppntDTO appntDTO) {
		this.appntDTO = appntDTO;
	}

	public String getInsuredType() {
		return insuredType;
	}

	public void setInsuredType(String insuredType) {
		this.insuredType = insuredType;
	}

	public String getBnfType() {
		return bnfType;
	}

	public void setBnfType(String bnfType) {
		this.bnfType = bnfType;
	}

	public List<InsuredDTO> getInsuredDTOList() {
		return insuredDTOList;
	}

	public void setInsuredDTOList(List<InsuredDTO> insuredDTOList) {
		this.insuredDTOList = insuredDTOList;
	}

	public List<BnfDTO> getBnfDTOList() {
		return bnfDTOList;
	}

	public void setBnfDTOList(List<BnfDTO> bnfDTOList) {
		this.bnfDTOList = bnfDTOList;
	}
	
}
