package com.godfunc;

import com.godfunc.lock.RedLockClient;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedLockApplicationTest {

    @Autowired
    private RedLockClient redLockClient;

    @Test
    public void test1() throws InterruptedException {
        RLock lock = redLockClient.getLock("lock");
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        // 获取锁，会去遍历三个RLock实例，分别执行内置的lua脚本
                        // 参数：
                        //    等待时间：获取这个锁，最长等待多久，超过这个时间就直接返回获取锁失败
                        //    持有锁的时间：持有这个锁多久
                        //    前面两个参数的单位
                        if (lock.tryLock(30, 20, TimeUnit.SECONDS)) {
                            System.out.println("线程" + Thread.currentThread().getName() + " 获取锁");
                            TimeUnit.SECONDS.sleep(2);
                            return;
                        } else {
                            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println("线程" + Thread.currentThread().getName() + " 释放锁");
                }
            }, "t" + i).start();
        }

        TimeUnit.MINUTES.sleep(1);
    }
}
