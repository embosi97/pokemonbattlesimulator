package com.impact.pokemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(Map.of(
                "spring.main.log-startup-info", false,
                "server.port", PORT));
        app.run(args);
        logger.info("Browse to: http://localhost:{}", PORT);
    }
}
