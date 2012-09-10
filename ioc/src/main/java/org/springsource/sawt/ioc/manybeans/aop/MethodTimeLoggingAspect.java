package org.springsource.sawt.ioc.manybeans.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MethodTimeLoggingAspect {

    @Around("@annotation(org.springsource.sawt.ioc.manybeans.Loggable)")
    public Object logMethodTimes(ProceedingJoinPoint invocation) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = invocation.proceed(); // call the method

        long stop = System.currentTimeMillis();
        System.out.println(invocation.getSignature().getName() + ": " + (stop - start) + "ms");
        return result;
    }
}

