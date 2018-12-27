package com.guohualife.ebiz.bpm.insurance.service;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.guohualife.common.util.cache.client.MemCache;
import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.edb.bpm.model.EbizAsset;

public class InsuranceAssetServiceTest extends BaseTester{

	@Resource(name = "memCacheClient")
	private MemCache cacheClient;

	private static String NAMESPACE = "gh.ebiz.util.lock";
	
	@Resource
	private AssetBo assetBo;
	
	@Test
	public void testSyncAssetLock() throws Throwable{
		String key = "lockObj.873918.873923";
		cacheClient.add(key, NAMESPACE, 1);
//		Object b = cacheClient.get(key, NAMESPACE);
//		System.out.println(b);
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo("20150505020100874664");
		
		AssetModifyDTO assetModifyDTO = new AssetModifyDTO();
		assetModifyDTO.setKey("873918");
		assetModifyDTO.setCode("873923");
//		assetModifyDTO.setEbizAsset(ebizAsset);
//		assetModifyDTO.setModifyAmount(new BigDecimal("2000.00"));
		assetModifyDTO.setModifyDate(new Date());
		assetModifyDTO.setModifyType(ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.SURRENDER.getValue());
//		assetModifyDTO.setSurrenderId(StringUtil.getString("873923"));
//		
//		assetBo.saveAssetAndHis(assetModifyDTO);
	}
}
