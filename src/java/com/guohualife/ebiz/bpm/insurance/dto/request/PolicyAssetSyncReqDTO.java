package com.guohualife.ebiz.bpm.insurance.dto.request;

import java.util.List;

import com.guohualife.ebiz.bpm.insurance.dto.PolicyAssetSyncDTO;

/**
 * 保单同步受益信息请求DTO
 * 
 * @author wangxulu
 *
 */
public class PolicyAssetSyncReqDTO {
	
	/**
	 * 同步类型(暂不使用)
	 */
	private String syncType;
	
	private List<PolicyAssetSyncDTO> policyAssetSyncList;

	public List<PolicyAssetSyncDTO> getPolicyAssetSyncList() {
		return policyAssetSyncList;
	}

	public void setPolicySyncAssetList(List<PolicyAssetSyncDTO> policyAssetSyncList) {
		this.policyAssetSyncList = policyAssetSyncList;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public void setPolicyAssetSyncList(List<PolicyAssetSyncDTO> policyAssetSyncList) {
		this.policyAssetSyncList = policyAssetSyncList;
	}
	
}
