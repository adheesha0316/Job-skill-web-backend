package com.adheesha.jobskill.jobskill_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // cv files
        registry.addResourceHandler("/cvs/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/cvs/");

        // logo files
        registry.addResourceHandler("/logos/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/logos/");

        // profile Image files
        registry.addResourceHandler("/profiles/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/profiles/");
    }


    // CORS Configuration
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // All paths
                .allowedOrigins("http://localhost:5173") // React frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
