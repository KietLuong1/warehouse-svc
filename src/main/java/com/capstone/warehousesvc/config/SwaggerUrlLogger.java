package com.capstone.warehousesvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUrlLogger {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerUrlLogger.class);

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        String swaggerUrl = String.format("http://localhost:%s%s/swagger-ui/index.html",
                serverPort, contextPath);

        logger.info("========================================================");
        logger.info("Swagger UI is available at: {}", swaggerUrl);
        logger.info("========================================================");
    }
}