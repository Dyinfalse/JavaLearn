## java IO 知识点

*2020-07-31 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744/1298069154955297)的总结

#### **File对象**

在计算机中，文件是非常重要的存储方式，Java标准库`java.io`提供`File`对象来操作文件和目录

构建一个`File`对象，只需要传入一个文件路径

```java
public class FileMainClass {
    public static void main(String[] args){
        File f = new File("C:\\Windows\\test.md");
        System.out.println(f);
    }
}
```

构造`File`对象时，既可以传入绝对路径，也可以传入相对路径。绝对路径是以根目录开头的完成路径，例如

```
    File f = new File("C:\\windows\\test.md");
```

注意Windows平台使用`/`作为路径分隔符，在Java字符串中需要使用`\\`表示一个`\`，Linux平台使用`/`作为分隔符

```
File f = new File("/user/bin/test.md");
```

传入相对路径的时候，相对路径前面加上当前目录就是绝对路径

```
// 假设当前目录是 C:\Docs
File f1 = new File("sub\\javac");
// 绝对路径：C:\Docs\sub\javac

File f3 = new File(".\\sub\\javac");
// 绝对路径：C:\Docs\sub\javac

File f4 = new File("..\\sub\\javac");
// 绝对路径：C:\sub\javac 
```

可以使用.表示当前目录，..表示上级目录

`File`对象有3中形式表示的路径，一种是`getPath()`，返回构造方法传入的路径，一种是`getAbsolutePath()`，返回绝对路径，还有一种是`getCanonicalpath()`，它和绝对路径类似，但是返回的是规范路径。

```java
public class FilePathClass {
    public static void main(String[] args){
        File f = new File("..");
        System.out.println(f.getPath()); // 输出 ..
        System.out.println(f.getAbsolutePath()); // 输出 /app/..
        System.out.println(f.getCanonocalPath());// 输出 /
    }
}
```

什么是规范路径？

绝对路径可能是类似这样`C:\Windows\System32\..\test.md`，而规范路径就是把`.`和`..`转化成标准的绝对路径，因为`.`是当前路径，`..`表示上一级目录，所以标准之后的路径就是`C:\Windows\notepad.exe`，`\System32`和`..`就抵消了

因为Windows和Linux系统的路径分割符不同，所以Java提供了一个静态变量，专门表示当前系统的系统分隔符

```
System.out,println(File.separator) // 根据当前平台打印"\"或"/"
```

文件和目录

`File`对象既可以表示文件，也可以表示目录。特别要注意的是，构造一个`File`对象，即使传入的文件或目录不存在，代码也不会出错，因为构造一个`File`，并不会导致任何磁盘操作，只有当我们调用`File`对象的某些方法的时候才是真正的进行磁盘操作。

例如，调用`isFile()`，判断`File`对象是否是一个已存在的文件，调用`isDirectory()`，判断该`File`对象是否是一个已存在的目录：

```java
public class FileTestClass {
    public static void main(String[] args){
        File f1 = new File("C:\\Windiws");
        File f2 = new File("C:\\Windows\\test.md");
        File f3 = new File("C:\\Windows\\nothing");
        
        System.out.println(f1.isFile()); // false
        System.out.println(f1.isDirectory()); // true
        System.out.println(f2.isFile()); // true
        System.out.println(f2.isDirectory()); // false
        System.out.println(f3.isFile()); // false
        System.out.println(f3.isDirectory()); // false
    }
}
```

对`File`对象获取到一个文件时，还可以进一步判断文件的权限和大小

- `boolean canRead()`：是否可读
- `boolean canWrite()`：是否可写
- `boolean canExecute()`：是否可执行
- `long length()`：文件字节大小

对目录而言，是否可执行表示能否列出它包含的文件和子目录

创建和删除文件

当`File`对象表示一个文件时，可以通过`boolean createNewFile()`创建一个新文件，用`boolean delete()`删除该文件：

```java
public class FileDeleteClass {
    public static void main(String[] args){
        File f = new File("/path/to/file");
        if(f.createNewFile()){
            // 文件创建成功
            if(f.delete()){
                // 文件删除成功
            }
        }
    }
}
```

有些时候，程序需要读写一些临时文件，`File`对象提供了`createTempFile()`来创建一个临时文件，以及`deleteOnexit()`在JVM退出时自动删除该文件。

```java
public class FileTempClass {
    public static void main(String[] args){
        File f = File.createTempFile("temp-", ".txt");
        f.deleteOnExit();
        System.out.println(f.isFile()); // true
        System.out.println(f.getAbsolutePath());// /tmp/tmp-121442323123345467.txt
    }
}
```

遍历文件和目录

当`File`对象表示一个目录时，可以使用`list()`和`listFiles()`列出目录下的文件和子目录名，`listFiles()`提供了一系列重载方法，可以过滤不想要的文件和目录

```java
public class ListFilesClass {
    public static void main(String[] args){
        File f = new File("C:\\Windows");
        File[] fs1 = f.listFiles(); // 列出文件夹内的所有子目录和文件
        printFiles(fs1);
        File[] fs2 = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name){
                // 仅列出.exe文件，rturn true 表示接受该文件
                return name.endsWith(".exe")
            }   
        });
        printFiles(fs2);
    }
    
    /**
     * 打印文件信息
     */
    static void printFiles (File[] files) {
        System.out.println("======");
        if(files != null){
            for(File f : files){
                System.out.println(f);
            }
        }
        System.out.println("======");
    }

}
```

和文件操作类似，`File`对象如果表示一个目录，可以通过下面的方法创建和删除目录

- `boolean mkdir()`：创建当前`File`对象表示的目录
- `boolean mkdirs()`：创建当前`File`对象表示的目录，并在必要时将不存在的父目录也创建出来
- `boolean delete()`：删除当前`File`对象表示的目录，当前目录必须为空才能删除成功

Path

Java标准库还提供了一个`Path`对象，它在`java.nio.file`包下，`Path`和`File`对象类似，但操作更加简单

```java
public class PathClss {
    public static void main(String[] args){
        Path p1 = Paths.get(".", "project", "study");
        System.out.println(p1);
        
        Path p2 = p1.toAbsolutePath(); // 转绝对路径
        System.out.println(p2);
     
        Path p3 = p2.normalize(); // 转规范路径
        System.out.println(p3);
    
        File f = p3.toFile(); // 转文件
        System.out.println(f);

        for(Path p : Paths.get("..").toAbsolutePath()){
            System.out.println(p);
        }
    }
}
```

#### **InputStream**

`InputStream`就是Java标准库提供的最基本的输入流。它属于`java.io`包下，这个包提供了所有同步IO的功能。

要特别注意的是，`InputStream`不是接口，而是一个抽象类，它是所有输入流的超类，这个抽象类定义了一个最重要的方法就是`int read()`，签名如下

```java
public abstract int read () throws IOException
```

这个方法就读取输入里的下一个字节，并返回表示的`int`值（0-255），如果已经读到末尾，返回`-1`，表示不能继续读取了。

`FileInputStraem`是`InputStream`的一个子类，顾名思义，`FileInputStream`是从文件流中读取数据，下面是一个读取文件流的例子

```java
public class FileInputStreamEXClass {
    public static void main(String[] args){
        InputStream is = new FileInputStream("src/readme.md");
        for(;;){
            int n = is.read();
            if(n == -1){
                break;
            }
            System.out.println(n); // 打印byte值
        }
        is.close(); // 关闭流
    }
}
```

在计算机中，类似文件，网络端口这些资源，都是由操作系统统一管理的，应用程序在运行的过程中，如果打开一个文件进行读写，完成后要及时的关闭，以便让操作系统把资源释放掉，否则应用程序占用的资源会越来越多，不但白白占用内存，还会影响其他应用程序的运行。

`InputStream`和`OutputStream`，都使用`close()`方法关闭流，关闭流就会释放底层的对应资源。

我们还特别需要注意在读取或者写入IO流的过程中，可能会发生错误，例如，文件不存在导致无法读取，没有写入权限导致写入失败，等等，这些底层错误由Java虚拟机自动封装成`IOException`异常抛出，因此，所有与IO操作相关代码都必须正确处理`IOException`。

因此我们使用`try...finally`来保证`InputStream`无论是否发生IO错误的时候都能正确的关闭

```java

public class FileInputStreamEXClass {
    public static void main(String[] args){
        InputStream is = null;
        try{
            InputStream is = new FileInputStream("src/readme.md");
            for(;;){
                int n = is.read();
                if(n == -1){
                    break;
                }
                System.out.println(n); // 打印byte值
            }
        } finally{
            if(is != null){
                is.close(); // 关闭流
            }
        }
    }
}
```

使用`try...finally`来编写感觉会比价麻烦，更好的写法是利用Java 7引入的`try(resource)`的语法，只要编写`try`语句，就让编译器自动为我们关闭资源，写法如下

```java
public class TryResourceClass {
    public static void main(String[] args){
        try (InputStream input = new FileInputStream("src/readme.md")) {
            int n;
            while ((n = input.read()) != -1) {
                System.out.println(n);
            }
        } // 编译器会自动给我门加上finally并调用close()，只是编译阶段
    }
}
```

实际上，编译器并不会特地给`InputStream`自动加上关闭，编译器只看`try(resource = ...)`中的对象是否实现了`java.lang.AutoCloseable`接口，如果实现了，就会自动加上`finally`语句并调用`close()`，`InputStream`和`OutputStream`都已经正确实现了这个接口，所以可以用在`try(resource)`中。

缓冲

在读取流的时候，一次读取一个字节不是最高效的方法，很多流支持一次性读取多个字节到缓冲区，对于文件和网络流来说，利用缓冲区一次性读取多个字节效率往往要高很多。`InputStream`提供两个重载方法来支持读取多个字节：

- `int read(byte[] b)`：读取若干字节并填充到`byte[]`数组，返回读取的字节数
- `int read(byte[] b. int off, int len)`：指定`byte[]`数组偏移量和最大填充数

利用上面一次读取多个字节时，需要先定义一个`byte[]`数组，作为缓冲区，`read()`方法会尽可能多读取字节到缓冲区，但不回超过缓冲区的大小，`read()`方法返回值不再是`int`值，而是返回实际读取了多少个字节，如果返回`-1`，表示没有更多的数据了

利用缓冲区一次读取多个字节的代码如下

```java
public class ReadBufferClass {
    public static void main(String[] args){
        try (InputStream input = new FileInputStream("src/readme.md")) {
            byte[] buffer = new byte[1000];
            int n;
            while ((n = input.read(buffer)) != -1) {
                System.out.println("read " + n + " bytes.");
            }
        } // 编译器会自动给我门加上finally并调用close()，只是编译阶段
    }
}
```

阻塞

在调用`InputStream`的`read()`方法读取数据时，我们说`read()`方法是阻塞(Blocking)的，他的意思是说，对于下面的代码

``` java
int n;
n = input.read();
int m = n;
```

执行到第二行的时候，就必须等待`read()`方法返回后才能继续。因为读取IO流相比执行普通代码，速度会慢很多，因此，无法确定`read()`方法调用到底要花多长时间。

InputStream的实现类

用`FileInputStream`可以从文件获取输入流，这是`InputStream`常用的一个实现类，此外还有，`ByteArrayInputStream`可以在内存中模拟一个`InputStream`

```java
public class ByteArrayInputStreamClass {
    public static void main(String[] args) throws IOException {
       byte[] data = { 72, 100, 103, 108, 221, 33, 78};
        
       try(InputStream input = new ByteArrayInputStream(data)){
           int n;
            while ((n = input.read()) != -1) {
                System.out.println(n);
            }
       }
    }
} 
```

`ByteArrayInputStream`实际上是把一个`byte[]`数组在内存中变成一个`InputStream`，虽然实际应用不多，但在测试的时候，可以用它来构造一个`InputStream`。

下面是一个例子，我们想从文件中读取所有字节，并转换成`char`然后滨城一个字符串，可以这么写

```java
public class InputStreamToChar {
    public static void main(String[] args) throws IOException{
        String s;
        try (InputStream is = new FileInputStream("C:\\test\\README.md")) {
            int n;
            StringBuilder sb = new StringBuilder();
            while((n = is.read()) != -1){
                sb.append((char) n);
            }
            s = sb.toString();
        }
        System.out.println(s);
    }
}
```

如果要测试上面的程序，就真的需要在本地硬盘上放一个真实的文本文件，如果我们把代码稍微修改一下，提取一个`readAsString()`方法：

```java
public class InputStreamToChar {
    public static void main(String[] args) throws IOException{
        String s;
        try (InputStream is = new FileInputStream("C:\\test\\README.md")) {
            s = readAsString(input);
        }
        System.out.println(s);
    }
    
    public static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while((n = is.read()) != -1){
            sb.append((char) n);
        }
        return sb.toString();
    }

}
```

那么对`String readAsString(InputStream input)`方法进行测试就相当简单了，因为不一定要传入一个真的`FileInputStream`

```java
public class InputStreamToChar {
    public static void main(String[] args) throws IOException{
        String s;
        byte[] data = {72, 10, 29, 110, 28, 30, 211};
        try (InputStream is = new ByteArrayInputStream(data)) {
            s = readAsString(input);
        }
        System.out.println(s);
    }
    
    public static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while((n = is.read()) != -1){
            sb.append((char) n);
        }
        return sb.toString();
    }
}
```

这也属于面向抽象编程的应用：接收`InputStream`抽象类型，而不是具体的`FileInputStream`，从而使得代码可以处理`InputStream`的任意实现类。

#### **OutputStream**

和`InputStream`相反，`OutputStream`是Java标准库提供的最基本输出流。

和`InputStream`类似，`OutputStream`也是抽象类，它是所有输出流的超类。这个抽象类定义了一个最重要的方法就是`void write(int b)`，签名如下

```java
public abstract void write (int b) throws IOException
```

这个方法会写入一个字节到输出流，注意，虽然传入的是`int`参数，但是只会写一个字节，即自写入`int`最低8位表示字节的部分(相当于`b & 0xff`)

和`InputStream`类似，`OutputStream`也提供了`close()`方法，用来关闭输出流，以便释放系统资源。要特别注意：`OutputStream`还提供一个`flush()`方法，它的目的是将缓冲区内容真正的输出到目的地。

为什么会有`flush()`方法？因为在向磁盘网络写入的时候，处于效率的考虑，操作系统并不是输出一个字节就立刻写入到文件或者发送网络，而是把输出的字节先放到内存中的一个缓冲区里（本质上就是一个`byte[]`数组），等到缓冲区满了，再一次性写入文件或问阿圭罗，对于很多IO设备来说，一次写一个字节，和一次写`1000`个字节，花费的时间几乎完全一样，所以`OutputStream`有个`flush()`方法，能强制把缓冲区内容输出/

通常情况下，我们不需要使用这个`flush()`方法，因为缓冲区写满了，`OutputStream`会自动调用它，并且在调用`close()`方法关闭之前`OutputStream`之前，也会自动调用`flush()`方法。

但是，某些情况下，我们必须手动调用`flush()`方法，下面是一个例子

小明正在开发一款在线聊天程序，当用户输入一句话之后，就通过`OutputStream`的`write()`方法写入网络，但是测试的时候发现，发送出去之后，接收方收不到任何消息，原因就是在写入网络流的时候是先写入内存缓冲区，等缓冲区满了之后，就会一次性发送到网络。如果缓冲区大小是4k，则发送放需要发送几千个字之后，操作系统才会把缓冲区的内容发送出去，这个时候，接收方就会一次性收到大量的消息。

解决的办法就是在每输入一句话之后，就立刻调用`flush()`方法，强制操作系统把缓冲区的内容发送出去。

实际上，`InputStream`也有缓冲区，例如，从`FileInputStream`读取一个字节的时候，操作系统往往会一次性读取若干字节到缓冲区，并维护一个指针指向未读的缓冲区。然后我们每次调用`int read()`读取下一个字节的时候，可以直接返回缓冲区的下一个字节，避免每次读一个字节都导致IO操作，当缓冲区全部读完之后继续调用`read()`，则会触发操作系统的下一次读取，再次把缓冲区填满。

FileOutputStream

和`InputStream`对应的，`OutputStream`也有一个实现类叫`FileOutputStream`，用来将若干字节写入文件，并且也支持`try(resource)`，下面查看一个完整例子

```java
public class OutputStreamClass {
    public static void main(String[] args){
        try (OutputStream os = new FileOutputStream("out/readme.md")) {
            output.write("Hello".getBytes());        
        }
    }
}
```

和`InputStream`一样，`OutputStream`的`write()`方法也是阻塞的

OutputStream实现类

`BytArrayOutputStream`是`OutputStream`的一个实现类，它可以在内存中模拟一个`OutputStream`，大部分是用来测试

```java
public class ByteArrayOutputStreamClass {
    public static void main(String[] args){
        byte[] data;
        try(ByteArrayOutputStream output = new ByteArrayOutputStream()){
            output.write("Hello".getBytes("UTF-8"));
            output.write("world!".getBytes("UTF-8"));
            data = output.toByteArray();
            
            System.out.println(new String(data, "UTF-8"));
        }
    }
}
```

`ByteArrayOutputStream`实际上是把一个`byte[]`数组在内存中编程一个`OutputStream`，虽然应用场景不多，但是可以用它来构造一个`OutputStream`来测试。

同时操作多个`AutoCloseable`资源时，在`try(resource){ ... }`语句中可以同时写出多个资源，使用`;`隔开。例如下面同时读写两个文件

```java
    try(
        InputStream input = new FileInputStream("input.txt");
        OutputStream output = new FileOutputStream("output.txt")
    ) {
        input.transferTo(output);
    }
```

`transferTo`方法是`InputStream`类在Java 9版本提供的方法，其作用是从输入流中读取所有字节，然后按照顺序将字节写入给定的输出流。返回时，此输入流将在流的末尾。此方法不会关闭任何一个流。

签名如下

```
public long transferTo(OutputStream out) throws IOException
```

#### **Filter模式**

Java标准库提供的`InputStream`根据来源可以分为

- `FileInputStream`：从文件读取数据，是最终数据源
- `ServletInputStream`：从HTTP请求读取数据，是最终数据源
- `Socket.getInputStream`：从TCP链接读取数据，是最终数据源

如果我们要给`FileInputStream`添加缓冲功能，则可以从`FIleInputStream`派生一个类：

```java
BufferedFileInputStream extends FileInputStream
```

如果要给`FileInputStream`添加计算签名功能，也可以从`FileInputStream`派生一个类

```java
DigestFileInputStream extends FileInputStream
```

如果要给`FileInputStream`添加加密/解密功能，同样也可以从`FileInputStream`派生一个类

```java
CipherFileInputStream extends FileInputStream
```

如果要给`FileInputStream`添加缓冲和签名功能，那么我们还需要派生`BufferedDigestFileInputStream`，如果要给`FileInputStream`添加缓冲和加解密功能，则需要派生`BufferedCipherFileInputStream`。

我们发现，给`FileInputStream`添加三种功能，至少需要三个子类，这三种功能的组合，又需要更多的子类：

而且这还只是针对`FileInputStream`的设计，如果针对另一种`InputStream`的设计，那很快就会出现子类爆炸的情况。

因此，只饿极使用继承，为各种`InputStream`附加更多功能，根本无法控制代码的复杂度，很快就会失控。为了解决依赖继承会导致数量失控的问题，JDK首先将`InputStream`分为两大类：

一类是直接提供数据的基础InputStream，例如：

- `FileInputStream`
- `ByteArrayInputStream`
- `ServletInputStream`

另一种是提供额外附加功能的`InputStream`，例如：

- `BufferedInputStream`
- `DigestInputStream`
- `CipherInputStream`

当我们需要给一个"基础"`InputStream`附加各种功能的时候，我们首先确定这个能提供数据的`InputStream`，因为我们需要的数据总得来自某个地方，例如，`FileInputStream`，数据来自文件

```java
InputStream file = new FileInputStream("test.gz");
```

紧接着，我们希望`FileInputStream`能提供缓冲的功能来提高读取效率，因此我们用`BufferedInputStream`包装这个`InputStream`，得到的包装类型是`BufferedInputStream`，但它仍然被视为一个`InputStream`：

```java
InputStream buffered = new BufferedInputStream(file);
```

最后，假设该文件已经用gzip压缩过了，我们希望直接读取压缩的内容，就再包装一个`GZIPInputStream`：

```java
InputStream gzip = new GZIPInputStream(buffered);
```

无论我们包装多少次，得到的对象始终是`InputStream`，我们直接用`InputStream`来引用它，就可以正常读取：

```
┌─────────────────────────┐
│GZIPInputStream          │
│┌───────────────────────┐│
││BufferedFileInputStream││
││┌─────────────────────┐││
│││   FileInputStream   │││
││└─────────────────────┘││
│└───────────────────────┘│
└─────────────────────────┘
```

上述这种通过一个"基础"组件再叠加各种附加功能组件的模式，称之为`Filter`模式（或者装饰器模式：`Decorator`）。它可以让我们通过少量的类来实现各种功能组合：

```
                 ┌─────────────┐
                 │ InputStream │
                 └─────────────┘
                       ▲ ▲
┌────────────────────┐ │ │ ┌─────────────────┐
│  FileInputStream   │─┤ └─│FilterInputStream│
└────────────────────┘ │   └─────────────────┘
┌────────────────────┐ │     ▲ ┌───────────────────┐
│ByteArrayInputStream│─┤     ├─│BufferedInputStream│
└────────────────────┘ │     │ └───────────────────┘
┌────────────────────┐ │     │ ┌───────────────────┐
│ ServletInputStream │─┘     ├─│  DataInputStream  │
└────────────────────┘       │ └───────────────────┘
                             │ ┌───────────────────┐
                             └─│CheckedInputStream │
                               └───────────────────┘
```

类似的，`OutputStream`也是以这种模式来提供各种功能：

编写`FilterInputStream`

我们也可以自己编写`FilterInputStream`，以便可以把自己的`FilterInputStream`"叠加"到任何一个`InputStream`中。

下民的例子演示了如何编写一个`CountInputStream`，它的作用是对输入的字节进行计算：

```java
public class FilterMainClass {
    public static void main(String[] args){
        byte[] data = "hello, world!".getBytes("UTF-8");
        try(CountInputStream input = new CountInputStream(new ByteArrayInputStream(data))) {
            int n;
            while((n = input.read()) != -1) {
                System.out.println((char) n);
            }
            System.out.println("Total read " + input.getByteRead() + " bytes");
        }
    }
}

public class CountInputStream extends FilterInputStream {
    private int count = 0;
    
    CountInputStream (InputStream in){
        super(in);
    }
    
    public int getBytesRead() {
        return this.count;
    }
    
    public int read() throws IOException {
        int n = in.read();
        if(n != -1) {
            this.count ++;
        }
        return n;
    }
    
    public int read (byte[] b, int off, int len) {
        int n = in.read();
        this.count += n;
        return n;
    }
}
```

注意叠加多个`FilterInputStream`，我们只需要持有最外层的`InputStream`，并且，当最外层的`InputStream`关闭时（在`try(resource)`块的结束处自动关闭），内层的`InputStream`的`close()`方法也会被自动调用，并最终调用到最核心的"基础"`InputStream`，所以不存在资源泄漏的问题。

#### **操作ZIP**

`ZipInputStream`是一种`FilterInputStream`，它可以直接读取zip包的内容：

```
┌───────────────────┐
│    InputStream    │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│ FilterInputStream │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│InflaterInputStream│
└───────────────────┘
          ▲
          │
┌───────────────────┐
│  ZipInputStream   │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│  JarInputStream   │
└───────────────────┘
```

另一个`JarInputStream`是从`ZipInputStream`派生的，它增加的主要功能是直接读取`jar`文件里面的`MANIFEST.MF`文件。因为本质上jar包就是zip包，只是额外加了一些固定的描述文件。

读取zip包

我们看一个`ZipInputStream`的基本用法我们要创建`ZipInputStream`，通常是传入一个`FileInputStream`，作为数据源，然后，循环调用`getNextEntry()`方法，直到返回`null`，表示zip流结束。

一个`ZipEntry`表示一个压缩文件或目录，如果是压缩文件，我们就直接用`read()`方法不断读取，直到返回`-1`:

```java
public class ZipInputStreamClass {
    public static void main(String[] args){
        try(ZipInputStream zip = new ZipInputStream(new FileInputStream("/test.zip"))){
            ZipEntry entry = null;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if(!entry.isDirectory()){
                    int n;
                    while ((n = zip.read()) != -1){
                        ...
                    }
                }
            }
        }
    }
}
```

写入zip包

`ZipOutputStream`是一种`FilterOutputStream`，它可以直接写入内容到zip包，我们先创建一个`ZipOutputStream`，通常是包装一个`FileOutputStream`，然后每写一个文件前，先调用`putNextEntry()`，然后用`write()`写入`byte[]`数据，写入完毕之后，调用`closeEntry()`结束这个文件的打包。

```java
public class ZipOutputStreamClass {
    public static void main(String[] args){
        try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream("/test.zip"))){
            File[] files = new File("C:\\test").listFiles(); // 文件夹读取多个文件

            for(File f : files){
                zip.putNextEntry(new ZipEntry(file.getName())); // 设置Entry
                zip.write(getFileDataAsBytes(file)); // 写入文件byte
                zip.closeEntry(); // 关闭Entry
            }
        }
    }
}
```

这个例子中没有考虑文件的目录结构，如果要实现目录层次结构，`new ZipEntry(name)`传入的`name`要用相对路径。

#### **classpath**

很多Java城区启动的时候，都需要读取配置文件，例如从一个`.properties`文件读取配置：

```java
String conf = "C:\\conf\\default.properties";
try (InputStream input = new FileInputStream(conf)){
    // TODO
}
```

这段代码要正常执行，必须在C盘创建conf目录，然后在目录里创建`default.properties`文件，但是在Linux系统中，路径和Windows的又不一样。

因此，从磁盘的固定目录读取配置文件不是一个好办法。

这个时候，我们就要使用classpath来读取文件

我们知道，Java存放`.class`的目录或`jar`包也可以包含任意其他类型的文件，例如：

- 配置文件，`properties`
- 图片文件，`jpg`，`png`
- 文本文件，`txt`，`csv`

从classpath读取文件就可以避免不同环境下路径不一致的问题：如果我们把`default.properties`文件放到classpath中，就不用关心它的实际路径。

在classpath中的资源文件，路径总是以`/`开头，我们先获取当前的Class对象，然后调用`getResourceAsStream()`方法，就可以从classpath中读取任意源文件：

```java
try(InputStream input = getClass().getResourceAsStream("/default.properties")){
    // TODO
}   
```

调用`getResourceAsStream()`方法需要注意的一点是，如果资源文件不在，它会返回`null`，因此我们需要检查返回的`InputStream`是不是`null`，如果是`null`，表示资源文件在classpath中没有找到：

```java
try(inputStream input = getClass().getResourceAsStream()){
    if(input != null) {
        // TODO
    }
}
```

如果我们把默认的配置放到`jar`包中，再从外部文件系统读取一个可选的配置文件，就可以做到既又默认的配置文件，又可以让用户自己修改配置：

```java
Properties props = new Properties();
props.load(inputStreamFromClassPath("/default.properties"));
props.load(inputStreamFromFile("./conf.properties"));
```

这样读取配置文件，应用程序就更加灵活。

#### **序列化**

序列化是指吧一个Java对象变成二进制内容，本质上就是一个`byte[]`数组。

为什么要把Java对象序列化？因为序列化后可以把`byte[]`保存到文件中，或者把`byte[]`通过网络传输到远程，这样就相当于把Java对象存储到文件或者通过网络传输出去。

有序列化就有反序列化，既把一个二进制内容（`byte[]`）变回Java对象。有了反序列化，保存到文件中的`byte[]`数组又可以变回Java对象，或者从网络上读取`byte[]`并把它把它变回Java对象。

我们来看如何把一个Java对象序列化

一个Java对象想要能够序列化，必须实现一个特殊的`java.io.Serializable`接口，它的定义如下：

```java
public interface Serializable { }
```

`Serializable`接口没有定义任何方法， 它是一个空接口。我们把这样的空接口称为"标记接口"（`Marker Interface`），实现了标记接口的类仅仅是给自身贴了个"标记"，并没有增加任何方法。

把一个Java对象变成`byte[]`数组，需要使用`ObjectOutputStream`。它负责把一个Java对象写入一个字节流：

```java
public class SerializableClass {
    public static void main(String[] args){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try(ObjectOutputStream oos = new ObjectOutputStream(buffer)){
            oos.writeInt(123);
            oos.writeUTF("HELLO");
            oos.writeObject(Double.valueOf(123.22));
        }
        System.out.println(Array.toString(buffer.toByteArray()));
    }
}
```

`ObjectOutputStream`既可以写入基本类型，如`int`，`boolean`，也可以写入`String`（以UTF-8编码），还可以写入实现了`Serializable`接口的`Object`。

因为写入`Object`时需要大量的类型信息，所以写入的内容很大。

反序列化

和`ObjectOutputStream`相反，`ObjectInputStream`负责把一个字节流读取为Java对象。

```java
try(ObjectInputStream input = new ObjectInputStream(/** 二进制流 */)){
    int n = input.readInt();
    String s = input.readUTF();
    Double d = (Double) input.readObject();
}
```

除了能读取基本类型和`String`类型外，调用`readObject()`可以直接返回一个`Object`对象。要把它变成特定类型，必须强制转型，强制转型过程中，`readObject()`可能会抛出异常。

- `ClassNotFoundException`：没有找到对应的Class
- `InvalidClassException`：Class不匹配

对于`ClassNotFoundException`，这种情况常见于一个电脑上的Java程序把一个Java对象，例如，`Person`对象序列化之后，通过网络传输给另一台电脑上的另一个Java程序，但是这台电脑的Java程序并没有定义`Person`类，所以无法反序列化。

对于`InvalidClassException`，这种情况常见于序列化的`Person`对象了一个`int`类型的`age`字段，但是反序列化的时候，`Person`类定义的`age`字段被改成了`long`类型，所以导致class不兼容。

为了避免这种class定义变动导致的不兼容，Java序列化允许class定义一个特殊的`serialVersionUID`静态变量，用于标识Java类的序列化版本，通常可以由IDE自动生成。如果增加了或修改了字段，可以改变`serialVersionUID`的值，这样就能自动组织不匹配的class版本。

```java
public class Person implements Serializable {
    private static final long serialVersionUID = 28392710930992783L;
}
```

特别要注意反序列化的几个重要特点：

反序列化，由JVM直接构造出Java对象，不调用构造方法，构造方法内部的代码在反序列化时不可能执行。

安全性

因为Java的系列化可以导致一个实例直接从`bytep[]`数组创建，而不通过构造方法，因此，它存在一定的安全隐患。一个精心构造的`byte[]`数组反序列化后可以执行特定的Java代码，从而导致严重的安全漏洞。

实际上，Java本身提供的基于对象的序列化和反序列化机制既存在安全性问题，也存在兼容性问题。更好的序列化方法是通过`JSON`这样的通用数据结构来实现，只输出基本类型（包括`String`）的内容，而不存储任何与代码相关信息。

#### **Reader**

`Reader`是Java的IO库提供的另一个输入接口。和`InputStream`的区别是，`InputStream`是一个字节流，即以`byte`为单位读取，而`Reader`是一个字符流，即以`char`为单位读取：

|InputStream|Reader|
|---|---|
|字节流，以byte为单位|字符流，以char为单位|
|读取字节(-1, 0~255)：int read()|读取字符(-1, 0~65535)：int read()|
|读到字节数组：int read(byte[] b)|读到字符串：int read(char[] c)|

`java.io.Reader`是所有字符输入流的超类，它的主要方法是：

```java
public int read() throws IOException
```

这个方法读取字符流的下一个字符，并返回字符表示的`int`，范围是`0~65535`。如果已经读到末尾，返回`-1`。

FileReader

`FileReader`是`Reader`的一个子类，它可以打开文件并获取`Reader`。下面的代码演示了如何完整的读取一个`FileReader`的所有字符：

```java
public class FileReaderClass {
    public static void main(String[] args){
        Reader reader = new FileReader("src/readme.md");
        for(;;){
            int n = reader.read();
            if(n == -1){
                break;
            }
            System.out.println((char) n);
        }
        reader.close();
    }
}
```

实际使用方法和`InputStream`的`FileInputStream`非常类似，也需要使用`close()`方法关闭流

如果我们读取一个纯`ASCII`编码的文件，上述代码工作是没有问题的。但如果文件中包含中文，就会出现乱码，因为`FileReader`默认的编码与系统相关，在Windows下的默认编码可能是`GBK`，打开一个`UTF-8`的文件就会出现乱码。

```java
Reader reader = new FileReader("src/readme.md", StandarCharsets.UTF_8);
```

和`InputStream`类似，`Reader`也是一种资源，需要保证出错的时候也能正确关闭，所以我们需要使用`try(resource)`来保证`Reader`在无论有没有IO错误的时候都能正确关闭：

```java
try(Reader reader = new FileReader("src/readme.md", StandarCharsets.UTR_8)){
    // some code    
}
```

`Reader`还提供了一次性读取若干字符并填充到`char[]`数组的方法：

它返回实际读取的字符个数，最大不超过`char[]`长度，返回`-1`表示读取完成。

利用这个方法我们可以先设置一个缓冲区，然后每次尽可能填充缓冲区。

```java
public void readFile () throws IOException {
    try(Reader reader = new FileReader("src/readme.md"), StandarCharsets.UTF_8){
        char[] buffer = new char[1000];
        int n;
        while((n = reader.read(buffer)) != -1){
            System.out.println("read "+n+" chars");
        }
    }
}
```

CharArrayReader

`CharArrayReader`可以在内存中模拟一个`Reader`，它的作用实际上是把一个`char[]`数组变成一个`Reader`，这和`ByteArrayInputStream`非常类似。

```java
try(Reader reader = new CharArrayReader("Hello".toCharArray())){
    // some code
}
```

StringReader

`StringReader`可以直接把`String`作为数据源，它和`CharArrayReader`几乎一样：

```java
try(Reader reader = new StringReader("Hello")) {
    // some code
}
```

InputStreamReader

`Reader`和`InputStream`有什么关系？

除了特殊的`CharArrayReader`和`StringReader`，普通的`Reader`实际上是基于`InputStream`构造的，因为`Reader`需要从`InputStream`中读取字节流(`byte`)，然后根据编码设置，再转成`char`就可以实现字节流。如果我们查看`FileReader`的源码，它在内部实际上持有一个`FileInputStream`。

既然`Reader`本质上是基于`InputStream`的`byte`到`char`的转换器，那么，如果我们已经有了一个`InputStream`，想把它转换为`Reader`，是完全可行的。`InputStreamReader`就是这样一个转换器，它可以把任何一个`InputStream`转换为`Reader`。实例代如下：

```java
InputStream input = new FileInputStream("src/readme.md");
Reader read = new InputStreamReader(input, "UTF-8");
```

构造`InputStreamReader`的时候，我们需要传入一个`InputStream`，还需要指定编码，就可以得到一个`Reader`对象，上述代码可以通过`try(resource)`更简洁改写如下：

```java
try(Reader reader = new InputStreamReader(new FileInputStream("src/readme.md"), "UTF-8")){
    // some code
}
```

上述代码实际上就是`FileReader`的一种实现方式。

使用`try(resource)`结构的时候，当我们关闭`Reader`时，它会在内部自动顺带调用`InputStream`的`close()`方法，所以只需要关闭最外层的`Reader`对象即可。

> 使用InputStreamReader，可以把一个InputStream转换成一个Reader。

#### **Writer**

`Reader`是自带编码转换器的`InputStream`，它把`byte`转换为`char`，而`Writer`就是自带编码转换器的`OutputStream`，它把`char`转换为`byte`并输出。

`Writer`和`OutputStream`的对比如下

|OutputStream|Writer|
|---|---|
|字节流，以byte为单位|字符流，以char为单位|
|写入字节(-1, 0~255)：int write()|写入字符(-1, 0~65535)：void write(int c)|
|写入字节数组：void write(byte[] b)|写入字符串：void write(char[] c)|
|无应对方法|写入String：void write(String s)|

`Writer`是所有输出字符流的超类，它日共主要方法有：

- 写入一个字符(0~65535)：void write(int c);
- 写入字符组的所有字符：void write(char[] c);
- 写入String表示的所有字符：void write(String s);

FileWriter

`FileWriter`就是向文件中写入字符流的`Writer`。它的使用方法和`FileReader`类似：

```java
try(Writer writer = new FileWriter("src/readme.md", "UTF-8")) {
    writer.write('H');
    writer.write("Hello".toCharArray());
    writer.write("Hello");
}
```

CharArrayWriter

`CharArrayWriter`可以在内存中创建一个`Writer`，它的作用实际上构造一个缓冲区，可以写入`char`，最后得到写入的`char[]`数组，这和`ByteArrayOutputStream`非常类似：

```java
try(Writer writer = new CharArrayWriter()) {
    writer.write(65);
    writer.write(66);
    writer.write(67);
    char[] data = writer.toCharArray(); // {'A', 'B', 'C'}
}
```

StringWriter

`StringWriter`也是一个基于内存的`Writer`，它和`CharArrayWriter`类似，实际上`StringWriter`在内部维护了一个`StringBuffer`，并对外提供了`Writer`接口。

OutputStreamWriter

除了`CharArrayWriter`，和`StringWriter`之外，不同的`Writer`实际上是基于`OutputStream`构造的，它接收`char`，然后在内部自动转换成一个或多个`byte`，并写入`OutputStream`。因此，`OutputWriter`就是一个将任意的`OutputStream`转换为`Writer`的转换器：

```java
try(Writer writer = new OutputStreamWriter(new FileOutputStream("readme.md"), "UTF-8")){
    // some code
}
```

上面的代码实际上就是`FileWriter`的一种实现方式，这和上一节的`InputStreamReader`是一样的。

#### **PrintStream和PrintWriter**

`PrintStream`是一种`FilterOutputStream`，它在`OutputStream`的接口上，额外提供了一些写入各种数据类型的方法：

- 写入int：print(int)
- 写入boolean：print(boolean)
- 写入String：print(String)
- 写入Object：print(Object)

以及应对的一组`println()`方法，他会自动加上换行。

我们经常使用的`System.out.println()`方法实际上就是使用`PrintStream`打印各种数据，其中，`System.out`是系统默认提供的`PrintStream`，表示标准输出：

```java
System.out.print(12345); // 12345
System.out.print(new Object()); // @3c732a
System.out.println("Hello"); // Hello\n 
```

`System.err`是系统默认提供的标准错误输出。

`PrintStream`和`OutputStream`相比，除了添加了一组`print()/println()`方法，可以打印各种属类型，比较方便外，它还有一个额外的有点，就是不会抛出IOException，这样我们在编写代码的时候，就不必捕获`IOException`。

PrintWriter

`PrintStream`最终输出总是`byte`数据，而`PrintWriter`则是扩展了`Writer`接口，它的`print()/println()`方法最终输出是`char`数据。两者使用方法几乎一摸一样：

```java
public class Main {
    public static void main(String[] args){
        StringWriter buffer = new StringWriter();
        try(PrintWriter pw = new PrintWriter(buffer)) {
            pw.println("Hello");
            pw.println(12345);
            pw.println(true);
        }
        System.out.println(buffer.toString());
    }
}
```





