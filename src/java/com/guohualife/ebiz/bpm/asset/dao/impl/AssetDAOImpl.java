/**
 * 
 */
package com.guohualife.ebiz.bpm.asset.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.guohualife.ebiz.bpm.asset.dao.AssetDAO;
import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailPageDTO;
import com.guohualife.edb.bpm.dao.impl.EbizAssetDAOImpl;
import com.guohualife.edb.bpm.model.EbizAssetPrincipal;
import com.guohualife.platform.base.api.model.PageGrid;


/**
 * 资产DAO实现
 *
 * @author wangxulu
 */
@Repository
public class AssetDAOImpl extends EbizAssetDAOImpl implements AssetDAO {

	/**
	 * 查询产品资产汇总信息
	 * 
	 * @param customerId
	 * @param productCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AssetDTO> getProductAsset(Long customerId){
		return this.queryForList("AssetDAOImpl.getProductAsset", customerId);
	}
	
	/**
	 * 查询产品资产信息分页
	 * 
	 * @param customerId
	 * @param productCode
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public PageGrid<AssetDTO> getProductAssetByPage(AssetPageDTO assetPageDTO) {
		return this.pageForObject("AssetDAOImpl.getProductAssetByPage", assetPageDTO);
	}

	/**
	 * 查询每日收益(分页)
	 * 
	 * @param assetPageDTO
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public PageGrid<DaliyIncomeDTO> getDaliyIncomeByPage(AssetPageDTO assetPageDTO) {
		return this.pageForObject("AssetDAOImpl.getDaliyIncomeByPage", assetPageDTO);
	}

	/**
	 * 查询资金交易列表(分页)
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public PageGrid<TradeDetailDTO> getTradeDetailByPage(TradeDetailPageDTO tradeDetailPageDTO) {
		return this.pageForObject("AssetDAOImpl.getTradeDetailByPage", tradeDetailPageDTO);
	}

	/**
	 * 查询资金交易列表
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TradeDetailDTO> getTradeDetail(
			TradeDetailPageDTO tradeDetailPageDTO) {
		return this.queryForList("AssetDAOImpl.getTradeDetail", tradeDetailPageDTO);
	}

	/**
	 * 查询距离principalDate最近一次的本金结算记录
	 * 
	 * @param orderNo
	 * @param principalDate
	 * @return
	 */
	public EbizAssetPrincipal getLastAssetPrincipal(String orderNo, Date principalDate) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderNo", orderNo);
		paramMap.put("principalDate", principalDate);
		return (EbizAssetPrincipal) this.queryForObject("AssetDAOImpl.getLastAssetPrincipal", paramMap);
	}
	
}
