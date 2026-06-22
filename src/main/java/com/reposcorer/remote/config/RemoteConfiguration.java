package com.reposcorer.remote.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "github.base-api")
public class RemoteConfiguration {
    private Long timeout;
    private String url;
    private Integer size;
    private Map<String, String> params;
}
