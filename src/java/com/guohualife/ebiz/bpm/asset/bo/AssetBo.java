package com.guohualife.ebiz.bpm.asset.bo;

import java.util.Date;
import java.util.List;

import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetListQueryDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailPageDTO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizAssetBalance;
import com.guohualife.edb.bpm.model.EbizAssetDetail;
import com.guohualife.edb.bpm.model.EbizAssetHis;
import com.guohualife.edb.bpm.model.EbizAssetPrincipal;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.platform.base.api.model.PageGrid;

/**
 * 资产Bo接口
 * 
 * @author wangxulu
 *
 */
public interface AssetBo {
	
	/**
	 * 保存/更新资产表
	 * 
	 * @param ebizAsset
	 * @return 
	 */
	public Long saveOrUpdateAsset(EbizAsset ebizAsset);
	
	/**
	 * 根据订单号查询订单资产信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizAsset getAssetByOrderNo(String orderNo);
	
	/**
	 * 查询资金交易明细(分页)
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public PageGrid<TradeDetailDTO> getTradeDetailByPage(TradeDetailPageDTO tradeDetailPageDTO);
	
	/**
	 * 查询资金交易明细
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public List<TradeDetailDTO> getTradeDetail(TradeDetailPageDTO tradeDetailPageDTO);
	
	/**
	 * 查询产品资产汇总信息
	 * 
	 * @param customerId
	 * @return
	 */
	public List<AssetDTO> getProductAsset(Long customerId);
	
	/**
	 * 查询产品资产信息(分页)
	 * 
	 * @param assetPageDTO
	 * @return
	 */
	public PageGrid<AssetDTO> getProductAssetByPage(AssetPageDTO assetPageDTO);
	
	/**
	 * 查询每日收益(分页)
	 * 
	 * @param assetPageDTO
	 * @return
	 */
	public PageGrid<DaliyIncomeDTO> getDaliyIncomeByPage(AssetPageDTO assetPageDTO);
	
	/**
	 * 保存资产明细
	 * 
	 * @param ebizAssetDetail
	 */
	public void saveAssetDetail(EbizAssetDetail ebizAssetDetail);
	
	/**
	 * 查询资产变更轨迹
	 * 
	 * @param operType
	 * @param operCode
	 * @param assetId
	 * @return
	 */
	public List<EbizAssetHis> getAssetHis(String operType, String operCode, Long assetId);
	
	/**
	 * 保存资产变更及轨迹
	 * 
	 * @param ebizAsset
	 * @param ebizAssetHis
	 */
	public EbizAsset saveAssetAndHis(AssetModifyDTO assetModifyDTO);
	
	/**
	 * 保存资产及明细
	 * 
	 * @param ebizOrder
	 */
	public void saveNewAssetAndHis(EbizOrder ebizOrder);
	
	/**
	 * 查询资产明细
	 * 
	 * @param orderNo
	 * @param incomeDate
	 * @return
	 */
	public List<EbizAssetDetail> getAssetDetail(String orderNo, Date incomeDate);
	
	/**
	 * 查询资产结算表
	 * 
	 * @param balanceType
	 * @param balanceDate
	 * @param status
	 * @return
	 */
	public EbizAssetBalance getAssetBalance(String balanceType, Date balanceDate, String status);
	
	/**
	 * 保存/更新资产结算表
	 * 
	 * @param ebizAssetBalance
	 * @return
	 */
	public Long saveOrUpdateAssetBalance(EbizAssetBalance ebizAssetBalance);
	
	/**
	 * 查询资产列表
	 * 
	 * @param assetListQueryDTO
	 * @return
	 */
	public List<EbizAsset> getAssetList(AssetListQueryDTO assetListQueryDTO);
	
	/**
	 * 查询最近本金结算信息
	 * 
	 * @param orderNo
	 * @param principalDate
	 * @return
	 */
	public EbizAssetPrincipal getLastAssetPrincipal(String orderNo,
			Date principalDate);
	
	/**
	 * 保存/更新资产本金结算信息
	 * 
	 * @param ebizAssetPrincipal
	 */
	public void saveOrUpdateAssetPrincipal(EbizAssetPrincipal ebizAssetPrincipal);
	
}
