package com.ai.common.exception;

import com.ai.common.result.ResultCode;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(ResultCode.FORBIDDEN, message);
    }
}
