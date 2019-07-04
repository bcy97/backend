package com.backend;

import com.backend.vo.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileProperties.class
})
public class BackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundApplication.class, args);
    }
}
