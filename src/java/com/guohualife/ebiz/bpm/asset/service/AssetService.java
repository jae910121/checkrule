package com.guohualife.ebiz.bpm.asset.service;

import java.util.List;

import javax.jws.WebService;

import com.guohualife.ebiz.bpm.asset.dto.AssetSummaryDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.DaliyIncomeReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.ProductAssetReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.request.TradeDetailReqDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.DaliyIncomeResDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.ProductAssetResDTO;
import com.guohualife.ebiz.bpm.asset.dto.response.TradeDetailResDTO;

/**
 * 资产Service接口
 * 
 * @author wangxulu
 *
 */
@WebService
public interface AssetService {

	/**
	 * 查询资产概要信息
	 * 
	 * @param customerId
	 * @return
	 */
	public AssetSummaryDTO getSummaryInfo(Long customerId);
	
	/**
	 * 查询针对资金增减变更(订单成交 + 赎回成功)
	 * (最大到客户，最小到某一订单) 
	 * 
	 * @param tradeDetailReqDTO
	 * @return
	 */
	public TradeDetailResDTO getTradeDetail(TradeDetailReqDTO tradeDetailReqDTO);
	
	/**
	 * 查询针对资金增减变更(订单成交 + 赎回成功)
	 * (最大到客户，最小到某一订单) 
	 * 
	 * @param tradeDetailReqDTO
	 * @return
	 */
	public List<TradeDetailDTO> getTradeDetailList(TradeDetailReqDTO tradeDetailReqDTO);
	
	/**
	 * 查询每日收益明细
	 * (最大到客户，最小到某一订单的某一天收益)
	 * 
	 * @param daliyIncomeReqDTO
	 * @return
	 */
	public DaliyIncomeResDTO getDaliyIncome(DaliyIncomeReqDTO daliyIncomeReqDTO);

	/**
	 * 查询产品资产信息
	 * 
	 * @param productAssetReqDTO
	 * @return
	 */
	public ProductAssetResDTO getProductAsset(ProductAssetReqDTO productAssetReqDTO);
}
