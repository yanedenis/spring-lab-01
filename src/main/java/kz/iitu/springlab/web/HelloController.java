package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController 
@RequestMapping("/api")

// Endpoints
public class HelloController {
    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner, 
                        System.getProperty("java.version"),
                        Runtime.getRuntime().availableProcessors());
    }

    @GetMapping("/sum")
    public Sum sum(@RequestParam(defaultValue = "0", name = "a") int firstNum, 
                   @RequestParam(defaultValue = "0", name = "b") int secondNum) {
        return new Sum(firstNum + secondNum, 
                       firstNum - secondNum, 
                       firstNum * secondNum);
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) {}

    public record Info(String owner, String javaVersion, int cpuCores) {}

    public record Sum(int sum, int difference, int product) {}
}
