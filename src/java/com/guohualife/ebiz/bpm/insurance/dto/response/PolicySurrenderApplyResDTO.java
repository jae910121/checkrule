package com.guohualife.ebiz.bpm.insurance.dto.response;

import com.guohualife.platform.base.api.dto.BaseResultDTO;


public class PolicySurrenderApplyResDTO extends BaseResultDTO{
	
	private String surrenderId;

	public String getSurrenderId() {
		return surrenderId;
	}

	public void setSurrenderId(String surrenderId) {
		this.surrenderId = surrenderId;
	}
	
}
