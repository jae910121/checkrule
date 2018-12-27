package com.guohualife.ebiz.bpm.asset.service;

import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.edb.bpm.model.EbizAsset;

public interface AssetModifyService {

	/**
	 * 处理资产结算
	 * 
	 * @param ebizAsset
	 */
	public EbizAsset dealAssetBalance(AssetModifyDTO assetModifyDTO);
	
	/**
	 * 处理资产赎回
	 * 
	 * @param assetModifyDTO
	 */
	public EbizAsset dealAssetSurrender(AssetModifyDTO assetModifyDTO);
	
}
