package main;

import JavaBean.Student;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main (String[] args){
        /**
         * 使用Spring 调用 bean XML 来创建类
         * 使用ClassPathXmlApplication.getBean() 方法通过XML配置实例化参数来创建类
         * 而不是使用 new Student() 来创建类 这种将实例化权限移交给Spring的实例化方法叫做[控制反转]
         */
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        Student student = (Student) context.getBean("student3", Student.class);
        System.out.println(student.toString());
    }
}
