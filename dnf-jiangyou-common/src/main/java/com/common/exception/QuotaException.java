
package com.common.exception;

import com.common.enums.QuotaErrorCode;
import lombok.Getter;

/**
 * <b>DESCRIPTION:
 * <b>Create on:</b>2017-4-28 下午1:49:56<br/>
 */
public class QuotaException extends RuntimeException{

    private static final long serialVersionUID = -8713734678959284746L;

    @Getter
    private final String code;

    public QuotaException(String code) {
        this.code = code;
    }

    public QuotaException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public QuotaException(String code, String message) {
        super(message);
        this.code = code;
    }

    public QuotaException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    public QuotaException(QuotaErrorCode quotaErrorCode) {
        super(quotaErrorCode.getErrordesc());
        this.code = quotaErrorCode.getErrorcode();
    }
}
