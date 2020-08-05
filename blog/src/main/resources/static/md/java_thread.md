## java Thread 知识点

*2020-07-31 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744/1304521607217185)的总结

#### **多线程基础**

现代的操作系统（Windows，macOS，Linux）都可以执行多任务。多任务就是同时运行多个任务。

CPU执行代码都是一条一条顺序执行的，但是，即使是单核cpu也可以同时运行多个任务。因为操作系统执行多任务实际上就是让CPU对多个任务轮流交替执行。

交替执行的切换速度非常快，在一些人眼里就像同时执行了多个任务一样，例如：让浏览器执行0.001秒，让QQ执行0.001秒，在让音乐播放器执行0.001秒，在人看来，CPU就是在同时执行这几个任务。

即使是多核CPU，因为通常执行的任务数量远远多余CPU的核数，所以任务也是交替执行的。

#### **进程**

在计算机中，我们把一个任务称为一个进程，浏览器就是一个进程，视频播放器是另一个进程，类似的，音乐播放器和Word都是进程。

某些进程内部还需要同时执行多个子任务。例如，我们在使用Word的时候，Word可以让我们一边打字，一边检查拼写，同时还可以在后台打印，我们把子任务称为线程。

```
                            ┌──────────┐
                            │Process   │
                            │┌────────┐│
                ┌──────────┐││ Thread ││┌──────────┐
                │Process   ││└────────┘││Process   │
                │┌────────┐││┌────────┐││┌────────┐│
    ┌──────────┐││ Thread ││││ Thread ││││ Thread ││
    │Process   ││└────────┘││└────────┘││└────────┘│
    │┌────────┐││┌────────┐││┌────────┐││┌────────┐│
    ││ Thread ││││ Thread ││││ Thread ││││ Thread ││
    │└────────┘││└────────┘││└────────┘││└────────┘│
    └──────────┘└──────────┘└──────────┘└──────────┘
    ┌──────────────────────────────────────────────┐
    │               Operating System               │
    └──────────────────────────────────────────────┘
```

操作系统调度的最小任务单元就是线程，常用的Windows，Linux都采用抢占式调度的模式，调度线程完全由操作系统决定，程序自己不能决定什么时候执行，以及执行多长时间。

因为同一个应用程序，既可以有多个进程，也可以有多个线程，实现多任务的方法，有以下几种

多进程模式（每个进程只有一个线程）：

```
    ┌──────────┐ ┌──────────┐ ┌──────────┐
    │Process   │ │Process   │ │Process   │
    │┌────────┐│ │┌────────┐│ │┌────────┐│
    ││ Thread ││ ││ Thread ││ ││ Thread ││
    │└────────┘│ │└────────┘│ │└────────┘│
    └──────────┘ └──────────┘ └──────────┘
```

多线程模式（一个进程有多个线程）

```
    ┌────────────────────┐
    │Process             │
    │┌────────┐┌────────┐│
    ││ Thread ││ Thread ││
    │└────────┘└────────┘│
    │┌────────┐┌────────┐│
    ││ Thread ││ Thread ││
    │└────────┘└────────┘│
    └────────────────────┘
```

多进程+多线程（普遍，难度最高）

```
    ┌──────────┐┌──────────┐┌──────────┐
    │Process   ││Process   ││Process   │
    │┌────────┐││┌────────┐││┌────────┐│
    ││ Thread ││││ Thread ││││ Thread ││
    │└────────┘││└────────┘││└────────┘│
    │┌────────┐││┌────────┐││┌────────┐│
    ││ Thread ││││ Thread ││││ Thread ││
    │└────────┘││└────────┘││└────────┘│
    └──────────┘└──────────┘└──────────┘
```

#### 进程 vs 线程

进程和线程是包含关系，但是多任务既可以由多进程实现，也可以由单进程内的多线程实现，还可以混合多进程+多线程实现。

具体采用的时候，要考虑到进程和线程的特点。

和多线程相比，多进程的缺点在于：

- 创建进程比创建线程的开销要大，尤其是在Windows平台上；
- 进程间通信比线程要慢，因为线程间通信就是读写同一个变量，速度非常快。

而多进程的优点在于：

多进程稳定性更高，因为在多进程的情况下，一个进程崩溃不回影响其他的进程，而在多线程的情况下，一个线程的崩溃会直接导致进程崩溃。

#### **多线程**

Java语言内置了多线程支持：一个Java程序实际上是一个JVM进程，JVM进程用一个主线程来执行`main()`方法，在`main()`方法内部，我们又可以启动多个线程。此外，JVM还有负责垃圾回收的其他工作线程等。

因此对于大多数Java程序员来说，我们说多任务，实际上是说如何使用多线程实现多任务。

和单线程相比，多线程编程的特点在于：多线程经常需要读写共享数据，并且需要同步。例如播放电影时，就必须由一个线程播放视频，另一个线程播放音频，两个线程需要协调运行，否则画面和声音就不同步。因此，多线程的复杂度高，调试难度更困难。

Java多线程编程的特点在于：

- 多线程模型是Java程序最基本的并发模型；
- 后续读写网络，数据库，Web开发等都依赖Java多线程模型。

#### **创建新线程**

Java语言内置了多线程的支持，当Java程序启动的时候，实际上启动了一个JVM进程，然后JVM主线程来执行`main()`方法，在`main()`方法中，我们又可以启动其他线程。

要创建一个新线程非常简单，我们需要实例化一个`Thread`实例，然后调用它的`start()`方法：

```java
public class ThreadClass {
    public static void main(String[] args){
        Thread t = new Thread();
        t.start();
    }
}
```

但是这个线程启动之后什么也不会做就立即结束了，我们希望新线程能执行指定的代码，有以下几种方法：

方法一：从Thread()派生一个自定义类，然后覆写run()方法：

```java
public class ThreadRunClass {
    public static void main(String[] args){
        Thread t = new MyThread();
        t.start();
    }
}

public class MyThread extends Thread {
    @Override
    public void run (){
        System.out.println("start new thread in MyThread class");
    }
}
```

执行上述代码，注意到start()方法会在内部调用实例的run()方法。

方法二：创建Thread实例，传入一个Runnable

```java
public class ThreadRunnableClass {
    public static void main(String[] args){
        Thread t = new Thread(new MyRunnable());
        t.start();
    }
}

public class MyRunnable implements Runnable {
    @Override
    public void run () {
        System.out.println("start new Thread in Runnabel");
    }
} 
```

或者使用Java8引用的lambda语法进一步简写：

```java
public class ThreadLambda {
    public static void main(String[] args){
        Thread t = new Thread(() -> System.out.println("start new Thread in Lambda"));
        t.start();
    }
}
```

下面查看一个多线程实例，为了提现多线程的特点，我们加入Thread.sleep(int)方法

```java
public class Main {
    public static void main(String[] args){
        System.out.println("main run");
        Thread t = new Thread(()->{
            System.out.println("thread run");
            try {
              Thread.sleep();
            } catch (InterruptedException ignored){}
            System.out.println("thread end");
        });
        t.start();
        System.out.println("main end");
    }
}
```

上例代码的打印结果如下

```
>> main run
>> main end
>> thread run
>> thread end
```

sleep传入的参数是毫秒数，调整时间的大小，我们可以看到main线程和t线程的执行顺序。事实上，我们只能肯定main run会最先打印，但是main end在thread run之前还是在thread end之后打印，我们都无法确定，因为t线程开始运行之后，两个线程就开始同时运行了，并且由操作系统调度，程序本身无法确定线程的调度顺序。

需要注意的一点：直接调用Thread实例的run()方法，是无效的：

``` java
Thread t = new Thread();
t.run()
```

直接调用run()方法，相当与调用了一个普通Java方法，当前线程不会由任何变化，也不会启动新线程，必须使用Thread实例的start方法才能启动新的线程，如果我们查看Thread类的源码，会看到start()方法内部调用了一个private native void start0()方法，native修饰符表示这是一个JVM虚拟机内部C代码实现的，不是由Java代码实现的。

线程调度的优先级

可以对线程设定优先级，设定优先级的方法是：
``` java
Thread.setPriority(int n);
```

优先级高的线程被操作系统调度的优先级较高，操作系统对更高优先级的线程可能调度更频繁，但我们绝不能通过设置优先级来确保高优先级的线程一定会先执行。

#### **线程的状态**

在Java程序中，一个线程对象只能调用一次start()方法启动新线程，并在新线程中执行run()方法。一旦run()方法执行完毕，线程就结束了，因此，Java线程状态有以下几种：

- New：新创建的线程，尚未执行。
- Runnable：运行中的线程，正在执行run()方法的Java代码。
- Blocked：运行中的线程，因为某些操作被阻塞而挂起。
- Waiting：运行中的线程，因为某些操作在等待中。
- Timed Waiting：运行中的线程，因为执行sleep()方法正在计时等待。
- Terminated：线程已终止，因为run()方法执行完毕。

用一个状态转移图表示如下：

```
             ┌─────────────┐
             │     New     │
             └─────────────┘
                    │
                    ▼
    ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
     ┌─────────────┐ ┌─────────────┐
    ││  Runnable   │ │   Blocked   ││
     └─────────────┘ └─────────────┘
    │┌─────────────┐ ┌─────────────┐│
     │   Waiting   │ │Timed Waiting│
    │└─────────────┘ └─────────────┘│
     ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
                    │
                    ▼
             ┌─────────────┐
             │ Terminated  │
             └─────────────┘
```

当线程启动之后，它可以在Runnable，Blocked，Waiting和Timeed Waiting这几个状态之间切换，直到最后编程Terminated状态，线程终结

线程终止的原因有

- 线程正常终止：run()方法执行到return语句返回；
- 线程意外终止：run()方法因为未捕获的异常导致线程终止；
- 对某个线程的Thread实例调用stop()方法强制终止（非常不建议）

一个线程还可以等待另一个线程直到其运行结束。例如，main线程在启动t线程后，可以通过t.join()方法等待t线程结束后再继续运行：

```java
public class Main {
    public static void main(String[] args){
        Thread t = new Thread(()-> System.out.println("hello"));
        System.out.println("start");
        t.start();
        t.join();
        System.out.println("end");
    }
}
```

当main线程对线程对象t调用join()方法时，主线程将等待变量t表示的线程运行结束，即join()就是指等待线程结束，然后才继续往下执行自身线程，所以，上述代码打印顺序肯定是main线程先打印start，t线程再打印hello，main线程最后再打印end。

如果t线程已经结束，对实例t调用join()会立即返回，此外，join(long)的重载方法也可以指定一个等待时间，超过等待时间就不再继续等待。

#### **中断线程**

如果线程需要执行一个长时间的任务，就可能需要使用中断线程。中断线程就是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。

我们举个例子，假设从网络下载一个100M的文件，如果网速很慢，用户等的不耐烦了，就可能在下载过程中点击"取消"，这时，程序就需要中断下载线程。

中断线程非常简单，只需要在其他线程中对目标线程调用interrupt()方法，目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。

我们还是看实例代码：

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        Thread.sleep(1);
        t.interrupt();
        t.join();
        System.out.println("end");
    }
}

class MyThread extends Thread {
    public void run (){
        int n = 0;
        while(! isInterrupted()) {
            n++;
            System.out.println(n + " hello !");
        }
    }
}
```

仔细看上述代码，main线程通过调用t.interrupt()方法中断t线程，但是要注意，interrupt()方法仅仅向t线程发送"中断请求"，至于t线程是否能立刻相应，要看具体代码的实现，而t线程的while循环会检测isInterrupt()，所以上述代码能够正确响应interrupt()的请求，使得自身立刻结束运行run()方法。

如果线程处于等待状态，例如，t.join()会让main线程进入等待状态，此时，如果对main线程调用interrupt()，join()方法会立刻抛出InterruptedException，就说明有其他线程对其调用了interrupt()方法，通常情况下该线程应该立刻结束运行。

我们来看下示例代码

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThreadTest();
        t.start();
        Thread.sleep(1000);
        t.interrupt();// 中断线程
        t.join(); // 等待线程结束
        System.out.println("end");
    }
}

public class MyThreadTest extends Thread {
    public void run () {
        Thread hello = new HelloThread();
        hello.start();
        try {
            hello.join();
        } catch (InterruptedException e) {
            System.out.println("interrupted exception");
        }
        
        hello.interrupt();
    }
}

public class HelloThread extends Thread {
    public void run () {
        int n = 0;
        while(! isInterrupted()) {
            n++;
            System.out.println(n + " in class HelloThread");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
```

main线程通过调用t.interrupt()从而请求t线程中断，而此时t线程正位于hello.join()的等待中，此方法会立刻结束并抛出InterruptedException。由于我们在t线程中捕获了InterruptedException，因此，就可以准备结束该线程，在t结束之前，对hello线程也进行了interrupt()调用请求中断，如果去掉这一行代码，可以发现hello仍然会继续运行，切JVM不会推出。

另一个常用的中断线程的方法是设置标志位。我们通常会用一个running标志来表示线程是否正在运行，在外部线程中，通过HelloThread.running设置为false，就可以让线程结束：

```java
public class Main {
    public static void main(String[] args) throws InterruptedException{
        Thread t = new HelloThread();
        t.start();
        Thread.sleep(1);
        t.running = false;
    }
}

class HelloThread extends Thread {
    
    public volatile boolean running = true;

    public void run () {
        int n = 0;
        while (running) {
            n ++;
            System.out.println(n + " hello !");
        }
        System.out.println("Hello end");
    }
}
```

注意到HelloThread的标志位boolean running是一个线程间共享变量。线程间共享变量需要使用volatile关键字标记，确保每个线程都能读取到更新后的变量。

为什么要对线程间共享变量用关键字volatile声明呢？这涉及到Java的内存模型，在Java虚拟机中，变量的值保存在内存中，但是当线程访问变量的时候，它会先取一个副本，并保存在自己的工作内存中，如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值写回到主内存，但是，这个时间是不确定的！

``` java
┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
           Main Memory
│                               │
   ┌───────┐┌───────┐┌───────┐
│  │ var A ││ var B ││ var C │  │
   └───────┘└───────┘└───────┘
│     │ ▲               │ ▲     │
 ─ ─ ─│─│─ ─ ─ ─ ─ ─ ─ ─│─│─ ─ ─
      │ │               │ │
┌ ─ ─ ┼ ┼ ─ ─ ┐   ┌ ─ ─ ┼ ┼ ─ ─ ┐
      ▼ │               ▼ │
│  ┌───────┐  │   │  ┌───────┐  │
   │ var A │         │ var C │
│  └───────┘  │   │  └───────┘  │
   Thread 1          Thread 2
└ ─ ─ ─ ─ ─ ─ ┘   └ ─ ─ ─ ─ ─ ─ ┘
```

这就会导致如果一个线程更新了某一个变量，另一个线程读取的值可能还是更新前的。例如，主内存的变量a = true，线程1执行 a = false时，它在此时此刻仅仅把变量a的副本变成了false，主内存的变量a还是true，在JVM把修改后的a写回到主内存之前，其他线程读取的a的值仍然是true，这就造成多线程之间共享变量不一致。

因此，volatile关键字就是告诉虚拟机：

- 每次访问变量时，总是获取内存的最新值；
- 每次修改变量后，立刻写回到主内存；

volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。

如果我们去掉volatile关键字，运行上述程序，发现效果和带volatile差不多，这是因为在x86架构下，JVM回写主内存的速度非常快，但是换成ARM架构，就会有显著的延迟。

#### **守护线程**

Java程序入口就是由JVM启动main线程，main线程又可以启动其他线程，当所有线程都结束时，JVM退出，进程结束。

如果有一个线程没有退出，JVM进程就不会退出，所以必须保证所有线程都能及时结束

但是有一种线程的目的就是无限循环，例如一个触发定时任务的线程：

```java
public class TimerThread extends Thread {
        @Override
        public void run () {
            while (true) {
                System.out.println(LocalTime.now());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
}
```

如果这个线程不结束，JVM就无法结束，问题是，谁负责结束这个线程？

然而这类线程经常没有负责人负责结束它们，但是当其他线程结束时，JVM进程又必须要结束，怎么办？

答案就是使用守护线程（Daemon Thread）

守护线程是指为其他线程服务的线程，在JVM中，所有的非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。

因此，JVM退出时，不必关心守护线程是否结束。

如何创建守护线程呢？方法和普通线程一样，只是在调用start()方法之前，调用setDeamon(true)，把该线程标记为守护线程：

``` java
Thread t = new Thread();
t.setDeamon(true);
t.start()
```

在守护线程中，编写代码要注意，守护线程不能持有任何需要关闭的资源，例如打开文件，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。

#### **线程同步**

当多个线程同时运行时，线程的调度由操作系统决定，程序本身无法决定。因此，任何一个线程都有可能在很和指令处被操作系统暂停，然后在某个时间段之后继续执行。

这个时候有个单线程模型下不存在的问题就来了：如果多个线程同时读写变量，会出现数据不一致的问题。

我们来看一个例子：

```java
public class Main {
    public static void main(String[] args){
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.cont);
    }
}

class Counter {
    public static int count = 0;
}

class AddThread extends Thread {
    public void run () {
        for (int i = 0; i < 10000; i++){
            Counter.count += 1;
        }
    }
}

class DecThread extends Thread {
    public void run () {
        for (int i = 0; i < 10000; i++) {
            Counter.count -= 1;
        }
    }
}
```

上面代码很简单，两个线程同时对一个int变量进行操作，一个加10000次，一个减10000次，最后结果应该是0，但是每次运行，结果实际上都不一样。

这是因为对变量进行读取和写入的时候，结果要正确，必须保证是原子操作，原子操作是指不能被中断的一个或一系列操作。

例如对于语句：

``` java
n = n + 1;
```

看上去一行语句，实际上对应了三条指令：

``` java
ILOAD
IADD
ISTORE
```

我们假设n的值是100，如果两个线程同时执行n = n + 1，得到的结果很可能不是102，而是101，原因在于：

``` java
    ┌───────┐    ┌───────┐
    │Thread1│    │Thread2│
    └───┬───┘    └───┬───┘
        │            │
        │ILOAD (100) │
        │            │ILOAD (100)
        │            │IADD
        │            │ISTORE (101)
        │IADD        │
        │ISTORE (101)│
        ▼            ▼
```

如果线程1在执行ILOAD后被操作系统中断，此时如果线程2被调度执行，它执行ILOAD后获取的值仍然是100，最终结果被两个线程ISTORE写入后变成101，而不是预期的102。

这说明多线程模型下，要保证逻辑正确，对共享变量进行读写时，必须保证一组指令以原子方式执行：即某一个线程执行时么，其他线程必须等待：

``` java
    ┌───────┐     ┌───────┐
    │Thread1│     │Thread2│
    └───┬───┘     └───┬───┘
        │             │
        │-- lock --   │
        │ILOAD (100)  │
        │IADD         │
        │ISTORE (101) │
        │-- unlock -- │
        │             │-- lock --
        │             │ILOAD (101)
        │             │IADD
        │             │ISTORE (102)
        │             │-- unlock --
        ▼             ▼
```

通过加锁和解锁的操作，就能保证三条指令总是在一个线程执行期间，不会有其他线程会进入此指令区间，即使在执行期间线程被操作系统中断，其他线程也会因为无法获得锁导致无法进入此指令区间。只有执行线程将锁释放后，其他线程才能有机会获得锁并执行。这种加锁和解锁之间的代码块，我们称之为临界区（Critical Section），任何时候玲姐去最多只有一个线程能执行。

可见，保证一段代码的原子性，就是通过加锁和解锁实现的，Java程序使用synchronized，关键字对一个对象进行加锁

``` java
sychronized(lock){
    n = n + 1;
}
```

synchronized保证了代码块在任意时刻最多只有一个线程能执行，我们把上面代码用synchronized改写：

```java
public class Main {
    public static void main(String[] args) throws Exception {
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.count);
    }
}

class Counter {
    public static final Object lock = new Object();
    public static int count = 0;
}

class AddThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(Counter.lock) {
                Counter.count += 1;
            }
        }
    }
}

class DecThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(Counter.lock) {
                Counter.count -= 1;
            }
        }
    }
}
```

注意到代码

``` java
synchronized(Counter.lock){
    ...
}
```

它表示Counter.lock实例作为锁，两个线程在各自的synchronized(Counter.lock){...}代码块时，必须先获得锁，才能进入代码块进行。执行结束后，在synchronized语句结束后会自动释放锁，这样一来，对Counter.count变量进行读写就不可能同时进行，上述代码无论运行多少次，最终结果都是0。

使用synchronized解决了多线程同步访问共享变量的正确性问题，但是，它的缺点是带来了性能下降，因此，synchronized代码块无法并发执行，加锁和解锁也有一定的耗时，所以synchronized会降低程序的执行效率。

我们来概括一下如何使用synchronized：

- 找出修改共享变量的线程代码块；
- 选择一个共享实例作为锁；
- 使用synchronized(lockObejct){...}

synchronized除了加锁的功能，还具备内存屏蔽功能，并且强制读取所有共享变量的主内存最新值，退出synchronized的时候，再强制回写到祝内存（如果有修改）

在使用synchronized的时候，不必担心抛出异常，因为无论是否有异常，都会在synchronized结束处正确释放锁：

``` java
public void add (){
    synchronized(obj){
        if(m < 0){
            throws new RuntimeException();
        }
        this.value += m;
    }
}
```

我们再来看一个错误使用synchronized的例子：

```java
public class Main {
    public static void main(String[] args) throws Exception {
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.count);
    }
}

class Counter {
    public static final Object lock1 = new Object();
    public static final Object lock2 = new Object();
    public static int count = 0;
}

class AddThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(Counter.lock1) {
                Counter.count += 1;
            }
        }
    }
}

class DecThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(Counter.lock2) {
                Counter.count -= 1;
            }
        }
    }
}
```

其结果并不是0，这是因为，两个线程各自的synchronized锁住的不是同一个对象，这使得两个线程各自都可以同时获得锁：因为JVM只保证同一个锁在任意时刻只能被一个线程获取，但是两个不同的锁，在同一时刻是可以被两个不同的线程分别获取的。

因此，使用synchronized的时候，获取到那个锁非常重要。锁的对象如果不对，代码逻辑就不对。

JVM规范定义了几种原子操作：

- 基本类型(long和double除外)赋值，例如：int n = m；
- 引用类型赋值，例如: List<String> list = testList;

long和double是64为数据，JVM没有明确规定64位赋值操作是不是一个原子操作，不过在x64平台的JVM是把long和double的赋值作为原子操作实现的。

单条原子操作的语句不需要同步。例如：

``` java
public void set(int m) {
    synchronized(lock){
        this.value = m;
    }
}
```

就不需要同步。

对引用也是类似，例如：

``` java
public void set(String s){
    this.value = s;
}
```

上述语句不需要同步。

但是，如果是多行赋值语句，就必须保证是同步操作，例如：

```java
class Pair {
    int first;
    int last;
    public void set (int first, int last) {
        synchronized (this) {
            this.first = first;
            this.last = last;
        }
    }
}
```

有的时候，通过一些巧妙的转换，可以把非原子操作变为原子操作，例如上面的代码如果改写成：

```java
class Pair {
    int[] pair;
    public void set(int first, int last) {
        int ps = new int[] {first, last};
        this.pair = ps;
    }
}
```

这样就不需要同步，因为this.pair = ps是引用赋值的原子操作。而语句：

``` java
int[] ps = new int[] { first, last };
```

这里的ps是方法内部定义的局部变量，每个线程都会有各自的局部变量，互不影响，并且互不可见，并不需要同步。

#### **同步方法**

我们知道Java程序依靠synchronized对线程进行同步，使用synchronized的时候，锁住的是哪个对象非常重要。

让线程自己选择锁住的对象，往往会使得代码逻辑混乱，也不利于封装。更好的方法是把synchronized逻辑封装起来，例如，我们编写一个计数器

```java
public class Counter {
    private int count = 0;
    
    public void add (int n) {
        synchronized (this) {
            count += n;
        }
    }
    
    public void dec (int n){
        synchronized (this){
            count -= n;
        }
    }
    
    public int get (){
        return count;
    }
}
```

这样一来，线程调用add()，dec()方法时，它不必关心同步逻辑，因为synchronized代码块在add()，dec()方法内部，并且我们注意到，synchronized锁住的对象是this，即当前实例，这又使得创建多个Counter实例的时候，它们之间互不影响，可以并发执行：

``` java
var c1 = Counter();
var c2 = Counter();

// c1
new Thread(() -> {
    c1.add();
}).start();

new Thread(() -> {
    c1.dec();
}).start();

// c2
new Thread(() -> {
    c2.add();
}).start();

new Thread(() -> {
    c2.dec();
}).start();
```

现在对于Counter类，多线程可以正确调用。

如果一个类被设计为允许多线程正确访问，我们就说这个类就是"线程安全"的(thread-safe)，上面的Counter类就是线程安全的类，Java标准库的java.lang.SringBuffer也是线程安全的。

还有一些不变类，例如String，Integer，LocalDate，它们所有的成员变量都是final，多线程同时访问时只能读不能写，这些不变类也是线程安全的。

最后，类似Math这也只提供静态方法，没有成员变量的类，也是线程安全的。

除了上述几种少数情况，大部分类，例如ArrayList，都是非线程安全的类，我们不能在多线程中修改它们。但是，如果所有线程都只能读取，不能写入，那么ArrayList是可以安全的线程间共享的。

> 如果没有特殊说明，一个类默认是非线程安全的。

观察下面的代码

``` java
public void add (int n) {
    synchronized(this) {
        count += n;
    }
}
```

和

``` java
public synchronized void add (int n) {
    count += n
}
```

使用synchronized修饰的方法，都使用this加锁。所有两个方法是等价的。因此，使用synchronized修饰的方法就是同步方法。

观察下面方法签名

``` java
public synchronized static void test (int n);
```

静态方法是没有this实例的，那么它锁住的是哪个对象呢？

static方法是针对类而不是实例，但是我们主要到，任何一个类都有一个由JVM创建的Class实例，因此，对于static方法添加synchronized修饰，锁住的是该类的Class实例，上述synchronized static方法实际上相当于

```java
public class Counter {
    public static void test (int n) {
        synchronized (Counter.class) {
            //...
        }
    }
}
```

观察get()方法

```java
public class Counter {
    private int count;
    
    public int get () {
        return count;
    }
}
```

它并没有同步，因为读取一个int变量不需要同步。

但是下面代码就需要同步了

```java
public class Counter {
    private int first;
    private int last;
    
    public Pair get () {
        Pair p = new Pair();
        p.first = first;
        p.last = last;
        return p;
    }
}
```

#### **死锁**

Java的线程锁是可重入的锁。

什么是可重入的锁？我们看一个例子

```java
public class Counter {
    private int count = 0;
    
    public synchronized void add (int n) {
        if(n < 0) {
            dec( -n);
        }else {
            count += n;
        }
    }
    
    public synchronized void dec (int n) {
        count -= n;
    }

}
```

观察synchronized修饰的add()方法，一旦线程执行到add()方法内部，说明它已经获取的当前实例的this锁，如果传入的n< 0，将在add方法内部调用dec()方法，由于dec()方法也需要this锁，选在问题来了：

对同一个线程，能否在获取到锁以后继续获取同一个锁？

答案是肯定的，JVM允许同一个线程重复获取同一个锁，这种被同一个线程反复获取的锁，就叫可重入锁。

由于Java的线程锁是可重入锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取，每回获取一次锁，就记录+1，每退出synchronized块，就记录-1，减到0的时候，才会真正的释放锁。

死锁

一个线程可以获取一个锁后，再继续获取另一个锁，例如：

``` java
public void add (int m) {
    synchronized (lockA){
        this.value += m;
        synchronized(lockB){
            this.another += m;
        }
    }
}

public void dec (int m){
    synchronized(lockB){
        this.value -= m;
        synchronized(lockA){
            this.another -= m;
        }
    }
}
```

在获取多个锁的时候，不同线程获取多个不同对象的锁可能是死锁，对于上述代码，线程1和线程2如果分贝执行add()和dec()方法时：

- 线程1：进入add()，获得lockA;
- 线程2：进入dec()，获得lockB；

随后

- 线程1：准备获得lockB，失败，等待中；
- 线程2：准备获得lockA，失败，等待中；

此时，两个线程各自持有不同的锁，然后各自试图获取对方手中的锁，造成了双方无限等待下去，这就是死锁。

死锁发生之后，没有任何机制能解除死锁，只能强制结束JVM进程。

因此，在编写多线程应用时，要特别注意防止死锁。因为死锁一旦形成，就只能强制结束进程。

那么我们应该如何避免死锁呢？答案是：线程获取锁的顺序要一致，即严格按照先获取lockA，再获取lockB的顺序，改写dec()方法。

``` java
public void dec(int m) {
    synchronized(lockA) { // 获得lockA的锁
        this.value -= m;
        synchronized(lockB) { // 获得lockB的锁
            this.another -= m;
        } // 释放lockB的锁
    } // 释放lockA的锁
}
```

#### **使用wait和notify**

在Java程序中，synchronized解决了对哦线程竞争的问题，例如，对于一个任务管理器，多个线程同时往队列中添加任务，可以用synchronized加锁。

```java
public class TaskQueue {
    Queue<String> queue = new LinkedList<>();
    public synchronized void addTask (String s) {
        this.queue.add(s);
    }
}
```

但是synchronized并没有解决多线程协调问题。

仍然以上面的TaskQueue为例，我们再编写一个getTask()方法取出队列的第一个任务：

```java
public class TaskQueue {
    Queue<String> queue = new LinkedList<>();
    public synchronized void addTask(String s) {
        this.queue.add(s);
    }
    
    public synchronized Sring getTask() {
        while (queue.isEmpty()){
            
        }
        return queue.remove();
    }
}
```

上述代码看上去没有问题：getTask()内部先判断队列是否为空，如果为空，就等待循环，直到另一个线程往队列中放如任务，while()循环退出，就可以返回队列的元素了。

但实际上while()循环永远不会退出。因为线程在执行while()循环时，已经在getTask()入口获取了this锁，其他线程根本无法调用addTask()，因为addTask()执行条件也是获取this锁。

因此，执行上述代码，线程会在getTask()中因死循环而100%占用CPU资源。

如果我们深入思考一下，我们想要的执行效果是：

- 线程1可以调用addTask()不断往队列中添加任务；
- 线程2可以调用getTask()从队列中获取任务。如果队列为空，则getTask()应该等待，直到队列中至少有一个任务再返回。

因此，多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务。

对于上述TaskQueue，我们改造getTask()方法，在条件不满足时，线程进入等待状态。

``` java
public synchronized String getTask() {
    while(queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

当一个线程执行到getTask()方法内部的while循环时，它必定已经获得到了this锁，此时，线程执行while条件判断，如果条件成立(队列为空)，线程将执行this.wait()方法，进入等待状态。

这里的关键是：wait()方法必须在当前获取的锁的对象上调用，这里获取的是this锁，因此调用this.wait()。

那么即使线程在getTask()内部等待，其他线程如果拿不到this锁，照样无法执行addTask()方法，怎么办？

这个问题的关键就在于wait()方法的执行机制非常复杂，首先，它不是一个普通的Java方法，而是定义在Object类的一个native方法，也就是由JVM的C代码实现，其次，必须在synchronized块中才能使用wait()，因为wait()方法调用时，会释放线程获得的锁，wait()方法返回之后，线程又会重新试图获取锁。

因此，只能在锁的对象上调用wait()方法，因为在getTask()中，我们获得this锁，所以只能在this对象上调用wait()方法。

当一个线程在this.wait()等待时，它就会释放this锁，从而使得其他线程能够在addTask()方法获得this锁。

现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从wait()方法返回？答案就是在相同锁对象上调用notify()方法，我们修改addTask()如下：

``` java
public synchronized void addTask(String s) {
    this.queue.add(s);
    this.notify();
}
```

注意到现在往队列中添加了任务后，线程立刻对this锁对象调用notify()方法，这个方法会唤醒一个正在this锁等待的线程（就是在getTask()中位于this.wait()的线程）从而使得等待线程从this.wait()方法返回。

我们来看一个完整的例子：

```java
public class Main {
    public static void main(String[] args){
        var q = new TaskQueue();
        var ts = new ArrayList<Thread>();
        for (int i = 0; i < 5; i++){
            var t = new Thread(){
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
        var add = new Thread(() -> {
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
        for (var t : ts) {
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
```

这个例子中，我们重点关注addTask()方法，内部调用了this.notifyAll()而不是this.notify()，使用notifyAll()将唤醒所有当前正在this锁等待的线程，而notify()只会唤醒一个（具体哪一个依赖操作系统，有一定的随机性），这是因为可能又多个线程正在调用getTask()方法内部的wait()中等待，使用notifyAll()方法将一次性全部唤醒。通常来说，notifyAll()更安全，有些时候，如果我们的代码逻辑考虑不周，用notify()会导致只唤醒了一个线程，而其他线程可能永远等下去醒不过来。

但是，注意到wait()方法返回时需要重新获得this锁，假设当前有三个线程被唤醒，唤醒之后，首先要等待执行addTask()的线程结束此方法，才能释放this锁，随后三个线程中只能有一个获得this锁，剩下两个继续等待。

再注意到我们在while()循环中调用wait()，而不是if语句：

``` java
public synchronized String getTask() throws InterruptedException {
    if (queue.isEmpty()){
        this.wait();
    }
    return queue.remove();
}
```

这种写法实际上是错误的，因为线程被唤醒之后，需要再次获得this锁，多个线程被唤醒后，只有一个线程能获得this锁，此刻，该线程执行queue.remove()可以获取队列的元素，然而剩下的线程如果获取this锁之后，执行queue.remove()，此刻队列可能已经没有任何元素了，所以要始终在while循环中wait()，并且每次被唤醒之后拿到this锁就必须再此判断：

``` java
while(queue.isEmpty()){
    this.wait();
}
```

所以，正确编写多线程代码非常困难，需要仔细考虑的条件非常多，任何一个地方考虑不周，都会导致多线程运行时的不正常。

#### **使用ReentrantLock**

从Java5开始，引入了一个高级的处理并发的java.util.concurrent包，它提供了大量更高级的并发功能，大大简化多线程的编写。

我们知道Java语言直接提供了synchronized关键字用于加锁，但是这种锁很重，而且获取时必须一直等待，没有额外的尝试机制。

java.util.concurrent.locks包提供的ReentrantLock用于替代synchronized加锁，看一下传统的synchronized代码。

```java
public class Counter {
    private int count;
    
    public void add (int n) {
        synchronized (this) {
            count += n;
        }
    }
}
```

如果使用ReentrantLock代替，可以把代码改造为：

```java
public class Counter {
    private final Lock lock = new ReentrantLock();
    private int count;
    
    public void add (int n) {
        lock.lock();
        try {
            count += n;
        } finally{
            lock.unlock();
        }
    }
}
```

因为synchronized是Java语言层面提供的语法，所以我们不需要考虑异常，而ReentrantLock是Java代码实现的锁，我们就必须先获取锁，然后finally中正确释放锁。

顾名思义，ReentrantLock是可重入锁，它和synchronized一样，一个线程可以多次获取同一个锁。

和synchronized不同的是，ReentrantLock可以尝试获取锁：

``` java
if(lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        ...
    } finally {
        lock.unlock();
    }
}
```

上述代码正在尝试获取锁的时候，最多等待1秒，如果1秒后仍未获得锁，tryLock()返回false，程序就可以做一些额外的处理，而不是无限等下去。

所以，使用ReentrantLock比直接使用synchronized更安全，线程在truLock()失败的时候不会导致死锁。

#### **使用Condition**

使用ReentrantLock比直接使用synchronized更安全，可以代替synchronized进行线程同步。

但是，synchronized可以配合wait()和notify()实现线程在条件不满足的时候等待，条件满足时被唤醒，用ReentranLock我们怎么编写wait和notify的功能呢？

答案是使用Condition对象来实现wait和notify的功能。

我们仍然以TaskQueue为例子，把前面用synchronized实现的功能，使用ReentrantLock和Condition来实现：

```java
class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();
    
    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally{
            lock.unlock();
        }
    }
    
    public String getTask () {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}
```

可见，使用Condition时，引用的Condition对象必须从Lock实例的newCondition()返回，这样才能获得一个绑定了Lock实例的Condition实例。

Condition提供await()，signal()，signalAll()原理和synchronized锁对象的wait()，notify()，notifyAll()是一致的，并且其行为意识一样的：

- await()会释放当前锁，进入等待状态；
- signal()会唤醒某个等待线程；
- signalAll()会唤醒所有等待线程；
- 唤醒线程从await()返回后，需要重新获得锁

此外，和tryLock()类似，await()可以在等待指定时间后，如果还没有被其他线程通过signal()或者signalAll()唤醒，可以自己醒来：

``` java
if(condition.await(1, TimeUnit.SECOND)){
    // 被唤醒
} else {
    // 指定时间内没有被其他线程唤醒
}
```

可见，使用Condition配合Lock，我们可以实现更灵活的线程同步。

#### **使用ReadWriteLock**

之前讲到了ReentrantLock保证了只有一个线程可以执行临界区代码：

```java
public class Counter {
    private final Lock lock = new ReentrantLock();
    private int[] counts = new int[10];
    
    public void inc (int index) {
        lock.lock();
        try {
            counts[index] += 1;
        } finally{
            lock.unlock();
        }
    }
    
    public int[] get () {
        lock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally{
            lock.unlock();
        }
    }
}
```

但是有些时候，这种保护有点过头了，因为我们发现，任何时候，只允许一个线程修改，也就是调用inc()方法是碧玺获取锁，但是，get()方法只读取数据，不修改数据，它实际上允许多个线程同时调用。

实际上我们想要的是：允许多个线程同时读，但只要有一个线程在写，其他线程就必须等待。

使用ReadWriteLock可以解决这个问题，它保证：

- 只允许一个线程写入（其他线程既不能写入也不能读取）；
- 没有写入时，多个线程允许同时读（提高性能）。

用ReadWriteLock实现这个功能非常简单，我们需要创建一个ReadWriteLock实例，然后分别获取读锁和写锁：

```java
public class Counter {
    private final ReadWriteLock rwLock = new ReadWriteLock();
    private final Lock rLock = rwLock.readLock();
    private final Lock wLock = rwLock.writeLock();
    private int[] counts = new int[10];
    
    public void inc (int index) {
        wLock.lock();
        try {
            counts[index] += 1;
        } finally{
             wLock.unlock();
        }
    }
    
    public int[] get () {
        rLock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally{
            rLock.unlock();
        }
    }
}
```

把读写操作分开，分别使用读锁和写锁，多个线程可以同时获得读锁，这样就大大提高了并发的执行效率。

使用ReadWriteLock时，适用条件是同一个数据，有大量线程读取，但仅有少数线程修改。

例如，一个论坛的帖子，回复可以看作写入操作，它不是频繁的，但是浏览可以看作读取操作，是非常频繁的，这个时候就可以使用ReadWriteLock。

#### **使用StampedLock**

之前介绍了ReadWriteLock可以解决多线程同时读取，但只有一个线程能写的问题。

如果我们深入分析ReadWriteLock，会发现它有一个潜在的问题：如果有线程正在读，写入线程需要等待读线程释放锁之后才能获取写锁，即读的过程中不允许写，这是一种悲观锁。

要进一步提升并发执行效率，Java8 引入了新的读写锁：StampedLock。

StampedLock和ReadWriteLock相比，改进之处在于：读的过程中也允许获取写锁后写入，这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。

> 乐观锁：乐观的估计读的过程中大概率不会有写入，因此被称为乐观锁。

> 悲观锁：读的过程中拒绝有写入，也就是写入必须等待。

显然，乐观锁的并发效率更高，但一旦有小概率的写入导致读取读数据不一致，需要能检测出来，再读一遍。

查看例子：

```java
public class Point {
    private final StampedLock stampedLock = new StampedLock();
    
    private double x;
    private double y;
    
    public void move(double deltaX, double deltaY){
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally{
            stampedLock.unlockWrite(stamp);  // 释放写锁
        }
    }
    
    public double distanceFromOrigin () {
        long stamp = stampedLock.tryOptimisticRead(); // 获取乐观读版本号，此时并没有锁
        
        double currentX = x;
        
        double currentY = y;
        
        // 验证读锁版本号，确保读的过程中没有写入操作，如果有，那么加悲观锁重新读
        if(!stampedLock.validte(stamp)) {
            stamp = stampLock.readLock(); // 获得一个悲观读锁
            try {
                currentX = x;
                currentY = y;
            } finally{
                stampedLock.unLockRead(stamp); // 释放悲观读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

和ReadWriteLock相比，写入的加锁是完全一样的，不同的是读取，注意到首先我们同归tryOptimisticRead()获得一个乐观锁，并返回版本号。接着进行读取，读取完成后，我们通过validate()去验证版本号，如果在读取过程中没有写入，版本号不变，验证成功，我们就可以放心的继续后面操作，如果在读取过程中有写入，版本号会发生变化，验证失败，在失败的时候，我们再通过悲观锁再次读取，由于写入的概率不高，程序在绝大部分情况下可以通过乐观 读锁获取数据，极少情况下使用悲观读锁获取数据。

可见，StampedLock还提供了更复杂的将悲观读锁升级为写锁的功能，它主要使用在if-then-update的场景：首先读，如果读的数据条件满足，就返回，如果读的数据不满足条件，再尝试写。

#### **使用Concurrent集合**

我们之前已经了解了ReentrantLock和Condition实现了一个BlockingQueue：

```java
public class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();
    
    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally{
            lock.unlock();
        }
    }
    
    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally{
            lock.unlock();
        }
    }
}
```

BlockingQueue的意思就是说，当一个线程调用这个TaskQueue的getTask()方法时，该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，getTask()方法才会返回。

因为BlockingQueue非常有用，所以我们不必自己编写，可以直接使用Java标准库的java.util.concurrent包提供的线程安全集合：ArrayBlockingQueue。

除了BlockingQueue之外，针对List，Map，Set，Deque等，java.util.concurrent包也提供了对应的并发集合类。归纳如下：

|interface|non-thread-safe|thread-safe|
|---|---|---|
|List|ArrayList|CopyOnWriteArrayList|
|Map|HashMap|ConcurrentHashMap|
|Set|HashSet/TreeSet|CopyOnwriteArraySey|
|Queue|ArrayDeque/LinkedList|ArrayBlockingQueue/LinkedBlockingQueue|
|Deque|ArrayDeque/LinkedList|LinkedBlockingDeque|

使用这些并发集合与使用非线程安全的集合类相同。我们以ConcurrentHashMap为例：

``` java
Map<String, String> map = new ConcurrentHashMap<>();

map.put("A", "1");
map.put("B", "2");
map.put("C", "3");
```

因为所有的同步和加锁的逻辑都在集合内部实现，对外部调用者来说，只需要正常按接口引用，其他代码和原来的非线程安全的代码完全一样。即我们需要多线程访问是，只需要把：

``` java
Map<String, String> map = new HashMap<>();
```

改成：

``` java
Map<String, String> map = new ConcurrentHashMap<>();
```

就可以了。

java.util.Collections工具类还提供了一个旧的线程安全集合转换器，可以用：

``` java
Map unsafeMap = new HashMap();

Map threadSafeMap = Collections.synchronizedMap(unsafeMap);
```

但是它实际上是用一个包装类包装了非线程安全Map，然后对所有读写方法都用synchronized加锁，这样获得的线程安全集合的性能比java.util.concurrent集合要低很多，所以不推荐使用。





























