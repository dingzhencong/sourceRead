package com.dingzhencong.lang.Thrend;


import java.util.LinkedList;

public class Store {
    private LinkedList linkedList = new LinkedList();
    private final int max = 10;

    private void producer() {
        synchronized (Store.class) {
            while (linkedList.size() > max) {
                try {
                    System.out.println("【生产者" + Thread.currentThread().getName()
                            + "】仓库已满");
                    Store.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.add(new Object());
            System.out.println("【生产者" + Thread.currentThread().getName()
                    + "】生产一个产品，现库存" + linkedList.size());
            Store.class.notifyAll();
            try {
                Store.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consumer() {
        synchronized (Store.class) {
            while (linkedList.size() == 0) {
                try {
                    System.out.println("【消费者" + Thread.currentThread().getName()
                            + "】仓库为空");
                    Store.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.remove();
            System.out.println("【消费者" + Thread.currentThread().getName()
                    + "】消费一个产品，现库存" + linkedList.size());
            Store.class.notifyAll();
            try {
                Store.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class Producer implements Runnable {
        private Store store;
        public Producer(Store store) {
            this.store = store;
        }
        @Override
        public void run() {
            while (true) {
                store.producer();
            }
        }
    }
    public static class Consumer implements Runnable {
        private Store store;
        public Consumer(Store store) {
            this.store = store;
        }
        @Override
        public void run() {
            while (true) {
                store.consumer();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Store storage = new Store();
        Thread p1 = new Thread(new Producer(storage));
        Thread p2 = new Thread(new Producer(storage));
        Thread p3 = new Thread(new Producer(storage));

        Thread c1 = new Thread(new Consumer(storage));
        Thread c2 = new Thread(new Consumer(storage));
        Thread c3 = new Thread(new Consumer(storage));

        p1.start();
        p2.start();
        p3.start();
        Thread.sleep(1000);

        c1.start();
        c2.start();
        c3.start();
    }
}
