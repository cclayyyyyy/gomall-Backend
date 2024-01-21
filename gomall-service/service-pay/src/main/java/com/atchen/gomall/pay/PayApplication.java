package com.atchen.gomall.pay;

import com.atchen.gomall.common.annotation.EnableUserWebMvcConfiguration;
import com.atchen.gomall.pay.utils.AlipayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.atchen.gomall"})
@EnableConfigurationProperties(value = {AlipayProperties.class})
@EnableUserWebMvcConfiguration
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args) ;
    }

}
