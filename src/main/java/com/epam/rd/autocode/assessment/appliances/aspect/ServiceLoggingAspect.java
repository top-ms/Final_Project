package com.epam.rd.autocode.assessment.appliances.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    // Pointcut для всіх методів у сервісах
    @Pointcut("execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.*(..))")
    public void serviceMethodPointcut() {}

    @Before("serviceMethodPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("🔵 [BEFORE] {}.{}() викликано з параметрами: {}",
                className, methodName, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "serviceMethodPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("✅ [SUCCESS] {}.{}() виконано успішно. Результат: {}",
                className, methodName, result);
    }

    @AfterThrowing(pointcut = "serviceMethodPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.error("❌ [ERROR] {}.{}() викинув виняток: {}",
                className, methodName, exception.getMessage());
    }

    @Around("serviceMethodPointcut()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            logger.info("⏱️ [TIMING] {}.{}() виконався за {} мс",
                    className, methodName, (endTime - startTime));

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("⏱️❌ [TIMING ERROR] {}.{}() завершився з помилкою за {} мс",
                    className, methodName, (endTime - startTime));
            throw e;
        }
    }
}