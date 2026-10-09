package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import kz.iitu.springlab.retry.RetryOnFailure;

@Aspect 
@Component 
@Order(0)
public class RetryAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Around("@annotation(retryOnFailure)")
    public Object retry(ProceedingJoinPoint pjp, RetryOnFailure retryOnFailure) throws Throwable {
        int attempt = 0;
        Throwable lastError;
        do {
            attempt++;
            try {
                return pjp.proceed();
            } catch (Throwable ex) {
                lastError = ex;
                log.warn("[RETRY] {} failed on attempt {}/{}: {}",
                        pjp.getSignature().toShortString(), attempt, retryOnFailure.maxAttempts(), ex.getMessage());
                if (attempt < retryOnFailure.maxAttempts()) {
                    Thread.sleep(retryOnFailure.delayMs());
                }
            }
        } while (attempt < retryOnFailure.maxAttempts());

        log.error("[RETRY] {} exhausted {} attempts, giving up",
                pjp.getSignature().toShortString(), retryOnFailure.maxAttempts());
        throw lastError;
    }
}
