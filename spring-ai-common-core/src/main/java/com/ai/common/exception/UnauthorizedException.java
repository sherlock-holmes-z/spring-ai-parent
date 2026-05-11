package com.ai.common.exception;

import com.ai.common.result.ResultCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED);
    }
}
