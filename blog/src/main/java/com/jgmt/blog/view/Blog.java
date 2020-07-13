package com.jgmt.blog.view;

import com.jgmt.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class Blog {

    @Autowired
    private MarkdownService markdownService;

    /**
     * 博客首页
     * @return
     */
    @GetMapping("/blog")
    public ModelAndView blogIndex () {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("blogIndex");

        return mv;
    }

    /**
     * 博客详情
     * @return
     */
    @GetMapping("/blog/{fileName}")
    public ModelAndView blog(@PathVariable String fileName) throws IOException {

        ModelAndView mv = new ModelAndView();

        try {
            ClassPathResource resource = new ClassPathResource("static/md/"+fileName+".md");

            InputStream is = resource.getInputStream();

            String html = markdownService.generateHtml(is);

            mv.addObject("html", html);

            mv.setViewName("blog");
        } catch (FileNotFoundException e) {
            /**
             * 未找到文件返回博客首页
             */
            System.out.println(e);
            mv.setViewName("redirect:/blog");
        }

        return mv;
    }

}
