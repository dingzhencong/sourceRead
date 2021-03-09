package com.dingzhencong.util.concurrent.locks.test;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    class Lock{
        int value = 1;
    }
    static class ThreadToGo {
        int value = 1;
    }

    final Lock lock = new Lock();
    private static Integer number = 1;
    private char character = 'A';

    int cur = 0;
    int max = 100;
    //12A34B56C78D910E1112F1314G1516H1718I1920J2122K2324L2526M2728N2930O3132
    // P3334Q3536R3738S3940T4142U4344V4546W4748X4950Y5152Z
    @Test
    public void test2() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition o = lock.newCondition();
        Condition j = lock.newCondition();

        new Thread(() ->{
            lock.lock();
            try {
                while (cur <= max) {
                    System.out.printf("线程[%s]打印数字:%d%n", Thread.currentThread().getName(), ++cur);
                    o.signalAll();
                    o.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        },"thread1").start();
        new Thread(() ->{
            lock.lock();
            try {
                while (cur <= max) {
                    System.out.printf("线程[%s]打印数字:%d%n", Thread.currentThread().getName(), ++cur);
                    o.signalAll();
                    o.await();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        },"thread2").start();
        Thread.sleep(1000);
    }
    @Test
    public void test() throws InterruptedException {
        ThreadTest.ThreadToGo threadToGo = new ThreadTest.ThreadToGo();
        new Thread(()-> {
            try {
                while (number < 53) {
                    synchronized (lock) {
                        while (lock.value == 2) {
                            lock.wait();
                        }
                        System.out.print((number++) + "" + (number++));
                        lock.value = 2;
                        lock.notify();
                    }
                }
            } catch (Exception e) {
                System.out.println("Oops...");
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(10);
        new Thread(() ->{
            try {
                while ( character <= 'Z') {
                    synchronized (lock) {
                        while (lock.value == 1) {
                            lock.wait();
                        }
                        System.out.print(character++);
                        lock.value = 1;
                        lock.notify();
                    }
                }
//                System.out.println("character ="+character);
            } catch (Exception e) {
                System.out.println("Oops...");
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
    }
    public static void main(String[] args) throws InterruptedException {
        ThreadTest.ThreadToGo threadToGo = new ThreadTest.ThreadToGo();
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();
        new Thread(() -> {
            try {
                for (int i = 0; i < aI.length; i++) {
                    synchronized(threadToGo){
                        System.out.println(aI[i]);
                        threadToGo.notify();
                        threadToGo.wait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                for (int i = 0; i < aC.length; i++) {
                    synchronized(threadToGo){
                        System.out.println(aC[i]);
                        threadToGo.notify();
                        threadToGo.wait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
