package com.guohualife.ebiz.bpm.insurance.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetModifyService;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyAssetSyncDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicyAssetSyncReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicyAssetSyncResDTO;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceAssetService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

@Service
public class InsuranceAssetServiceImpl implements InsuranceAssetService {

	private final Log logger = LogFactory.getLog(getClass());

	@Resource
	AssetModifyService assetModifyService;

	@Resource
	private OrderBo orderBo;

	/**
	 * 更新同步收益
	 * 
	 * @param policyAssetSyncReqDTO
	 * @return
	 */
	public PolicyAssetSyncResDTO syncAsset(
			PolicyAssetSyncReqDTO policyAssetSyncReqDTO) {
		logger.info("更新同步收益接口syncAsset入参PolicyAssetSyncReqDTO：\n"
				+ XmlUtil.toXml(policyAssetSyncReqDTO, new Class[] {}));

		List<PolicyAssetSyncDTO> policySyncAssetList = policyAssetSyncReqDTO
				.getPolicyAssetSyncList();

		for (PolicyAssetSyncDTO policyAssetSyncDTO : policySyncAssetList) {
			BigDecimal recentIncome = policyAssetSyncDTO.getRecentIncome();
			Date modifyDate = policyAssetSyncDTO.getModifyDate();

			AssetModifyDTO assetModifyDTO = new AssetModifyDTO();
			assetModifyDTO.setModifyIncome(recentIncome);
			assetModifyDTO.setModifyValue(recentIncome); // 资产变更量 = 收益变更量
			assetModifyDTO.setModifyDate(modifyDate);
			assetModifyDTO.setOperCode(
					DateUtil.formatDate(modifyDate, "yyyyMMdd"));
			assetModifyDTO.setModifyType(
					ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.ASSET_SYNC.getValue());
			assetModifyDTO.setOrderNo(policyAssetSyncDTO.getOrderNo());
			EbizAsset ebizAsset = assetModifyService.dealAssetBalance(assetModifyDTO);
			
			if(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue().equals(
					ebizAsset.getStatus())){
				orderBo.updateOrderStatus(
						assetModifyDTO.getOrderNo(), 
						ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());
			}

		}

		PolicyAssetSyncResDTO policyAssetSyncResDTO = new PolicyAssetSyncResDTO();
		logger.info("更新同步收益接口syncAsset出参PolicyAssetSyncResDTO：\n"
				+ XmlUtil.toXml(policyAssetSyncResDTO, new Class[] {}));

		return policyAssetSyncResDTO;

	}

}
