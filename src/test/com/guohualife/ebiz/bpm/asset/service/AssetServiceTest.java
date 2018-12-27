package com.guohualife.ebiz.bpm.asset.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.guohualife.ebiz.bpm.asset.dto.AssetSummaryDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.TradeDetailReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.TradeDetailResDTO;
import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.platform.base.api.model.PageQueryDTO;
import com.guohualife.platform.common.api.util.XmlUtil;

public class AssetServiceTest extends BaseTester{
	
	@SuppressWarnings("rawtypes")
	private Class[] clazz = {String.class, List.class, AssetSummaryDTO.class};
	
	@Resource
	private AssetService assetService;
	
	@Test
	public void testGetAssetSummaryInfo(){
		AssetSummaryDTO assetSummaryDTO = 
				assetService.getSummaryInfo(new BigDecimal("97139").longValue());
		System.out.println("查询客户资产概要信息：\n" + XmlUtil.toXml(assetSummaryDTO, clazz));
	}
	
	@Test
	public void testTradeDetail(){
		TradeDetailReqDTO tradeDetailReqDTO = new TradeDetailReqDTO();
		tradeDetailReqDTO.setCustomerId(new BigDecimal("85546").longValue());
		PageQueryDTO pageQueryDTO = new PageQueryDTO();
		pageQueryDTO.setDoCount(true);
//		pageQueryDTO.setQueryAll(true);
		pageQueryDTO.setPageSize(10);
		tradeDetailReqDTO.setPageQueryDTO(pageQueryDTO);
//		tradeDetailReqDTO.getPageQueryDTO().setQueryAll(true);
		TradeDetailResDTO tradeDetailResDTO = 
				assetService.getTradeDetail(tradeDetailReqDTO);
		System.out.println("查询资金交易明细：\n" + XmlUtil.toXml(tradeDetailResDTO, clazz));
	}
//	
//	@Test
//	public void testProductIncome(){
//		ProductAssetReqDTO productAssetReqDTO = new ProductAssetReqDTO();
//		productAssetReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		productAssetReqDTO.setProductCode("121");
//		ProductAssetResDTO productAssetResDTO = 
//				assetService.getProductAsset(productAssetReqDTO);
//		System.out.println("查询产品收益：\n" + XmlUtil.toXml(productAssetResDTO, clazz));
//	}
//	
//	@Test
//	public void testIncomeDetail(){
//		DaliyIncomeReqDTO daliyIncomeReqDTO = new DaliyIncomeReqDTO();
//		daliyIncomeReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		DaliyIncomeResDTO daliyIncomeResDTO = 
//				assetService.getDaliyIncome(daliyIncomeReqDTO);
//		System.out.println("查询收益明细：\n" + XmlUtil.toXml(daliyIncomeResDTO, clazz));
//	}
}
