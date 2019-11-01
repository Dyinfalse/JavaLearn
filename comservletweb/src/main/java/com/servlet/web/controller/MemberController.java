package com.servlet.web.controller;

import com.servlet.web.dao.Member;
import com.servlet.web.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/member"})
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;

    @RequestMapping({"/findMember"})
    public ModelAndView findMember (){
        ModelAndView mv = new ModelAndView();
        Member m = memberService.get(1L);
        mv.addObject("member", m);
        mv.setViewName("member");
        return mv;
    }
}
