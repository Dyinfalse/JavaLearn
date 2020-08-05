package com.jgmt.blog.practice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskQueue q = new TaskQueue();
        List<Thread> ts = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++){
            Thread t = new Thread(){
                public void run () {
                    while (true) {
                        try {
                            String s = q.getTask();
                            System.out.println("execute task: " + s);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            };
            t.start();
            ts.add(t);
        }
        Thread add = new Thread(() -> {
            for(int i = 0; i < 10; i++){
                // 放入task
                String s = "t-" + Math.random();
                System.out.println("add task " + s);
                q.addTask(s);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        });
        add.start();
        add.join();
        Thread.sleep(100);
        for (Thread t : ts) {
            t.interrupt();
        }
    }
}

class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask (String s) {
        this.queue.add(s);
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty()){
            this.wait();
        }
        return queue.remove();
    }

}