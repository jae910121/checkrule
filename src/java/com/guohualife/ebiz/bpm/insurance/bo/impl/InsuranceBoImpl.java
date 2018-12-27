package com.guohualife.ebiz.bpm.insurance.bo.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.guohualife.ebiz.bpm.asset.bo.AssetBo;
import com.guohualife.ebiz.bpm.base.bo.BpmBaseBo;
import com.guohualife.ebiz.bpm.base.constants.Constants;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.constants.ConstantsForInsurance;
import com.guohualife.ebiz.bpm.insurance.constants.ConstantsForInsurance.ENUM_INSURED_TYPE;
import com.guohualife.ebiz.bpm.insurance.dto.AppntDTO;
import com.guohualife.ebiz.bpm.insurance.dto.BnfDTO;
import com.guohualife.ebiz.bpm.insurance.dto.InsuredDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyInfoDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyListQueryDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicySuccessDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderChargeReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderChargeResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.order.bo.OrderBo;
import com.guohualife.ebiz.bpm.order.bo.OrderSurrenderBo;
import com.guohualife.ebiz.bpm.order.constants.ConstantsForOrder;
import com.guohualife.ebiz.bpm.order.dto.OrderDTO;
import com.guohualife.ebiz.common.config.dto.EbizCodeDTO;
import com.guohualife.ebiz.common.config.service.ConfigService;
import com.guohualife.ebiz.gateway.ins.dto.request.RefundChargeReqDTO;
import com.guohualife.ebiz.gateway.ins.dto.response.RefundChargeResDTO;
import com.guohualife.ebiz.gateway.ins.service.InsTradeService;
import com.guohualife.ebiz.product.constant.ConstantsForProduct;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.bpm.dao.EbizOrderDAO;
import com.guohualife.edb.bpm.dao.EbizPolicyBnfDAO;
import com.guohualife.edb.bpm.dao.EbizPolicyDAO;
import com.guohualife.edb.bpm.dao.EbizPolicyInsuredDAO;
import com.guohualife.edb.bpm.dao.EbizSurrenderDAO;
import com.guohualife.edb.bpm.model.EbizAsset;
import com.guohualife.edb.bpm.model.EbizOrder;
import com.guohualife.edb.bpm.model.EbizOrderAccount;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizPolicyBnf;
import com.guohualife.edb.bpm.model.EbizPolicyBnfExample;
import com.guohualife.edb.bpm.model.EbizPolicyExample;
import com.guohualife.edb.bpm.model.EbizPolicyExample.Criteria;
import com.guohualife.edb.bpm.model.EbizPolicyInsured;
import com.guohualife.edb.bpm.model.EbizPolicyInsuredExample;
import com.guohualife.edb.product.model.EbizProductProperty;
import com.guohualife.platform.base.api.bo.implement.BaseBOImpl;
import com.guohualife.platform.common.api.exception.ServiceException;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.IdNoUtil;
import com.guohualife.platform.common.api.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class InsuranceBoImpl extends BaseBOImpl implements InsuranceBo {

	@Resource
	EbizPolicyDAO ebizPolicyDAO;
	
	@Resource(name = "ebizOrderDAOImpl")
	EbizOrderDAO ebizOrderDAO;
	
	@Resource
	EbizPolicyInsuredDAO ebizPolicyInsuredDAO;
	
	@Resource
	private EbizPolicyBnfDAO ebizPolicyBnfDAO;
	
	@Resource
	private ProductService productService;
	
	@Resource
	private OrderBo orderBo;
	
	@Resource(name = "ebizSurrenderDAOImpl")
	EbizSurrenderDAO ebizSurrenderDAO;
	
	@Resource
	private AssetBo assetBo;
	 
	@Resource
	private OrderSurrenderBo orderSurrenderBo;
	
	@Resource
	private InsTradeService insTradeService;
	
	@Resource
	private ConfigService configService;
	
	@Resource
	private BpmBaseBo bpmBaseBo;
	
	/**
	 * 保存保单信息
	 * 
	 * @param productCode
	 * @param policyDTO
	 * @return
	 * @throws ServiceException 
	 */
	public Long savePolicy(OrderDTO orderDTO,
			PolicyDTO policyDTO) throws ServiceException {
		String orderNo = orderDTO.getOrderNo();
		long start = System.currentTimeMillis();
		
		improveAppnt(policyDTO.getAppntDTO());
		Long policyId = savePoilcy(orderDTO, policyDTO);
		logger.info("订单" + orderNo + "保存保单表信息耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		
		if(!ENUM_INSURED_TYPE.SELF.getValue().equals(policyDTO.getInsuredType())){
			improveInsured(policyDTO);
			saveInsured(orderNo, policyDTO.getInsuredType(), 
					policyDTO.getAppntDTO(), policyDTO.getInsuredDTOList());
			logger.info("订单" + orderNo + "保存被保人表信息耗时："
					+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		}
		
		improveBnf(policyDTO);
		saveBnf(orderNo, policyDTO.getBnfDTOList());
		logger.info("订单" + orderNo + "保存受益人表信息耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		
		return policyId;
	}
	
	/**
	 * 保存/更新保单表信息
	 * 
	 * @param ebizPolicy
	 * @return
	 */
	public Long saveOrUpdatePolicy(EbizPolicy ebizPolicy) {
		ebizPolicy.setModifiedDate(new Date());
		if("".equals(StringUtil.getString(ebizPolicy.getPolicyId()))){
			ebizPolicy.setIsDelete((short)0);
			ebizPolicyDAO.insertSelective(ebizPolicy);
		}else{
			EbizPolicyExample ebizPolicyExample = new EbizPolicyExample();
			ebizPolicyExample.createCriteria().andPolicyIdEqualTo(ebizPolicy.getPolicyId());
			ebizPolicyDAO.updateByPrimaryKeySelective(ebizPolicy);
		}
		return ebizPolicy.getPolicyId();
	}
	
	/**
	 * 根据订单号查询保单表
	 * 
	 * @param orderNo
	 * @return
	 */
	public EbizPolicy getPolicyByOrderNo(String orderNo){
		EbizPolicyExample ebizPolicyExample = new EbizPolicyExample();
		ebizPolicyExample.createCriteria().andOrderNoEqualTo(orderNo);
		return ebizPolicyDAO.selectByExample(ebizPolicyExample).get(0);
	}
	
	/**
	 * 根据订单号查询保单详细信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public PolicyInfoDTO getPolicyDetailByOrderNo(String orderNo) {
		PolicyInfoDTO policyInfoDTO = new PolicyInfoDTO();
		policyInfoDTO.setEbizPolicy(getPolicyByOrderNo(orderNo));
		policyInfoDTO.setEbizPolicyInsuredList(getInsuredByPolicyNo(orderNo));
		policyInfoDTO.setEbizPolicyBnfList(getBnfByPolicyNo(orderNo));
		return policyInfoDTO;
	}

	/**
	 * 出单成功处理
	 * 
	 * @param policySuccessDTO
	 */
	public void dealPolicySuccess(PolicySuccessDTO policySuccessDTO) {
		
		EbizPolicy ebizPolicy = getPolicyByOrderNo(policySuccessDTO.getOrderNo());
		ebizPolicy.setPolicyNo(policySuccessDTO.getPolicyNo());
		
		EbizOrder ebizOrder = orderBo.getOrder(policySuccessDTO.getOrderNo());
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();
		
		Date cvalidate = 
				orderBo.getValueDate(productCode, orderType, policySuccessDTO.getSuccessDate());
		ebizPolicy.setCvlidate(cvalidate);
		policySuccessDTO.setCvalidate(cvalidate);
		
		ebizPolicy.setLastHesitateDate(
				getLastHesitateDate(productCode, orderType, ebizPolicy.getCvlidate()));
		
		ebizPolicy.setProcessStatus(
				ConstantsForInsurance.ENUM_POLICY_STATUS.ACCEPTINSURANCESUCCESS.getValue());
		saveOrUpdatePolicy(ebizPolicy);
		
	}

	/**
	 * 查询保单列表
	 * 
	 * @param policyListQueryDTO
	 * @return
	 */
	public List<EbizPolicy> getPolicyList(PolicyListQueryDTO policyListQueryDTO) {
		EbizPolicyExample ebizPolicyExample = new EbizPolicyExample();
		Criteria criteria = ebizPolicyExample.createCriteria();
		criteria.andIsDeleteEqualTo((short)0).andPolicyIdIsNotNull();
		
		if(policyListQueryDTO.getProcessStatusList() != null && policyListQueryDTO.getProcessStatusList().size() > 0){
			criteria.andProcessStatusIn(policyListQueryDTO.getProcessStatusList());
		}
		
		if(policyListQueryDTO.getConfirmStatusList() != null && policyListQueryDTO.getConfirmStatusList().size() > 0){
			criteria.andComfirmStatusIn(policyListQueryDTO.getConfirmStatusList());
		}
		
		if(Constants.FAILED.equals(policyListQueryDTO.getIsThaw())){
			criteria.andLastHesitateDateLessThanOrEqualTo(new Date())
			.andIsThawNotEqualTo(Constants.SUCCESS);
		}
			
		return ebizPolicyDAO.selectByExample(ebizPolicyExample);
	}
	
	/**
	 * 查询赎回信息
	 * 
	 * @param policySurrenderInfoReqDTO
	 * @param policySurrenderInfoResDTO
	 * @return
	 */
	public void getSurrenderInfo(
			PolicySurrenderInfoReqDTO policySurrenderInfoReqDTO,
			PolicySurrenderInfoResDTO policySurrenderInfoResDTO) {
		
		String orderNo = policySurrenderInfoReqDTO.getOrderNo();
		policySurrenderInfoResDTO.setOrderNo(orderNo);
		
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		String result = checkSurrender(policySurrenderInfoReqDTO, ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			policySurrenderInfoResDTO.failure(result);
			return;
		}
		EbizPolicy ebizPolicy = getPolicyByOrderNo(orderNo);
		EbizAsset ebizAsset = assetBo.getAssetByOrderNo(orderNo);
		
		initSurrenderByProduct(policySurrenderInfoResDTO, ebizPolicy, ebizOrder, ebizAsset);
		initSurrenderBasic(policySurrenderInfoResDTO, ebizPolicy, ebizOrder, ebizAsset);
		initSurrenderAccount(policySurrenderInfoResDTO, orderNo);
		
		return;
	}

	/**
	 * 赎回核算
	 * 
	 * @param policySurrenderChargeReqDTO
	 * @param policySurrenderChargeResDTO
	 * @return
	 */
	public void surrenderCharge(
			PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO,
			PolicySurrenderChargeResDTO policySurrenderChargeResDTO) {
		
		String orderNo = policySurrenderChargeReqDTO.getOrderNo();
		
		EbizPolicy ebizPolicy = getPolicyByOrderNo(orderNo);
		EbizOrder ebizOrder = orderBo.getOrder(orderNo);
		String result = checkSurrenderCharge(policySurrenderChargeReqDTO, ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			policySurrenderChargeResDTO.failure(result);
			return;
		}
		
		RefundChargeReqDTO refundChargeReqDTO = new RefundChargeReqDTO();
		refundChargeReqDTO.setOrderId(orderNo);

		String surrenderType = policySurrenderChargeReqDTO.getSurrenderType();
		if (ConstantsForOrder.ENUM_SURRENDER_TYPE.REDEEM.getValue().equals(surrenderType)) {
			refundChargeReqDTO.setIsCancelPolicy(Constants.FAILED);
			refundChargeReqDTO.setIsWithdrawAll(Constants.FAILED);
		}
		if(ConstantsForOrder.ENUM_SURRENDER_TYPE.REFUND.getValue().equals(surrenderType)
				|| ConstantsForOrder.ENUM_SURRENDER_TYPE.SPECIALREDEEM.getValue().equals(surrenderType)){
			refundChargeReqDTO.setIsCancelPolicy(Constants.SUCCESS);
			refundChargeReqDTO.setIsWithdrawAll(Constants.SUCCESS);
		}
		refundChargeReqDTO.setWithdrawMoney(
				policySurrenderChargeReqDTO.getApplyMoney());
		refundChargeReqDTO.setPolicyNo(ebizPolicy.getPolicyNo());
		
		refundChargeReqDTO.setThirdType(bpmBaseBo.getThirdType(orderNo));
		
		long start = System.currentTimeMillis();
		logger.info("订单" + orderNo + "调用gateway赎回核算接口开始");
		RefundChargeResDTO refundChargeResDTO = insTradeService.doRefundCharge(refundChargeReqDTO);
		logger.info("订单" + orderNo + "调用gateway赎回核算接口开始，耗时："
				+ ((System.currentTimeMillis() - start) / 1000f) + "秒");
		
		String chargeResult = "订单" + orderNo + "核算";
		
		String responseCode = refundChargeResDTO.getResponseCode();
		String errorMessage = refundChargeResDTO.getErrorMessage();
		
		if(Constants.FAILED.equals(responseCode)){
			chargeResult = chargeResult + "失败：" + errorMessage;
			logger.info(chargeResult);
			policySurrenderChargeResDTO.failure(errorMessage);  
			return;
		}
		
		String isSuccess = refundChargeResDTO.getIsSuccess();
		String message = refundChargeResDTO.getMessage();
		if(Constants.FAILED.equals(isSuccess)){
			chargeResult = chargeResult + "失败：" + message;
			logger.info(chargeResult);
			policySurrenderChargeResDTO.failure(message); 
			return ;
		}
		
		BigDecimal availableMoney = refundChargeResDTO.getAvailableMoney();
		BigDecimal surrenderFee = policySurrenderChargeReqDTO.getApplyMoney().subtract(availableMoney);
		chargeResult = chargeResult + "成功，可领金额：" + availableMoney + "，手续费：" + surrenderFee;
		policySurrenderChargeResDTO.setAvailableMoney(availableMoney);
		policySurrenderChargeResDTO.setSurrenderFee(surrenderFee);
		policySurrenderChargeResDTO.setConfirmMessage(message);
		return;
	}
	
	private String checkSurrender(
			PolicySurrenderInfoReqDTO policySurrenderInfoReqDTO, EbizOrder ebizOrder){
		if (policySurrenderInfoReqDTO == null
				|| StringUtil.isEmpty(policySurrenderInfoReqDTO.getOrderNo())
				|| policySurrenderInfoReqDTO.getCustomerId() == null) {
			return "传入参数有误";
		}
		
		String result = orderSurrenderBo.checkSurrenderInfo(ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			return result;
		}
		return "";
	}
	
	private String checkSurrenderCharge(
			PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO, EbizOrder ebizOrder){
		
		if (policySurrenderChargeReqDTO == null
				|| policySurrenderChargeReqDTO.getCustomerId() == null
				|| StringUtil.isEmpty(policySurrenderChargeReqDTO.getOrderNo())
				|| StringUtil.isEmpty(policySurrenderChargeReqDTO
						.getSurrenderType())
				|| policySurrenderChargeReqDTO.getApplyMoney() == null
				|| policySurrenderChargeReqDTO.getApplyMoney().compareTo(
						BigDecimal.ZERO) < 0) {
			return "传入参数有误";
		}
		
		String result = orderSurrenderBo.checkSurrenderInfo(ebizOrder);
		if(StringUtil.isNotEmpty(result)){
			return result;
		}
		
		result = orderSurrenderBo.checkByProduct(ebizOrder,
				policySurrenderChargeReqDTO.getApplyMoney());
		
		return result;
	}
	
	/**
	 * 保存保单表
	 * 
	 * @param orderDTO
	 * @param policyDTO
	 * @return
	 * @throws ServiceException 
	 */
	private Long savePoilcy(OrderDTO orderDTO, PolicyDTO policyDTO) throws ServiceException{
		EbizPolicy ebizPolicy = new EbizPolicy();
		AppntDTO appntDTO = policyDTO.getAppntDTO();
		ebizPolicy.setAppntMobile(appntDTO.getMobile());
		ebizPolicy.setAppntName(appntDTO.getName());
		ebizPolicy.setAppntSex(appntDTO.getSex());
		ebizPolicy.setAppntBirthday(appntDTO.getBirthday());
		ebizPolicy.setAppntIdType(appntDTO.getIdType());
		ebizPolicy.setAppntIdNo(appntDTO.getIdNo());
		ebizPolicy.setAppntMobile(appntDTO.getMobile());
		ebizPolicy.setAppntEmail(appntDTO.getEmail());
		ebizPolicy.setAppntPhone(appntDTO.getPhone());
		ebizPolicy.setAppntProvince(appntDTO.getProvince());
		ebizPolicy.setAppntCity(appntDTO.getCity());
		ebizPolicy.setAppntAddress(appntDTO.getAddress());
		ebizPolicy.setAppntZip(appntDTO.getZip());
		
		ebizPolicy.setAmt(policyDTO.getPrem());
		EbizProductProperty ebizProductProperty = 
				productService.getSingleProductPropertyByOrderType(
						orderDTO.getProductCode(),
						ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.AMNT.getValue(), 
						orderDTO.getOrderType());
		if(ebizProductProperty != null){
			ebizPolicy.setAmt(new BigDecimal(ebizProductProperty.getPropertyValue()));
		}
		
		ebizPolicy.setPrem(policyDTO.getPrem());
		ebizPolicy.setMult(policyDTO.getMult());
		ebizPolicy.setBnfType(policyDTO.getBnfType());
		ebizPolicy.setInsuredType(policyDTO.getInsuredType());
		ebizPolicy.setCreatedDate(new Date());
		ebizPolicy.setCreatedUser("交易中心");
		ebizPolicy.setModifiedUser("交易中心");
		ebizPolicy.setOrderNo(orderDTO.getOrderNo());
		ebizPolicy.setProcessStatus(
				ConstantsForInsurance.ENUM_POLICY_STATUS.WAITUNDERWRITE.getValue());
		ebizPolicy.setIsThaw(Constants.FAILED);
		return saveOrUpdatePolicy(ebizPolicy);
	}
	
	/**
	 * 保存被保人表
	 * 
	 * @param orderNo
	 * @param insuredType
	 * @param appntDTO
	 * @param insuredDTOList
	 * @throws Exception
	 */
	private void saveInsured(String orderNo, String insuredType,
			AppntDTO appntDTO, List<InsuredDTO> insuredDTOList) {
		
		for (InsuredDTO insuredDTO : insuredDTOList) {
			EbizPolicyInsured ebizPolicyInsured = new EbizPolicyInsured();
			ebizPolicyInsured.setOrderNo(orderNo);
			ebizPolicyInsured.setRelationToAppnt(insuredDTO.getRelationToAppnt());
			ebizPolicyInsured.setName(insuredDTO.getName());
			ebizPolicyInsured.setSex(insuredDTO.getSex() != null ? 
					insuredDTO.getSex() : StringUtil.getString(IdNoUtil.getSexFromIdNo(insuredDTO.getIdNo())));
			ebizPolicyInsured.setBirthday(insuredDTO.getBirthday() != null ? 
					insuredDTO.getBirthday() : IdNoUtil.getBirthdayFromIdNo(insuredDTO.getIdNo()));
			ebizPolicyInsured.setIdType(insuredDTO.getIdType());
			ebizPolicyInsured.setIdNo(insuredDTO.getIdNo());
			ebizPolicyInsured.setMobile(insuredDTO.getMobile());
			ebizPolicyInsured.setEmail(insuredDTO.getEmail());
			ebizPolicyInsured.setTelephone(insuredDTO.getTelephone());
			ebizPolicyInsured.setZip(insuredDTO.getZip());
			ebizPolicyInsured.setAddressNo(insuredDTO.getAddress());
			ebizPolicyInsured.setCreatedDate(new Date());
			ebizPolicyInsured.setCreatedUser("交易中心");
			ebizPolicyInsured.setModifiedUser("交易中心");
			saveOrUpdateInsured(ebizPolicyInsured);
		}
	}
	
	/**
	 * 保存受益人表
	 * 
	 * @param orderNo
	 * @param bnfDTOList
	 */
	private void saveBnf(String orderNo, List<BnfDTO> bnfDTOList){
		if(bnfDTOList != null && bnfDTOList.size() > 0){
			for (int i = 0; i < bnfDTOList.size(); i++) {
				BnfDTO bnfDTO = bnfDTOList.get(i);
				EbizPolicyBnf ebizPolicyBnf = new EbizPolicyBnf();
				ebizPolicyBnf.setBnfType(bnfDTO.getBnfType());
				ebizPolicyBnf.setBnfNo(new BigDecimal(i));
				ebizPolicyBnf.setBnfOrder(bnfDTO.getBnfOrder() != null ? bnfDTO.getBnfOrder() : new BigDecimal("1"));
				ebizPolicyBnf.setBnfLot(new BigDecimal(bnfDTO.getBnfLot()));
				ebizPolicyBnf.setName(bnfDTO.getName());
				ebizPolicyBnf.setSex(bnfDTO.getSex() != null ? 
						bnfDTO.getSex() : StringUtil.getString(IdNoUtil.getSexFromIdNo(bnfDTO.getIdNo())));
				ebizPolicyBnf.setBirthday(bnfDTO.getBirthday() != null ? 
						bnfDTO.getBirthday() : IdNoUtil.getBirthdayFromIdNo(bnfDTO.getIdNo()));
				ebizPolicyBnf.setIdType(bnfDTO.getIdType());
				ebizPolicyBnf.setIdNo(bnfDTO.getIdNo());
				ebizPolicyBnf.setRelationToInsured(bnfDTO.getRelationToInsured());
				ebizPolicyBnf.setOrderNo(orderNo);
				ebizPolicyBnf.setCreatedDate(new Date());
				ebizPolicyBnf.setCreatedUser("交易中心");
				ebizPolicyBnf.setModifiedUser("交易中心");
				saveOrUpdateBnf(ebizPolicyBnf);
			}
		}
	}
	
	/**
	 * 保存/更新被保人表信息
	 */
	private Long saveOrUpdateInsured(EbizPolicyInsured ebizPolicyInsured){
		ebizPolicyInsured.setModifiedDate(new Date());
		if("".equals(StringUtil.getString(ebizPolicyInsured.getInsuredId()))){
			ebizPolicyInsured.setIsDelete((short)0);
			ebizPolicyInsuredDAO.insertSelective(ebizPolicyInsured);
		}else{
			ebizPolicyInsuredDAO.updateByPrimaryKeySelective(ebizPolicyInsured);
		}
		return ebizPolicyInsured.getInsuredId();
	}
	
	/**
	 * 保存/更新受益人表信息
	 */
	private Long saveOrUpdateBnf(EbizPolicyBnf ebizPolicyBnf){
		ebizPolicyBnf.setModifiedDate(new Date());
		if("".equals(StringUtil.getString(ebizPolicyBnf.getBnfId()))){
			ebizPolicyBnf.setIsDelete((short)0);
			ebizPolicyBnfDAO.insertSelective(ebizPolicyBnf);
		}else{
			ebizPolicyBnfDAO.updateByPrimaryKeySelective(ebizPolicyBnf);
		}
		return ebizPolicyBnf.getBnfId();
	}
	
	/**
	 * 根据订单号查询被保人表
	 * 
	 * @param orderNo
	 * @return
	 */
	private List<EbizPolicyInsured> getInsuredByPolicyNo(String orderNo) {
		EbizPolicyInsuredExample ebizPolicyInsuredExample = new EbizPolicyInsuredExample();
		ebizPolicyInsuredExample.createCriteria().andOrderNoEqualTo(orderNo);
		return ebizPolicyInsuredDAO.selectByExample(ebizPolicyInsuredExample);
	}

	/**
	 * 根据订单号查询受益人表
	 * 
	 * @param orderNo
	 * @return
	 */
	private List<EbizPolicyBnf> getBnfByPolicyNo(String orderNo) {
		EbizPolicyBnfExample ebizPolicyBnfExample = new EbizPolicyBnfExample();
		ebizPolicyBnfExample.createCriteria().andOrderNoEqualTo(orderNo);
		return ebizPolicyBnfDAO.selectByExample(ebizPolicyBnfExample);
	}
	
	private void initSurrenderAccount(
			PolicySurrenderInfoResDTO policySurrenderInfoResDTO,
			String orderNo) {
		EbizOrderAccount ebizOrderAccount = orderBo.getOrderAccount(orderNo);
		EbizPolicy ebizPolicy = getPolicyByOrderNo(orderNo);
		//截取后四位
		policySurrenderInfoResDTO.setRecAccountNo(StringUtil.substring(ebizOrderAccount.getCardBookCode(), ebizOrderAccount.getCardBookCode().length()-4));
		policySurrenderInfoResDTO.setRecAccountName(ebizPolicy.getAppntName());
		policySurrenderInfoResDTO.setRecAccountType(ebizOrderAccount.getBankCode());
		policySurrenderInfoResDTO.setBankName(ebizOrderAccount.getBankName());
	}
	
	private void initSurrenderByProduct(
			PolicySurrenderInfoResDTO policySurrenderInfoResDTO,
			EbizPolicy ebizPolicy, EbizOrder ebizOrder, EbizAsset ebizAsset) {
		String isDraw = Constants.FAILED;
		String isThawRefund = Constants.FAILED;
		String isRefund = Constants.FAILED;
		
		String productCode = ebizOrder.getProductCode();
		String orderType = ebizOrder.getOrderType();
		
		//赎回(领取)
		EbizProductProperty ebizProductProperty = productService
				.getSingleProductPropertyByOrderType(productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_REDEEM.getValue(),
						orderType);
		if (ebizProductProperty != null) {
			isDraw = ebizProductProperty.getPropertyValue();
		}
		
		//退保(犹豫期退保)
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_REFUND.getValue(),
				orderType);
		if (ebizProductProperty != null) {
			isThawRefund = ebizProductProperty.getPropertyValue();
		}
		
		//特殊赎回(正常退保)
		ebizProductProperty = productService.getSingleProductPropertyByOrderType(
				productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.IS_SURRENDER.getValue(),
				orderType);
		if (ebizProductProperty != null) {
			isRefund = ebizProductProperty.getPropertyValue();
		}
		
		String isAfterThaw = Constants.FAILED;
		if(ebizPolicy.getLastHesitateDate().before(new Date())){ //已过犹豫期不能进行犹豫期退保
			isAfterThaw = Constants.SUCCESS;
			isThawRefund = Constants.FAILED;
		}
		
		if(ebizPolicy.getLastHesitateDate().after(new Date())){ //未过犹豫期不能进行正常退保和领取
			isRefund = Constants.FAILED;
			isDraw = Constants.FAILED;
		}
		
		policySurrenderInfoResDTO.setIsAfterThaw(isAfterThaw);
		
		policySurrenderInfoResDTO.setIsDraw(isDraw);
		policySurrenderInfoResDTO.setIsRefund(isRefund);
		policySurrenderInfoResDTO.setIsThawRefund(isThawRefund);
		
		if(isDraw.equals(Constants.SUCCESS)){
			BigDecimal surrenderUnit = BigDecimal.ZERO;
			BigDecimal surrenderMin = BigDecimal.ZERO;
			BigDecimal surrenderRestMin = BigDecimal.ZERO;
			BigDecimal surrenderMax = BigDecimal.ZERO;
			
			//赎回金额倍数
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MUL_GET_MONEY.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderUnit = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回最小金额
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MIN_GET_MONEY.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderMin = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回账户剩余最低金额
			ebizProductProperty = productService.getSingleProductPropertyByOrderType(
					productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.MIN_SURPLUS_VALUE.getValue(),
					orderType);
			if(ebizProductProperty != null){
				surrenderRestMin = new BigDecimal(ebizProductProperty.getPropertyValue());
			}
			
			//赎回最大金额 
			surrenderMax = orderSurrenderBo.getMaxForAmount(
					ebizAsset.getTotalValue(), surrenderRestMin, surrenderUnit);
			
			policySurrenderInfoResDTO.setSurrenderRestMin(surrenderRestMin);
			policySurrenderInfoResDTO.setSurrenderMax(surrenderMax);
			policySurrenderInfoResDTO.setSurrenderMin(surrenderMin);
			policySurrenderInfoResDTO.setSurrenderUnit(surrenderUnit);
		}
		
	}
	
	private void initSurrenderBasic(
			PolicySurrenderInfoResDTO policySurrenderInfoResDTO,
			EbizPolicy ebizPolicy, EbizOrder ebizOrder, EbizAsset ebizAsset) {
		
		policySurrenderInfoResDTO.setPolicyNo(ebizPolicy.getPolicyNo());
		policySurrenderInfoResDTO.setProductName(ebizOrder.getProductName());
		policySurrenderInfoResDTO.setContValue(ebizAsset.getTotalValue());
		if(Constants.FAILED.equals(policySurrenderInfoResDTO.getIsAfterThaw())){
			policySurrenderInfoResDTO.setContValue(ebizPolicy.getPrem());
		}
		policySurrenderInfoResDTO.setCvaliDate(ebizPolicy.getCvlidate());
		policySurrenderInfoResDTO.setOrderType(ebizOrder.getOrderType());
		EbizCodeDTO ebizCodeDTO = configService.getEbizCode("orderType", ebizOrder.getOrderType(), "");
		if(ebizCodeDTO != null){
			if (ebizCodeDTO.getEbizCodeList() != null
					&& ebizCodeDTO.getEbizCodeList().size() > 0) {
				policySurrenderInfoResDTO.setOrderTypeLabel(
						ebizCodeDTO.getEbizCodeList().get(0).getCodeLabel());
			}	
		}
		
	}
	
	private Date getLastHesitateDate(String productCode, String orderType, Date cvalidate){
		EbizProductProperty ebizProductProperty = 
				productService.getSingleProductPropertyByOrderType(
						productCode, ConstantsForProduct.ENUM_PRODUCT_PROPERTY_TYPE.CVALIDATE.getValue(), 
						orderType);
		if(ebizProductProperty != null){
			return (DateUtil.addDays(cvalidate, 
					Integer.parseInt(ebizProductProperty.getPropertyValue())));
		}
		return DateUtil.addDays(cvalidate, 10);
	}
	
	private void improveAppnt(AppntDTO appntDTO){
		if(appntDTO.getSex() == null){
			appntDTO.setSex(StringUtil.getString(IdNoUtil.getSexFromIdNo(appntDTO.getIdNo())));
		}
		if(appntDTO.getBirthday() == null){
			appntDTO.setBirthday(IdNoUtil.getBirthdayFromIdNo(appntDTO.getIdNo()));
		}
	}
	
	private void improveInsured(PolicyDTO policyDTO){
		for (InsuredDTO insuredDTO : policyDTO.getInsuredDTOList()) {
			if (insuredDTO.getSex() == null) {
				insuredDTO.setSex(StringUtil.getString(IdNoUtil.getSexFromIdNo(insuredDTO.getIdNo())));
			}
			if (insuredDTO.getBirthday() == null) {
				insuredDTO.setBirthday(IdNoUtil.getBirthdayFromIdNo(insuredDTO.getIdNo()));
			}
		}
	}
	
	private void improveBnf(PolicyDTO policyDTO){
		if(policyDTO.getBnfDTOList() != null && policyDTO.getBnfDTOList().size() > 0){
			for (BnfDTO bnfDTO : policyDTO.getBnfDTOList()) {
				if(bnfDTO.getSex() == null){
					bnfDTO.setSex(StringUtil.getString(IdNoUtil.getSexFromIdNo(bnfDTO.getIdNo())));
				}
				if(bnfDTO.getBirthday() == null){
					bnfDTO.setBirthday(IdNoUtil.getBirthdayFromIdNo(bnfDTO.getIdNo()));
				}
			}
		}
	}

}
