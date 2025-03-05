package com.sw.cmc.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * packageName    : com.sw.cmc.common.aop
 * fileName       : LoggingAop
 * author         : ihw
 * date           : 2025. 3. 5.
 * description    : logging aop
 */
@Aspect
@Component
public class LoggingAop {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAop.class);

    /**
     * Controller 클래스 내의 모든 메서드를 대상으로 하는 포인트컷
     */
    @Pointcut("execution(* com.sw..*Controller*.*(..))")
    public void controllerMethods() {}

    /**
     * 컨트롤러 메서드 실행 전/후 로깅 및 예외 처리
     */
    @Around("controllerMethods()")
    public Object logControllerExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("[Controller] {} 실행 완료 ({}ms)", joinPoint.getSignature(), elapsedTime);
            return result;
        } catch (Exception ex) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.error("[Controller] {} 예외 발생: {} ({}ms)", joinPoint.getSignature(), ex.getMessage(), elapsedTime, ex);
            throw ex;
        }
    }
}
