package com.godfunc;

import com.godfunc.lock.ZookeeperLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ZookeeperApplicationTest {

    @Autowired
    private ZookeeperLock zookeeperLock;

    @Test
    public void test1() throws InterruptedException {
        InterProcessMutex lock = zookeeperLock.getLock("/lock");
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        // 获取锁，会去遍历三个RLock实例，分别执行内置的lua脚本
                        // 参数：
                        //    等待时间：获取这个锁，最长等待多久，超过这个时间就直接返回获取锁失败
                        //    持有锁的时间：持有这个锁多久
                        //    前面两个参数的单位
                        lock.acquire();
                        System.out.println("线程" + Thread.currentThread().getName() + " 获取锁");
                        TimeUnit.SECONDS.sleep(2);
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程" + Thread.currentThread().getName() + " 释放锁");
                }
            }, "t" + i).start();
        }

        TimeUnit.MINUTES.sleep(1);
    }
}
