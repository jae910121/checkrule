package com.guohualife.ebiz.bpm.asset.dto.response;

import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.platform.base.api.model.PageGrid;


/**
 * 收益查询返回结果DTO
 * 
 * @author wangxulu
 *
 */
public class ProductAssetResDTO {
	
	/**
	 * 产品资产分页列表
	 */
	private PageGrid<AssetDTO> productAssetGrid;

	public PageGrid<AssetDTO> getProductAssetGrid() {
		return productAssetGrid;
	}

	public void setProductAssetGrid(PageGrid<AssetDTO> productAssetGrid) {
		this.productAssetGrid = productAssetGrid;
	}
	
}
