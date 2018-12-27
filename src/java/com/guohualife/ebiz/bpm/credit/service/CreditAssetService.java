package com.guohualife.ebiz.bpm.credit.service;

import java.util.Date;


/**
 * 债权资产Service接口
 * 
 * @author wangxulu
 *
 */
public interface CreditAssetService {
	
	/**
	 * 计算资产
	 * 
	 * @param balanceDate
	 */
	public void dealAsset(Date balanceDate);
}
