package com.jgmt.blog.practice;

import java.util.Objects;

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