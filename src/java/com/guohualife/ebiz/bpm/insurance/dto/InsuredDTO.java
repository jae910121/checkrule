package com.guohualife.ebiz.bpm.insurance.dto;

import java.util.Date;

/**
 * 被保人信息DTO
 * 
 * @author wangxulu
 *
 */
public class InsuredDTO {

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
	
	/**
	 * 手机号码
	 */
	private String mobile;
	
	/**
	 * 电话号码
	 */
	private String telephone;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 邮编
	 */
	private String zip;
	
	/**
	 * 与投保人关系
	 */
	private String relationToAppnt;

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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRelationToAppnt() {
		return relationToAppnt;
	}

	public void setRelationToAppnt(String relationToAppnt) {
		this.relationToAppnt = relationToAppnt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	
}
