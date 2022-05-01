package com.godfunc.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "curator")
public class CuratorProperties {

    private String nodes;
    private Integer connectionTimeoutMs;
    private Integer sessionTimeoutMs;
    private RetryPolicy retryPolicy;

    @Data
    public static class RetryPolicy {
        private Integer baseSleepTimeMs;
        private Integer maxRetries;
    }

}
