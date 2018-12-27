package com.guohualife.ebiz.bpm.order.bo.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.asset.dto.AssetDTO;
import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderAccountDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderListQueryDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessDTO;
import com.guohualife.ebiz.bpm.order.dto.OrderSuccessMsgDTO;
import com.guohualife.ebiz.bpm.order.dto.SurrenderSuccessMsgDTO;
import com.guohualife.ebiz.bpm.order.dto.ThirdOrderDTO;
import com.guohualife.ebiz.bpm.order.dto.request.OrderReqDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;
import com.guohualife.ebiz.common.config.constant.ConstantsForConfig;
import com.guohualife.ebiz.common.config.dto.EbizPropertyValueDTO;
import com.guohualife.ebiz.common.config.dto.EbizSyBanksDTO;
import com.guohualife.ebiz.common.config.service.ConfigService;
import com.guohualife.ebiz.customer.account.dto.CustomerQryDTO;
import com.guohualife.ebiz.customer.account.dto.CustomerQryResultDTO;
import com.guohualife.ebiz.customer.account.service.CustomerAccountService;
import com.guohualife.ebiz.customer.common.constant.ConstantForCustomer.ENUM_SEARCH_TYPE;
import com.guohualife.ebiz.customer.common.constant.ENUM_CUSTOMER_THIRD_TYPE;
import com.guohualife.ebiz.message.dto.MessageInfoDTO;
import com.guohualife.ebiz.message.service.MessageService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.constant.ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.dao.EbizOrderAccountDAO;
import com.guohualife.edb.bpm.dao.EbizOrderDAO;
import com.guohualife.edb.bpm.dao.EbizThirdOrderDAO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizOrderAccountExample;
import com.guohualife.edb.bpm.model.EbizOrderExample;
import com.guohualife.edb.bpm.model.EbizOrderExample.Criteria;
import com.guohualife.edb.bpm.model.EbizSurrender;
import com.guohualife.edb.bpm.model.EbizThirdOrder;
import com.guohualife.edb.bpm.model.EbizThirdOrderExample;
import com.guohualife.edb.common.util.GenerateSequenceUtil;
import com.guohualife.edb.config.model.EbizOrderTypeProperty;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.JsonUtil;
import com.guohualife.platform.common.api.util.StringUtil;

/**
 * 订单Bo实现
 * 
 * @author wangxulu
 *
 */
@Component
public class OrderBoImpl extends BaseBOImpl implements OrderBo {

	@Resource(name = "ebizOrderDAOImpl")
	private EbizOrderDAO ebizOrderDAO;
	
	@Resource
	private EbizThirdOrderDAO ebizThirdOrderDAO;
	
	@Resource
	private EbizOrderAccountDAO ebizOrderAccountDAO;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private CustomerAccountService customerAccountService;
	
	@Resource
	private ConfigService configService;
	
	@Resource
	private AssetBo assetBo;
	
	@Resource
	private BpmBaseBo bpmBaseBo;
	
	@Resource
	private MessageService messageService;
	
	@Resource
	private Properties bpmProperties;
	
	/**
	 * 保存订单信息
	 * 
	 * @param orderDTO
	 * @return
	 * @throws ServiceException
	 */
	public String saveOrder(OrderDTO orderDTO) throws ServiceException {
		long start = System.currentTimeMillis();
		
		String orderNo = saveEbizOrder(orderDTO);
		logger.info("保存订单信息" + orderNo + "，耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");

		if (orderDTO.getThirdOrderDTO() != null) {
			saveThirdOrder(orderDTO.getThirdOrderDTO(), orderNo);
			logger.info("订单" + orderNo + "保存第三方订单信息耗时："
					+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		}
		
		return orderNo;
	}
	
	/**
	 * 申请订单支付
	 * 
	 * @param orderAccountDTO
	 * @throws ServiceException
	 */
	public void applyOrderPay(OrderAccountDTO orderAccountDTO)
			throws ServiceException {
		long start = System.currentTimeMillis();
		
		String orderNo = orderAccountDTO.getOrderNo();
		//orderAccountDTO.setOrderType(getOrder(orderNo).getOrderType());
		saveOrderAccount(orderAccountDTO);
		logger.info("订单" + orderNo + "保存账户信息耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");

		updateOrderStatus(orderNo,
				ConstantsForOrder.ENUM_ORDER_STATUS.PAYING.getValue());
		logger.info("订单" + orderNo + "更新支付中耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");

		if (orderAccountDTO.getThirdOrderDTO() != null) {
			ThirdOrderDTO thirdOrderDTO = orderAccountDTO.getThirdOrderDTO();
			EbizThirdOrder ebizThirdOrder = new EbizThirdOrder();
			ebizThirdOrder.setThirdOrderNo(thirdOrderDTO.getThirdOrderNo());
			ebizThirdOrder.setThirdAccDate(thirdOrderDTO.getThirdAccDate());
			ebizThirdOrder.setThirdAccName(thirdOrderDTO.getThirdAccName());
			ebizThirdOrder.setThirdAccNo(thirdOrderDTO.getThirdAccNo());
			ebizThirdOrder.setThirdPayNo(thirdOrderDTO.getThirdPayNo());
			ebizThirdOrder.setThirdUserId(new BigDecimal(thirdOrderDTO.getThirdUserId()));
			updateThirdOrder(ebizThirdOrder);
			logger.info("订单" + orderNo + "更新第三方订单信息耗时："
					+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		}
		
	}
	
	/**
	 * 根据订单号查询订单表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizOrder getOrder(String orderNo){
		EbizOrderExample ebizOrderExample = new EbizOrderExample();
		ebizOrderExample.createCriteria().andOrderNoEqualTo(orderNo);
		return ebizOrderDAO.selectByPrimaryKey(orderNo);
	}
	
	/**
	 * 查询订单信息
	 * 
	 * @param orderReqDTO
	 * @param orderResDTO
	 * @return
	 */
	public void getOrderInfo(OrderReqDTO orderReqDTO,
			OrderResDTO orderResDTO) {
		String orderNo = orderReqDTO.getOrderNo();
		EbizOrder ebizOrder = getOrder(orderNo);
		
		orderResDTO.setCustomerId(ebizOrder.getCustomerId());
		orderResDTO.setOrderAmount(ebizOrder.getOrderAmount());
		orderResDTO.setOrderNo(ebizOrder.getOrderNo());
		orderResDTO.setOrderStatus(ebizOrder.getOrderStatus());
		orderResDTO.setOrderType(ebizOrder.getOrderType());
		orderResDTO.setProductCode(ebizOrder.getProductCode());
		orderResDTO.setProductName(ebizOrder.getProductName());
		orderResDTO.setProductType(ebizOrder.getProductType());
		orderResDTO.setValueDate(ebizOrder.getValueDate());
		orderResDTO.setExpiryDate(ebizOrder.getExpiryDate());
		orderResDTO.setSuccessDate(ebizOrder.getSuccessDate());
		// 过封闭期到期日期
		orderResDTO.setEndCloseDate(ebizOrder.getEndCloseDate());
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		AssetDTO assetDTO = null;
		if (ebizAsset != null) {
			assetDTO = new AssetDTO();
			assetDTO.setCreatedDate(ebizAsset.getCreatedDate());
			assetDTO.setCreatedUser(ebizAsset.getCreatedUser());
			assetDTO.setExpiryDate(ebizAsset.getExpiryDate());
			assetDTO.setModifiedDate(ebizAsset.getModifiedDate());
			assetDTO.setModifiedUser(ebizAsset.getModifiedUser());
			assetDTO.setOrderNo(ebizAsset.getOrderNo());
			assetDTO.setProductCode(ebizAsset.getProductCode());
			assetDTO.setProductType(ebizAsset.getProductType());
			assetDTO.setRecentIncome(ebizAsset.getLastIncome());
			assetDTO.setSuccessDate(ebizAsset.getCreatedDate());
			assetDTO.setTotalAsset(ebizAsset.getTotalValue());
			assetDTO.setTotalIncome(ebizAsset.getTotalIncome());
			assetDTO.setTotalInvestAmount(ebizAsset.getInvestAmount());
			assetDTO.setValueDate(ebizAsset.getValueDate());
		}
		orderResDTO.setAssetDTO(assetDTO);
		
		EbizOrderAccount ebizOrderAccount = getOrderAccount(orderNo);
		if(ebizOrderAccount != null){
			orderResDTO.setBankCode(ebizOrderAccount.getBankCode());
			orderResDTO.setBankName(ebizOrderAccount.getBankName());
			//截取后四位
			orderResDTO.setCardBookCode(
					StringUtil.substring(
							ebizOrderAccount.getCardBookCode(), ebizOrderAccount.getCardBookCode().length() - 4));
		}
		
	}
	
	/**
	 * 根据订单号查询订单账户表信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizOrderAccount getOrderAccount(String orderNo){
		EbizOrderAccountExample ebizOrderAccountExample = new EbizOrderAccountExample();
		ebizOrderAccountExample.createCriteria().andOrderNoEqualTo(orderNo);
		List<EbizOrderAccount> ebizOrderAccounts = ebizOrderAccountDAO.selectByExample(ebizOrderAccountExample);
		if(ebizOrderAccounts != null && ebizOrderAccounts.size() > 0){
			return ebizOrderAccounts.get(0);
		}
		return null;
	}
	
	/**
	 * 查询订单列表
	 * 
	 * @param orderListQueryDTO
	 * @return
	 */
	public List<EbizOrder> getOrderList(OrderListQueryDTO orderListQueryDTO) {
		EbizOrderExample ebizOrderExample = new EbizOrderExample();
		Criteria criteria = ebizOrderExample.createCriteria();
		criteria.andIsDeleteEqualTo((short)0);
		if(orderListQueryDTO.getStatusList() != null && orderListQueryDTO.getStatusList().size() > 0){
			criteria.andOrderStatusIn(orderListQueryDTO.getStatusList());
		}
		if(orderListQueryDTO.getProductCodeList() != null && orderListQueryDTO.getProductCodeList().size() > 0){
			criteria.andProductCodeIn(orderListQueryDTO.getProductCodeList());
		}
		if(orderListQueryDTO.getOrderTypeList() != null && orderListQueryDTO.getOrderTypeList().size() > 0){
			criteria.andOrderTypeIn(orderListQueryDTO.getOrderTypeList());
		}
		if(StringUtil.isNotEmpty(orderListQueryDTO.getProductType())){
			criteria.andProductTypeEqualTo(orderListQueryDTO.getProductType());
		}
		if(orderListQueryDTO.getSuccessDate() != null){
			criteria.andSuccessDateLessThan(orderListQueryDTO.getSuccessDate());
		}
		if(orderListQueryDTO.getValueDate() != null){
			criteria.andValueDateLessThan(orderListQueryDTO.getValueDate());
		}
		if(orderListQueryDTO.getExpiryDate() != null){
			criteria.andExpiryDateLessThanOrEqualTo(orderListQueryDTO.getExpiryDate());
		}
		if(orderListQueryDTO.getCustomerId() != null){
			criteria.andCustomerIdEqualTo(orderListQueryDTO.getCustomerId());
		}
		return ebizOrderDAO.selectByExample(ebizOrderExample);
	}
	
	/**
	 * 更新订单状态
	 * 
	 * @param orderNo
	 * @param status
	 */
	public void updateOrderStatus(String orderNo, String status) {
		EbizOrder ebizOrder = new EbizOrder();
		ebizOrder.setOrderNo(orderNo);
		ebizOrder.setOrderStatus(status);
		saveOrUpdateOrder(ebizOrder);
	}
	
	/**
	 * 校验购买客户是否存在
	 * 
	 * @param customerId
	 * @return
	 */
	public CustomerQryResultDTO getCustomerInfo(Long customerId) {
		logger.info("查询customerId" + customerId + "客户信息");
		CustomerQryDTO customerQryDTO = new CustomerQryDTO();
		customerQryDTO.setCustomerId(customerId);
		List<String> searchTypes = new ArrayList<String>();
		searchTypes.add(ENUM_SEARCH_TYPE.CUS_INFO.getValue());
		searchTypes.add(ENUM_SEARCH_TYPE.CUS_THIRD_INFO.getValue());
		customerQryDTO.setSearchTypeLst(searchTypes);
		customerQryDTO.setThirdUserType(ENUM_CUSTOMER_THIRD_TYPE.WX.getValue());
		return customerAccountService.queryCustomerInfo(customerQryDTO);
	}
	
	/**
	 * 根据客户ID和查询类型查询订单列表
	 * 
	 * @param customerId
	 * @param queryType
	 * @return
	 */
	public List<EbizOrder> getOrderByCustomerId(Long customerId,
			ConstantsForOrder.ENUM_ORDER_QUERY_TYPE queryType) {
		
		EbizOrderExample ebizOrderExample = new EbizOrderExample();
		Criteria criteria = ebizOrderExample.createCriteria();
		criteria.andCustomerIdEqualTo(customerId);
		if(ConstantsForOrder.ENUM_ORDER_QUERY_TYPE.EFFECTIVE.equals(queryType)){
			List<String> statusList = new ArrayList<String>();
			statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
			statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMING.getValue());
			criteria.andOrderStatusIn(statusList);
		}	
		return ebizOrderDAO.selectByExample(ebizOrderExample);
	}
	
	/**
	 * 保存/更新订单表
	 * 
	 * @param ebizOrder
	 * @return
	 */
	public String saveOrUpdateOrder(EbizOrder ebizOrder) {
		ebizOrder.setModifiedDate(new Date());
		if ("".equals(StringUtil.getString(ebizOrder.getOrderNo()))) {
			String orderNo = 
					generateOrderNo(ebizOrder.getOrderType(), ebizOrder.getProductType());
			ebizOrder.setOrderNo(orderNo);
			ebizOrder.setIsDelete((short)0);
			ebizOrderDAO.insertSelective(ebizOrder);
		} else {
			ebizOrderDAO.updateByPrimaryKeySelective(ebizOrder);
		}
		return ebizOrder.getOrderNo();
	}
	
	/**
	 * 订单成交处理
	 * 
	 * @param orderNo
	 */
	public void dealOrderSuccess(OrderSuccessDTO orderSuccessDTO) {		
		EbizOrder ebizOrder = getOrder(orderSuccessDTO.getOrderNo());
		
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();
		
		ebizOrder.setOrderNo(orderSuccessDTO.getOrderNo());
		ebizOrder.setValueDate(orderSuccessDTO.getValueDate());
		ebizOrder.setExpiryDate(orderSuccessDTO.getExpiryDate());
		if(orderSuccessDTO.getExpiryDate() == null){
			ebizOrder.setExpiryDate(getExpiryDate(productCode, orderType, ebizOrder.getValueDate()));
		}
		ebizOrder.setSuccessDate(orderSuccessDTO.getSuccessDate());
		ebizOrder.setBusinessNo(orderSuccessDTO.getBusinessNo());
		ebizOrder.setOrderStatus(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
		ebizOrder.setOrderType(orderSuccessDTO.getOrderType());
		
		//设置封闭期结束日期
		ebizOrder.setEndCloseDate(
				getOrderEndCloseDate(productCode, orderType, ebizOrder.getValueDate()));
		
		// 校验是否为第一单
		HashMap<String, String> orderRemarkMap = new HashMap<String, String>();
		Integer firstBuy = checkIsFirstBuy(ebizOrder.getCustomerId(),
				ebizOrder.getOrderNo(), ebizOrder.getProductCode());
		orderRemarkMap.put("isFirstBuy", StringUtil.getString(firstBuy));
		try {
			String orderRemark = JsonUtil.writeValue(orderRemarkMap);
			logger.info("订单备注信息内容：" + orderRemark);
			ebizOrder.setRemark(orderRemark);
		} catch (Exception e) {
			logger.error("设置订单备注信息出现异常", e);
			throw new ServiceException("设置订单备注信息出现异常");
		}
		saveOrUpdateOrder(ebizOrder);
		
		orderSuccessDTO.setExpiryDate(ebizOrder.getExpiryDate());
		orderSuccessDTO.setSuccessDate(ebizOrder.getSuccessDate());
		orderSuccessDTO.setValueDate(ebizOrder.getValueDate());
		orderSuccessDTO.setCloseDate(ebizOrder.getEndCloseDate());
		
		assetBo.saveNewAssetAndHis(ebizOrder);
		
		sendMsgForOrderSuccess(ebizOrder);
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("productName", ebizOrder.getProductName());
		paramMap.put("valiDate", DateUtil.formatDate(orderSuccessDTO.getValueDate(), "yyyy-MM-dd"));
		paramMap.put("orderAmount", ebizOrder.getOrderAmount().toString());
		
		//查询封闭期属性配置
		EbizProductProperty ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				ebizOrder.getProductCode(),
				ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CLOSE_PERIOD_UNIT.getValue(), 
				ebizOrder.getOrderType());
		if(ebizProductProperty != null){
			String closePeriodUnit = ebizProductProperty.getPropertyValue();
			if("Y".equals(closePeriodUnit)){
				closePeriodUnit = "年";
			}
			if("M".equals(closePeriodUnit)){
				closePeriodUnit = "月";
			}
			if("D".equals(closePeriodUnit)){
				closePeriodUnit = "天";
			}
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					ebizOrder.getProductCode(),
					ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CLOSE_PERIOD.getValue(), 
					ebizOrder.getOrderType());
			if(ebizProductProperty != null){
				String closePeriod = ebizProductProperty.getPropertyValue() + closePeriodUnit;
				paramMap.put("closePeriod", closePeriod);
			}
			
		}
		
		//查询到期属性配置
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				ebizOrder.getProductCode(),
				ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.PRO_PERIOD_UNIT.getValue(), 
				ebizOrder.getOrderType());
		if(ebizProductProperty != null){
			String periodUnit = ebizProductProperty.getPropertyValue();
			if("Y".equals(periodUnit)){
				periodUnit = "年";
			}
			if("M".equals(periodUnit)){
				periodUnit = "月";
			}
			if("D".equals(periodUnit)){
				periodUnit = "天";
			}
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					ebizOrder.getProductCode(),
					ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.PRO_PERIOD.getValue(), 
					ebizOrder.getOrderType());
			if(ebizProductProperty != null){
				String proPeriod = ebizProductProperty.getPropertyValue() + periodUnit;
				paramMap.put("proPeriod", proPeriod);
			}
			
		}
		
		EbizPropertyValueDTO ebizPropertyValueDTO = 
				configService.getSinglePropertyValue(
						ebizOrder.getProductCode(), 
						ConstantsForConfig.ENUM_PROPERTY_TYPE.ACCINSURE_NOTICE_TEMPLATE.getValue(), 
						ebizOrder.getOrderType());
		List<String> propertyValues = ebizPropertyValueDTO.getEbizPropertyValueList();
		if(propertyValues != null && propertyValues.size() > 0){
			
			MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
			messageInfoDTO.setCustomerId(StringUtil.getString(ebizOrder.getCustomerId()));
			messageInfoDTO.setParamMap(paramMap);
			
			for(String propertyValue : propertyValues){
				
				messageInfoDTO.setTemplateCode(propertyValue);
				
				try{
					messageService.sendMessageByCustomerId(messageInfoDTO);
				}catch(Exception e){
					logger.info("订单" + orderSuccessDTO.getOrderNo() + "订单成交发送消息异常", e);
				}
				
			}
		}
		
		
	}
	
	/**
	 * 订单支付失败处理
	 * 
	 * @param orderNo
	 */
	public void dealOrderFail(String orderNo) {
		updateOrderStatus(orderNo, ConstantsForOrder.ENUM_ORDER_STATUS.WAITPAY.getValue());
	}
	
	/**
	 * 订单赎回成功发送消息
	 * 
	 * @param ebizOrder
	 * @param firstBuy
	 */
	public void sendMsgForSurrenderSuccess(EbizOrder ebizOrder,
			EbizSurrender ebizSurrender) {
		SurrenderSuccessMsgDTO surrenderSuccessMsgDTO = new SurrenderSuccessMsgDTO();
		surrenderSuccessMsgDTO.setSurrenderId(ebizSurrender.getSurrenderId());
		surrenderSuccessMsgDTO.setCustomerId(ebizOrder.getCustomerId());
		surrenderSuccessMsgDTO.setOrderNo(ebizOrder.getOrderNo());
		surrenderSuccessMsgDTO.setSuccessDate(ebizOrder.getSuccessDate());
		surrenderSuccessMsgDTO.setRecommendCode(ebizOrder.getRecommendCode());
		surrenderSuccessMsgDTO.setOrderType(ebizOrder.getOrderType());
		surrenderSuccessMsgDTO.setProductCode(ebizOrder.getProductCode());
		surrenderSuccessMsgDTO.setProductType(ebizOrder.getProductType());
		surrenderSuccessMsgDTO.setRemark(ebizOrder.getRemark());
		try {
			surrenderSuccessMsgDTO.setRemarkMap();
		} catch (Exception e) {
			logger.error("读取订单备注信息出现异常", e);
			throw new ServiceException("读取订单备注信息出现异常");
		}
		surrenderSuccessMsgDTO.setActivityCode(ebizOrder.getActivityCode());
		surrenderSuccessMsgDTO.setActRemark(ebizOrder.getActivityRemark());
		surrenderSuccessMsgDTO.setRecommendCode(ebizOrder.getRecommendCode());
		
		surrenderSuccessMsgDTO.setAcceptNo(ebizSurrender.getAcceptNo());
		surrenderSuccessMsgDTO.setActualMoney(ebizSurrender.getSurrenderAmount());
		surrenderSuccessMsgDTO.setApplyMoney(ebizSurrender.getApplyAmount());
		surrenderSuccessMsgDTO.setPlatformType(ebizSurrender.getPlatformType());
		surrenderSuccessMsgDTO.setSurrenderDate(ebizSurrender.getSurrenderDate());
		surrenderSuccessMsgDTO.setSurrenderFee(ebizSurrender.getSurrenderFee());
		surrenderSuccessMsgDTO.setSurrenderType(ebizSurrender.getSurrenderType());
		CustomerQryResultDTO customerQryResultDTO = getCustomerInfo(ebizOrder.getCustomerId());
		surrenderSuccessMsgDTO.setCustomerName(customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
		
		//目前tag暂时写死成 policy_hesi_refuend-犹豫期退保
		bpmBaseBo.sendMessage(
				bpmProperties.getProperty("gh.ebiz.bpm.mq.surrenderSuccess.topic"), 
				StringUtil.getString(ebizSurrender.getSurrenderId()), 
				"policy_hesi_refuend", surrenderSuccessMsgDTO);
	}
	
	/**
	 * 获取起息日
	 * 
	 * @param productCode
	 * @param orderType
	 * @param successDate
	 * @return
	 */
	public Date getValueDate(String productCode, String orderType, Date successDate){
		successDate = DateUtil.parseDate(
				DateUtil.formatDate(successDate, "yyyy-MM-dd"), "yyyy-MM-dd");
		EbizProductProperty ebizProductProperty = 
				productService.getSingleProductPropertyByOrderType(
						productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CVALIDATE.getValue(), 
						orderType);
		if(ebizProductProperty != null){
			return (DateUtil.addDays(successDate, 
					Integer.parseInt(ebizProductProperty.getPropertyValue())));
		}
		return DateUtil.addDays(successDate, 1);
	}
	
	/**
	 * 渠道检查
	 * 
	 * @param creditApplyPurchaseReqDTO
	 * @return
	 */
	public boolean checkOrderType(String productCode, String orderType){
		EbizProductProperty ebizProductProperty = productService
				.getSingleProductPropertyByOrderType(productCode,
						ENUM_PRODUCT_PROPERTY_TYPE.ORDER_TYPE.getValue(),
						orderType);
		if (ebizProductProperty != null) {
			if (StringUtil.isNotBlank(ebizProductProperty.getPropertyValue())
					&& ebizProductProperty.getPropertyValue()
							.equals(orderType))
				return true;
			else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 获取订单到期时间
	 * 
	 * @param productCode
	 * @param orderType
	 * @param valueDate
	 * @return
	 */
	public Date getExpiryDate(String productCode, String orderType, Date valueDate){
		valueDate = DateUtil.parseDate(
				DateUtil.formatDate(valueDate, "yyyy-MM-dd"), "yyyy-MM-dd");
		Date expriyDate = DateUtil.addYears(valueDate, 1);
		EbizProductProperty ebizProductProperty = 
				productService.getSingleProductPropertyByOrderType(
						productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.PRO_PERIOD_UNIT.getValue(), 
						orderType);
		if(ebizProductProperty != null){
			String proPeriodUnit = ebizProductProperty.getPropertyValue();
			EbizProductProperty ebizProductProperty2 = 
					productService.getSingleProductPropertyByOrderType(
							productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.PRO_PERIOD.getValue(), 
							orderType);
			Integer proPeriod = Integer.parseInt(ebizProductProperty2.getPropertyValue());
			if("D".equals(proPeriodUnit)){
				expriyDate = DateUtil.addDays(valueDate, proPeriod);
			}
			if("M".equals(proPeriodUnit)){
				expriyDate = DateUtil.addMonths(valueDate, proPeriod);
			}
			if("Y".equals(proPeriodUnit)){
				expriyDate = DateUtil.addYears(valueDate, proPeriod);
			}
		}
		return expriyDate;
	}
	
	/**
	 * 获取订单封闭期结束日期
	 * 
	 * @param productCode
	 * @param orderType
	 * @param valueDate
	 * @return
	 */
	private Date getOrderEndCloseDate(String productCode, String orderType, Date valueDate){
		Date closeDate = null;
		EbizProductProperty ebizProductProperty = 
				productService.getSingleProductPropertyByOrderType(
						productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CLOSE_PERIOD_UNIT.getValue(), 
						orderType);
		if(ebizProductProperty != null){
			String closePeriodUnit = ebizProductProperty.getPropertyValue();
			ebizProductProperty = 
					productService.getSingleProductPropertyByOrderType(
							productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CLOSE_PERIOD.getValue(), 
							orderType);
			if(ebizProductProperty != null){
				Integer closePeriod = Integer.parseInt(ebizProductProperty.getPropertyValue());
				if("D".equals(closePeriodUnit)){
					closeDate = DateUtil.addDays(valueDate, closePeriod);
				}
				if("M".equals(closePeriodUnit)){
					closeDate = DateUtil.addMonths(valueDate, closePeriod);
				}
				if("Y".equals(closePeriodUnit)){
					closeDate = DateUtil.addYears(valueDate, closePeriod);
				}
			}
		}
		return closeDate;
	}
	
	/**
	 * 保存订单表
	 * 
	 * @param orderDTO
	 * @return
	 */
	private String saveEbizOrder(OrderDTO orderDTO){
		EbizOrder ebizOrder = new EbizOrder();
		ebizOrder.setCustomerId(orderDTO.getCustomerId());
		ebizOrder.setCreatedDate(new Date());
		ebizOrder.setCommitDate(new Date());
		ebizOrder.setCreatedUser("交易中心");
		ebizOrder.setModifiedUser("交易中心");
		ebizOrder.setOrderAmount(orderDTO.getOrderAmount());
		ebizOrder.setOrderStatus(orderDTO.getOrderStatus());
		ebizOrder.setOrderType(orderDTO.getOrderType());
		ebizOrder.setProductCode(orderDTO.getProductCode());
		ebizOrder.setProductMerchant(orderDTO.getOrderType());
		if (orderDTO.getEbizProductDTO() == null){
			orderDTO.setEbizProductDTO(productService
					.getProductByProductCode(orderDTO.getProductCode()));
		}
		ebizOrder.setProductName(orderDTO.getEbizProductDTO().getProductName());
		ebizOrder.setProductType(orderDTO.getEbizProductDTO().getProductType());
		
		if(orderDTO.getActivityDTO() != null){
			ebizOrder.setActivityCode(orderDTO.getActivityDTO().getActivityCode());
			ebizOrder.setRecommendCode(StringUtil.getString(orderDTO.getActivityDTO().getRecommendCode()));
			
			HashMap<String, String> orderActRemarkMap = new HashMap<String, String>();
			orderActRemarkMap.put("involveType", orderDTO.getActivityDTO().getInvolveType());
			orderActRemarkMap.put("involveUserId", orderDTO.getActivityDTO().getInvolveUserId());
			orderActRemarkMap.put("remark", orderDTO.getActivityDTO().getRemark());
			try {
				String orderActRemark = JsonUtil.writeValue(orderActRemarkMap);
				logger.info("订单活动备注信息内容：" + orderActRemark);
				ebizOrder.setActivityRemark(orderActRemark);
			} catch (Exception e) {
				logger.error("保存活动备注信息出现异常", e);
				throw new ServiceException("保存活动备注信息出现异常");
			}
		}
		return saveOrUpdateOrder(ebizOrder);
	}

	/**
	 * 保存订单账户表
	 * 
	 * @param orderAccountDTO
	 * @param orderDTO
	 */
	private void saveOrderAccount(OrderAccountDTO orderAccountDTO){
		EbizOrderAccount ebizOrderAccount = getOrderAccount(orderAccountDTO.getOrderNo());
		if(ebizOrderAccount == null){
			ebizOrderAccount = new EbizOrderAccount();
			ebizOrderAccount.setBankCode(orderAccountDTO.getBankCode());
			EbizSyBanksDTO ebizSyBanksDTO = 
					configService.getEbizSyBanks(new BigDecimal(orderAccountDTO.getBankCode()).longValue());
			if(ebizSyBanksDTO != null){
				ebizOrderAccount.setBankName(ebizSyBanksDTO.getName());
			}
			ebizOrderAccount.setCardBookCode(orderAccountDTO.getCardBookCode());
			ebizOrderAccount.setCardBookType(orderAccountDTO.getCardBookType());
			ebizOrderAccount.setCreatedDate(new Date());
			ebizOrderAccount.setCreatedUser("交易中心");
			ebizOrderAccount.setModifiedUser("交易中心");
			ebizOrderAccount.setOrderNo(orderAccountDTO.getOrderNo());
			EbizOrderTypeProperty orderTypeProperty = null;
			orderTypeProperty = configService.getOrderTypeProperty(
					orderAccountDTO.getOrderType(),
					ConstantsForConfig.ENUM_PROPERTY_TYPE.PAY_TYPE.getValue());
			if(orderTypeProperty != null){
				ebizOrderAccount.setPayMode(orderTypeProperty.getPropertyValue());
			}
			saveOrUpdateOrderAccount(ebizOrderAccount);
			return;
		}
		
		ebizOrderAccount.setBankCode(orderAccountDTO.getBankCode());
		EbizSyBanksDTO ebizSyBanksDTO = 
				configService.getEbizSyBanks(new BigDecimal(orderAccountDTO.getBankCode()).longValue());
		if(ebizSyBanksDTO != null){
			ebizOrderAccount.setBankName(ebizSyBanksDTO.getName());
		}
		ebizOrderAccount.setCardBookCode(orderAccountDTO.getCardBookCode());
		ebizOrderAccount.setCardBookCode(orderAccountDTO.getCardBookCode());
		ebizOrderAccount.setCardBookType(orderAccountDTO.getCardBookType());
		saveOrUpdateOrderAccount(ebizOrderAccount);
	}

	/**
	 * 保存/更新订单账户表
	 * 
	 * @param ebizOrderAccount
	 */
	private void saveOrUpdateOrderAccount(EbizOrderAccount ebizOrderAccount){
		ebizOrderAccount.setModifiedDate(new Date());
		if ("".equals(StringUtil.getString(ebizOrderAccount.getAccountId()))) {
			ebizOrderAccount.setIsDelete((short)0);
			ebizOrderAccountDAO.insertSelective(ebizOrderAccount);
		} else {
			ebizOrderAccountDAO.updateByPrimaryKeySelective(ebizOrderAccount);
		}
	}

	/**
	 * 保存第三方订单表
	 * 
	 * @param thirdOrderDTO
	 * @param orderNo
	 */
	private void saveThirdOrder(ThirdOrderDTO thirdOrderDTO, String orderNo){
		EbizThirdOrder ebizThirdOrder = new EbizThirdOrder();
		ebizThirdOrder.setCreatedDate(new Date());
		ebizThirdOrder.setModifiedDate(new Date());
		ebizThirdOrder.setCreatedUser("交易中心");
		ebizThirdOrder.setModifiedUser("交易中心");
		ebizThirdOrder.setOrderNo(orderNo);
		ebizThirdOrder.setThirdOrderNo(thirdOrderDTO.getThirdOrderNo());
		ebizThirdOrder.setThirdType(thirdOrderDTO.getThirdType());
		ebizThirdOrder.setThirdProductCode(thirdOrderDTO.getThirdProductCode());
		ebizThirdOrder.setSpecialCode(thirdOrderDTO.getSpecialCode());
		ebizThirdOrder.setIsDelete((short)0);
		ebizThirdOrderDAO.insertSelective(ebizThirdOrder);
	}
	
	/**
	 * 更新第三方订单表
	 * 
	 * @param thirdOrder
	 */
	private void updateThirdOrder(EbizThirdOrder thirdOrder) {
        EbizThirdOrderExample ebizThirdOrderExample = new EbizThirdOrderExample();
        ebizThirdOrderExample.createCriteria().andThirdOrderNoEqualTo(thirdOrder.getThirdOrderNo());
        ebizThirdOrderDAO.updateByExampleSelective(thirdOrder, ebizThirdOrderExample);
    }	

	/**
	 * 生成新订单号
	 * 
	 * @param orderType
	 * @param productType
	 * @return
	 */
	private String generateOrderNo(String orderType, String productType){
		 // 8为日期+2位ORDER_TYPE +2位PRODUCT_TYPE+8位流水号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String orderNo = sdf.format(new Date()) + orderType + productType
				+ StringUtils.leftPad(GenerateSequenceUtil.getStringSequence("EBIZ_ORDER_SEQ"), 8, "0");
		return orderNo;
	}

	/**
	 * 校验是否为首单
	 * true-是 false-否
	 * 
	 * @param customerId
	 * @param orderNo
	 * @return
	 */
	private Integer checkIsFirstBuy(Long customerId, String orderNo, String productCode) {
		List<String> statusList = new ArrayList<String>();
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.DEAL.getValue());
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMING.getValue());
		statusList.add(ConstantsForOrder.ENUM_ORDER_STATUS.REDEEMALL.getValue());
		
		EbizOrderExample ebizOrderExample = new EbizOrderExample();
		ebizOrderExample.createCriteria()
			.andCustomerIdEqualTo(customerId)
			.andOrderStatusIn(statusList)
			.andOrderNoNotEqualTo(orderNo)
			.andProductCodeEqualTo(productCode)
			.andIsDeleteEqualTo((short)0);
		
		List<EbizOrder> orders = ebizOrderDAO.selectByExample(ebizOrderExample);
		if(orders != null && orders.size() > 0){
			return 0;
		}
		return 1;
	}

	/**
	 * 订单成交发送消息
	 * 
	 * @param ebizOrder
	 * @param firstBuy
	 */
	public void sendMsgForOrderSuccess(EbizOrder ebizOrder) {
		OrderSuccessMsgDTO orderSuccessMsgDTO = new OrderSuccessMsgDTO();
		orderSuccessMsgDTO.setCustomerId(ebizOrder.getCustomerId());
		orderSuccessMsgDTO.setOrderNo(ebizOrder.getOrderNo());
		orderSuccessMsgDTO.setOrderType(ebizOrder.getOrderType());
		orderSuccessMsgDTO.setOrderAmount(ebizOrder.getOrderAmount());
		orderSuccessMsgDTO.setSuccessDate(ebizOrder.getSuccessDate());
		orderSuccessMsgDTO.setActivityCode(ebizOrder.getActivityCode());
		orderSuccessMsgDTO.setRecommendCode(ebizOrder.getRecommendCode());
		orderSuccessMsgDTO.setBusinessNo(ebizOrder.getBusinessNo());
		orderSuccessMsgDTO.setRemark(ebizOrder.getRemark());
		try {
			orderSuccessMsgDTO.setRemarkMap();
		} catch (Exception e) {
			logger.error("读取订单备注信息出现异常", e);
			throw new ServiceException("读取订单备注信息出现异常");
		}
		orderSuccessMsgDTO.setProductCode(ebizOrder.getProductCode());
		orderSuccessMsgDTO.setProductType(ebizOrder.getProductType());
		orderSuccessMsgDTO.setValueDate(ebizOrder.getValueDate());
		orderSuccessMsgDTO.setActRemark(ebizOrder.getActivityRemark());
		orderSuccessMsgDTO.setRecommendCode(ebizOrder.getRecommendCode());
		CustomerQryResultDTO customerQryResultDTO = getCustomerInfo(ebizOrder.getCustomerId());
		orderSuccessMsgDTO.setCustomerName(customerQryResultDTO.getCustomerDTOList().get(0).getRealName());
		bpmBaseBo.sendMessage(
				bpmProperties.getProperty("gh.ebiz.bpm.mq.orderSuccess.topic"), 
				ebizOrder.getOrderNo(), "", orderSuccessMsgDTO);
	}
	
}
