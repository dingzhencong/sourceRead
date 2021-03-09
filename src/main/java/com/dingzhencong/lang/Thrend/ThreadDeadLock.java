package com.dingzhencong.lang.Thrend;

public class ThreadDeadLock{

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread----1,  lock1 wait lock2");
                synchronized (lock2) {
                    System.out.println("Thread----1,  lock1 and lock2");
                }
            }
        },"Thread----1");

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread----2,  lock2 wait lock1");
                synchronized (lock1) {
                    System.out.println("Thread----2,  lock1 and lock2");
                }
            }
        },"Thread----2");

        t1.start();
        t2.start();
    }

}
