package org.springsource.sawt.ioc.manybeans.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Simple {@link Aspect} that both filters out the candidate beans and intercepts appropriately.
 * <p/>
 * You can decouple these two aspects appropriately into "pointcuts" and "advice."
 * <p/>
 * The pointcuts then may be used with different "advice."
 */
@Aspect
public class MethodTimeLoggingAspect {

    @Around("@annotation(org.springsource.sawt.ioc.manybeans.Loggable)")
    public Object logMethodTimes(ProceedingJoinPoint invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long stop = System.currentTimeMillis();
        System.out.println(invocation.getSignature().getName() + ": " + (stop - start) + "ms");
        return result;
    }
}

