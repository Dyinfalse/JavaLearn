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

    /**
     * 查找成员信息
     * @return
     */
    @RequestMapping({"/findMember"})
    public ModelAndView findMember (){
        ModelAndView mv = new ModelAndView();
        Member m = memberService.get(1L);
        mv.addObject("member", m);
        mv.setViewName("member");
        return mv;
    }

    /**
     * 新增成员表单
     * @return
     */
    @RequestMapping({"/setMember"})
    public ModelAndView setMember (){
        ModelAndView mv = new ModelAndView();
        mv.addObject("member", new Member());
        mv.setViewName("setMember");
        return mv;
    }

    /**
     * 保存成员信息
     * @return
     */
    @RequestMapping({"/saveMember"})
    public ModelAndView saveMember (Member member){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("saveMember");
        return mv;
    }
}
