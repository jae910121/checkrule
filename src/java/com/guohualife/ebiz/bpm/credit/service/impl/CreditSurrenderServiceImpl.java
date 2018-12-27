package com.guohualife.ebiz.bpm.credit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.constants.ConstantsForAsset;
import com.guohualife.ebiz.bpm.asset.dto.AssetModifyDTO;
import com.guohualife.ebiz.bpm.asset.service.AssetModifyService;
import com.guohualife.ebiz.bpm.credit.bo.CreditBo;
import com.guohualife.ebiz.bpm.credit.service.CreditSurrenderService;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderListQueryDTO;
import com.guohualife.ebiz.bpm.order.dto.SurrenderApplyDTO;
import com.guohualife.ebiz.common.base.ConstantsForCommon;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.ebiz.message.dto.MessageInfoDTO;
import com.guohualife.ebiz.message.service.MessageService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.constant.ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE;
import com.guohualife.ebiz.product.dto.EbizProductPropertyDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.edb.payment.dao.EpayYqPayTransactionsDAO;
import com.guohualife.edb.payment.model.EpayYqPayTransactions;
import com.guohualife.edb.payment.model.EpayYqPayTransactionsExample;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.StringUtil;

@Service
public class CreditSurrenderServiceImpl implements CreditSurrenderService {
	
	private static final Log logger = LogFactory
			.getLog(CreditServiceImpl.class);
	
	@Resource
	private CreditBo creditBo;
	
	@Resource
	private EpayYqPayTransactionsDAO epayYqPayTransactionsDAO;
	
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private AssetModifyService assetModifyService;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private MessageService messageService;
	
	
	/**
	 * 处理赎回
	 * 
	 */
	public void dealSurrender(EbizSurrender ebizSurrender){
		/*
		 * 1.更新资金划拨状态-01-待划拨
		 * 2.更新资产表 + 本金结算表 + 新增资产轨迹表
		 */
		logger.info("处理赎回信息开始");
		AssetModifyDTO assetModifyDTO = new AssetModifyDTO();
		assetModifyDTO.setModifyValue(
				ebizSurrender.getApplyAmount().multiply(new BigDecimal("-1")));
		assetModifyDTO.setModifyPrincipal(
				ebizSurrender.getSurrenderPrincipal().multiply(new BigDecimal("-1")));
		assetModifyDTO.setModifyIncome(
				ebizSurrender.getApplyAmount().subtract(
						ebizSurrender.getSurrenderPrincipal()).multiply(new BigDecimal("-1")));
		assetModifyDTO.setModifyDate(
				DateUtil.parseDate(DateUtil.formatDate(
				ebizSurrender.getSurrenderDate(), "yyyy-MM-dd"), "yyyy-MM-dd"));
		assetModifyDTO.setOperCode(StringUtil.getString(ebizSurrender.getSurrenderId()));
		assetModifyDTO.setModifyType(
				ConstantsForAsset.ENUM_ASSET_MODIFY_TYPE.SURRENDER.getValue());
		assetModifyDTO.setOrderNo(ebizSurrender.getOrderNo());
		EbizAsset ebizAsset = assetModifyService.dealAssetSurrender(assetModifyDTO);
		
		ebizSurrender.setTransferStatus(
				ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.WAIT_TRNASFER.getValue());
		ebizSurrender.setStatus(ConstantsForOrder.ENUM_SURRENDER_STATUS.ACCEPTSUCCESS.getValue());
		orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
		
		EbizOrder ebizOrder = orderBo.getOrder(ebizSurrender.getOrderNo());
		ebizOrder.setOrderStatus(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
		
		if(ConstantsForAsset.ENUM_ASSET_STATUS.SURRENDER_INVEST_ALL.getValue().equals(
				ebizAsset.getStatus())){
			ebizOrder.setOrderStatus(ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());
		}	
		orderBo.saveOrUpdateOrder(ebizOrder);
		
		logger.info("处理赎回信息结束");
	}

	/**
	 * 处理赎回资金划拨
	 * 
	 * @param ebizSurrender
	 */
	public void dealSurrenderTransfer() {
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;

		List<String> statusList = new ArrayList<String>();
		statusList.add(
				ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.WAIT_TRNASFER.getValue());

		List<EbizSurrender> ebizSurrenders = orderSurrenderBo
				.getSurrenderByTransferStatus(statusList);
		logger.info("本次扫描到待划拨数据" + ebizSurrenders.size() + "条");

		if (ebizSurrenders != null && ebizSurrenders.size() > 0) {
			for (EbizSurrender ebizSurrender : ebizSurrenders) {
				try {
					EbizOrder ebizOrder = orderBo.getOrder(ebizSurrender.getOrderNo());
					EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(ebizSurrender.getOrderNo());
					
					if(ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue().equals(
							ebizOrder.getOrderType())){
						EpayYqPayTransactions epayYqPayTransactions = new EpayYqPayTransactions();
						epayYqPayTransactions.setOrderid(StringUtil.getString(ebizSurrender.getSurrenderId()));
						EbizProductProperty ebizProductProperty = productService.getSingleProductPropertyByOrderType(
								ebizOrder.getProductCode(),
								ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.GETMONEY_TYPE.getValue(), 
								ebizOrder.getOrderType());
						if(ebizProductProperty != null){
							epayYqPayTransactions.setSourceflag(ebizProductProperty.getPropertyValue());
						}
						epayYqPayTransactions.setRecbank(ebizOrderAccount.getBankCode());
						epayYqPayTransactions.setRecaccount(ebizOrderAccount.getCardBookCode());
						epayYqPayTransactions.setRecmoney(ebizSurrender.getSurrenderAmount().doubleValue());
						epayYqPayTransactions.setIsprivate((short)1);
						epayYqPayTransactions.setOthersysbillcode(ebizSurrender.getOrderNo());
						epayYqPayTransactions.setCancelstate((short)1); //未撤销
						epayYqPayTransactions.setPaystate((short)1); //未支付
						epayYqPayTransactions.setOthersystemdealstate((short)1); //未更新
						epayYqPayTransactions.setDealstate((short)1); //未抽档
						epayYqPayTransactions.setCreationDate(DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
						epayYqPayTransactions.setCreationTime(DateUtil.formatDate(new Date(), "hh:mm:ss"));
						epayYqPayTransactions.setCardflag((short)0); //借记卡
						epayYqPayTransactions.setCredentials((short)0);
						CustomerQryResultDTO customerQryResultDTO = orderBo.getCustomerInfo(ebizOrder.getCustomerId());
						epayYqPayTransactions.setIdcard(customerQryResultDTO.getCustomerDTOList().get(0).getIdno());
						epayYqPayTransactions.setRecaccountname(customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
						epayYqPayTransactionsDAO.insertSelective(epayYqPayTransactions);
						ebizSurrender.setTransferStatus(
								ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.TRNASFERING.getValue());
						orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
						successNum++;
					}
				} catch (ServiceException e) {
					logger.warn(
							"订单" + ebizSurrender.getOrderNo() + "资金划拨处理异常", e);
					exceptionNum++;
					continue;
				}
			}
		}

		logger.info("本次资金划拨处理成功" + successNum + "条，异常：" + exceptionNum + "条");
	}

	public void dealSurrenderTransferResult() {
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;

		List<String> statusList = new ArrayList<String>();
		statusList
				.add(ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.TRNASFERING
						.getValue());
		List<EbizSurrender> ebizSurrenders = orderSurrenderBo
				.getSurrenderByTransferStatus(statusList);
		logger.info("本次扫描到待查询结果数据" + ebizSurrenders.size() + "条");
		if (ebizSurrenders != null && ebizSurrenders.size() > 0) {
			for (EbizSurrender ebizSurrender : ebizSurrenders) {
				try {
					List<Short> dealStateList = new ArrayList<Short>();
					dealStateList.add((short)2); //支付成功
					dealStateList.add((short)3); //支付失败
					EpayYqPayTransactionsExample epayYqPayTransactionsExample = 
							new EpayYqPayTransactionsExample();
					epayYqPayTransactionsExample.createCriteria()
						.andOrderidEqualTo(StringUtil.getString(ebizSurrender.getSurrenderId()))
						.andDealstateEqualTo((short)2) //已抽档
						.andPaystateIn(dealStateList);
					List<EpayYqPayTransactions> epayYqPayTransList =
							epayYqPayTransactionsDAO.selectByExample(epayYqPayTransactionsExample);
					if(epayYqPayTransList == null || epayYqPayTransList.size() == 0){
						logger.info("订单" + ebizSurrender.getOrderNo() + "资金划拨本次扫描不到数据");
						continue;
					}
					
					EpayYqPayTransactions epayYqPayTransactions = epayYqPayTransList.get(0);
					if("2".equals(StringUtil.getString(epayYqPayTransactions.getPaystate()))){
						logger.info("订单" + ebizSurrender.getOrderNo() + "资金划拨成功");
						
						ebizSurrender.setTransferStatus(
								ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.TRNASFER_SUCCESS.getValue());
						orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
						
						epayYqPayTransactions.setOthersystemdealstate((short)2); //更新成功
						epayYqPayTransactionsDAO.updateByPrimaryKeySelective(epayYqPayTransactions);
						successNum++;
						
						sendTransferSuccessMsg(ebizSurrender);
				
					}
					
					if("3".equals(StringUtil.getString(epayYqPayTransactions.getPaystate()))){
						logger.info("订单" + ebizSurrender.getOrderNo()
								+ "资金划拨失败："
								+ epayYqPayTransactions.getErrorMessage());
						
						ebizSurrender.setTransferStatus(
								ConstantsForOrder.ENUM_SURRENDER_TRANSFER_STATUS.TRNASFER_FAILED.getValue());
						ebizSurrender.setTransferRemark(epayYqPayTransactions.getFailreason());
						orderSurrenderBo.saveOrUpdateSurrender(ebizSurrender);
						
						epayYqPayTransactions.setOthersystemdealstate((short)2); //更新成功
						epayYqPayTransactionsDAO.updateByPrimaryKeySelective(epayYqPayTransactions);
						successNum++;
					}
					
				} catch (ServiceException e) {
					logger.warn(
							"订单" + ebizSurrender.getOrderNo() + "查询资金划拨处理结果异常", e);
					exceptionNum++;
					continue;
				}
			}
		}
		logger.info("本次资金划拨结果处理成功" + successNum + "条，异常：" + exceptionNum + "条");
	}
	
	
	/**
	 * 到期自动赎回
	 */
	public void autoSurrender(){
		// 成功笔数
		Integer successNum = 0;
		// 异常笔数
		Integer exceptionNum = 0;
		
		Date expiryDate = DateUtil.parseDate(DateUtil.formatDate(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
		
		OrderListQueryDTO orderListQueryDTO = new OrderListQueryDTO();
		orderListQueryDTO.setExpiryDate(expiryDate);
		orderListQueryDTO.setProductType(ConstantsForProduct.ENUM_PRODUCT_TYPE.CREDITOR.getValue());
		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
		orderListQueryDTO.setStatusList(statusList);
		List<EbizOrder> ebizOrders = orderBo.getOrderList(orderListQueryDTO);
		logger.info("本次扫描到待自动赎回数据" + ebizOrders.size() + "条");
		for(EbizOrder ebizOrder : ebizOrders){
			EbizAsset ebizAsset = assetBo.getAssetByOrderNo(ebizOrder.getOrderNo());
			if(ebizAsset.getLastIncomeDate().compareTo(ebizOrder.getExpiryDate()) != 0){
				logger.info("订单" + ebizOrder.getOrderNo() + "最近收益日期不为到期日，无法自动赎回");
				exceptionNum++;
				continue;
			}
			
			boolean flag = true;
			List<EbizSurrender> ebizSurrenderList = orderSurrenderBo
					.getSurrenderByOrderNo(ebizAsset.getOrderNo());
			if (ebizSurrenderList != null && ebizSurrenderList.size() > 0) {
				for (EbizSurrender ebizSurrender : ebizSurrenderList) {
					if (ConstantsForOrder.ENUM_SURRENDER_STATUS.INFORM
							.getValue().equals(ebizSurrender.getStatus())
							|| ConstantsForOrder.ENUM_SURRENDER_STATUS.ACCEPTAUDIT
							.getValue().equals(ebizSurrender.getStatus())) {
						flag = false;
						break;
					}
				}
			}
			
			if(!flag){
				logger.info("订单" + ebizOrder.getOrderNo() + "仍有待处理的赎回，暂不做自动赎回");
				exceptionNum++;
				continue;
			}
			
			SurrenderApplyDTO surrenderApplyDTO = new SurrenderApplyDTO();
			surrenderApplyDTO.setApplyMoney(ebizAsset.getTotalValue());
			surrenderApplyDTO.setCustomerId(ebizOrder.getCustomerId());
			surrenderApplyDTO.setOrderNo(ebizOrder.getOrderNo());
			BigDecimal surrenderFeeRate = new BigDecimal(0);
			EbizProductProperty ebizProductProperty = productService
					.getSingleProductPropertyByOrderType(
							ebizOrder.getProductCode(),
							ENUM_PRODUCT_PROPERTY_TYPE.SURRENDER_FEE_RATE
									.getValue(), ebizOrder.getOrderType());
			if (ebizProductProperty != null){
				surrenderFeeRate = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			surrenderApplyDTO.setSurrenderFee(surrenderFeeRate);
			surrenderApplyDTO.setAvailableMoney(surrenderApplyDTO.getApplyMoney().subtract(surrenderFeeRate));
			surrenderApplyDTO.setSurrenderPrincipal(ebizAsset.getInvestAmount());
			surrenderApplyDTO.setSurrenderType(ConstantsForOrder.ENUM_SURRENDER_TYPE.AUTOREDEEM.getValue());
			surrenderApplyDTO.setPlatformType(ConstantsForCommon.ENUM_PLATFORM_TYPE.WX.getValue());
			orderSurrenderBo.applySurrender(surrenderApplyDTO);
			
			if(StringUtil.isEmpty(surrenderApplyDTO.getErrorMessage())){
				ebizOrder.setOrderStatus(ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMING.getValue());
				orderBo.saveOrUpdateOrder(ebizOrder);
				successNum++;
			}
		}
		logger.info("本次自动赎回处理成功" + successNum + "条，异常：" + exceptionNum + "条");
	}
	
	private void sendTransferSuccessMsg(EbizSurrender ebizSurrender) throws ServiceException{
		EbizOrder ebizOrder = orderBo.getOrder(ebizSurrender.getOrderNo());
		EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(ebizSurrender.getOrderNo());
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("applyAmount", ebizSurrender.getSurrenderAmount().toString());
		paramMap.put("productName", ebizOrder.getProductName());
		paramMap.put("customerName", ebizOrder.getProductName());
		paramMap.put("bankName", ebizOrderAccount.getBankName());
		paramMap.put("cardBookCode", 
				StringUtils.substring(ebizOrderAccount.getBankName(), ebizOrderAccount.getBankName().length() - 4, ebizOrderAccount.getBankName().length()));
		paramMap.put("surrenderDate", DateUtil.formatDate(ebizSurrender.getSurrenderDate(), "yyyy-MM-dd"));
		paramMap.put("applyAmount", ebizSurrender.getApplyAmount().toString());
		paramMap.put("surrenderPrincipal", ebizSurrender.getSurrenderPrincipal().toString());
		paramMap.put("surrenderIncome", ebizSurrender.getApplyAmount().subtract(ebizSurrender.getSurrenderPrincipal()).toString());

		EbizProductPropertyDTO ebizProductPropertyDTO = productService.getProductPropertyByOrderType(
				ebizOrder.getProductCode(), 
				"redeem_arrive_template", 
				ebizOrder.getOrderType());
		
		if(ebizProductPropertyDTO.getEbizProductPropertyList() != null &&
				ebizProductPropertyDTO.getEbizProductPropertyList().size() > 0){
			
			MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
			messageInfoDTO.setCustomerId(StringUtil.getString(ebizOrder.getCustomerId()));
			messageInfoDTO.setParamMap(paramMap);
			
			for(EbizProductProperty ebizProductProperty : ebizProductPropertyDTO.getEbizProductPropertyList()){
				
				messageInfoDTO.setTemplateCode(ebizProductProperty.getPropertyValue());
				
				try{
					messageService.sendMessageByCustomerId(messageInfoDTO);
				}catch(Exception e){
					logger.info("订单" + ebizSurrender.getOrderNo() + "资金划拨成功发送消息异常", e);
				}
				
			}
		}
	}
	
}
