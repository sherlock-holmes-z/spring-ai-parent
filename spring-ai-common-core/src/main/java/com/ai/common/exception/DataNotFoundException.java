package com.ai.common.exception;

import com.ai.common.ResultCode;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(ResultCode.DATA_NOT_EXISTS, message);
    }
}
