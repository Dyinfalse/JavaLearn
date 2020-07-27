## java IO 知识点

*2020-07-27 Dyinfalse* 本篇是阅读[廖雪峰文章](https://www.liaoxuefeng.com/wiki/1252599548343744/1298069154955297)的总结

#### **File对象**

在计算机中，文件是非常重要的存储方式，Java标准库java.io提供File对象来操作文件和目录

构建一个File对象，只需要传入一个文件路径

```java
public class FileMainClass {
    public static void main(String[] args){
        File f = new File("C:\\Windows\\test.md");
        System.out.println(f);
    }
}
```

构造FIle对象时，既可以传入绝对路径，也可以传入相对路径。绝对路径是以根目录开头的完成路径，例如

```
    File f = new File("C:\\windows\\test.md");
```

注意Windows平台使用/作为路径分隔符，在Java字符串中需要使用\\表示一个\，Linux平台使用/作为分隔符
