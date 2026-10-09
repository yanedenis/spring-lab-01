package kz.iitu.springlab.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public void before(JoinPoint jp) {
        log.info("[LOG] -> {} args={}", 
                        jp.getSignature().toShortString(),
                        Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "kz.iitu.springlab.aspect.Pointcuts.serviceOperation()", returning = "result")
    public void afterReturning(JoinPoint jp, Object result) {
        log.info("[LOG] <- {} returned {}", jp.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "kz.iitu.spring.lab.aspect.Pointcuts.serviceOperation()", throwing = "ex")
    public void afterThrowing(JoinPoint jp, Exception ex) {
        log.info("[LOG] X {} threw {}: {}", jp.getSignature().toShortString(), ex.getClass().getSimpleName(), ex.getMessage());
    }
}
