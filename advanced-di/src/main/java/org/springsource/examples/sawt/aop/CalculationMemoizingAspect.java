package org.springsource.examples.sawt.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CalculationMemoizingAspect {

    private Log log = LogFactory.getLog(getClass());

    private Map<String, Integer> cache = new ConcurrentHashMap<String, Integer>();

    @Around(value = "execution(public int org.springsource.examples.sawt.aop.Calculator.*(int, int)) && args(a,b)", argNames = "joinPoint,a,b")
    public Object cacheAround(ProceedingJoinPoint joinPoint, int a, int b) throws Throwable {

        String key = a + "," + b;

        Integer complex = cache.get(key);

        if (complex == null) {

            log.info("Cache MISS for (" + key + ")");
            complex = (Integer) joinPoint.proceed();
            cache.put(key, complex);
        } else {

            log.info("Cache HIT for (" + key + ")");
        }

        return complex;
    }
}
