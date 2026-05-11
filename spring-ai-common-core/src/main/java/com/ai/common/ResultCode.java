package com.ai.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // 成功
    SUCCESS(200, "操作成功"),

    // 客户端错误 4xx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    // 服务端错误 5xx
    INTERNAL_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),

    // 业务错误 1xxx
    BUSINESS_ERROR(1000, "业务处理失败"),
    DATA_ALREADY_EXISTS(1001, "数据已存在"),
    DATA_NOT_EXISTS(1002, "数据不存在"),
    DATA_VALIDATION_FAILED(1003, "数据校验失败"),
    OPERATION_NOT_ALLOWED(1004, "操作不允许"),

    // AI 模块 2xxx
    AI_SERVICE_ERROR(2000, "AI 服务异常"),
    AI_MODEL_TIMEOUT(2001, "模型响应超时"),
    AI_TOKEN_LIMIT_EXCEEDED(2002, "Token 超出限制"),
    AI_CONTENT_FILTERED(2003, "内容被安全过滤"),
    AI_VECTOR_STORE_ERROR(2004, "向量存储异常"),
    AI_DOCUMENT_PARSE_ERROR(2005, "文档解析失败");

    private final int code;
    private final String message;
}
