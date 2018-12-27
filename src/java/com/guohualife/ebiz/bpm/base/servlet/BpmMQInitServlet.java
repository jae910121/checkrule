package com.guohualife.ebiz.bpm.base.servlet;

import java.util.Collection;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.guohualife.common.util.mq.client.MQConfig;
import com.guohualife.common.util.mq.client.MQConsumer;
import com.guohualife.common.util.mq.context.MQConsumerContext;
import com.guohualife.common.util.mq.factory.MQFactory;
import com.guohualife.common.util.mq.listener.MQMessageListener;
import com.guohualife.platform.common.api.context.SpringContext;

public class BpmMQInitServlet extends HttpServlet {

	private static final long serialVersionUID = -509406553843843889L;
	
	private final Log logger = LogFactory.getLog(getClass());

	private static MQFactory factory = null;
	
	private static Properties bpmProperties = null;
	
	private static void ensureInit() {
		if (null == factory) {
			factory = (MQFactory) SpringContext.getBean("MQFactoryImpl");
		}

		if (null == bpmProperties) {
			bpmProperties = (Properties) SpringContext.getBean("bpmProperties");
		}
	}
	
	@Override
	public void init() {
		try {
			logger.info("消息初始化开始");
			ensureInit();
			
			MQConfig config = new MQConfig();			
			config.setClientType(bpmProperties.getProperty("mq.producer.clientType"));
			config.setGroupName(bpmProperties.getProperty("mq.producer.groupName"));
			config.setInstanceName(bpmProperties.getProperty("mq.producer.instanceName"));
			config.setServerAddress(bpmProperties.getProperty("mq.producer.serverAddress"));
			
			MQConsumer consumer = factory.createConsumer(config);
			consumer.subscribe("gh_ebiz_customer_mq_accountMerge", "",
					(MQMessageListener) SpringContext
							.getBean("orderMergeListener"));
			consumer.start();
			MQConsumerContext.addConsumer(consumer);
			
			logger.info("消息初始化成功");
		} catch (Exception e) {
			logger.error("加载消息异常", e);
		}

	}
	
	/**
	 * 重新调度消息任务
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		logger.info("BpmMQConsumer重新调度开始");
		try {
			reloadConsumer();
		} catch (Exception e) {
			logger.info("BpmMQConsumer调度失败！\n", e);
		}
		logger.info("BpmMQConsumer重新调度结束");
	}

	/**
	 * 重新调度MQConsumer任务
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		this.doGet(request, response);
	}
	
	public void shutdownConsumer() throws Exception {
		Collection<MQConsumer> consumers = MQConsumerContext.getRunningConsumer();
		for (MQConsumer consumer : consumers) {
			consumer.shutdown();
			MQConsumerContext.removeConsumer(consumer);
		}
		logger.info("removeBpmConsumer success");		
	}

	public void reloadConsumer() throws Exception {
		shutdownConsumer();		
		init();
		logger.info("reloadBpmConsumer success");		
	}
	
}
