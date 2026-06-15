package com.ghost.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
public class GhostApplication {
    public static void main(String[] args) {
        SpringApplication.run(GhostApplication.class, args);
    }

    @Bean
public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    // SET TO 1GB (1024MB)
    factory.setMaxFileSize(DataSize.ofMegabytes(1024));
    factory.setMaxRequestSize(DataSize.ofMegabytes(1024));
    return factory.createMultipartConfig();
}
}