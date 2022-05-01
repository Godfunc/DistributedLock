package com.godfunc.lock;

import org.redisson.api.RLock;

public interface RedLockClient {

    RLock getLock(String name);
}
