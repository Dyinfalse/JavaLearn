package com.servlet.web.controller;

//import org.springframework.stereotype.Controller;
//import org.jetbrains.annotations.NotNull;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.Date;

///**
// * 非注解形式
// * 需要javax包
// * Class对应一个页面请求
// * Class需要实现Controller接口的 handleRequest 方法
// */
//public class Home implements Controller {
//    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) {
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("serverTime", new Date().getTime());
//        mv.setViewName("home");
//        return mv;
//    }
//}
/**
 * 注解形式
 * 不需要实现 Controller 类，只需要在类上面加上 Controller 注解
 * 一个RequestMapping 对应一个请求地址
 * 加了RequestMapping的方法接受一个Model类型的参数，对应这次请求的数据模型
 * 方法可以返回String或者ModelAndView
 * 类上加@RequestMapping注解 所有请求都会映射在 /index 后面
 */
@org.springframework.stereotype.Controller
@RequestMapping({"index"})
public class Home {

    /**
     * 返回String
     */
    @RequestMapping({ "/home" })
    public String toHome (Model model){
        model.addAttribute("serverTime", new Date().getTime());
        System.out.println("访问home");
        return "home";
    }

    /**
     * 返回ModelAndView
     */
    @RequestMapping(value = {"/about"})
    public ModelAndView about (){

        ModelAndView mv = new ModelAndView();

        mv.addObject("message", "This's About Page.");

        mv.setViewName("about");

        return mv;
    }
}
