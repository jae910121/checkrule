package com.guohualife.ebiz.bpm.insurance.service;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.insurance.dto.request.PolicyAssetSyncReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicyAssetSyncResDTO;

/**
 * 保险资产Service接口
 * 
 * @author wangxulu
 *
 */
@WebService
public interface InsuranceAssetService {

	/**
	 * 更新同步收益
	 * 
	 * @param policyAssetSyncReqDTO
	 * @return
	 */
	public PolicyAssetSyncResDTO syncAsset(
			PolicyAssetSyncReqDTO policyAssetSyncReqDTO);
	
}
