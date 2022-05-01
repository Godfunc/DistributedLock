package com.godfunc.config;

import com.godfunc.config.property.RedissonProperties;
import com.godfunc.lock.RedLockClient;
import com.godfunc.lock.RedissonRedLockClient;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfig {

    @Bean
    public RedLockClient createRedLockClient(RedissonProperties redissonProperties) {
        List<String> nodes = redissonProperties.getNodes();
        if (nodes == null || nodes.size() < 3) {
            throw new RuntimeException("redlock 节点数量不能小于3个");
        }
        RedissonClient[] redissonClients = new RedissonClient[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            Config config = new Config();
            config.useSingleServer().setAddress(nodes.get(i));
            redissonClients[i] = Redisson.create(config);
        }

        return new RedissonRedLockClient(redissonClients);
    }
}
