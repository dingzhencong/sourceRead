package com.dingzhencong.util.concurrent.locks.aqs;

import java.util.ArrayDeque;
import java.util.Stack;

public class Lock {
//    volatile static boolean type = true;
    public static void main(String[] args) {
        ArrayDeque<Integer> integers = new ArrayDeque<>();
        Stack stack = new Stack();
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (Lock.class) {
                    System.out.println("A");

                    System.out.println("t1 notifyAll pre");
                    Lock.class.notifyAll();
                    System.out.println("t1 notifyAll after");
                    try {
                        System.out.println("t1 wait pre");
                        Lock.class.wait();
                        System.out.println("t1 wait after");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (Lock.class) {
                    System.out.println("B");

                    System.out.println("t2 notifyAll pre");
                    Lock.class.notifyAll();
                    System.out.println("t2 notifyAll after");

                    try {
                        System.out.println("t2 wait pre");
                        Lock.class.wait();
                        System.out.println("t2 wait after");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                }
            }
        });
        t1.start();
        t2.start();
    }
}
