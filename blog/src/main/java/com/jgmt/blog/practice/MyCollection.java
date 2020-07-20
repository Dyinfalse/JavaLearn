package com.jgmt.blog.practice;

import com.sun.tools.javac.util.List;

import java.util.Objects;

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