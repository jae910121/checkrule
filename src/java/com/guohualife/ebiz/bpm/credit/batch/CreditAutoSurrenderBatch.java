package com.guohualife.ebiz.bpm.credit.batch;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.guohualife.common.util.quartz.batch.SingleThreadBatch;
import com.guohualife.ebiz.bpm.credit.service.CreditSurrenderService;

@Component
public class CreditAutoSurrenderBatch implements SingleThreadBatch {
	
	private static final Log logger = LogFactory
			.getLog(CreditSurrenderTransferBatch.class);
	
	@Resource
	private CreditSurrenderService creditSurrenderService;
	
	public void execute() throws Exception {
		logger.info("债权到期自动赎回批处理开始");
		creditSurrenderService.autoSurrender();
		logger.info("债权到期自动赎回批处理结束");
	}

}
