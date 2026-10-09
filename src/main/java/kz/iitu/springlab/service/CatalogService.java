package kz.iitu.springlab.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import kz.iitu.springlab.audit.Audited;
import kz.iitu.springlab.retry.RetryOnFailure;

@Service 
public class CatalogService {
    public String findById(long id) {
        sleep(50);
        return "Item no. " + id;
    }

    @Audited(action="CATALOG_LIST", logArguments=true)
    public List<String> findAll(int limit) {
        sleep(300);
        return IntStream.rangeClosed(1, limit)
                        .mapToObj(i -> "Item no. " + i)
                        .toList();
    }

    @Audited(action="CATALOG_REMOVE")
    public String remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid identifier: " + id);
        }
        return "Remove Item no. " + id;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Autowired
    @Lazy
    private CatalogService self;
    public String removeTwice(long id) {
        String first = self.remove(id);
        String second = self.remove(id + 1);
        return first + "; " + second;
    }

    private int attempts = 0;

    @RetryOnFailure(maxAttempts = 3, delayMs = 300)
    public String retryOnFailure(long id) {
        attempts++;
        if (attempts < 3) {
            throw new RuntimeException("Unstable source failed (attempt " + attempts + ")");
        }
        return "fetched item " + id + " after " + attempts + " attempts";
    }
}
