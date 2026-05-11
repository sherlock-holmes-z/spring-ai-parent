package com.ai.common.exception;

import com.ai.common.ResultCode;

public class AiServiceException extends BaseException {
    public AiServiceException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public AiServiceException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }
}
