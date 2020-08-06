## java Thread 知识点

*2020-08-06 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744/1304521607217185)的总结

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

方法一：从`Thread()`派生一个自定义类，然后覆写`run()`方法：

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

执行上述代码，注意到`start()`方法会在内部调用实例的`run()`方法。

方法二：创建`Thread`实例，传入一个`Runnable`

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

或者使用Java8引用的`lambda`语法进一步简写：

```java
public class ThreadLambda {
    public static void main(String[] args){
        Thread t = new Thread(() -> System.out.println("start new Thread in Lambda"));
        t.start();
    }
}
```

下面查看一个多线程实例，为了提现多线程的特点，我们加入`Thread.sleep(int)`方法

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

`sleep`传入的参数是毫秒数，调整时间的大小，我们可以看到`main`线程和`t`线程的执行顺序。事实上，我们只能肯定`main run`会最先打印，但是`main end`在`thread run`之前还是在`thread end`之后打印，我们都无法确定，因为`t`线程开始运行之后，两个线程就开始同时运行了，并且由操作系统调度，程序本身无法确定线程的调度顺序。

需要注意的一点：直接调用`Thread`实例的`run()`方法，是无效的：

``` java
Thread t = new Thread();
t.run()
```

直接调用`run()`方法，相当与调用了一个普通Java方法，当前线程不会由任何变化，也不会启动新线程，必须使用`Thread`实例的`start`方法才能启动新的线程，如果我们查看`Thread`类的源码，会看到`start()`方法内部调用了一个`private native void start0()`方法，`native`修饰符表示这是一个JVM虚拟机内部C代码实现的，不是由Java代码实现的。

线程调度的优先级

可以对线程设定优先级，设定优先级的方法是：
``` java
Thread.setPriority(int n);
```

优先级高的线程被操作系统调度的优先级较高，操作系统对更高优先级的线程可能调度更频繁，但我们绝不能通过设置优先级来确保高优先级的线程一定会先执行。

#### **线程的状态**

在Java程序中，一个线程对象只能调用一次`start()`方法启动新线程，并在新线程中执行`run()`方法。一旦`run()`方法执行完毕，线程就结束了，因此，Java线程状态有以下几种：

- `New`：新创建的线程，尚未执行。
- `Runnable`：运行中的线程，正在执行`run()`方法的Java代码。
- `Blocked`：运行中的线程，因为某些操作被阻塞而挂起。
- `Waiting`：运行中的线程，因为某些操作在等待中。
- `Timed Waiting`：运行中的线程，因为执行`sleep()`方法正在计时等待。
- `Terminated`：线程已终止，因为`run()`方法执行完毕。

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

当线程启动之后，它可以在`Runnable`，`Blocked`，`Waiting`和`Timeed Waiting`这几个状态之间切换，直到最后编程`Terminated`状态，线程终结

线程终止的原因有

- 线程正常终止：`run()`方法执行到`return`语句返回；
- 线程意外终止：`run()`方法因为未捕获的异常导致线程终止；
- 对某个线程的`Thread`实例调用`stop()`方法强制终止（非常不建议）

一个线程还可以等待另一个线程直到其运行结束。例如，`main`线程在启动`t`线程后，可以通过`t.join()`方法等待`t`线程结束后再继续运行：

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

当main线程对线程对象t调用`join()`方法时，主线程将等待变量`t`表示的线程运行结束，即`join()`就是指等待线程结束，然后才继续往下执行自身线程，所以，上述代码打印顺序肯定是`main`线程先打印`start`，`t`线程再打印`hello`，`main`线程最后再打印`end`。

如果`t`线程已经结束，对实例t调用`join()`会立即返回，此外，`join(long)`的重载方法也可以指定一个等待时间，超过等待时间就不再继续等待。

#### **中断线程**

如果线程需要执行一个长时间的任务，就可能需要使用中断线程。中断线程就是其他线程给该线程发一个信号，该线程收到信号后结束执行`run()`方法，使得自身线程能立刻结束运行。

我们举个例子，假设从网络下载一个`100M`的文件，如果网速很慢，用户等的不耐烦了，就可能在下载过程中点击"取消"，这时，程序就需要中断下载线程。

中断线程非常简单，只需要在其他线程中对目标线程调用`interrupt()`方法，目标线程需要反复检测自身状态是否是`interrupted`状态，如果是，就立刻结束运行。

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

仔细看上述代码，main线程通过调用`t.interrupt()`方法中断`t`线程，但是要注意，`interrupt()`方法仅仅向`t`线程发送"中断请求"，至于`t`线程是否能立刻相应，要看具体代码的实现，而`t`线程的`while`循环会检测`isInterrupt()`，所以上述代码能够正确响应`interrupt()`的请求，使得自身立刻结束运行`run()`方法。

如果线程处于等待状态，例如，`t.join()`会让`main`线程进入等待状态，此时，如果对`main`线程调用`interrupt()`，`join()`方法会立刻抛出`InterruptedException`，就说明有其他线程对其调用了`interrupt()`方法，通常情况下该线程应该立刻结束运行。

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

`main`线程通过调用`t.interrupt()`从而请求`t`线程中断，而此时`t`线程正位于`hello.join()`的等待中，此方法会立刻结束并抛出`InterruptedException`。由于我们在`t`线程中捕获了`InterruptedException`，因此，就可以准备结束该线程，在`t`结束之前，对`hello`线程也进行了`interrupt()`调用请求中断，如果去掉这一行代码，可以发现`hello`仍然会继续运行，且JVM不会推出。

另一个常用的中断线程的方法是设置标志位。我们通常会用一个`running`标志来表示线程是否正在运行，在外部线程中，通过`HelloThread.running`设置为`false`，就可以让线程结束：

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

注意到`HelloThread`的标志位`boolean running`是一个线程间共享变量。线程间共享变量需要使用`volatile`关键字标记，确保每个线程都能读取到更新后的变量。

为什么要对线程间共享变量用关键字`volatile`声明呢？这涉及到Java的内存模型，在Java虚拟机中，变量的值保存在内存中，但是当线程访问变量的时候，它会先取一个副本，并保存在自己的工作内存中，如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值写回到主内存，但是，这个时间是不确定的！

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

这就会导致如果一个线程更新了某一个变量，另一个线程读取的值可能还是更新前的。例如，主内存的变量`a = true`，线程1执行`a = false`时，它在此时此刻仅仅把变量`a`的副本变成了`false`，主内存的变量`a`还是`true`，在JVM把修改后的`a`写回到主内存之前，其他线程读取的`a`的值仍然是`true`，这就造成多线程之间共享变量不一致。

因此，`volatile`关键字就是告诉虚拟机：

- 每次访问变量时，总是获取内存的最新值；
- 每次修改变量后，立刻写回到主内存；

`volatile`关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。

如果我们去掉`volatile`关键字，运行上述程序，发现效果和带`volatile`差不多，这是因为在`x86`架构下，JVM回写主内存的速度非常快，但是换成ARM架构，就会有显著的延迟。

#### **守护线程**

Java程序入口就是由JVM启动`main`线程，`main`线程又可以启动其他线程，当所有线程都结束时，JVM退出，进程结束。

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

如何创建守护线程呢？方法和普通线程一样，只是在调用`start()`方法之前，调用`setDaemon(true)`，把该线程标记为守护线程：

``` java
Thread t = new Thread();
t.setDaemon(true);
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

上面代码很简单，两个线程同时对一个`int`变量进行操作，一个加`10000`次，一个减`10000`次，最后结果应该是`0`，但是每次运行，结果实际上都不一样。

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

我们假设`n`的值是`100`，如果两个线程同时执行`n = n + 1`，得到的结果很可能不是`102`，而是`101`，原因在于：

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

如果线程1在执行`ILOAD`后被操作系统中断，此时如果线程2被调度执行，它执行`ILOAD`后获取的值仍然是`100`，最终结果被两个线程`ISTORE`写入后变成`101`，而不是预期的`102`。

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

通过加锁和解锁的操作，就能保证三条指令总是在一个线程执行期间，不会有其他线程会进入此指令区间，即使在执行期间线程被操作系统中断，其他线程也会因为无法获得锁导致无法进入此指令区间。只有执行线程将锁释放后，其他线程才能有机会获得锁并执行。这种加锁和解锁之间的代码块，我们称之为临界区（Critical Section），任何时候临界区最多只有一个线程能执行。

可见，保证一段代码的原子性，就是通过加锁和解锁实现的，Java程序使用`synchronized`，关键字对一个对象进行加锁

``` java
sychronized(lock){
    n = n + 1;
}
```

`synchronized`保证了代码块在任意时刻最多只有一个线程能执行，我们把上面代码用`synchronized`改写：

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

它表示`Counter.lock`实例作为锁，两个线程在各自的`synchronized(Counter.lock){...}`代码块时，必须先获得锁，才能进入代码块进行。执行结束后，在`synchronized`语句结束后会自动释放锁，这样一来，对`Counter.count`变量进行读写就不可能同时进行，上述代码无论运行多少次，最终结果都是`0`。

使用`synchronized`解决了多线程同步访问共享变量的正确性问题，但是，它的缺点是带来了性能下降，因此，`synchronized`代码块无法并发执行，加锁和解锁也有一定的耗时，所以`synchronized`会降低程序的执行效率。

我们来概括一下如何使用`synchronized`：

- 找出修改共享变量的线程代码块；
- 选择一个共享实例作为锁；
- 使用`synchronized(lockObject){...}`

`synchronized`除了加锁的功能，还具备内存屏蔽功能，并且强制读取所有共享变量的主内存最新值，退出`synchronized`的时候，再强制回写到祝内存（如果有修改）

在使用`synchronized`的时候，不必担心抛出异常，因为无论是否有异常，都会在`synchronized`结束处正确释放锁：

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

我们再来看一个错误使用`synchronized`的例子：

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

其结果并不是`0`，这是因为，两个线程各自的`synchronized`锁住的不是同一个对象，这使得两个线程各自都可以同时获得锁：因为JVM只保证同一个锁在任意时刻只能被一个线程获取，但是两个不同的锁，在同一时刻是可以被两个不同的线程分别获取的。

因此，使用`synchronized`的时候，获取到那个锁非常重要。锁的对象如果不对，代码逻辑就不对。

JVM规范定义了几种原子操作：

- 基本类型(`long`和`double`除外)赋值，例如：`int n = m`；
- 引用类型赋值，例如: `List<String> list = testList`;

`long`和`double`是64位数据，JVM没有明确规定64位赋值操作是不是一个原子操作，不过在`x64`平台的JVM是把`long`和`double`的赋值作为原子操作实现的。

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

这样就不需要同步，因为`this.pair = ps`是引用赋值的原子操作。而语句：

``` java
int[] ps = new int[] { first, last };
```

这里的`ps`是方法内部定义的局部变量，每个线程都会有各自的局部变量，互不影响，并且互不可见，并不需要同步。

#### **同步方法**

我们知道Java程序依靠`synchronized`对线程进行同步，使用`synchronized`的时候，锁住的是哪个对象非常重要。

让线程自己选择锁住的对象，往往会使得代码逻辑混乱，也不利于封装。更好的方法是把`synchronized`逻辑封装起来，例如，我们编写一个计数器

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

这样一来，线程调用`add()`，`dec()`方法时，它不必关心同步逻辑，因为`synchronized`代码块在`add()`，`dec()`方法内部，并且我们注意到，`synchronized`锁住的对象是`this`，即当前实例，这又使得创建多个`Counter`实例的时候，它们之间互不影响，可以并发执行：

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

现在对于`Counter`类，多线程可以正确调用。

如果一个类被设计为允许多线程正确访问，我们就说这个类就是"线程安全"的(thread-safe)，上面的`Counter`类就是线程安全的类，Java标准库的`java.lang.SringBuffer`也是线程安全的。

还有一些不变类，例如`String`，`Integer`，`LocalDate`，它们所有的成员变量都是`final`，多线程同时访问时`只能读不能写`，这些不变类也是线程安全的。

最后，类似`Math`这也只提供静态方法，没有成员变量的类，也是线程安全的。

除了上述几种少数情况，大部分类，例如`ArrayList`，都是非线程安全的类，我们不能在多线程中修改它们。但是，如果所有线程都只能读取，不能写入，那么`ArrayList`是可以安全的线程间共享的。

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

使用`synchronized`修饰的方法，都使用`this`加锁。所有两个方法是等价的。因此，使用`synchronized`修饰的方法就是同步方法。

观察下面方法签名

``` java
public synchronized static void test (int n);
```

静态方法是没有`this`实例的，那么它锁住的是哪个对象呢？

`static`方法是针对类而不是实例，但是我们主要到，任何一个类都有一个由JVM创建的`Class`实例，因此，对于`static`方法添加`synchronized`修饰，锁住的是该类的`Class`实例，上述`synchronized static`方法实际上相当于

```java
public class Counter {
    public static void test (int n) {
        synchronized (Counter.class) {
            //...
        }
    }
}
```

观察`get()`方法

```java
public class Counter {
    private int count;
    
    public int get () {
        return count;
    }
}
```

它并没有同步，因为读取一个`int`变量不需要同步。

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

观察`synchronized`修饰的`add()`方法，一旦线程执行到`add()`方法内部，说明它已经获取的当前实例的`this`锁，如果传入的`n < 0`，将在`add`方法内部调用`dec()`方法，由于`dec()`方法也需要`this`锁，选在问题来了：

对同一个线程，能否在获取到锁以后继续获取同一个锁？

答案是肯定的，JVM允许同一个线程重复获取同一个锁，这种被同一个线程反复获取的锁，就叫可重入锁。

由于Java的线程锁是可重入锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取，每回获取一次锁，就记录`+1`，每退出`synchronized`块，就记录`-1`，减到`0`的时候，才会真正的释放锁。

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

在获取多个锁的时候，不同线程获取多个不同对象的锁可能是死锁，对于上述代码，线程1和线程2如果分别执行`add()`和`dec()`方法时：

- 线程1：进入`add()`，获得`lockA`;
- 线程2：进入`dec()`，获得`lockB`；

随后

- 线程1：准备获得`lockB`，失败，等待中；
- 线程2：准备获得`lockA`，失败，等待中；

此时，两个线程各自持有不同的锁，然后各自试图获取对方手中的锁，造成了双方无限等待下去，这就是死锁。

死锁发生之后，没有任何机制能解除死锁，只能强制结束JVM进程。

因此，在编写多线程应用时，要特别注意防止死锁。因为死锁一旦形成，就只能强制结束进程。

那么我们应该如何避免死锁呢？答案是：线程获取锁的顺序要一致，即严格按照先获取`lockA`，再获取`lockB`的顺序，改写`dec()`方法。

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

在Java程序中，`synchronized`解决了多线程竞争的问题，例如，对于一个任务管理器，多个线程同时往队列中添加任务，可以用`synchronized`加锁。

```java
public class TaskQueue {
    Queue<String> queue = new LinkedList<>();
    public synchronized void addTask (String s) {
        this.queue.add(s);
    }
}
```

但是`synchronized`并没有解决多线程协调问题。

仍然以上面的`TaskQueue`为例，我们再编写一个`getTask()`方法取出队列的第一个任务：

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

上述代码看上去没有问题：`getTask()`内部先判断队列是否为空，如果为空，就等待循环，直到另一个线程往队列中放如任务，`while()`循环退出，就可以返回队列的元素了。

但实际上`while()`循环永远不会退出。因为线程在执行`while()`循环时，已经在`getTask()`入口获取了this锁，其他线程根本无法调用`addTask()`，因为`addTask()`执行条件也是获取`this`锁。

因此，执行上述代码，线程会在`getTask()`中因死循环而100%占用CPU资源。

如果我们深入思考一下，我们想要的执行效果是：

- 线程1可以调用`addTask()`不断往队列中添加任务；
- 线程2可以调用`getTask()`从队列中获取任务。如果队列为空，则`getTask()`应该等待，直到队列中至少有一个任务再返回。

因此，多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务。

对于上述`TaskQueue`，我们改造`getTask()`方法，在条件不满足时，线程进入等待状态。

``` java
public synchronized String getTask() {
    while(queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

当一个线程执行到`getTask()`方法内部的`while`循环时，它必定已经获得到了`this`锁，此时，线程执行`while`条件判断，如果条件成立(队列为空)，线程将执行`this.wait()`方法，进入等待状态。

这里的关键是：`wait()`方法必须在当前获取的锁的对象上调用，这里获取的是`this`锁，因此调用`this.wait()`。

那么即使线程在`getTask()`内部等待，其他线程如果拿不到`this`锁，照样无法执行`addTask()`方法，怎么办？

这个问题的关键就在于`wait()`方法的执行机制非常复杂，首先，它不是一个普通的Java方法，而是定义在`Object`类的一个`native`方法，也就是由JVM的C代码实现，其次，必须在`synchronized`块中才能使用`wait()`，因为`wait()`方法调用时，会释放线程获得的锁，`wait()`方法返回之后，线程又会重新试图获取锁。

因此，只能在锁的对象上调用`wait()`方法，因为在`getTask()`中，我们获得`this`锁，所以只能在`this`对象上调用`wait()`方法。

当一个线程在`this.wait()`等待时，它就会释放`this`锁，从而使得其他线程能够在`addTask()`方法获得`this`锁。

现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从`wait()`方法返回？答案就是在相同锁对象上调用`notify()`方法，我们修改`addTask()`如下：

``` java
public synchronized void addTask(String s) {
    this.queue.add(s);
    this.notify();
}
```

注意到现在往队列中添加了任务后，线程立刻对this锁对象调用`notify()`方法，这个方法会唤醒一个正在`this`锁等待的线程（就是在`getTask()`中位于`this.wait()`的线程）从而使得等待线程从`this.wait()`方法返回。

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

这个例子中，我们重点关注`addTask()`方法，内部调用了`this.notifyAll()`而不是`this.notify()`，使用`notifyAll()`将唤醒所有当前正在`this`锁等待的线程，而`notify()`只会唤醒一个（具体哪一个依赖操作系统，有一定的随机性），这是因为可能又多个线程正在调用`getTask()`方法内部的`wait()`中等待，使用`notifyAll()`方法将一次性全部唤醒。通常来说，`notifyAll()`更安全，有些时候，如果我们的代码逻辑考虑不周，用`notify()`会导致只唤醒了一个线程，而其他线程可能永远等下去醒不过来。

但是，注意到`wait()`方法返回时需要重新获得`this`锁，假设当前有三个线程被唤醒，唤醒之后，首先要等待执行`addTask()`的线程结束此方法，才能释放`this`锁，随后三个线程中只能有一个获得`this`锁，剩下两个继续等待。

再注意到我们在`while()`循环中调用`wait()`，而不是`if`语句：

``` java
public synchronized String getTask() throws InterruptedException {
    if (queue.isEmpty()){
        this.wait();
    }
    return queue.remove();
}
```

这种写法实际上是错误的，因为线程被唤醒之后，需要再次获得`this`锁，多个线程被唤醒后，只有一个线程能获得`this`锁，此刻，该线程执行`queue.remove()`可以获取队列的元素，然而剩下的线程如果获取`this`锁之后，执行`queue.remove()`，此刻队列可能已经没有任何元素了，所以要始终在`while`循环中`wait()`，并且每次被唤醒之后拿到`this`锁就必须再此判断：

``` java
while(queue.isEmpty()){
    this.wait();
}
```

所以，正确编写多线程代码非常困难，需要仔细考虑的条件非常多，任何一个地方考虑不周，都会导致多线程运行时的不正常。

#### **使用ReentrantLock**

从Java5开始，引入了一个高级的处理并发的`java.util.concurrent`包，它提供了大量更高级的并发功能，大大简化多线程的编写。

我们知道Java语言直接提供了`synchronized`关键字用于加锁，但是这种锁很重，而且获取时必须一直等待，没有额外的尝试机制。

`java.util.concurrent.locks`包提供的`ReentrantLock`用于替代`synchronized`加锁，看一下传统的`synchronized`代码。

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

如果使用`ReentrantLock`代替，可以把代码改造为：

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

因为`synchronized`是Java语言层面提供的语法，所以我们不需要考虑异常，而`ReentrantLock`是Java代码实现的锁，我们就必须先获取锁，然后`finally`中正确释放锁。

顾名思义，`ReentrantLock`是可重入锁，它和`synchronized`一样，一个线程可以多次获取同一个锁。

和`synchronized`不同的是，`ReentrantLock`可以尝试获取锁：

``` java
if(lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        ...
    } finally {
        lock.unlock();
    }
}
```

上述代码正在尝试获取锁的时候，最多等待1秒，如果1秒后仍未获得锁，`tryLock()`返回`false`，程序就可以做一些额外的处理，而不是无限等下去。

所以，使用`ReentrantLock`比直接使用`synchronized`更安全，线程在`truLock()`失败的时候不会导致死锁。

#### **使用Condition**

使用`ReentrantLock`比直接使用`synchronized`更安全，可以代替`synchronized`进行线程同步。

但是，`synchronized`可以配合`wait()`和`notify()`实现线程在条件不满足的时候等待，条件满足时被唤醒，用`ReentrantLock`我们怎么编写`wait`和`notify`的功能呢？

答案是使用`Condition`对象来实现`wait`和`notify`的功能。

我们仍然以`TaskQueue`为例子，把前面用`synchronized`实现的功能，使用`ReentrantLock`和`Condition`来实现：

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

可见，使用`Condition`时，引用的`Condition`对象必须从`Lock`实例的`newCondition()`返回，这样才能获得一个绑定了`Lock`实例的`Condition`实例。

`Condition`提供`await()`，`signal()`，`signalAll()`原理和`synchronized`锁对象的`wait()`，`notify()`，`notifyAll()`是一致的，并且其行为意识一样的：

- `await()`会释放当前锁，进入等待状态；
- `signal()`会唤醒某个等待线程；
- `signalAll()`会唤醒所有等待线程；
- 唤醒线程从`await()`返回后，需要重新获得锁

此外，和`tryLock()`类似，`await()`可以在等待指定时间后，如果还没有被其他线程通过`signal()`或者`signalAll()`唤醒，可以自己醒来：

``` java
if(condition.await(1, TimeUnit.SECOND)){
    // 被唤醒
} else {
    // 指定时间内没有被其他线程唤醒
}
```

可见，使用`Condition`配合`Lock`，我们可以实现更灵活的线程同步。

#### **使用ReadWriteLock**

之前讲到了`ReentrantLock`保证了只有一个线程可以执行临界区代码：

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

但是有些时候，这种保护有点过头了，因为我们发现，任何时候，只允许一个线程修改，也就是调用`inc()`方法是必须获取锁，但是，`get()`方法只读取数据，不修改数据，它实际上允许多个线程同时调用。

实际上我们想要的是：允许多个线程同时读，但只要有一个线程在写，其他线程就必须等待。

使用`ReadWriteLock`可以解决这个问题，它保证：

- 只允许一个线程写入（其他线程既不能写入也不能读取）；
- 没有写入时，多个线程允许同时读（提高性能）。

用`ReadWriteLock`实现这个功能非常简单，我们需要创建一个`ReadWriteLock`实例，然后分别获取读锁和写锁：

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

使用`ReadWriteLock`时，适用条件是同一个数据，有大量线程读取，但仅有少数线程修改。

例如，一个论坛的帖子，回复可以看作写入操作，它不是频繁的，但是浏览可以看作读取操作，是非常频繁的，这个时候就可以使用`ReadWriteLock`。

#### **使用StampedLock**

之前介绍了`ReadWriteLock`可以解决多线程同时读取，但只有一个线程能写的问题。

如果我们深入分析`ReadWriteLock`，会发现它有一个潜在的问题：如果有线程正在读，写入线程需要等待读线程释放锁之后才能获取写锁，即读的过程中不允许写，这是一种悲观锁。

要进一步提升并发执行效率，Java8 引入了新的读写锁：`StampedLock`。

`StampedLock`和`ReadWriteLock`相比，改进之处在于：读的过程中也允许获取写锁后写入，这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。

> 乐观锁：乐观的估计读的过程中大概率不会有写入，因此被称为乐观锁。

> 悲观锁：读的过程中拒绝有写入，也就是写入时必须等待。

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

和`ReadWriteLock`相比，写入的加锁是完全一样的，不同的是读取，注意到首先我们同归`tryOptimisticRead()`获得一个乐观锁，并返回版本号。接着进行读取，读取完成后，我们通过`validate()`去验证版本号，如果在读取过程中没有写入，版本号不变，验证成功，我们就可以放心的继续后面操作，如果在读取过程中有写入，版本号会发生变化，验证失败，在失败的时候，我们再通过悲观锁再次读取，由于写入的概率不高，程序在绝大部分情况下可以通过乐观 读锁获取数据，极少情况下使用悲观读锁获取数据。

可见，`StampedLock`还提供了更复杂的将悲观读锁升级为写锁的功能，它主要使用在`if-then-update`的场景：首先读，如果读的数据条件满足，就返回，如果读的数据不满足条件，再尝试写。

#### **使用Concurrent集合**

我们之前已经了解了`ReentrantLock`和`Condition`实现了一个`BlockingQueue`：

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

`BlockingQueue`的意思就是说，当一个线程调用这个`TaskQueue的getTask()`方法时，该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，`getTask()`方法才会返回。

因为`BlockingQueue`非常有用，所以我们不必自己编写，可以直接使用Java标准库的`java.util.concurrent`包提供的线程安全集合：`ArrayBlockingQueue`。

除了`BlockingQueue`之外，针对`List`，`Map`，`Set`，`Deque`等，`java.util.concurrent`包也提供了对应的并发集合类。归纳如下：

|interface|non-thread-safe|thread-safe|
|---|---|---|
|List|ArrayList|CopyOnWriteArrayList|
|Map|HashMap|ConcurrentHashMap|
|Set|HashSet/TreeSet|CopyOnwriteArraySey|
|Queue|ArrayDeque/LinkedList|ArrayBlockingQueue/LinkedBlockingQueue|
|Deque|ArrayDeque/LinkedList|LinkedBlockingDeque|

使用这些并发集合与使用非线程安全的集合类相同。我们以`ConcurrentHashMap`为例：

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

`java.util.Collections`工具类还提供了一个旧的线程安全集合转换器，可以用：

``` java
Map unsafeMap = new HashMap();

Map threadSafeMap = Collections.synchronizedMap(unsafeMap);
```

但是它实际上是用一个包装类包装了非线程安全`Map`，然后对所有读写方法都用`synchronized`加锁，这样获得的线程安全集合的性能比`java.util.concurrent`集合要低很多，所以不推荐使用。

#### **使用Atomic**

Java的`java.util.concurrent`包除了提供底层锁，并发集合外，还提供了一组原子操作的封装类，它们位于`java.util.concurrent.atomic`包。

我们以`AtomicInteger`为例，它提供的主要操作有：

- 增加值返回新值：`int addAndGet(int delta)`；
- 加1后返回新值：`int incrementAndGet()`；
- 获取当前值：`int get()`；
- 用CAS方式设置：`int compareAndSet(int expect, int update)`。

`Atomic`类是通过无锁（lock-free）的方式实现的线程安全（thread-safe）访问。它的主要原因是利用CAS：Compare and Set。

如果我们自己通过CAS编写`incrementAndGet()`方法，大概长这样：

``` java
public int incrementAndGet(AtimicInteger var) {
    int prev, next;
    do {
        prev = var.get();
        next = prev + 1;
    } while (! var.compareAndSet(prev, next));
    return next;
}
```

CAS是指，在这个操作过程中，如果`AtomicInteger`的当前值是`prev`，那么就更新为`next`，返回`true`，如果`AtomicInteger`的当前值不是`prev`，就什么也不干，返回`false`。通过CAS操作并配合`do...while`循环，即使其他线程修改了`AtomicInteger`的值，最终结果也是正确的。

> `compareAndSet`方法的具体作用：接收了`prev`，`next`两个参数，对比`prev`和实际内存中存储的值是否相同，如果期间被其他线程干扰修改了`prev`，那么表示`next`是无效的，返回`false`，继续循环，如果`prev`和内存中的值相同，说明`next`是有效的，返回`true`

我们利用`AtomicLong`可以编写一个线程安全的全局唯一ID生成器：

```java
class IdGenerator {
    AtomicLong var = new AtomicLong();
    
    public long getNextId() {
        return var.incrementAndGet();
    }
}
```

通常情况下，我们并不需要直接使用`do...while`循环调用`compareAndSet`实现复杂的并发操作，而是用`incrementAndGet()`这样的封装好的方法，因此，使用起来非常简单。

在高度竞争情况下，还可以使用Java 8提供的`LongAdd`和`LongAccumulator`。

#### **使用线程池**

Java语言虽然内置了多线程，启动一个新线程非常方便，但是，创建线程需要操作系统资源（线程资源，栈空间），频繁的创建和销毁大量线程需要消耗大量时间。

如果可以反复使用一组线程：

```
    ┌─────┐ execute  ┌──────────────────┐
    │Task1│─────────>│ThreadPool        │
    ├─────┤          │┌───────┐┌───────┐│
    │Task2│          ││Thread1││Thread2││
    ├─────┤          │└───────┘└───────┘│
    │Task3│          │┌───────┐┌───────┐│
    ├─────┤          ││Thread3││Thread4││
    │Task4│          │└───────┘└───────┘│
    ├─────┤          └──────────────────┘
    │Task5│
    ├─────┤
    │Task6│
    └─────┘
      ...
```

那么我们就可以把很多小任务让一组线程来执行，而不是一个任务对应一个新线程。这种能接收大量小任务并进行分发处理的就是线程池。

简单来说，线程池内部维护了若干线程，没有任务的时候，这些线程处于等待状态，如果没有新任务，就分配一个空闲线程执行，如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。

Java标准库提供了`ExecutorService`接口表示线程池，它的典型用法如下：

```
ExecutorService executor = Executors.newFixedThreadPool(3);

executor.submit(task1);
executor.submit(task2);
executor.submit(task3);
executor.submit(task4);
```

因为`ExecutorService`只是接口，Java标准库提供的几个常用实现类有：

- `FixedThreadPool`：线程数固定的线程池；
- `CachedThreadPool`：线程数根据任务动态调整线程池；
- `SingleThreadExecutor`：仅单线程执行的线程池。

创建这些线程的方法都被封装到`Executors`这个类中。我们以`FixedThreadPool`为例，看看线程池的执行逻辑

```java
public class Main {
    public static void main (String[] argus) {
        ExecutorService es = Executor.newFixedThreadPool(4);
        for (int i = 0; i < 6; i ++) {
            es.submit(new Task("" + i));
        }
        
        es.shutdown();
    }
}

class Task implements Runnable {
    private final String name;
    
    public Task (String name) {
        this.name = name;
    }
    
    @Override
    public void run () {
        System.out.println("start task " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            
        }
        System.out.println("end task " + name);
    }
}
```

输出结果为：

```
> start task 1
> start task 3
> start task 0
> start task 2
> end task 0
> end task 2
> end task 3
> end task 1
> start task 4
> start task 5
> end task 4
> end task 5
```

我们观察执行结果，一次放入6个任务，但是因为线程池固定只有4个线程，因此，前4个任务会同时执行，等到有线程空闲的时候，才会执行后面的两个任务。

线程池在程序结束的时候需要关闭，使用`shutdown()`方法关闭线程池的时候，它会等待正在执行的任务完成，然后关闭，`shutdownNow()`会企图立刻停止正在执行的任务，事实上也不一定，`awaitTermination()`则会等待指定的时间返回线程池是否关闭。

如果我们把线程池改为`CachedThreadPool`，由于这个线程池会根据任务数量动态调整线程池大小，所以六个任务可以一次性全部同时执行。

如果我们的线程池限制在4～10之间动态调整怎么办？我们查看`Executors.newCachedThreadPool()`方法的源码：

```
public static ExecutorService new CachedThreadPool () {
    return ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
}
```

因此，想创建指定动态范围的线程池可以这么写：

``` java
int min = 4;
int max = 10;
ExecutorService es = new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, new Synchronous<Runnable>());
```

**ScheduledThreadPool**

还有一种任务，需要定期反复执行，例如，每秒刷新证券价格。这种任务本身是固定的，需要反复执行，可以使用`ScheduledThreadPool`。放入`ScheduledThreadPool`的任务可以定期反复执行。

创建一个`ScheduledThreadPool`仍然是通过`Executors`类：

``` java
ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
```

我们可以提交一次任务，它会在指定延迟后执行一次：

``` java
// 一秒之后开始执行一次
ses.shedule(new Task("one-time"), 1, Timeunit.SECONDS);
```

如果任务固定没三秒执行一次，我们可以这样写：

``` java
// 两秒后开始执行，每三秒执行一次
ses.scheduleAtFixedRate(new Task("fixed"), 2, 3, TimeUnit.SECONDS);
```

如果任务以固定的3秒为间隔执行，我们可以这样写：

``` java
// 两秒后开始执行，三秒执行间隔
ses.scheduleWithFixedDelay(new Task("fixed-delay"), 2, 3, TimeUnit.SECONDS);
```

我们需要注意`FixedRate`和`FixedDelay`的区别。`FixedRate`是指任务总是以固定时间触发，不管有没有执行完，执行多久，而`FixedDelay`是上次任务执行完之后，等待固定时间间隔，再开始执行下一次任务

```
// FixedRate
│░░░░    │░░░░░░  │░░░     │░░░░░   │░░░  
├────────┼────────┼────────┼────────┼────>
│<──3s──>│<──3s──>│<──3s──>│<──3s──>│


// FixedDelay
│░░░│        │░░░░░│        │░░│        │░
└───┼────────┼─────┼────────┼──┼────────┼──>
    │<──3s──>│     │<──3s──>│  │<──3s──>│

```

因此，使用`ScheduledThreadPool`时，我们需要选择任务只续执行一次，还是`FixedRate`，还是`FixedDelay`。

那么：

如果在`FixedRate`模式下，假设任务执行时间超过了间隔时间，后续任务会不会并行？

> 如果此任务的任何执行时间超过其周期，则后续执行可能会延迟开始，但并不会并行执行。

- Q2：任务抛出了异常，后续任务还是否执行？

> 如果任务的执行遇到了任何异常，则禁止后续任务的执行。

Java标准库提供了`java.util.Timer`类，这个类也可以定期执行任务，但是，一个`Timer`会对应一个`Thread`，所以`Timer`只能定期执行一个任务，多个定时任务必须启动多个`Timer`，而一个`ScheduledThreadPool`就可以调度多个定时任务，所以，我们完全可以用`ScheduledThreadPool`取代旧的`Timer`。

#### **使用Future**

在执行多个任务的时候，使用Java标准库提供的线程池是非常方便的。我们提交的任务只需要实现Runnable接口，就可以让线程池去执行：

```java
class Task implements Runnable {
    public String result;
    
    public void run () {
        this.result = longTimeCalculation();
    }
}
```

Runnable接口有一个问题，它的方法没有返回值。如果一个任务需要返回一个结果，那么只能保存到变量，还要额外的读取方法，非常不方便，所以Java标准库提供了一个Callable接口，和Runnable接口比，它多了一个返回值：

```java
class Task implements Callable<String> {
    public String call() throws Exception {
        return longTimeCalculation();
    }
}
```

并且Callable接口是一个泛型接口，可以返回指定类型的结果。

现在的问题是，如何获得异步执行的结果？

如果仔细看ExecutorService.submit()方法，可以看到，它返回了一个Future类型，一个Future类型的实例代表一个未来能获取结果的对象：

``` java
ExecutorService executor = Executor.newFixedThreadPool(4);

Callable<String> task = new Task();

Future<String> future = executor.submit(task);

String result = future.get();
```

当我们提交了一个Callable任务之后，我们会同时获得一个Future对象，然后，我们在主线程某个时刻调用Future对象的get()方法，就可以获得异步执行的结果。在调用get()时，如果异步任务已经完成，我们就直接获取结果，如果异步任务还没完成，那么get()会阻塞，直到任务完成后才返回结果。

一个Future<V>接口表示一个未来可能返回的结果，它定义的方法有：

- get()：获取结果（等待任务完成）；
- get(long timeout, TimeUnit unit)；获取结果，但只等待指定时间；
- cancel(boolean mayInterruptIfRunning)：取消当前任务；
- isDone()：判断任务是否已经完成

#### **CompletableFuture**

使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询查看isDone()是否为true，两种方法都不是很好，因为主线程会被迫等待。

从Java 8开始引入了CompletableFuture，它针对Future做了改进，可以传回调对象，当异步任务完成或发生异常的时候，自动调用对象的回调方法。

我们以获取股票价格为例，看看如何使用CompletableFuture：

```java
public class Main {
    public static void main(String[] args){
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(Main::fetchPrice);
        
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        
        cf.exceptionally((e)-> {
            e.printStackTrace();
            return null;
        });
         
        Thread.sleep(200);
    }
    
    static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        
        if(Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
    
        return 5 + Math.random() * 20;
    }
}
```

创建了一个CompletableFuture是通过CompletableFuture.supplyAsync()实现的，它需要一个实现了Supplier接口对象：

```java
public interface Supplier<T> {
    T get();
}
```

这里我们用lambda语法简化了一下，直接传入Main::fetchPrice，因为Main.fetchPrice()静态方法的签名符合Supplier接口的定义(除了方法名以外，指返回类型)。

紧接着，CompletableFuture已经被提交给默认的线程执行了，我们需要定义的是CompletableFuture完成时和异常时需要回调的实例。完成时，CompletableFuture会调用Consumer对象：

```java
public interface Consumer<T> {
    void accept(T t);
}
```

异常时，CompletableFuture会待哦用Function对象：

```java
public interface Function<T, R> {
    R apply(T t);
}
```

这里我们都用lambda语法简化了代码。

可见CompletableFuture的优点是：

- 异步任务结束时，会自动回调某个对象的方法；
- 异步任务出错时，会自动调用某个对象的方法；
- 主线程设置好回调后，不再关心异步任务的执行。

如果只是实现了异步回调机制，我们还看不出CompletableFuture和Future的优势。

CompletableFuture更强大的功能是，多个CompletableFuture可以串行执行，例如，定义两个CompletableFuture，第一个CompletableFuture根据证券名称查询证券代码，第二个CompletableFuture根据证券代码查询证券价格，使用CompletableFuture实例的thenApplyAsync方法，可以在当前任务完成的时候，立刻执行下一个任务，并且可以拿到上一个任务的返回结果，CompletableFuture实现串行操作如下：

```java
public class Main {
    public static void main(String[] args){
        // 第一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> queryCode("中国石油"));
    
        // 紧接着执行下一个任务，注意方法来源是上一个任务的实例，且上任务的返回只会回传到这个任务
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> fetchPrice(code));
        
        // cfFetch成功后打印结果
        cfFetch.thenAccept((result) -> System.out.println("price: " + result));

        // 祝线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
    
        Thread.sleep(2000);
    }

    static String queryCode (String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        return "601857";
    }

    static Double fetchPrice (String code) {
        try {
            Thread.sleep(100);
        } catch (interruptedException e) {}
        return 5 + Math.random() * 20;
    }
}
```

除了串行执行外，多个CompletableFuture还可以并行执行。例如，同时查询新浪和网易的证券代码，只要任意一个返回结果，就进行下一步查询价格，查询价格也同时从新浪和网易查询，只要任意一个返回结果就完成操作：

```java
public class Main {
    public static void main(String[] args) throws Exception {
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://finance.sina.com.cn/code/");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://money.163.com/code/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
        });
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://money.163.com/price/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }
}
```

上述代码逻辑实现异步查询规则实际上是：

```
    ┌─────────────┐ ┌─────────────┐
    │ Query Code  │ │ Query Code  │
    │  from sina  │ │  from 163   │
    └─────────────┘ └─────────────┘
           │               │
           └───────┬───────┘
                   ▼
            ┌─────────────┐
            │    anyOf    │
            └─────────────┘
                   │
           ┌───────┴────────┐
           ▼                ▼
    ┌─────────────┐  ┌─────────────┐
    │ Query Price │  │ Query Price │
    │  from sina  │  │  from 163   │
    └─────────────┘  └─────────────┘
           │                │
           └────────┬───────┘
                    ▼
             ┌─────────────┐
             │    anyOf    │
             └─────────────┘
                    │
                    ▼
             ┌─────────────┐
             │Display Price│
             └─────────────┘
```

除了anyOf()可以实现"任意个CompletableFuture只要一个成功"，allOf()可以实现"所有CompletableFuture都必须成功"，这些组合操作可以实现非常复杂的异步流程控制。

最后我们注意到CompletableFuture的命名规范：

- xxx()：表示该方法将继续在已有线程中执行；
- xxxAsync()：表示将已不再线程池中执行。

#### **ForkJoin**

Java 7开始引入一种新的Fork/Join线程池，它可以执行一种特殊任务：把一个大任务拆成多个小任务并行执行。

Fork/Join的原理就是：判断一个任务是否足够小，如果是，直接计算，否则，就拆分成几个小任务分别计算，这个过程可以反复分裂成一系列小任务。

下面观察一个例子：

```java
public class Main {
    public static void main(String[] args){
        long[] array = new long[2000];
        long expectedSum = 0;
        
        for (int i = 0; i < array.length; i++){
            array[i] = random();
            expectedSum += array[i];
        }
        
        System.out.println("Expected sum: " + expectedSum);
        
        // fork / join
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        
        long startTime = System.currentTimeMillis();
        
        long result = ForkJoinPool.commonPool().invoke(task);
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("Fork/Join sum: " + result + " in " + (endTime - startTime) + "ms");
    }
    
    static Random random = new Random(0);
    
    static long random () {
        return random.nextInt(10000);
    }
}

class SumTask extends RecursiveTask<Long> {
    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;
    
    SumTask (long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute () {
        if (end - start <= THRESHOLD) {
            // 足够小，直接计算
            long sum = 0;
            for (int i = start; i < end; i++){
                sum += array[i];
                try {
                    // 故意放慢速度
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    
                }
            }
            return sum;
        }
        
        int middle = (end - start) / 2;
        System.out.println(String.format("split %d~%d => %d~%d,%d~%d", start, end, start, middle, middle, end));
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subTask2);
        
        Long subresult1 = subtask1.join();
        Long subresult2 = subtask2.join();

        Long result = subresult1 + subresult2;
        
        System.out.println("result = " + subresult1 + " + " + subresult2 + " ===> " + result);
        
        return result;
    }

}
```

输入
```
> Expected sum: 9788366
> split 0~2000 ==> 0~1000, 1000~2000
> split 0~1000 ==> 0~500, 500~1000
> split 1000~2000 ==> 1000~1500, 1500~2000
> result = 2391591 + 2419573 ==> 4811164
> result = 2485485 + 2491717 ==> 4977202
> result = 4811164 + 4977202 ==> 9788366
> Fork/join sum: 9788366 in 1169 ms.
```

观察上面代码的执行过程，一个大的计算任务0～2000，首先分类为两个小人物0～1000和1000～2000，这两个任务仍然太大，继续分为0～500，500～1000，1000～1500和1500～2000，最后计算依次合并，得到最终结果。

因此，核心代码SumTask继承自RecursiveTask，在compute()方法中，关键是如何分裂出子任务并提交：

``` java
class SumTask extends RecursiveTask <Long> {
    protected Long compute () {
        // 分类子任务
        SumTask subtask1 = new SumTask(...);
        SumTask subtask2 = new SumTask(...);
        // 发送子任务
        invokeAll(subtask1, subtask2);
        // 获得子任务结果
        Long result1 = subtask1.join();
        Long result2 = subtask2.join();
        // 返回结果
        return result1 + result2;
    }
}
```

Fork/Join线程池在Java标准库中就有应用。Java标准库提供的java.util.Arrays.parallelSort(array)可以进行并行排序，它的原理就是内部通过Fork/Join对大数组进行拆分并发排序，在多核CPU上就可以大大提高排序速度。

#### **ThreadLocal**

多线程是Java实现多线程任务的基础，Thread对象代表一个线程，我们可以在代码中调用Thread.currentThread()获取当前线程。例如打印日志的时候，可以同时打印出线程名称：

```java
public class Main {
    public static void main (String[] args) throws Exception {
        log("start main ...");
        new Thread(() -> log("run task ...")).start();
        
        new Thread(() -> log("print...")).start();
    
        log("end main");
    }
    
    static void log (String s ){
        System.out.println(Thread.currentThread().getName() + ": " + s);
    }
}
```

输出：

```
> main: start main...
> Thread-0: run task...
> main: end main.
> Thread-1: print...
```

对于多任务，Java标准库提供的献策很难过可以方便的执行这些任务，同时复用线程。Web应用程序就是典型的多任务应用，每个用户请求页面时，我们都会创建一个任务，类似：

``` java
public void process (User user) {
    checkPermission();
    doWork();
    saveStatus();
    sendResponse();
}
```

然后通过线程池去执行这些任务。

观察process()方法，它的内部需要调用若干其他方法，同时，我们遇到一个问题：如何在一个线程内传递状态？

process()方法需要传递的状态就是User实例，通常我们会认为直接把User作为参数直接穿进去就好了：

```
public void process(User user) {
    checkPermission(user);
    doWork(user);
    saveStatus(user);
    sendResponse(user);
}
```

但是往往一个方法又会调用很多其他方法，这样会导致User传递到所有地方：

```
void doWork (User user) {
    queryStatus(user);
    checkStatus();
    setNewStatus(User);
    log();
}
```

这种在一个线程中，横阔若干方法调用，需要传递的对象，我们通常称之为上下文(Context)，它是一种状态，可以是用户信息，任务信息等。

给每个方法增加一个context参数非常麻烦，而且有些时候，如果调用链有无法修改的源码的第三方库，User对象就传不进去了。

Java标准库提供了一个特殊的ThreadLocal，它可以在一个线程中传递对象。

ThreadLocal实例通常总是以静态字段初始化：

``` java
static ThreadLocal<User> threadLocalUser = new ThreadLocal<>();
```

它的典型用法如下：

``` java
void processUser (User user) {
    try {
        threadLocalUser.set(user);
        step1();
        step2();
    } finally {
        threadLocalUser.remove();
    }
}
```

通过设置一个User实例关联到ThreadLocal中，在移除之前，所有方法都可以随时获取到该User实例：

```
void step1() {
    User user = threadLocalUser.get();
    log();
    printUser();
}

void log() {
    User user = threadLocakUser.get();
    println(u,name)
}
```

注意到普通的方法调用一定是同一个线程执行的，所以step1(), step2()以及log()方法内部，threadLocalUser.get()获取的User对象实例是同一个。

实际上，可以把ThreadLocal看成一个全局Map<Thread, Object>：每个线程获取ThreadLocal变量时，总是使用Thread自身作为key：

```
Object threadLocalValue = threadLocalMap.get(Thread.currentThread());
```

因此，ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不相干。

特别注意ThreadLocal一定要在finally中清除：

``` java
try {
    threadLocalUser.set();
    ...
} finally {
    threadLocalUser.remove();
}
```

这是因为当前线程执行完相关代码后，很可能会被重新放入线程池，如果ThreadLocal没被清除，该线程执行其他代码是，会把上一次的状态带进去。

为了保证能释放ThreadLocal关联的实例，我们可以通过AutoCloseable接口配合try(resource) {...}结构，让编译器自动为我们关闭，例如，一个保存了当前用户的ThreadLocal可以封装为一个UserContext对象：

```java
public class UserContext implements AutoCloseable {
    static final ThreadLocal<String> ctx = new ThreadLocal<>();
    
    public UserContext (String user) {
        ctx.set(user);
    }
    
    public static String currentUser() {
        return ctx.get();
    }
    
    @Override
    public void close() {
        ctx.remove();
    }
}
```

使用的时候，我们借助try(resource){...}结构，如下：

``` java
try (var ctx = new UserContext("Bob")) {
    String currentUser = UserContext.currentUser();
}
```

这样就在UserContext中完全封装了ThreadLocal，外部代码在try(resource) {...}内部可以随时使用UserContext.currentUser()获取当前线程绑定的用户名。


























































