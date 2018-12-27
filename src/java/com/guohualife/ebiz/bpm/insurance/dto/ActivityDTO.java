package com.guohualife.ebiz.bpm.insurance.dto;

/**
 * 活动信息DTO
 * 
 * @author wangxulu
 *
 */
public class ActivityDTO {

	/**
	 * 活动编码
	 */
	private String activityCode;
	
	/**
	 * 活动推荐码
	 */
	private String recommendCode;
	
	/**
	 * 参与类型
	 * 01-微信公众平台
	 */
	private String involveType;
	
	/**
	 * 参与用户ID
	 */
	private String involveUserId;
	
	/**
	 * 活动备注信息
	 */
	private String remark;

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public String getInvolveType() {
		return involveType;
	}

	public void setInvolveType(String involveType) {
		this.involveType = involveType;
	}

	public String getInvolveUserId() {
		return involveUserId;
	}

	public void setInvolveUserId(String involveUserId) {
		this.involveUserId = involveUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
