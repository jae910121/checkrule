package com.guohualife.ebiz.bpm.credit.check.dto;

public class CheckRuleDTO {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数名
     */
    private String paramName;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 校验范围
     */
    private String scopeValue;
    
    /**
     * 说明
     */
    private String remark;
    
    /**
     * 子结果标志
     */
	private String subResultCode;
	
	/**
	 * 子错误原因
	 */
	private String subResultMessage;
    
    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

	public String getScopeValue() {
		return scopeValue;
	}

	public void setScopeValue(String scopeValue) {
		this.scopeValue = scopeValue;
	}

	/**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the subResultCode
	 */
	public String getSubResultCode() {
		return subResultCode;
	}

	/**
	 * @param subResultCode the subResultCode to set
	 */
	public void setSubResultCode(String subResultCode) {
		this.subResultCode = subResultCode;
	}

	/**
	 * @return the subResultMessage
	 */
	public String getSubResultMessage() {
		return subResultMessage;
	}

	/**
	 * @param subResultMessage the subResultMessage to set
	 */
	public void setSubResultMessage(String subResultMessage) {
		this.subResultMessage = subResultMessage;
	}

}
