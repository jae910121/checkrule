package com.guohualife.ebiz.bpm.insurance.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 受益人信息DTO
 * 
 * @author bnf
 *
 */
public class BnfDTO {
	
	/**
	 * 受益人类别
	 * 0-生存受益人, 1-死亡受益人, 2-红利受益人
	 */
	private String bnfType;
	
	/**
	 * 受益人顺序
	 */
	private BigDecimal bnfOrder;
	
	/**
	 * 受益比例
	 */
	private String bnfLot;
	
	/**
	 * 与被保人关系
	 */
	private String relationToInsured;
	
	/**
	 * 姓名
	 */
	private String name;
	
	/**
	 * 性别
	 */
	private String sex;
	
	/**
	 * 出生日期
	 */
	private Date birthday;
	
	/**
	 * 证件类型
	 */
	private String idType;
	
	/**
	 * 证件号码
	 */
	private String idNo;

	public String getBnfType() {
		return bnfType;
	}

	public void setBnfType(String bnfType) {
		this.bnfType = bnfType;
	}

	public BigDecimal getBnfOrder() {
		return bnfOrder;
	}

	public void setBnfOrder(BigDecimal bnfOrder) {
		this.bnfOrder = bnfOrder;
	}

	public String getBnfLot() {
		return bnfLot;
	}

	public void setBnfLot(String bnfLot) {
		this.bnfLot = bnfLot;
	}

	public String getRelationToInsured() {
		return relationToInsured;
	}

	public void setRelationToInsured(String relationToInsured) {
		this.relationToInsured = relationToInsured;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
}
