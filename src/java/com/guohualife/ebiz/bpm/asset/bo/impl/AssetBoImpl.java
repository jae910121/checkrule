/**
 * 
 */
package com.guohualife.ebiz.bpm.asset.bo.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dao.AssetDAO;
import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetListQueryDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.dto.AssetPageDTO;
import com.guohualife.ebiz.bpm.asset.dto.DaliyIncomeDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailDTO;
import com.guohualife.ebiz.bpm.asset.dto.TradeDetailPageDTO;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.dao.EbizAssetBalanceDAO;
import com.guohualife.edb.bpm.dao.EbizAssetDAO;
import com.guohualife.edb.bpm.dao.EbizAssetDetailDAO;
import com.guohualife.edb.bpm.dao.EbizAssetHisDAO;
import com.guohualife.edb.bpm.dao.EbizAssetPrincipalDAO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizAssetBalance;
import com.guohualife.edb.bpm.model.EbizAssetBalanceExample;
import com.guohualife.edb.bpm.model.EbizAssetBalanceExample.Criteria;
import com.guohualife.edb.bpm.model.EbizAssetDetail;
import com.guohualife.edb.bpm.model.EbizAssetDetailExample;
import com.guohualife.edb.bpm.model.EbizAssetExample;
import com.guohualife.edb.bpm.model.EbizAssetHis;
import com.guohualife.edb.bpm.model.EbizAssetHisExample;
import com.guohualife.edb.bpm.model.EbizAssetPrincipal;
import com.guohualife.edb.bpm.model.EbizAssetPrincipalExample;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.base.api.model.PageGrid;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.StringUtil;

/**
 * 资产Bo接口
 * 
 * @author wangxulu
 *
 */
@Component
public class AssetBoImpl extends BaseBOImpl implements AssetBo {
	
	@Resource(name = "ebizAssetDAOImpl")
	private EbizAssetDAO ebizAssetDAO;
	
	@Resource
	private EbizAssetDetailDAO ebizAssetDetailDAO;
	
	@Resource(name = "assetDAOImpl")
	private AssetDAO assetDAO;

	@Resource
	private EbizAssetHisDAO ebizAssetHisDAO;
	
	@Resource
	private EbizAssetPrincipalDAO ebizAssetPrincipalDAO;
	
	@Resource
	private EbizAssetBalanceDAO ebizAssetBalanceDAO;
	
	@Resource
	private ProductService productService; 
	
	@Resource
	private OrderBo orderBo;
	
	/**
	 * 保存/更新资产表
	 * 
	 * @param ebizAsset
	 * @return 
	 */
	public Long saveOrUpdateAsset(EbizAsset ebizAsset) {
		ebizAsset.setModifiedDate(new Date());
		if("".equals(StringUtil.getString(ebizAsset.getAssetId()))){
			ebizAsset.setIsDelete((short) 0);
			Long assetId = ebizAssetDAO.insertSelective(ebizAsset);
			return assetId;
		}else{
			ebizAssetDAO.updateByPrimaryKeySelective(ebizAsset);
			return ebizAsset.getAssetId();
		}
	}
	
	/**
	 * 保存资产明细
	 * 
	 * @param ebizAssetDetail
	 */
	public void saveAssetDetail(EbizAssetDetail ebizAssetDetail) {
		ebizAssetDetail.setModifiedDate(new Date());
		ebizAssetDetail.setIsDelete((short) 0);
		ebizAssetDetailDAO.insertSelective(ebizAssetDetail);
	}
	
	/**
	 * 保存/更新资产结算表
	 * 
	 * @param ebizAssetBalance
	 */
	public Long saveOrUpdateAssetBalance(EbizAssetBalance ebizAssetBalance){
		ebizAssetBalance.setModifiedDate(new Date());
		if(StringUtil.isEmpty(StringUtil.getString(ebizAssetBalance.getBalanceId()))){
			ebizAssetBalance.setIsDelete((short)0);
			Long balanceId = ebizAssetBalanceDAO.insertSelective(ebizAssetBalance);
			ebizAssetBalance.setBalanceId(balanceId);
		}else{
			ebizAssetBalanceDAO.updateByPrimaryKeySelective(ebizAssetBalance);
		}
		return ebizAssetBalance.getBalanceId();
	}
	
	/**
	 * 保存/更新本金结算表信息
	 * 
	 * @param ebizAssetPrincipal
	 */
	public void saveOrUpdateAssetPrincipal(EbizAssetPrincipal ebizAssetPrincipal) {
		ebizAssetPrincipal.setModifiedDate(new Date());
		
		EbizAssetPrincipalExample ebizAssetPrincipalExample = new EbizAssetPrincipalExample();
		ebizAssetPrincipalExample.createCriteria()
				.andOrderNoEqualTo(ebizAssetPrincipal.getOrderNo())
				.andPrincipalDateEqualTo(ebizAssetPrincipal.getPrincipalDate())
				.andIsDeleteEqualTo((short)0);
		List<EbizAssetPrincipal> assetPrincipals = ebizAssetPrincipalDAO.selectByExample(ebizAssetPrincipalExample);
		if(assetPrincipals == null || assetPrincipals.size() == 0){
			ebizAssetPrincipal.setCreatedDate(new Date());
			
			ebizAssetPrincipal.setIsDelete((short) 0);
			ebizAssetPrincipalDAO.insertSelective(ebizAssetPrincipal);
			return;
		}
		
		ebizAssetPrincipalDAO.updateByExampleSelective(ebizAssetPrincipal, ebizAssetPrincipalExample);
	}

	/**
	 * 查询订单资产信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizAsset getAssetByOrderNo(String orderNo) {
		logger.info("根据订单号" + orderNo + "查询资产信息");
		EbizAssetExample ebizAssetExample = new EbizAssetExample();
		ebizAssetExample.createCriteria().andOrderNoEqualTo(orderNo);
		List<EbizAsset> ebizAssets = ebizAssetDAO.selectByExample(ebizAssetExample);
		if(ebizAssets != null && ebizAssets.size() > 0){
			return ebizAssets.get(0);
		}
		return null;
	}

	/**
	 * 查询产品资产汇总信息
	 * 
	 * @param customerId
	 * @return
	 */
	public List<AssetDTO> getProductAsset(Long customerId){
		return assetDAO.getProductAsset(customerId);
	}
	
	/**
	 * 查询产品资产信息(分页)
	 * 
	 * @param customerId
	 * @param productCode
	 * @return
	 */
	public PageGrid<AssetDTO> getProductAssetByPage(AssetPageDTO assetPageDTO) {
		PageGrid<AssetDTO> pageGrid = assetDAO.getProductAssetByPage(assetPageDTO);
		for(AssetDTO assetDTO : pageGrid.getData()){
			
			assetDTO.setIsSurrender("1");
			if(!ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue().equals(
					assetDTO.getOrderStatus())){
				assetDTO.setIsSurrender("0");
				continue;
			}
			
			 EbizOrder ebizOrder = orderBo.getOrder(assetDTO.getOrderNo());
			 EbizProductProperty ebizProductProperty = productService.getSingleProductPropertyByOrderType(ebizOrder.getProductCode(),
					ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_REDEEM
					.getValue(), ebizOrder.getOrderType());
			 
			 if(ebizProductProperty == null){
				assetDTO.setIsSurrender("0");
				continue;
			 }
			 
			 if("0".equals(ebizProductProperty.getPropertyValue())){
				assetDTO.setIsSurrender("0");
				continue;
			 }
			 
			 if("2".equals(ebizProductProperty.getPropertyValue())){
				if (assetDTO.getEndCloseDate() != null) {
					if (assetDTO.getEndCloseDate().compareTo(
							DateUtil.parseDate(DateUtil.formatDate(new Date(),
									"yyyy-MM-dd"), "yyyy-MM-dd")) > 0) {
						assetDTO.setIsSurrender("0");
						continue;
					}
				}
			 }
		}
		return pageGrid;
	}

	/**
	 * 查询每日收益(分页)
	 * 
	 * @param assetPageDTO
	 * @return
	 */
	public PageGrid<DaliyIncomeDTO> getDaliyIncomeByPage(AssetPageDTO assetPageDTO) {
		return assetDAO.getDaliyIncomeByPage(assetPageDTO);
	}

	/**
	 * 查询资金交易明细(分页)
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public PageGrid<TradeDetailDTO> getTradeDetailByPage(TradeDetailPageDTO tradeDetailPageDTO) {
		return assetDAO.getTradeDetailByPage(tradeDetailPageDTO);
	}
	
	/**
	 * 查询资金交易明细
	 * 
	 * @param tradeDetailPageDTO
	 * @return
	 */
	public List<TradeDetailDTO> getTradeDetail(
			TradeDetailPageDTO tradeDetailPageDTO) {
		return assetDAO.getTradeDetail(tradeDetailPageDTO);
	}

	/**
	 * 查询资产变更轨迹
	 * 
	 * @param operType
	 * @param operCode
	 * @param assetId
	 * @return
	 */
	public List<EbizAssetHis> getAssetHis(String operType, String operCode, Long assetId) {
		EbizAssetHisExample ebizAssetHisExample = new EbizAssetHisExample();
		ebizAssetHisExample.createCriteria()
			.andOperCodeEqualTo(operCode)
			.andOperTypeEqualTo(operType)
			.andAssetIdEqualTo(assetId);
		return ebizAssetHisDAO.selectByExample(ebizAssetHisExample);
	}
	
	/**
	 * 查询资产明细
	 * 
	 * @param orderNo
	 * @param incomeDate
	 * @return
	 */
	public List<EbizAssetDetail> getAssetDetail(String orderNo, Date incomeDate) {
		EbizAssetDetailExample ebizAssetDetailExample = new EbizAssetDetailExample();
		ebizAssetDetailExample.createCriteria().andIncomeDateEqualTo(incomeDate).andOrderNoEqualTo(orderNo);
		return ebizAssetDetailDAO.selectByExample(ebizAssetDetailExample);
	}
	
	/**
	 * 查询资产结算表
	 * 
	 * @param balanceType
	 * @param balanceDate
	 * @param status
	 * @return
	 */
	public EbizAssetBalance getAssetBalance(String balanceType, Date balanceDate, String status) {
		EbizAssetBalanceExample ebizAssetBalanceExample = new EbizAssetBalanceExample();
		Criteria criteria = ebizAssetBalanceExample.createCriteria();
		criteria.andBalanceDateEqualTo(balanceDate)
				.andBalanceTypeEqualTo(balanceType)
				.andIsDeleteEqualTo((short) 0);
		if(StringUtil.isNotEmpty(status)){
			criteria.andStatusEqualTo(status);
		}
		List<EbizAssetBalance> assetBalances = ebizAssetBalanceDAO.selectByExample(ebizAssetBalanceExample);
		if(assetBalances != null && assetBalances.size() > 0){
			return assetBalances.get(0);
		}
		return null;
	}
	
	/**
	 * 查询本金结算信息
	 * 
	 * @param orderNo
	 * @param principalDate
	 * @return
	 */
	public EbizAssetPrincipal getLastAssetPrincipal(String orderNo,
			Date principalDate) {
		return assetDAO.getLastAssetPrincipal(orderNo, principalDate);
	}
	
	/**
	 * 查询资产列表
	 * 
	 * @param assetListQueryDTO
	 * @return
	 */
	public List<EbizAsset> getAssetList(AssetListQueryDTO assetListQueryDTO){
		EbizAssetExample ebizAssetExample = new EbizAssetExample();
		com.guohualife.edb.bpm.model.EbizAssetExample.Criteria criteria 
			= ebizAssetExample.createCriteria();
		criteria.andIsDeleteEqualTo((short) 0);
		if(assetListQueryDTO.getProductCodeList() != null && assetListQueryDTO.getProductCodeList().size() > 0){
			criteria.andProductCodeIn(assetListQueryDTO.getProductCodeList());
		}
		
		if(StringUtil.isNotEmpty(assetListQueryDTO.getProductType())){
			criteria.andProductTypeEqualTo(assetListQueryDTO.getProductType());
		}
		
		if(StringUtil.isNotEmpty(assetListQueryDTO.getStatus())){
			criteria.andStatusEqualTo(assetListQueryDTO.getStatus());
		}
		
		if(assetListQueryDTO.getLastIncomeDate() != null){
			criteria.andLastIncomeDateLessThan(assetListQueryDTO.getLastIncomeDate());
		}
		
		if(assetListQueryDTO.getValueDate() != null){
			criteria.andValueDateLessThan(assetListQueryDTO.getValueDate());
		}
		
		if(assetListQueryDTO.getExpiryDate() != null){
			criteria.andExpiryDateGreaterThanOrEqualTo(assetListQueryDTO.getExpiryDate());
		}
		
		return ebizAssetDAO.selectByExample(ebizAssetExample);
	}

	/**
	 * 保存资产变更及轨迹
	 * 
	 * @param ebizAsset
	 * @param ebizAssetHis
	 */
	public EbizAsset saveAssetAndHis(AssetModifyDTO assetModifyDTO){
		
		EbizAsset asset = getAssetByOrderNo(assetModifyDTO.getOrderNo());
		
		List<EbizAssetHis> assetHisList = 
				getAssetHis(
						assetModifyDTO.getModifyType(),
						assetModifyDTO.getOperCode(), 
						asset.getAssetId());
		if(assetHisList != null && assetHisList.size() > 0){
			logger.info("订单" + assetModifyDTO.getOrderNo() + "资产变更编号"
					+ assetModifyDTO.getOperCode() + "已同步资产");
			return asset;
		}
		
		logger.info("订单" + assetModifyDTO.getOrderNo() + "变更前总资产：" + asset.getTotalValue()
				+ "，本金：" + asset.getInvestAmount());
		
		BigDecimal beforeValue = asset.getTotalValue();
		BigDecimal investAmount = asset.getInvestAmount();

		if (assetModifyDTO.getModifyPrincipal() != null) {
			asset.setInvestAmount(
					asset.getInvestAmount().add(
							assetModifyDTO.getModifyPrincipal()));
		}
		
		if (assetModifyDTO.getModifyValue() != null) {
			asset.setTotalValue(
					asset.getTotalValue().add(
							assetModifyDTO.getModifyValue()));
		}
		
		if (assetModifyDTO.getModifyIncome() != null) {
			if (ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.ASSET_SYNC.getValue()
					.equals(assetModifyDTO.getModifyType())) {
				asset.setLastIncome(assetModifyDTO.getModifyIncome());
				asset.setLastIncomeDate(assetModifyDTO.getModifyDate());
			}
			
			if(asset.getTotalIncome() == null){
				asset.setTotalIncome(assetModifyDTO.getModifyIncome());
			}else{
				asset.setTotalIncome(
						asset.getTotalIncome().add(
								assetModifyDTO.getModifyIncome()));
			}
		}
		
		//总资产为0 且 最近收益时间为当天或为空 则更新资产状态为全部赎回
		if(BigDecimal.ZERO.compareTo(asset.getTotalValue()) == 0 && 
				(asset.getLastIncomeDate() == null || 
				DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").compareTo(
						asset.getLastIncomeDate()) == 0)){
			logger.info("订单" + asset.getOrderNo()
					+ "总资产为0 且 最近收益时间为当天或为空 更新资产状态为全部赎回");
			asset.setStatus(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue());
		}
		
		logger.info("订单" + assetModifyDTO.getOrderNo() + "变更后总资产：" + asset.getTotalValue()
				+ "，本金：" + asset.getInvestAmount());
		
		saveOrUpdateAsset(asset);
		
		// 更新结算本金
		if (assetModifyDTO.getModifyPrincipal() != null) {
			EbizAssetPrincipal ebizAssetPrincipal = new EbizAssetPrincipal();
			ebizAssetPrincipal.setOrderNo(assetModifyDTO.getOrderNo());
			ebizAssetPrincipal.setPrincipalDate(assetModifyDTO.getModifyDate());
			ebizAssetPrincipal.setCreatedUser("交易中心");
			ebizAssetPrincipal.setModifiedUser("交易中心");
			ebizAssetPrincipal.setPrincipalAmount(asset.getInvestAmount());
			saveOrUpdateAssetPrincipal(ebizAssetPrincipal);
		}
		
		EbizAssetHis ebizAssetHis = new EbizAssetHis();
		ebizAssetHis.setAssetId(asset.getAssetId());
		ebizAssetHis.setOperType(assetModifyDTO.getModifyType());
		ebizAssetHis.setOperCode(assetModifyDTO.getOperCode());
		ebizAssetHis.setBeforeValue(beforeValue);
		ebizAssetHis.setOperValue(assetModifyDTO.getModifyValue());
		ebizAssetHis.setAfterValue(asset.getTotalValue());
		if (assetModifyDTO.getModifyPrincipal() != null) {
			ebizAssetHis.setBeforePrincipal(investAmount);
			ebizAssetHis.setAfterPrincipal(asset.getInvestAmount());
			ebizAssetHis.setOperPrincipal(assetModifyDTO.getModifyPrincipal());
		}
		saveAssetHis(ebizAssetHis);
		
		return asset;
	}

	/**
	 * 保存资产及明细
	 * 
	 * @param ebizOrder
	 */
	public void saveNewAssetAndHis(EbizOrder ebizOrder) {
		logger.info("订单号" + ebizOrder.getOrderNo() + "保存资产及明细");
		String orderNo = ebizOrder.getOrderNo();
		EbizAsset ebizAsset = getAssetByOrderNo(orderNo);
		
		Long assetId = null;
		
		if(ebizAsset == null){
			logger.info("订单号" + ebizOrder.getOrderNo() + "没有资产信息，新增资产");
			ebizAsset = new EbizAsset();
			ebizAsset.setCreatedDate(new Date());
			ebizAsset.setCreatedUser("交易中心");
			ebizAsset.setModifiedUser("交易中心");
			ebizAsset.setTotalIncome(BigDecimal.ZERO);
			ebizAsset.setLastIncome(BigDecimal.ZERO);
			ebizAsset.setOrderNo(ebizOrder.getOrderNo());
			ebizAsset.setInvestAmount(ebizOrder.getOrderAmount());
			ebizAsset.setTotalValue(ebizOrder.getOrderAmount());
			
			ebizAsset.setCustomerId(ebizOrder.getCustomerId());
			ebizAsset.setProductCode(ebizOrder.getProductCode());
			ebizAsset.setProductType(ebizOrder.getProductType());
			ebizAsset.setStatus(ConstantsForAsset.ENUM_ASSET_STATUS.EFFECTIVE.getValue());
			ebizAsset.setExpiryDate(ebizOrder.getExpiryDate());
			ebizAsset.setValueDate(ebizOrder.getValueDate());
			assetId = saveOrUpdateAsset(ebizAsset);
			logger.info("保存资产信息完成，assetId:" + assetId);
		}else{
			logger.info("订单号" + ebizOrder.getOrderNo() + "有资产信息，assetId" + ebizAsset.getAssetId());
			assetId = ebizAsset.getAssetId();
		}
		
		EbizAssetHis ebizAssetHis = new EbizAssetHis();
		ebizAssetHis.setAfterValue(ebizOrder.getOrderAmount());
		ebizAssetHis.setAssetId(assetId);
		ebizAssetHis.setBeforeValue(BigDecimal.ZERO);
		ebizAssetHis.setOperCode(DateUtil.formatDate(ebizOrder.getSuccessDate(), "yyyyMMdd"));
		ebizAssetHis.setOperType(ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.ORDER_DEAL.getValue());
		ebizAssetHis.setOperValue(ebizOrder.getOrderAmount());
		saveAssetHis(ebizAssetHis);
		
		if (ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue().equals(
				ebizOrder.getProductType())) {
			EbizAssetPrincipal ebizAssetPrincipal = new EbizAssetPrincipal();
			ebizAssetPrincipal.setOrderNo(ebizOrder.getOrderNo());
			ebizAssetPrincipal.setCreatedUser("交易中心");
			ebizAssetPrincipal.setModifiedUser("交易中心");
			ebizAssetPrincipal.setPrincipalAmount(ebizOrder.getOrderAmount());
			ebizAssetPrincipal.setPrincipalDate(ebizOrder.getValueDate());
			saveOrUpdateAssetPrincipal(ebizAssetPrincipal);
		}
	}
	
	/**
	 * 保存资产轨迹表
	 * 
	 * @param ebizAsset
	 */
	private void saveAssetHis(EbizAssetHis ebizAssetHis) {
		ebizAssetHis.setCreatedDate(new Date());
		ebizAssetHis.setCreatedUser("交易中心");
		ebizAssetHis.setModifiedDate(new Date());
		ebizAssetHis.setModifiedUser("交易中心");
		ebizAssetHis.setIsDelete((short) 0);
		ebizAssetHisDAO.insertSelective(ebizAssetHis);
	}
	
}
