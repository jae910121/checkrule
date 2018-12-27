package com.guohualife.ebiz.bpm.credit.check.constant;


/**
 * 校验规则枚举类
 * 
 * @author hezg_cyd
 * @history add by hezg_cyd 2015/8/4
 */
public class ConstantsForCheckRule {

	/**
	 * 平台类型
	 * @author hezg_cyd
	 *
	 */
	public enum ENUM_PLATFORM_TYPE {
		WEBSITE("01", "官网"), MOBILE("02", "手机app"), WEIXIN("03", "微信"), THIRDPLAT(
				"04", "第三方接口"), ALIPAY("05", "支付宝钱包"), AUTO("06", "自动处理"), YIXIN(
				"07", "易信");

		private String value;
		private String name;

		ENUM_PLATFORM_TYPE(String value, String name) {
			this.value = value;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
		public static ENUM_PLATFORM_TYPE getEnum(String value) {
	        for (ENUM_PLATFORM_TYPE e : ENUM_PLATFORM_TYPE.values()) {
	            if (value.equals(e.getValue())) {
	                return e;
	            }
	        }
	        return null;
	    }

	}
	/**
	 * 交易类型枚举
	 */
	public static enum ENUM_BUSSINESS_TYPE {

		CREDITAPPLY("1", "债权购买"), 
		CREDITSURRENDER("2", "债权赎回");

		private final String value;

		private final String name;

		public String getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		ENUM_BUSSINESS_TYPE(String value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public static ENUM_BUSSINESS_TYPE getEnum(String value) {
	        for (ENUM_BUSSINESS_TYPE e : ENUM_BUSSINESS_TYPE.values()) {
	            if (value.equals(e.getValue())) {
	                return e;
	            }
	        }
	        return null;
	    }
	}
	
	/**
	 * 交易子类型枚举
	 * 
	 * @author zhangyl_ebiz
	 * 
	 */
	public static enum ENUM_SUB_BUSSINESS_TYPE {
		// 债权购买
		BUY("1", "债权购买"),
		// 债权赎回
		SURRENDER("1", "赎回"), 
		HESITATE("2", "退款"), 
		SPECIAL("2", "特殊赎回"), 
		AUTOSURRENDER("4", "到期自动赎回");
		
		private final String value;

		private final String name;

		public String getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		ENUM_SUB_BUSSINESS_TYPE(String value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public static ENUM_SUB_BUSSINESS_TYPE getEnum(String value) {
	        for (ENUM_SUB_BUSSINESS_TYPE e : ENUM_SUB_BUSSINESS_TYPE.values()) {
	            if (value.equals(e.getValue())) {
	                return e;
	            }
	        }
	        return null;
	    }
	}
	
	
	/**
	 * 规则校验结果枚举
	 * 
	 * @author zhangyl_ebiz
	 * 
	 */
	public static enum CHECK_RESULT {

		SUCCESS("1", "校验通过"), 
		FAIL("0", "校验不通过"), 
		NEED_OTHER_HANDLE("2", "需其它处理"); 

		private final String value;

		private final String name;

		public String getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		CHECK_RESULT(String value, String name) {
			this.value = value;
			this.name = name;
		}
	}
	
}
