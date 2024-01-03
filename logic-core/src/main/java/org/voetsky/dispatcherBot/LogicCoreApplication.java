package org.voetsky.dispatcherBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LogicCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogicCoreApplication.class, args);
    }
}