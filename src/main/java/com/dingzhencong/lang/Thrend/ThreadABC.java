package com.dingzhencong.lang.Thrend;

public class ThreadABC implements Runnable{

    private String name;
    private String pre;
    private String cur;

    public ThreadABC(String name, String pre, String cur) {
        this.name = name;
        this.pre = pre;
        this.cur = cur;
    }

    @Override
    public void run() {
        int count = 10;
        while (count > 0) {
            synchronized (pre) {
                System.out.println(Thread.currentThread().getName() + ", get pre lock : " + pre);
                synchronized (cur) {
                    System.out.println(Thread.currentThread().getName() + ", get cur lock : " + cur);
                    System.out.println(name);
                    count--;
                    cur.notify();
                }
                System.out.println(Thread.currentThread().getName() + ", release cur lock : " + cur);
                try {
                    pre.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + ", release pre lock : " + pre);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String oA = "A";
        String oB = "B";
        String oC = "C";
        Thread tA = new Thread(new ThreadABC("A", oC, oA),"Thread ---- A ");
        Thread tB = new Thread(new ThreadABC("B", oA, oB),"Thread ---- B ");
        Thread tC = new Thread(new ThreadABC("C", oB, oC),"Thread ---- C ");
        tA.start();
        Thread.sleep(10);
        tB.start();
        Thread.sleep(10);
        tC.start();
    }
}
