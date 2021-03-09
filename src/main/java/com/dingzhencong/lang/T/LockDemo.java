package com.dingzhencong.lang.T;

import com.dingzhencong.lang.Thrend.Lock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {
    public static class Consumer implements Runnable{
        private LockDemo lockDemo;

        public Consumer(LockDemo lockDemo) {
            this.lockDemo = lockDemo;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    lockDemo.consume();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Producer implements Runnable{
        private LockDemo lockDemo;

        public Producer(LockDemo lockDemo) {
            this.lockDemo = lockDemo;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    lockDemo.produce();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int max = 10;
    private LinkedList list = new LinkedList();

    private ReentrantLock lock = new ReentrantLock();
    private Condition full = lock.newCondition();
    private Condition empty = lock.newCondition();

    public void consume() throws InterruptedException {
        lock.lock();
        while (list.size() >= max) {
            System.out.println("【生产者" + Thread.currentThread().getName()
                    + "】仓库已满");
            full.await();
        }
        list.add(new Object());
        System.out.println("【生产者" + Thread.currentThread().getName()
                + "】生产一个产品，现库存" + list.size());
        empty.signalAll();
        full.await();
        lock.unlock();
    }

    public void produce() throws InterruptedException {
        lock.lock();
        while (list.size() == 0) {
            System.out.println("【消费者" + Thread.currentThread().getName()
                    + "】仓库为空");
            empty.await();
        }
        list.remove();
        System.out.println("【消费者" + Thread.currentThread().getName()
                + "】消费一个产品，现库存" + list.size());
        full.signalAll();
        empty.await();
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo lockDemo = new LockDemo();
        Thread c1 = new Thread(new Consumer(lockDemo));
        Thread c2 = new Thread(new Consumer(lockDemo));
        Thread c3 = new Thread(new Consumer(lockDemo));
        Thread p1 = new Thread(new Producer(lockDemo));
        Thread p2 = new Thread(new Producer(lockDemo));
        Thread p3 = new Thread(new Producer(lockDemo));

        p1.start();
        p2.start();
        p3.start();
        Thread.sleep(1000);

        c1.start();
        c2.start();
        c3.start();
    }
}
