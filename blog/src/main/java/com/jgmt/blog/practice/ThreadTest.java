package com.jgmt.blog.practice;

public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        Thread.sleep(1000); // 暂停1毫秒
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
    }
}

class MyThread extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start();
        try {
            hello.join();
        } catch (InterruptedException e){
            System.out.println("Interrupted exception");
        }
        hello.interrupt();
    }
}

class HelloThread extends Thread {
    @Override
    public void run() {
        int n = 0;

        while (! isInterrupted()) {
            n ++;
            System.out.println(n + " HelloThread");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){
                System.out.println("HelloThread InterruptedException");
                break;
            }
        }

    }
}