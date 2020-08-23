package com.dingzhencong.util.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockThread implements Runnable{

    // 创建一个ReentrantLock对象
    ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        try{
            // 使用lock()方法加锁
            lock.lock();
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + "输出了：  " + i);
            }
        }finally{
            // 别忘了执行unlock()方法释放锁
            lock.unlock();
        }
    }
}
