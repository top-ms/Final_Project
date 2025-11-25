package com.epam.rd.autocode.assessment.appliances.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CrudOperationsAspect {

    private static final Logger logger = LoggerFactory.getLogger(CrudOperationsAspect.class);

    @AfterReturning("execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.save*(..))")
    public void logCreateOperation(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("🆕 [CREATE] {}.{} - Новий запис створено", className, methodName);
    }

    @AfterReturning("execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.delete*(..))")
    public void logDeleteOperation(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.warn("🗑️ [DELETE] {}.{} - Запис видалено. ID: {}", className, methodName, args[0]);
    }

    @AfterReturning("execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.update*(..))")
    public void logUpdateOperation(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("📝 [UPDATE] {}.{} - Запис оновлено. Параметри: {}",
                className, methodName, args.length > 0 ? args[0] : "N/A");
    }

    @AfterReturning(pointcut = "execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.find*(..))",
            returning = "result")
    public void logFindOperation(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        boolean found = result != null && !result.toString().contains("Optional.empty");

        if (found) {
            logger.info("[FIND] {}.{} - Дані знайдено", className, methodName);
        } else {
            logger.warn("[FIND] {}.{} - Дані НЕ знайдено", className, methodName);
        }
    }

    @Before("execution(* com.epam.rd.autocode.assessment.appliances.service.impl.*.*Password*(..))")
    public void logPasswordOperation(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.warn("🔐 [SECURITY] {}.{} - Операція з паролем виконується", className, methodName);
    }
}