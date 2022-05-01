package com.godfunc.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RedissonRedLockClient implements RedLockClient {

    public RedissonRedLockClient(RedissonClient[] clients) {
        this.clients = clients;
    }

    private RedissonClient[] clients;

    @Override
    public RLock getLock(String name) {
        RLock[] locks = new RLock[clients.length];
        for (int i = 0; i < clients.length; i++) {
            locks[i] = clients[i].getLock(name);
        }
        return new org.redisson.RedissonRedLock(locks);
    }

}
