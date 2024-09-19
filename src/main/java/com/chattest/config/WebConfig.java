package com.chattest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);

    }

    private String connectPath = "/uploadPath/**";

    final Path FILE_ROOT = Paths.get("./").toAbsolutePath().normalize();
    // resources 폴더가 아닌 위치일 때, 현재 위치를 지정함
    private String uploadPath = FILE_ROOT.toString() + "/upload/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(connectPath)
                .addResourceLocations("file:///"+uploadPath);
    }

}
