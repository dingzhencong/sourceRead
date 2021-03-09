package com.dingzhencong.lang.T;

public class Demo {
    Boolean s = true;
    static int val = 1;
    static int count = 0;
    public static void main(String[] args) {
        Thread t1 = new Thread(() ->{
            while (true) {
                synchronized (Demo.class) {
                    while (val != 1) {
                        try {
                            Demo.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("A");
                    val = 2;
                    Demo.class.notifyAll();
                    try {
                        Demo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() ->{
            while (true) {
                synchronized (Demo.class) {
                    while (val != 2) {
                        try {
                            Demo.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("B");
                    val = 3;
                    Demo.class.notifyAll();
                    try {
                        Demo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t3 = new Thread(() ->{
            while (true) {
                synchronized (Demo.class) {
                    while (val != 3) {
                        try {
                            Demo.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("C");
                    val = 1;
                    Demo.class.notifyAll();
                    try {
                        Demo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
