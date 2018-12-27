package com.guohualife.ebiz.bpm.credit.dto.response;

import com.guohualife.platform.base.api.dto.BaseResultDTO;

/**
 * 赎回申请返回DTO
 * 
 * @author wangxulu
 * 
 */
public class CreditSurrenderApplyResDTO extends BaseResultDTO {

	private String surrenderId;

	public String getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(String surrenderId) {
		this.surrenderId = surrenderId;
	}
	
}
