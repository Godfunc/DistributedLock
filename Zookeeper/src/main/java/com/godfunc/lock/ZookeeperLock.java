package com.godfunc.lock;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public interface ZookeeperLock {

    InterProcessMutex getLock(String name);
}
