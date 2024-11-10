package com.sololiving.global.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class ExecutionTimeLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExecutionTimeLoggerAspect.class);
    private static final String START_TIME_ATTR = "requestStartTime";
    private static final String CONTROLLER_EXECUTED_ATTR = "controllerExecuted";

    @Pointcut("execution(* com.sololiving.domain..*(..))")
    public void applicationPackagePointcut() {
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPointcut() {
    }

    @Around("applicationPackagePointcut() && !controllerPointcut()")
    @Order(1)
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        logExecutionTimeAsync(joinPoint.getSignature().toString(), executionTime);

        return proceed;
    }

    @Around("controllerPointcut()")
    @Order(0)
    public Object logTotalExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            requestAttributes.setAttribute(START_TIME_ATTR, System.currentTimeMillis(),
                    RequestAttributes.SCOPE_REQUEST);
            requestAttributes.setAttribute(CONTROLLER_EXECUTED_ATTR, true,
                    RequestAttributes.SCOPE_REQUEST);
        }

        Object proceed = joinPoint.proceed();

        if (requestAttributes != null) {
            Long startTime = (Long) requestAttributes.getAttribute(START_TIME_ATTR,
                    RequestAttributes.SCOPE_REQUEST);
            Boolean controllerExecuted = (Boolean) requestAttributes.getAttribute(CONTROLLER_EXECUTED_ATTR,
                    RequestAttributes.SCOPE_REQUEST);
            if (startTime != null && controllerExecuted != null && controllerExecuted) {
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                logTotalExecutionTimeAsync(joinPoint.getSignature().toString(),
                        totalExecutionTime);
                requestAttributes.removeAttribute(CONTROLLER_EXECUTED_ATTR,
                        RequestAttributes.SCOPE_REQUEST);
            }
        }

        return proceed;
    }

    @Async("logTaskExecutor")
    public void logExecutionTimeAsync(String signature, long executionTime) {
        logger.info("{} 실행 시간 : {} ms", signature, executionTime);
    }

    @Async("logTaskExecutor")
    public void logTotalExecutionTimeAsync(String signature, long executionTime) {
        logger.info("전체 요청 처리 시간 : ({}부터) : {} ms", signature, executionTime);
    }
}
