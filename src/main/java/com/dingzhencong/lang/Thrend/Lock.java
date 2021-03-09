package com.dingzhencong.lang.Thrend;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {
    private int max = 10;
    private LinkedList linkedList = new LinkedList();

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition full = lock.newCondition();
    private final Condition empty = lock.newCondition();

    private void producer() throws InterruptedException {
        lock.lock();
        while (linkedList.size() > max) {
            System.out.println("【生产者" + Thread.currentThread().getName()
                    + "】仓库已满");
            full.await();
        }

        linkedList.add(new Object());
        System.out.println("【生产者" + Thread.currentThread().getName()
                + "】生产一个产品，现库存" + linkedList.size());
        empty.signalAll();
        full.await();
        lock.unlock();
    }

    private void consumer() throws InterruptedException {
        lock.lock();
        while (linkedList.size() == 0) {
            System.out.println("【消费者" + Thread.currentThread().getName()
                    + "】仓库为空");
            empty.await();
        }
        linkedList.remove();
        System.out.println("【消费者" + Thread.currentThread().getName()
                + "】消费一个产品，现库存" + linkedList.size());
        full.signalAll();
        empty.await();
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new Lock();
        Thread p1 = new Thread(new Product(lock));
        Thread p2 = new Thread(new Product(lock));
        Thread p3 = new Thread(new Product(lock));

        Thread c1 = new Thread(new consumer(lock));
        Thread c2 = new Thread(new consumer(lock));
        Thread c3 = new Thread(new consumer(lock));

        p1.start();
        p2.start();
        p3.start();
        Thread.sleep(1000);

        c1.start();
        c2.start();
        c3.start();
    }

    public static class Product implements Runnable{
        private Lock lock;
        public Product(Lock lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    lock.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class consumer implements Runnable {
        private Lock lock;

        public consumer(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    lock.consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
