package com.guohualife.ebiz.bpm.credit.batch;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.guohualife.common.util.quartz.batch.SingleThreadBatch;
import com.guohualife.ebiz.bpm.credit.service.CreditAssetService;
import com.guohualife.platform.common.api.util.DateUtil;

@Component
public class CreditAssetBatch implements SingleThreadBatch {

	private static final Log logger = LogFactory
			.getLog(CreditAssetBatch.class);
	
	@Resource
	private CreditAssetService creditAssetService;
	
	public void execute() throws Exception {
		logger.info("债权产品计算收益批处理开始");
		creditAssetService.dealAsset(
				DateUtil.parseDate(
				DateUtil.formatDate(new Date(), DateUtil.ZH_CN_DATE_PATTERN),
				DateUtil.ZH_CN_DATE_PATTERN));
		logger.info("债权产品计算收益批处理结束");
	}
	
}
