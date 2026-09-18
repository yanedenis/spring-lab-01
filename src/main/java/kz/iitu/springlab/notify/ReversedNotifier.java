package kz.iitu.springlab.notify;

import org.springframework.context.annotation.*;
import org.slf4j.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.annotation.*;


@Component("reversed")
@Order(3)
public class ReversedNotifier implements Notifier {
    private static final Logger log = LoggerFactory.getLogger(ReversedNotifier.class);

    @PostConstruct 
    void init() {
        log.info("Reversed notifier has been initialized");
    }

    @Override 
    public String send(String message) {
        return new StringBuilder(message).reverse().toString();
    }

    @Override 
    public String channel() { return "reversed"; }
}
