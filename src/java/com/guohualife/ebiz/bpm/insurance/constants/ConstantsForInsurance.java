package com.guohualife.ebiz.bpm.insurance.constants;


public class ConstantsForInsurance {

	public static final String HUNDRED = "100";
	/**
	 * 保单状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_POLICY_STATUS{
	    WAITUNDERWRITE("01","等待核保"),
	    UNDERWRITESUCCESS("02","核保通过"),
	    WAITACCEPTINSURANCE("03","等待承保"),
	    ACCEPTINSURANCESUCCESS("04","承保成功"),
	    ACCEPTINSURANCEFAILED("05","承保失败"),
	    SURRENDERSUCCESS("06","退保成功"),
	    UNDERWRITEFAILED("07","核保失败");
	    
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_POLICY_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 保单在线回访状态
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_REVISIT_STATUS{
		REVISITSUCCESS("0","回访成功"),
		REVISITFAILED("1","已回访");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_REVISIT_STATUS(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
	
	/**
	 * 投被保人是否是本人
	 * 
	 * @author wangxulu
	 *
	 */
	public static enum ENUM_INSURED_TYPE{
		SELF("1","本人"),
		NOTSELF("2","非本人");
		
	    private final String value;
	    private final String name;
	    
	    public String getValue() {
            return value;
        }
	    
	    public String getName() {
            return name;
        }
	    
	    ENUM_INSURED_TYPE(String value, String name){
	        this.value = value;
            this.name = name;
	    }
	    
	}
}
