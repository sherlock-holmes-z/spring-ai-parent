package com.ai.common.exception;

import com.ai.common.result.Result;
import com.ai.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 1. 自定义业务异常（最高优先级）
    @ExceptionHandler(BaseException.class)
    public Result<Void> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("业务异常 | uri={} | code={} | message={}",
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 2. 参数校验异常（@Valid 触发）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败 | {}", message);
        return Result.fail(ResultCode.BAD_REQUEST, message);
    }

    // 3. 参数绑定异常（@RequestParam 触发）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数约束违反 | {}", message);
        return Result.fail(ResultCode.BAD_REQUEST, message);
    }

    // 4. 请求方法不支持
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return Result.fail(ResultCode.METHOD_NOT_ALLOWED,
                "不支持 " + e.getMethod() + " 请求方式");
    }

    // 5. 请求体解析失败
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败 | {}", e.getMessage());
        return Result.fail(ResultCode.BAD_REQUEST, "请求体格式错误");
    }

    // 6. Feign 调用异常（服务间调用）
//    @ExceptionHandler(FeignException.class)
//    public Result<Void> handleFeignException(FeignException e, HttpServletRequest request) {
//        log.error("Feign 调用异常 | uri={} | status={} | message={}",
//                request.getRequestURI(), e.status(), e.getMessage());
//        if (e.status() == 404) {
//            return Result.fail(ResultCode.NOT_FOUND, "下游服务资源不存在");
//        }
//        return Result.fail(ResultCode.SERVICE_UNAVAILABLE, "下游服务调用失败");
//    }

    // 7. 兜底异常（必须放最后）
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 | uri={} | message={}",
                request.getRequestURI(), e.getMessage(), e);
        // 生产环境不暴露内部错误详情
        return Result.fail(ResultCode.INTERNAL_ERROR);
    }
}
