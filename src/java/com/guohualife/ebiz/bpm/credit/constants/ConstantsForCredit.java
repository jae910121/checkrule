package com.guohualife.ebiz.bpm.credit.constants;



public class ConstantsForCredit {
	
	public static final String SUCCESS = "1";
	
	public static final String FAILED = "0";

	public static final String PROCESSING = "2";

	/**
	 * 债权状态
	 * 
	 * @author wangxulu
	 * 
	 */
	public static enum ENUM_CREDIT_STATUS{
		WAITCONFIRM("01", "待确认"),
	    RAISING("02","募集中"),
	    RAISESUCCESS("03","募集成功"),
	    RAISEFAILED("04","募集失败"),
	    REFUNDING("05","还款中"),
	    REFUNDSUCCESS("06","还款成功");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_CREDIT_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}

	public enum ENUM_PAY_CUSTOMER_CARD_TYPE {
		IDCARD("01", "0");

		private String payCardType;

		private String customerCardType;

		private ENUM_PAY_CUSTOMER_CARD_TYPE(String payCardType,
				String customerCardType) {
			this.payCardType = payCardType;
			this.customerCardType = customerCardType;
		}

		public static String getPayByCustomer(String customerCardType) {
			for (ENUM_PAY_CUSTOMER_CARD_TYPE payCustomerCardType : ENUM_PAY_CUSTOMER_CARD_TYPE
					.values()) {
				if (payCustomerCardType.customerCardType
						.equals(customerCardType)) {
					return payCustomerCardType.payCardType;
				}
			}
			return null;
		}

	}

}
