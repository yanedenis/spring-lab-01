package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import kz.iitu.springlab.audit.Audited;

@Aspect
@Component
@Order(1)
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Around("@annotation(audited)")
    public Object audit(ProceedingJoinPoint pjp, Audited audited) throws Throwable {
        String name = pjp.getSignature().toShortString();
        log.info("[AUDIT] {} success", audited.action());
        try {
            Object result = pjp.proceed();
            log.info("[AUDIT] {} success", audited.action());
            return result;
        } catch (Exception ex) {
            log.info("[AUDIT] {} failure: {}", audited.action(), ex.getMessage());
            throw ex;
        }
    }
}
