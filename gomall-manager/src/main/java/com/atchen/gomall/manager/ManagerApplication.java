package com.atchen.gomall.manager;

import com.atchen.gomall.common.log.annotation.EnableLogAspect;
import com.atchen.gomall.manager.properties.MinioProperties;
import com.atchen.gomall.manager.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableLogAspect
@SpringBootApplication
@ComponentScan(basePackages = {"com.atchen.gomall"})
@EnableConfigurationProperties(value = {UserProperties.class, MinioProperties.class})
@EnableScheduling
public class ManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

}
