package com.jgmt.blog;

import com.jgmt.blog.practice.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) { SpringApplication.run(BlogApplication.class, args);
        String first = "first";
        String last = "last";
        Pair.create(first, last);
        System.out.println("=====");
        new Pair<Integer>(1,2);
    }

//    public void add(Pair<? extends Number> p){
//        p.setFirst(new Integer(100));
//    }
}
