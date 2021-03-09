package com.dingzhencong.lang.Thrend;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadAB {
    private volatile static int count = 10;

    //ABC CBA ABC
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicInteger s = new AtomicInteger(1);
        AtomicInteger t = new AtomicInteger(1);
        Thread t1 = new Thread(() ->{
            while (true) {
                synchronized (lock) {
                    if (s.get() == 1) {
                        System.out.println("A");
                        if (t.get() == 1) {
                            s.set(2);
                        }
                    }
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() ->{
            while (true) {
                synchronized (lock) {
                    if (s.get() == 2) {
                        System.out.println("B");
                        if (t.get() == 1) {
                            s.set(3);
                        } else {
                            s.set(1);
                        }
                    }
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t3 = new Thread(() ->{
            while (true) {
                synchronized (lock) {
                    if (s.get() == 3) {
                        System.out.println("C");
                        if (t.get() == 1) {
                            s.set(1);
                        }
                    }
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(1000);
    }
}
