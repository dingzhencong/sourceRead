package com.dingzhencong.lang.T;

import java.util.concurrent.CopyOnWriteArrayList;

public class DeadLock {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (DeadLock.class) {
                System.out.println("t1 has deadlock");
                synchronized (Object.class) {
                    System.out.println("t1 has object lock");
                }
            }
        });
        Thread t2 = new Thread(() ->{
            synchronized (Object.class) {
                System.out.println("t2 has object lock");
                synchronized (DeadLock.class) {
                    System.out.println("t2 has DeadLock lock");
                }
            }
        });

//        t1.start();
//        t2.start();
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        copyOnWriteArrayList.add(null);

        System.out.println(copyOnWriteArrayList.get(0));
    }
}
