package com.guohualife.ebiz.bpm.insurance.service;


/**
 * 保险批处理Service接口
 * 
 * @author wangxulu
 *
 */
public interface InsuranceBatchService {

	/**
	 * 处理犹豫期解冻
	 * 
	 * @param ebizPolicy
	 */
	public void dealPolicyThaw();
	
}
