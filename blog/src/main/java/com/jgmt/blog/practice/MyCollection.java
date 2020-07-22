package com.jgmt.blog.practice;

import com.sun.tools.javac.util.List;

import java.io.*;
import java.time.DayOfWeek;
import java.util.*;

/**
 * java 集合部分练习类
 */
public class MyCollection {
    public static void main(String[] args) {
        List<Person> list = List.of(
                new Person("Mick", 20),
                new Person("Mary", 18),
                new Person("Bob", 30)
        );
        System.out.println(list.contains(new Person("Bob", 30)));
        System.out.println("位与运算符");
        System.out.println(11000010 & 0xf);
        System.out.println(DayOfWeek.MONDAY);
        /**
         * 实例化一个TreeMap
         */
        Map<Person, Integer> map = new TreeMap<>(new Comparator<Person>(){
            public int compare(Person s1, Person s2){
                return s1.age > s2.age ? -1 : 1;
            }
        });
        /**
         * 读取Properties 文件  写入Properties文件
         */
        String f = "/Users/yanjietu/Documents/java/JavaLearn/blog/src/main/resources/application.properties";
        String byteF = "# 注释\nlang=Java\nyear=2020";
        Properties pro = new Properties();
        try {
            /**
             * 文件系统读取
             */
//            pro.load(new FileInputStream(f));
            /**
             * classpath 读取
             */
            pro.load(MyCollection.class.getResourceAsStream("/static/test.properties"));
            /**
             * 内存读取
             */
//            ByteArrayInputStream bais = new ByteArrayInputStream(byteF.getBytes("UTF-8"));
//            pro.load(bais);
        } catch (IOException e){
           System.out.println(e);
        }

        String bingImageUrl = pro.getProperty("bingImageUrl");
        String hymeleafCache = pro.getProperty("spring.thymeleaf.cache");
        /**
         * 写入文件
         */
        pro.setProperty("test_set", "是个测试");
        pro.put(new Integer(100), "测试Integer");
        pro.put(new String("String"), "测试Integer");
        pro.put(new HashMap<String, Integer>(), "测试HashMap");
        try{
            String testFile = "/Users/yanjietu/Documents/java/JavaLearn/blog/src/main/resources/static/test.properties";
            /**
             * 字节流
             */
//            pro.store(new FileOutputStream(testFile), "这是一段注释");
            /**
             * 字符流
             */
            pro.store(new FileWriter(testFile), "这是一段注释11");
        } catch (IOException e){
            System.out.println("写入文件异常");
            System.out.println(e);
        }
        System.out.println(bingImageUrl +" "+ hymeleafCache);
//        System.out.println(pro.getProperty("lang") + pro.getProperty("year")); // Java2020
    }

}

class Person {
    String name;
    Integer age;
    public Person (String name, Integer age){
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals (Object o){
        if(o instanceof Person){
            Person p = (Person) o;
            return Objects.equals(this.name, p.name) && Objects.equals(this.age, p.age);
        }
        return false;
    }
}