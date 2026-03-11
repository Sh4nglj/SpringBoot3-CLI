package com.rosy.framework.aspect;

import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class ValidationAspect {

    private final Validator validator;

    public ValidationAspect(Validator validator) {
        this.validator = validator;
    }

    @Around("@annotation(com.rosy.common.annotation.ValidateRequest)")
    public Object validateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
            }
            Set<ConstraintViolation<Object>> violations = validator.validate(arg);
            if (!violations.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder();
                for (ConstraintViolation<Object> violation : violations) {
                    errorMsg.append(violation.getMessage()).append("; ");
                }
                throw new BusinessException(ErrorCode.PARAMS_ERROR, errorMsg.toString().trim());
            }
        }
        return joinPoint.proceed();
    }
}
