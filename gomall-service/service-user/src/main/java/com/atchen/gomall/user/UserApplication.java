package com.atchen.gomall.user;

import com.atchen.gomall.common.annotation.EnableUserWebMvcConfiguration;
import kotlin.sequences.USequencesKt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.atchen.gomall"})
@EnableUserWebMvcConfiguration
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
