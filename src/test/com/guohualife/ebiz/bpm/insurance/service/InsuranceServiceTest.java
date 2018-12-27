package com.guohualife.ebiz.bpm.insurance.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.insurance.dto.ActivityDTO;
import com.guohualife.ebiz.bpm.insurance.dto.AppntDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyDTO;
import com.guohualife.ebiz.bpm.insurance.dto.PolicyInfoDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.AcceptInsuranceReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.PolicySurrenderInfoReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.request.UnderwriteReqDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.AcceptInsuranceResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.PolicySurrenderInfoResDTO;
import com.guohualife.ebiz.bpm.insurance.dto.response.UnderwriteResDTO;
import com.guohualife.ebiz.bpm.order.dto.response.OrderResDTO;
import com.guohualife.ebiz.message.service.MessageService;
import com.guohualife.edb.bpm.model.EbizPolicy;
import com.guohualife.edb.bpm.model.EbizPolicyBnf;
import com.guohualife.edb.bpm.model.EbizPolicyInsured;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.JsonUtil;
import com.guohualife.platform.common.api.util.XmlUtil;

public class InsuranceServiceTest extends BaseTester{

	@SuppressWarnings("rawtypes")
	private Class[] clazz = {String.class, List.class, OrderResDTO.class, 
		PolicyInfoDTO.class, EbizPolicy.class, EbizPolicyInsured.class, EbizPolicyBnf.class,
		PolicySurrenderInfoReqDTO.class, PolicySurrenderInfoResDTO.class};
	
	@Resource
	private MessageService messageService;
	@Resource
	private InsuranceService insuranceService;
	
//	@Resource
//	private PolicySurrenderApplyBatch policySurrenderApplyBatch;
	
	// 测试核保
	@Test
	public void testUnderwrite() throws Exception{
		UnderwriteReqDTO underwriteReqDTO = new UnderwriteReqDTO();
		underwriteReqDTO.setCustomerId(new BigDecimal("92147").longValue());
		underwriteReqDTO.setOrderAmount(new BigDecimal(3000));
		underwriteReqDTO.setOrderType("02");
		underwriteReqDTO.setProductCode("1");
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setInsuredType("1");
		policyDTO.setMult(new BigDecimal("2"));
		policyDTO.setBnfType("1");
		policyDTO.setPrem(new BigDecimal(2000));
		AppntDTO appntDTO = new AppntDTO();
		appntDTO.setBirthday(DateUtil.parseDate("1989-03-17"));
		appntDTO.setIdNo("231002198903302089");
		appntDTO.setIdType("0");
		appntDTO.setMobile("15922260317");
		appntDTO.setName("刘琳驰");
		policyDTO.setAppntDTO(appntDTO);
		underwriteReqDTO.setPolicyDTO(policyDTO);
		
//		ActivityDTO activityDTO = new ActivityDTO();
//		activityDTO.setActivityCode("YQ_001");
//		activityDTO.setInvolveType("01");
//		activityDTO.setInvolveUserId("wangxulu987310");
//		activityDTO.setRecommendCode("YQ_0012273");
//		activityDTO.setRemark(JsonUtil.writeValue("20150523130400111"));
//		underwriteReqDTO.setActivityDTO(activityDTO);
		UnderwriteResDTO underwriteResDTO = insuranceService.underwrite(underwriteReqDTO);
		System.out.println("核保：\n" + XmlUtil.toXml(underwriteResDTO, clazz));
	}
	
	@Test
	public void testAcceptInsurance(){
		AcceptInsuranceReqDTO acceptInsuranceReqDTO = new AcceptInsuranceReqDTO();
		acceptInsuranceReqDTO.setOrderNo("20150525020100877508");
		acceptInsuranceReqDTO.setProposalNo("8828900001140418");
		acceptInsuranceReqDTO.setCardBookType("01");
		acceptInsuranceReqDTO.setCardBookCode("6222800064671006049");
		acceptInsuranceReqDTO.setBankCode("0102");
		acceptInsuranceReqDTO.setCustomerId(new BigDecimal("97979").longValue());
		AcceptInsuranceResDTO acceptInsuranceResDTO = insuranceService.acceptInsurance(acceptInsuranceReqDTO);
		System.out.println("承保：\n" + XmlUtil.toXml(acceptInsuranceResDTO, clazz));
		
//		Map paramMap = new HashMap();
//		 paramMap.put("orderTypeName", "官网");
//	     paramMap.put("productName", "国华财富人生");
//	     paramMap.put("orderNo", "11152211212121");
//	     paramMap.put("contNo", "555555555555");
//	     paramMap.put("name", "zhangsan");
//	     paramMap.put("password", "55222222");
//			//2.调用短信平台
//			messageService.sendMessageByMessageInfo("1541515615", "1", "{\"smsType\": \"9\",\"smsContext\":\"尊敬的客户，您通过官网购买的国华养老金产品，投保已成功，订单号为：161546131545,保单号是：1651541414，系统为您注册的国华人寿会员注册名为:dsfd，注册密码为:dsadsadsdewrgrgr，您可使用此账户登录国华人寿官网www.95549.cn查询保单详情及后续操作，请您牢记。\"}");
//		Map<String,String> paramMap = new HashMap<String,String>();
//		paramMap.put("appntName", "张三");
//		paramMap.put("productName", "国华两全险");
//		paramMap.put("contNo", "46541605619");
//		paramMap.put("payDate", (new Date()).toString());
//		paramMap.put("payToDate", (new Date()).toString());
//		paramMap.put("mainRiskCode", "1153");
//		paramMap.put("paymoney", "1000");
//		paramMap.put("bankname", "中国银行");
//		paramMap.put("accountno", "1615615615151");
//		paramMap.put("currentDate",(new Date()).toString());
//		List<String> list = new ArrayList<String>();
//		list.add("254661169@qq.com");
//		messageService.sendMessageByMessageInfo("123@qq.com", "2", "{\"mailTitle\": \"《投连报告2015年度通知书》\",\"mailDecorate\":\"DECORATE_COMMON\",\"mailContext\": \"&lt;h1&gt;尊敬的客户:您好！&lt;h1/&gt;&lt;p&gt;您的保单(54948116516)2015年度投连报告通知书已生成，现将投连报告通知书通过电子邮箱发送给您，请您妥善保存。&lt;p/&gt;&lt;p&gt;更多信息可登录国华人寿官网( www.95549.cn )查看。&lt;p/&gt;\"}");
	
	}
	
//	@Test
//	public void testGetSurrenderInfo(){
//		PolicySurrenderInfoQryReqDTO policySurrenderInfoQryReqDTO = new PolicySurrenderInfoQryReqDTO();
//		policySurrenderInfoQryReqDTO.setOrderNo("20150425010100873831");
//		policySurrenderInfoQryReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		PolicySurrenderInfoQryResDTO  policySurrenderInfoQryResDTO =
//				insuranceService.getSurrenderInfo(policySurrenderInfoQryReqDTO);
//		System.out.println("保险订单赎回信息查询：\n" + XmlUtil.toXml(policySurrenderInfoQryResDTO, clazz));
//	}
	
//	@Test
//	public void testCharge(){
//		PolicySurrenderChargeReqDTO policySurrenderChargeReqDTO = new PolicySurrenderChargeReqDTO();
//		policySurrenderChargeReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		policySurrenderChargeReqDTO.setOrderNo("20150425010100873831");
//		policySurrenderChargeReqDTO.setApplyDate(new Date());
//		policySurrenderChargeReqDTO.setApplyMoney(new BigDecimal("10"));
//		policySurrenderChargeReqDTO.setSurrenderType(ConstantsForInsurance.ENUM_SURRENDER_TYPE.REFUND.getValue()); //退款(犹豫期退保)
//		PolicySurrenderChargeResDTO policySurrenderChargeResDTO = 
//				insuranceService.charge(policySurrenderChargeReqDTO);
//		System.out.println("保险订单赎回核算：\n" + XmlUtil.toXml(policySurrenderChargeResDTO, clazz));
//	}
//	
//	@Test
//	public void testSurrenderApply(){
//		PolicySurrenderApplyReqDTO policySurrenderApplyReqDTO = new PolicySurrenderApplyReqDTO();
//		policySurrenderApplyReqDTO.setApplyMoney(new BigDecimal("10"));
//		policySurrenderApplyReqDTO.setAvaliableMoney(new BigDecimal("9.9"));
//		policySurrenderApplyReqDTO.setCustomerId(new BigDecimal("96798").longValue());
//		policySurrenderApplyReqDTO.setOrderNo("20150425010100873831");
//		policySurrenderApplyReqDTO.setPolicyNo("8828900001121048");
//		policySurrenderApplyReqDTO.setPlatformType("1");
//		policySurrenderApplyReqDTO.setSurrenderFee(new BigDecimal("0.1"));
//		policySurrenderApplyReqDTO.setSurrenderType(ConstantsForInsurance.ENUM_SURRENDER_TYPE.REFUND.getValue());
//		PolicySurrenderApplyResDTO policySurrenderApplyResDTO  =
//				insuranceService.applySurrender(policySurrenderApplyReqDTO);
//		System.out.println("保险订单赎回提交：\n" + XmlUtil.toXml(policySurrenderApplyResDTO, clazz));
//	}
	
	
//	@Test
//	public void testSurrenderImprto() throws Exception{
//		policySurrenderApplyBatch.execute();
//	}
}
