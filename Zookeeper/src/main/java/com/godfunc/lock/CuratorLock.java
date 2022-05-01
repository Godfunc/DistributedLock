package com.godfunc.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class CuratorLock implements ZookeeperLock {

    private CuratorFramework curatorFramework;

    public CuratorLock(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @Override
    public InterProcessMutex getLock(String name) {
        return new InterProcessMutex(curatorFramework, name);
    }
}
