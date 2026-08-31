package com.portfolio.jobplatform.exception;
import org.springframework.http.HttpStatus;
public class BusinessRuleException extends ApiException {
    public BusinessRuleException(HttpStatus status, String message) { super(status, message); }
    public static BusinessRuleException conflict(String message) { return new BusinessRuleException(HttpStatus.CONFLICT, message); }
    public static BusinessRuleException unprocessable(String message) { return new BusinessRuleException(HttpStatus.UNPROCESSABLE_ENTITY, message); }
    public static BusinessRuleException forbidden(String message) { return new BusinessRuleException(HttpStatus.FORBIDDEN, message); }
}
