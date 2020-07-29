## java IO 知识点

*2020-07-27 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744/1298069154955297)的总结

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

为什么会有`flush()`方法？因为在向磁盘网络写入的时候，处于效率的考虑，操作系统并不是输出一个字节就立刻写入到文件或者发送网络，而是把输出的字节先放到内存中的一个缓冲区里（本质上就是一个`byte[]`数组），等到缓冲区满了，再一次性写入文件或问阿圭罗，对于很多IO设备来说，一次写一个字节，和一次写`1000个字节，话费的时间几乎完全一样，所以`OutputStream`有个`flush()`方法，能强制把缓冲区内容输出/

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


































