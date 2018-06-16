package com.common.enums;

/**
 *  报错编码与信息枚举
 */
public enum QuotaErrorCode {
    /**
     * 000001---参数校验错误
     */
    PARAM_VALIDATION_ERROR("000001", "参数校验错误");

    private String errorCode;
    private String errorDesc;

    QuotaErrorCode(String errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public String getErrorcode() {
        return errorCode;
    }

    public String getErrordesc() {
        return errorDesc;
    }

}
