package com.guohualife.ebiz.bpm.asset.dao;

import java.util.Date;
import java.util.List;

import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailPageDTO;
import com.guohualife.edb.bpm.model.EbizAssetPrincipal;
import com.guohualife.platform.base.api.model.PageGrid;

/**
 * 资产DAO接口
 * 
 * @author wangxulu
 *
 */
public interface AssetDAO {

	/**
	 * 查询产品资产汇总信息
	 * 
	 * @param customerId
	 * @param productCode
	 * @return
	 */
	public List<AssetDTO> getProductAsset(Long customerId);
	
	/**
	 * 查询产品资产信息(分页)
	 * 
	 * @param customerId
	 * @param productCode
	 * @return
	 */
	public PageGrid<AssetDTO> getProductAssetByPage(AssetPageDTO assetPageDTO);
	
	/**
	 * 查询资金交易列表(分页)
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public PageGrid<TradeDetailDTO> getTradeDetailByPage(TradeDetailPageDTO tradeDetailPageDTO);
	
	/**
	 * 查询资金交易列表
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public List<TradeDetailDTO> getTradeDetail(TradeDetailPageDTO tradeDetailPageDTO);
	
	/**
	 * 查询每日收益(分页)
	 * 
	 * @param assetPageDTO
	 * @return
	 */
	public PageGrid<DaliyIncomeDTO> getDaliyIncomeByPage(AssetPageDTO assetPageDTO);
	
	/**
	 * 查询距离principalDate最近一次的本金结算记录
	 * 
	 * @param orderNo
	 * @param principalDate
	 * @return
	 */
	public EbizAssetPrincipal getLastAssetPrincipal(String orderNo, Date principalDate);
	
}
