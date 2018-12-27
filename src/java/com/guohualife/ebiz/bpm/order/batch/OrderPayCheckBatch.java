package com.guohualife.ebiz.bpm.order.batch;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.guohualife.common.util.quartz.batch.SingleThreadBatch;
import com.guohualife.ebiz.bpm.order.service.OrderBatchService;

/**
 * 支付结果批处理
 * 
 * @author wangxulu
 *
 */
@Component
public class OrderPayCheckBatch implements SingleThreadBatch{

	private final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private OrderBatchService orderBatchService;
	
	public void execute() throws Exception {
		logger.info("支付中查询状态批处理开始");
		orderBatchService.dealPayingOrder();
		logger.info("支付中查询状态批处理结束");
	}
	
}
