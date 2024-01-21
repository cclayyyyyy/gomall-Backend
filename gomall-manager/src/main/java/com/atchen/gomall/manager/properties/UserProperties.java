package com.atchen.gomall.manager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "gomall.auth") // application-dev.yml
public class UserProperties {

    private List<String> noAuthUrls;
}
