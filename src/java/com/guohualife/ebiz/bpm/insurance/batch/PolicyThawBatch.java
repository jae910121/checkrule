package com.guohualife.ebiz.bpm.insurance.batch;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.guohualife.common.util.quartz.batch.SingleThreadBatch;
import com.guohualife.ebiz.bpm.insurance.bo.InsuranceBo;
import com.guohualife.ebiz.bpm.insurance.service.InsuranceBatchService;

/**
 * 保单犹豫期解冻批处理
 * 
 * @author wangxulu
 *
 */
@Component
public class PolicyThawBatch implements SingleThreadBatch{

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private InsuranceBo insuranceBo;
	
	@Resource
	private InsuranceBatchService insuranceBatchService;	

	public void execute() throws Exception {
		logger.info("保单犹豫期解冻批处理开始");
		insuranceBatchService.dealPolicyThaw();
		logger.info("保单犹豫期解冻批处理结束");
	}
	
}
