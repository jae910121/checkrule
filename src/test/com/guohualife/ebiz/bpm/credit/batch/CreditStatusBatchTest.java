package com.guohualife.ebiz.bpm.credit.batch;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.guohualife.ebiz.bpm.base.BaseTester;
import com.guohualife.ebiz.bpm.order.batch.OrderSurrenderBatch;

public class CreditStatusBatchTest extends BaseTester {

	private static final Log logger = LogFactory
			.getLog(CreditStatusBatchTest.class);

	@Resource
	private OrderSurrenderBatch orderSurrenderBatch;

	@Test
	public void testExecute() {
		try {
			orderSurrenderBatch.execute();
		} catch (Exception e) {
			logger.info("债权状态更新批处理运行出错", e);
		}
	}

}
