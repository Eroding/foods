package com.cnh.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {


    //  @Around("execution(* com.cnh.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("执行{}.{}", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long take = end - begin;
        if (take > 3000) {
            log.error("{}", take);
        } else if (take > 2000) {
            log.warn("{}", take);
        } else {
            log.info("{}", take);
        }
        return result;
    }
}
