package com.guohualife.ebiz.bpm.credit.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guohualife.ebiz.bpm.credit.service.CreditAssetService;
import com.guohualife.platform.common.api.context.SpringContext;
import com.guohualife.platform.common.api.util.DateUtil;
import com.guohualife.platform.common.api.util.StringUtil;

public class CreditAssetServlet extends HttpServlet {

	private static final long serialVersionUID = -509406553843843889L;
	
//	private final Log logger = LogFactory.getLog(getClass());

	private static CreditAssetService creditAssetService = null;
	
	private static void ensureInit() {
		if (null == creditAssetService) {
			creditAssetService = (CreditAssetService) SpringContext.getBean("creditAssetServiceImpl");
		}
	}
	
	/**
	 * 重新调度消息任务
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		ensureInit();
		String balanceDate = request.getParameter("balanceDate");
		if(!StringUtil.isEmpty(balanceDate)){
			creditAssetService.dealAsset(DateUtil.parseDate(balanceDate, "yyyy-MM-dd"));
		}
	}

	/**
	 * 重新调度MQConsumer任务
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		this.doGet(request, response);
	}
	
}
