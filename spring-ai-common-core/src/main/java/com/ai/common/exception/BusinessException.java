package com.ai.common.exception;

import com.ai.common.result.ResultCode;

// 业务异常（最常用）
public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(ResultCode.BUSINESS_ERROR, message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode);
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }
}
