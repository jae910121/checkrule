package com.guohualife.ebiz.bpm.asset.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产概要信息DTO
 * 
 * @author wangxulu
 *
 */
public class AssetSummaryDTO {

	/**
	 * 总资产
	 */
	private BigDecimal totalAsset = BigDecimal.ZERO;
	
	/**
	 * 总收益
	 */
	private BigDecimal totalIncome = BigDecimal.ZERO;
	
	/**
	 * 最近收益总和
	 */
	private BigDecimal totalRecentIncome = BigDecimal.ZERO;

	/**
	 * 总本金
	 */
	private BigDecimal totalInvestAmount = BigDecimal.ZERO;
	
	/**
	 * 产品资产信息
	 */
	private List<AssetDTO> assetList;
	
	public BigDecimal getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(BigDecimal totalAsset) {
		this.totalAsset = totalAsset;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalInvestAmount() {
		return totalInvestAmount;
	}

	public void setTotalInvestAmount(BigDecimal totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}

	public List<AssetDTO> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<AssetDTO> assetList) {
		this.assetList = assetList;
	}
	
	public BigDecimal getTotalRecentIncome() {
		return totalRecentIncome;
	}

	public void setTotalRecentIncome(BigDecimal totalRecentIncome) {
		this.totalRecentIncome = totalRecentIncome;
	}
	
}
