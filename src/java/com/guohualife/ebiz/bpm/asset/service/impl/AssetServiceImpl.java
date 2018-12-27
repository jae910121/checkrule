/**
 * 
 */
package com.guohualife.ebiz.bpm.asset.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetSummaryDTO;
import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.DaliyIncomeReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.ProductAssetReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.TradeDetailReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.DaliyIncomeResDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.ProductAssetResDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.TradeDetailResDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetService;
import com.guohualife.platform.base.api.model.PageGrid;
import com.guohualife.platform.common.api.util.ReflectionUtil;
import com.guohualife.platform.common.api.util.StringUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

/**
 * 资产Service接口实现
 * 
 * @author wangxulu
 *
 */
@Service
public class AssetServiceImpl implements AssetService {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private AssetBo assetBo;
	
	/**
	 * 查询资产概要信息
	 * 
	 * @param customerId
	 * @return
	 */
	public AssetSummaryDTO getSummaryInfo(Long customerId) {
		logger.info("查询客户" + customerId + "资产概要信息");
		logger.info("查询资产概要信息接口getSummaryInfo入参customerId：\n" + XmlUtil.toXml(customerId, new Class[]{}));

		/*
		 * 总资产：EbizAsset.totalValue
		 * 总收益：EbizAsset.totalIncome
		 * 总本金：EbziAsset.TotalInvestAmount
		 * 以上这三个都是基于customer维度的，查询customer的所有有效订单
		 */
		BigDecimal totalAsset = BigDecimal.ZERO;
		BigDecimal totalIncome = BigDecimal.ZERO;
		BigDecimal totalInvestAmount = BigDecimal.ZERO;
		BigDecimal totalRecentIncome = BigDecimal.ZERO;
		
		List<AssetDTO> assets = assetBo.getProductAsset(customerId);
		for(AssetDTO assetDTO : assets){
			if(assetDTO.getTotalAsset() != null){
				totalAsset = totalAsset.add(assetDTO.getTotalAsset());
			}
			if(assetDTO.getTotalIncome() != null){
				totalIncome = totalIncome.add(assetDTO.getTotalIncome());
			}
			if(assetDTO.getRecentIncome() != null){
				totalRecentIncome = totalRecentIncome.add(assetDTO.getRecentIncome());
			}
			totalInvestAmount = totalInvestAmount.add(assetDTO.getTotalInvestAmount());
		}
		
		AssetSummaryDTO assetSummaryDTO = new AssetSummaryDTO();
		assetSummaryDTO.setTotalAsset(totalAsset);
		assetSummaryDTO.setTotalInvestAmount(totalInvestAmount);
		assetSummaryDTO.setTotalIncome(totalIncome);
		assetSummaryDTO.setTotalRecentIncome(totalRecentIncome);
		assetSummaryDTO.setAssetList(assets);
		logger.info("查询资产概要信息接口getSummaryInfo出参assetSummaryDTO：\n" + XmlUtil.toXml(assetSummaryDTO, new Class[]{}));

		return assetSummaryDTO;
	}

	/**
	 * 查询针对资金增减变更(订单成交 + 赎回成功)
	 * (最大到客户，最小到某一订单) 
	 * 
	 * @param tradeDetailReqDTO
	 * @return
	 */
	public TradeDetailResDTO getTradeDetail(TradeDetailReqDTO tradeDetailReqDTO) {
		logger.info("查询针对资金增减变更接口getTradeDetail入参tradeDetailReqDTO：\n" + XmlUtil.toXml(tradeDetailReqDTO, new Class[]{}));

		TradeDetailResDTO tradeDetailResDTO = new TradeDetailResDTO();
		
		TradeDetailPageDTO tradeDetailPageDTO = new TradeDetailPageDTO();
		tradeDetailPageDTO.setCustomerId(tradeDetailReqDTO.getCustomerId());
		if(!"".equals(StringUtil.getString(tradeDetailReqDTO.getOrderNo()))){
			tradeDetailPageDTO.setOrderNo(tradeDetailReqDTO.getOrderNo());
		}
		if(tradeDetailReqDTO.getQueryBeginDate() != null){
			tradeDetailPageDTO.setQueryBeginDate(tradeDetailReqDTO.getQueryBeginDate());
		}
		if(tradeDetailReqDTO.getQueryEndDate() != null){
			tradeDetailPageDTO.setQueryEndDate(tradeDetailReqDTO.getQueryEndDate());
		}
		ReflectionUtil.copyProperties(tradeDetailReqDTO.getPageQueryDTO(), tradeDetailPageDTO);
		PageGrid<TradeDetailDTO> pageGrid = assetBo.getTradeDetailByPage(tradeDetailPageDTO);
		
		tradeDetailResDTO.setTradeDetailGrid(pageGrid);
		logger.info("查询针对资金增减变更接口getTradeDetail出参tradeDetailResDTO：\n" + XmlUtil.toXml(tradeDetailResDTO, new Class[]{}));

		return tradeDetailResDTO;
	}

	/**
	 * 查询每日收益明细
	 * (最大到客户，最小到某一订单的某一天收益)
	 * 
	 * @param daliyIncomeReqDTO
	 * @return
	 */
	public DaliyIncomeResDTO getDaliyIncome(DaliyIncomeReqDTO daliyIncomeReqDTO) {
		logger.info("查询每日收益明细接口getDaliyIncome入参daliyIncomeReqDTO：\n" + XmlUtil.toXml(daliyIncomeReqDTO, new Class[]{}));

		DaliyIncomeResDTO daliyIncomeResDTO = new DaliyIncomeResDTO();
		
		AssetPageDTO assetPageDTO = new AssetPageDTO();
		assetPageDTO.setCustomerId(daliyIncomeReqDTO.getCustomerId());
		if(daliyIncomeReqDTO.getQueryBeginDate() != null){
			assetPageDTO.setQueryBeginDate(daliyIncomeReqDTO.getQueryBeginDate());
		}
		if(daliyIncomeReqDTO.getQueryEndDate() != null){
			assetPageDTO.setQueryEndDate(daliyIncomeReqDTO.getQueryEndDate());
		}
		ReflectionUtil.copyProperties(daliyIncomeReqDTO.getPageQueryDTO(), assetPageDTO);
		
		PageGrid<DaliyIncomeDTO> pageGrid = assetBo.getDaliyIncomeByPage(assetPageDTO);
		daliyIncomeResDTO.setDaliyIncomeGrid(pageGrid);
		logger.info("查询每日收益明细接口getDaliyIncome出参daliyIncomeResDTO：\n" + XmlUtil.toXml(daliyIncomeResDTO, new Class[]{}));

		return daliyIncomeResDTO;
	}

	/**
	 * 查询产品资产信息
	 * 
	 * @param productAssetReqDTO
	 * @return
	 */
	public ProductAssetResDTO getProductAsset(ProductAssetReqDTO productAssetReqDTO) {
		logger.info("查询产品资产信息接口getProductAsset入参productAssetReqDTO：\n" + XmlUtil.toXml(productAssetReqDTO, new Class[]{}));

		ProductAssetResDTO productAssetResDTO = new ProductAssetResDTO();
		
		AssetPageDTO assetPageDTO = new AssetPageDTO();
		assetPageDTO.setCustomerId(productAssetReqDTO.getCustomerId());
		if(productAssetReqDTO.getProductCode() != null){
			assetPageDTO.setProductCode(productAssetReqDTO.getProductCode());
		}
		ReflectionUtil.copyProperties(productAssetReqDTO.getPageQueryDTO(), assetPageDTO);
		
		PageGrid<AssetDTO> pageGrid = assetBo.getProductAssetByPage(assetPageDTO);
		productAssetResDTO.setProductAssetGrid(pageGrid);
		logger.info("查询产品资产信息接口getProductAsset出参productAssetResDTO：\n" + XmlUtil.toXml(productAssetResDTO, new Class[]{}));

		return productAssetResDTO;
	}

	public List<TradeDetailDTO> getTradeDetailList(TradeDetailReqDTO tradeDetailReqDTO) {
		
		TradeDetailPageDTO tradeDetailPageDTO = new TradeDetailPageDTO();
		tradeDetailPageDTO.setCustomerId(tradeDetailReqDTO.getCustomerId());
		if(!"".equals(StringUtil.getString(tradeDetailReqDTO.getOrderNo()))){
			tradeDetailPageDTO.setOrderNo(tradeDetailReqDTO.getOrderNo());
		}
		if(tradeDetailReqDTO.getQueryBeginDate() != null){
			tradeDetailPageDTO.setQueryBeginDate(tradeDetailReqDTO.getQueryBeginDate());
		}
		if(tradeDetailReqDTO.getQueryEndDate() != null){
			tradeDetailPageDTO.setQueryEndDate(tradeDetailReqDTO.getQueryEndDate());
		}
		
		return assetBo.getTradeDetail(tradeDetailPageDTO);
	}

}
