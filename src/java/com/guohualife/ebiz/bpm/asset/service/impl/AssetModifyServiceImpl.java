package com.guohualife.ebiz.bpm.asset.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetModifyService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizAssetDetail;
import com.guohualife.platform.common.api.util.StringUtil;

@Service
public class AssetModifyServiceImpl implements AssetModifyService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private OrderBo orderBo;
	
	/**
	 * 处理资产结算
	 * 
	 * @param ebizAsset
	 */
	public EbizAsset dealAssetBalance(AssetModifyDTO assetModifyDTO) {

		/*
		 * 资产结算只会传收益变更量
		 * 本金变更量为0
		 * 资产变更量 = 收益变更量
		 */
		
		String orderNo = assetModifyDTO.getOrderNo();
		String modifyType = assetModifyDTO.getModifyType();
		Date modifyDate = assetModifyDTO.getModifyDate();
		
		BigDecimal modifyPrincipal = assetModifyDTO.getModifyPrincipal();
		BigDecimal modifyIncome = assetModifyDTO.getModifyIncome();
		BigDecimal modifyValue = assetModifyDTO.getModifyValue();
		
		logger.info("订单" + orderNo + "变更类型："
				+ ENUM_ASSET_MODIFY_TYPE.getNameByValue(modifyType)
				+ "，资产变更金额：" + modifyValue + "，本金变更金额：" + modifyPrincipal
				+ "，收益变更金额：" + modifyIncome + "，变更日期" + modifyDate);
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		
		if(ebizAsset.getValueDate().compareTo(modifyDate) > 0){
			logger.info("资产同步日期" + modifyDate + "还未到订单起息日期"
					+ ebizAsset.getValueDate() + "不予同步收益");
			return ebizAsset;
		}
		
		//如果已经全部赎回不再更新收益
		if(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue().equals(
				ebizAsset.getStatus())){
			logger.info("订单" + orderNo + "已经全部赎回，不再更新资产信息");
			return ebizAsset;
		}
		
		setAssetLock(assetModifyDTO, ebizAsset.getAssetId());
		
		ebizAsset = assetBo.saveAssetAndHis(assetModifyDTO);
		
		List<EbizAssetDetail> ebizAssetDetailList = assetBo.getAssetDetail(orderNo, modifyDate);
		if(ebizAssetDetailList == null || ebizAssetDetailList.size() == 0){
			EbizAssetDetail ebizAssetDetail = new EbizAssetDetail();
			ebizAssetDetail.setOrderNo(orderNo);
			ebizAssetDetail.setCreatedDate(new Date());
			ebizAssetDetail.setCreatedUser("交易中心");
			ebizAssetDetail.setModifiedUser("交易中心");
			ebizAssetDetail.setIncome(modifyIncome);
			ebizAssetDetail.setIncomeDate(modifyDate);
			if (ebizAsset.getTotalIncome() == null) {
				ebizAssetDetail.setTotalIncome(modifyIncome);
			} else {
				ebizAssetDetail.setTotalIncome(
						ebizAsset.getTotalIncome().add(modifyIncome));
			}
			ebizAssetDetail.setTotalValue(ebizAsset.getTotalValue());
			ebizAssetDetail.setInvestAmount(ebizAsset.getInvestAmount());
			assetBo.saveAssetDetail(ebizAssetDetail);
		}
		
		return ebizAsset;
	
	}
	
	/**
	 * 处理资产赎回
	 * 
	 * @param assetModifyDTO
	 */
	public EbizAsset dealAssetSurrender(AssetModifyDTO assetModifyDTO){
		
		/*
		 * 保险：只有总资产变更
		 * 债券：总资产和本金都有变更  
		 */
		
		String orderNo = assetModifyDTO.getOrderNo();
		String modifyType = assetModifyDTO.getModifyType();
		Date modifyDate = assetModifyDTO.getModifyDate();
		
		BigDecimal modifyPrincipal = assetModifyDTO.getModifyPrincipal();
		BigDecimal modifyValue = assetModifyDTO.getModifyValue();
		BigDecimal modifyIncome = assetModifyDTO.getModifyIncome();
		
		logger.info("订单" + orderNo + "变更类型："
				+ ENUM_ASSET_MODIFY_TYPE.getNameByValue(modifyType)
				+ "，资产变更金额：" + modifyValue + "，本金变更金额：" + modifyPrincipal
				+ "，收益变更金额：" + modifyIncome + "，变更日期" + modifyDate);
		
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		setAssetLock(assetModifyDTO, ebizAsset.getAssetId());
		
		ebizAsset = assetBo.saveAssetAndHis(assetModifyDTO);
		
		return ebizAsset;
	}
	
	private void setAssetLock(AssetModifyDTO assetModifyDTO, Long assetId){
		assetModifyDTO.setKey(StringUtil.getString(assetId));
		assetModifyDTO.setCode(StringUtil.getString(assetId));
		assetModifyDTO.setMaxWaitTime(5000L);
	}

}
