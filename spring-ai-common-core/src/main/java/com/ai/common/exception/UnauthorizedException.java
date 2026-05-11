package com.ai.common.exception;

import com.ai.common.ResultCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED);
    }
}
