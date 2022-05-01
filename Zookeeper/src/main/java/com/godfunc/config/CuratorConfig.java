package com.godfunc.config;

import com.godfunc.config.property.CuratorProperties;
import com.godfunc.lock.CuratorLock;
import com.godfunc.lock.ZookeeperLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = true)
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorConfig {

    @Bean
    public CuratorFramework curatorFramework(CuratorProperties properties) {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(properties.getNodes());
        if (properties.getConnectionTimeoutMs() != null && properties.getConnectionTimeoutMs() > 0) {
            builder.connectionTimeoutMs(properties.getConnectionTimeoutMs());
        }
        if (properties.getSessionTimeoutMs() != null && properties.getSessionTimeoutMs() > 0) {
            builder.sessionTimeoutMs(properties.getSessionTimeoutMs());
        }
        if (properties.getRetryPolicy() != null
                && properties.getRetryPolicy().getMaxRetries() != null && properties.getRetryPolicy().getBaseSleepTimeMs() != null
                && properties.getRetryPolicy().getMaxRetries() > 0 && properties.getRetryPolicy().getBaseSleepTimeMs() > 0) {
            builder.retryPolicy(new ExponentialBackoffRetry(properties.getRetryPolicy().getBaseSleepTimeMs(), properties.getRetryPolicy().getMaxRetries()));
        }
        CuratorFramework curatorFramework = builder.build();
        curatorFramework.start();
        return curatorFramework;
    }

    @Bean
    public ZookeeperLock zookeeperLock(CuratorFramework curatorFramework) {
        return new CuratorLock(curatorFramework);
    }
}
