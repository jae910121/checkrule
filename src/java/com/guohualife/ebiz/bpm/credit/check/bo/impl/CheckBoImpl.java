package com.guohualife.ebiz.bpm.credit.check.bo.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.guohualife.ebiz.bpm.credit.check.bo.CheckBo;
import com.guohualife.ebiz.bpm.credit.check.constant.ConstantsForCheckRule;
import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleInputDTO;
import com.guohualife.ebiz.bpm.credit.check.dto.CheckRuleResultDTO;
import com.guohualife.ebiz.product.dto.EbizProductCheckruleDTO;
import com.guohualife.ebiz.product.service.ProductService;
import com.guohualife.edb.product.model.EbizProductCheckrule;
import com.guohualife.platform.common.api.context.SpringContext;
import com.guohualife.platform.common.api.util.CollectionUtil;
import com.guohualife.platform.common.api.util.ReflectionUtil;
import com.guohualife.platform.common.api.util.StringUtil;

public class CheckBoImpl implements CheckBo {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private ProductService productService;
	
	/**
     * 执行校验
     * 
     * @param CheckRuleInputDTO
     * @return 
     * @throws Exception
     */
	public CheckRuleResultDTO check(CheckRuleInputDTO checkRuleInputDTO) throws Exception {
		CheckRuleResultDTO checkRuleResultDTO = new CheckRuleResultDTO();
		checkRuleResultDTO.setResultCode(ConstantsForCheckRule.CHECK_RESULT.SUCCESS.getValue());
		checkRuleResultDTO.setResultMessage("");
		
		List<EbizProductCheckrule> checkRuleList = this.getCheckRuleList(checkRuleInputDTO);
		if (null != checkRuleList && checkRuleList.size() > 0) {
			for (EbizProductCheckrule rule : checkRuleList) {
				checkRuleResultDTO = this.execute(rule,
						checkRuleInputDTO.getParamObject());
				if (!(ConstantsForCheckRule.CHECK_RESULT.SUCCESS.getValue())
						.equals(checkRuleResultDTO.getResultCode())) {
					break;
				}
			}
		} else {
			logger.info("ProductCode:" + checkRuleInputDTO.getProductCode() 
					+ "BusinessType:" + checkRuleInputDTO.getBusinessType() 
					+ "没有为此渠道产品配置校验规则,不进行校验");
			if (checkRuleInputDTO.getBusinessType().equals(
					ConstantsForCheckRule.ENUM_BUSSINESS_TYPE.CREDITAPPLY
							.getValue())) {
				checkRuleResultDTO.setResultCode(ConstantsForCheckRule.CHECK_RESULT.SUCCESS.getValue());
			}
		}
		return checkRuleResultDTO;
	}
	
	/**
	 * 查询校验规则
	 * 
	 * @param checkRuleInputDTO
	 * @return
	 */
	private List<EbizProductCheckrule> getCheckRuleList(CheckRuleInputDTO checkRuleInputDTO){
		EbizProductCheckruleDTO checkruleDTO = 
				this.productService.getProductCheckrule(
				checkRuleInputDTO.getProductCode(), 
				checkRuleInputDTO.getOrderType(),
				checkRuleInputDTO.getPlatformType(),
				checkRuleInputDTO.getBusinessType(),
				checkRuleInputDTO.getBusinessSubType());
		return checkruleDTO.getEbizProductCheckruleList();
	}
	
	/**
	 * 执行校验规则
	 * 
	 * @param ebizProductCheckrule
	 * @param paramObj
	 * @throws Exception 
	 */
	private CheckRuleResultDTO execute(EbizProductCheckrule rule, Object paramObj) throws Exception{
		CheckRuleResultDTO checkRuleResultDTO = new CheckRuleResultDTO();
		checkRuleResultDTO.setResultCode(ConstantsForCheckRule.CHECK_RESULT.SUCCESS.getValue());
		checkRuleResultDTO.setResultMessage("");
		logger.info("开始执行校验，校验备注为：" + rule.getRemark());
		List<List<?>> paramList = getParamValue(paramObj, rule.getParamName());
		for (List<?> param : paramList) {
			Class<?> invokeClazz = Class.forName(rule.getClassName());
			Method invokeMethod = invokeClazz.getDeclaredMethod(rule.getMethodName(), List.class, List.class);
			Object methodResult;
			if (Modifier.isStatic(invokeMethod.getModifiers())) {
				methodResult = invokeMethod.invoke(null, param,
						getOneScopeValue(rule.getScopeValue()));
			} else {
				Object invokeObject;
				try {// 优先使用spring中注入的对象
					invokeObject = SpringContext.getBean(invokeClazz);
				} catch (IllegalArgumentException iae) {
					invokeObject = null;
				}
				if (invokeObject == null) {// 非spring中注入对象的实例方法
					invokeObject = invokeClazz.newInstance();
				}
				methodResult = invokeMethod.invoke(invokeObject, param,
						getOneScopeValue(rule.getScopeValue()));
			}
			if (ConstantsForCheckRule.CHECK_RESULT.FAIL.getValue().equals(
					methodResult.toString())) {
				logger.info(rule.getErrorMessage());
				checkRuleResultDTO.setResultCode(methodResult.toString());
				checkRuleResultDTO.setResultMessage(rule.getErrorMessage());
			} else if (ConstantsForCheckRule.CHECK_RESULT.NEED_OTHER_HANDLE
					.getValue().equals(methodResult.toString())) {
				logger.info(rule.getErrorMessage());
				checkRuleResultDTO.setResultCode(methodResult.toString());
				checkRuleResultDTO.setResultMessage(rule.getErrorMessage());
			}
		}
		return checkRuleResultDTO;
	
	}	
	
	/**
	 * 方法功能描述
	 * 
	 * @param 参数名称
	 *            参数说明
	 * @return void
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<List<?>> getParamValue(Object obj, String paramPaths) throws Exception {
		// 多个参数以','号分割
		String[] paramNameList = StringUtils.split(paramPaths, ",");

		// 各个参数的list集合
		List<List<?>> paramValueList = new ArrayList<List<?>>();

		// 校验参数对象-从根路径到校验参数对象的所有对象List
		Map<Object, ArrayList<Object>> paramToPath = new HashMap<Object, ArrayList<Object>>();

		for (String paramNames : paramNameList) {
			List<Object> params = new ArrayList<Object>();

			Object lastObj = obj;
			String[] param = StringUtils.split(paramNames, ".");
			ArrayList<Object> path = new ArrayList<Object>();
			for (int i = 0; i < param.length; i++) {
				if ("insuranceInputDTO".equalsIgnoreCase(paramNames)) {
					/*
					 * ii对象本身,配置时可以带此前缀.比如'insuranceInputDTO.ebizAppntDto.birthday'和'ebizAppntDto.birthday'是一样的;
					 * 对于直接使用insuranceInputDTO的参数
					 * ，则必须是insuranceInputDTO(而不能是空白，不然无法区分是否置值)
					 */
					continue;
				}
				if ("surrenderCheckDTO".equalsIgnoreCase(paramNames)) {
					continue;
				}
				if (lastObj instanceof Collection) {// 容器类型
					List<Object> tempLastList = new ArrayList<Object>();
					for (Object oneInColl : (Collection<?>) lastObj) {

						ArrayList<Object> pathi = null;
						if (paramToPath.get(oneInColl) != null) {
							pathi = (ArrayList<Object>) paramToPath.get(
									oneInColl).clone();
						}
						if (pathi == null) {
							pathi = (ArrayList<Object>) path.clone();
						}
						paramToPath.put(oneInColl, (ArrayList<Object>) pathi
								.clone());
						pathi.add(oneInColl);
						Object innerObj = ReflectionUtil.invokeGetterMethod(
								oneInColl, param[i]);

						if (innerObj instanceof Collection) {// 支持多层容器类型属性
							pathi.add(innerObj);
							tempLastList.addAll((Collection) innerObj);
							for (Object innerInnerObj : (Collection) innerObj) {
								paramToPath.put(innerInnerObj, pathi);
							}
						} else if (innerObj instanceof Object[]) {
							pathi.add(innerObj);
							tempLastList.addAll(Arrays
									.asList((Object[]) innerObj));
							for (Object innerInnerObj : (Object[]) innerObj) {
								paramToPath.put(innerInnerObj, pathi);
							}
						} else {
							tempLastList.add(innerObj);
							paramToPath.put(innerObj, pathi);
						}
					}
					lastObj = tempLastList;
				} else if (lastObj instanceof Object[]) {// 数组类型
					List<Object> tempLastList = new ArrayList<Object>();
					for (Object oneInColl : (Object[]) lastObj) {
						ArrayList<Object> pathi = null;
						if (paramToPath.get(oneInColl) != null) {
							pathi = (ArrayList<Object>) paramToPath.get(
									oneInColl).clone();
						}
						if (pathi == null) {
							pathi = (ArrayList<Object>) path.clone();
						}
						paramToPath.put(oneInColl, (ArrayList<Object>) pathi
								.clone());
						pathi.add(oneInColl);
						Object innerObj = ReflectionUtil.invokeGetterMethod(
								oneInColl, param[i]);

						if (innerObj instanceof Collection) {// 支持多层容器类型属性
							pathi.add(innerObj);
							tempLastList.addAll((Collection) innerObj);
							for (Object innerInnerObj : (Collection) innerObj) {
								paramToPath.put(innerInnerObj, pathi);
							}
						} else if (innerObj instanceof Object[]) {
							pathi.add(innerObj);
							tempLastList.addAll(Arrays
									.asList((Object[]) innerObj));
							for (Object innerInnerObj : (Object[]) innerObj) {
								paramToPath.put(innerInnerObj, pathi);
							}
						} else {
							paramToPath.put(innerObj, pathi);
							tempLastList.add(innerObj);
						}
					}
					lastObj = tempLastList;
				} else if(lastObj instanceof Map){
					List<Object> tempLastList = new ArrayList<Object>();
					tempLastList.add(lastObj);
				}
				else {
					ArrayList<Object> pathi = null;
					if (paramToPath.get(lastObj) != null) {
						pathi = (ArrayList<Object>) paramToPath.get(lastObj).clone();
					}
					if (pathi == null) {
						pathi = path;
					}
					pathi.add(lastObj);
					lastObj = ReflectionUtil.invokeGetterMethod(lastObj, param[i]);
					paramToPath.put(lastObj, pathi);
				}
			}
			if (lastObj instanceof Collection) {
				params.addAll((Collection) lastObj);
			} else if (lastObj instanceof Object[]) {
				params.addAll(Arrays.asList((Object[]) lastObj));
			}else if(lastObj instanceof Map){
				params.add(lastObj);
			} 
			else {
				params.add(lastObj);
			}

			paramValueList.add(params);
		}

		// 整理，同路径的对象放到一起

		// 找出数量最多的参数，相同参数个数的，取路径深的
		int maxIndex = -1;
		int maxNum = -1;
		for (int i = 0; i < paramValueList.size(); i++) {
			List<?> paramList = paramValueList.get(i);
			if (paramList.size() > maxNum) {
				maxIndex = i;
				maxNum = paramList.size();
			}
		}

		// 根据paramToPath中各个参数对象路径整理参数
		List<List<?>> rtnParamValueList = new ArrayList<List<?>>(maxNum);
		for (int i = 0; i < maxNum; i++) {
			List<Object> addedParamList = new ArrayList<Object>(paramValueList
					.size());
			Object maxObj = paramValueList.get(maxIndex).get(i);
			for (int j = 0; j < paramValueList.size(); j++) {
				if (j == maxIndex) {// 最多个数的参数不需要整理
					addedParamList.add(maxObj);
				} else {// 根据maxObj的路径得到paramValueList.get(j)中合适的对象
					List<Object> maxObjPath = (ArrayList<Object>) paramToPath
							.get(maxObj).clone();
					maxObjPath.add(maxObj);
					Object maxLikeObj = null;// 找出和maxobj路径最近的对象
					int maxLikeNum = -1;
					for (Object curObj : paramValueList.get(j)) {
						if (curObj == obj) {
							maxLikeObj = curObj;
							break;
						}
						List<Object> curObjPath = (ArrayList<Object>) paramToPath
								.get(curObj).clone();
						curObjPath.add(curObj);
						// 如果比maxLikeObj关联的path更和maxObj相似
						int curLikeNum = getSameObjNum(curObjPath, maxObjPath);
						if (curLikeNum > maxLikeNum) {
							maxLikeObj = curObj;
							maxLikeNum = curLikeNum;
						}
					}
					addedParamList.add(maxLikeObj);
				}
			}
			rtnParamValueList.add(addedParamList);
		}

		return rtnParamValueList;
	}

	/**
	 * 取两个列表从开始一直相同元素的数量
	 * 
	 * @param l1
	 * @param l2
	 * @return
	 */
	private int getSameObjNum(List<Object> l1, List<Object> l2) {
		if (CollectionUtil.isEmpty(l1)) {
			return 0;
		}
		if (CollectionUtil.isEmpty(l2)) {
			return 0;
		}
		int compareSize = Math.min(l1.size(), l2.size());
		for (int i = 0; i < compareSize; i++) {
			if (l2.get(i) != l1.get(i)) {
				return i;
			}
		}
		return compareSize;
	}
	
	private List<String> getOneScopeValue(String scopeValues) throws Exception {
		List<String> scopes = new ArrayList<String>();
		if (StringUtil.isEmpty(scopeValues)) {
			return scopes;
		}
		String[] list = StringUtils.split(scopeValues, ",");
		for (String scope : list) {
			scopes.add(scope);
		}
		return scopes;
	}
	
}
