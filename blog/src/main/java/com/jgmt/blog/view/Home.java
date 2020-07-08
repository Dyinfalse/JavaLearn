package com.jgmt.blog.view;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class Home {

    /**
     * 首页
     */
    @RequestMapping("/")
    public ModelAndView home () {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("home");

        return mv;
    }

    /**
     * 书本页面
     */
    @RequestMapping("/bookList")
    public ModelAndView bookList () {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("bookList");

        return mv;
    }
}
