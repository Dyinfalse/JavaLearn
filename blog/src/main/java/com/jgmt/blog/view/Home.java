package com.jgmt.blog.view;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class Home {

    @RequestMapping("/")
    @ResponseBody
    public ModelAndView home () {

        ModelAndView mv = new ModelAndView();

        mv.setViewName("home");

        return mv;
    }
}
