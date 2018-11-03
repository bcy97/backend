package com.backend.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ClasspathConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String rootPath = System.getProperty("user.dir");
        registry.addResourceHandler("/baseConfigs/**").addResourceLocations("file:" + rootPath + "/baseConfigs/");
        registry.addResourceHandler("/pictures/**").addResourceLocations("file:" + rootPath + "/pictures/");
        registry.addResourceHandler("/unitConfigs/**").addResourceLocations("file:" + rootPath + "/unitConfigs/");
        registry.addResourceHandler("/iconlibrary/public/**").addResourceLocations("file:" + rootPath + "/iconlibrary/public/");
        registry.addResourceHandler("/iconlibrary/user/**").addResourceLocations("file:" + rootPath + "/iconlibrary/user/");
    }
}
