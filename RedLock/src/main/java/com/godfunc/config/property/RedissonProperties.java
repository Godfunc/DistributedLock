package com.godfunc.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    private List<String> nodes;

}
