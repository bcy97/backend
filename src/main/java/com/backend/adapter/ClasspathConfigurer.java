package com.backend.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ClasspathConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootPath = System.getProperty("user.dir");
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + rootPath + "/static/");
        registry.addResourceHandler("/pictures/**").addResourceLocations("file:" + rootPath + "/pictures/");
        registry.addResourceHandler("/unitCfg/**").addResourceLocations("file:" + rootPath + "/unitCfg/");
        registry.addResourceHandler("/iconlibrary/public/**").addResourceLocations("file:" + rootPath + "/iconlibrary/public/");
        registry.addResourceHandler("/iconlibrary/user/**").addResourceLocations("file:" + rootPath + "/iconlibrary/user/");
    }
}
