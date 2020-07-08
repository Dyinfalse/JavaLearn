package com.jgmt.blog.view;

import com.jgmt.blog.service.Markdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class Blog {

    @Autowired
    Markdown md;

    @RequestMapping("/blog/{fileName}")
    public ModelAndView blog(@PathVariable String fileName) throws IOException {

        ModelAndView mv = new ModelAndView();

        File file = new File("src/main/resources/static/md/" + fileName + ".md");

        String html = md.generateHtml(file);

        System.out.println(html);

        mv.setViewName("home");

        return mv;
    }

}
