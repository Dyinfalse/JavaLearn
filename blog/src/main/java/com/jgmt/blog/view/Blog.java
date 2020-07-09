package com.jgmt.blog.view;

import com.jgmt.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/")
public class Blog {

    @Autowired
    private MarkdownService markdownService;

    @RequestMapping("/blog/{fileName}")
    public ModelAndView blog(@PathVariable String fileName) throws IOException {

        ModelAndView mv = new ModelAndView();

        try {
            ClassPathResource resource = new ClassPathResource("static/md/"+fileName+".md");

            File file = resource.getFile();

            String html = markdownService.generateHtml(file);

            mv.addObject("html", html);

            mv.setViewName("blog");
        } catch (FileNotFoundException e) {
            System.out.println(e);
            mv.setViewName("redirect:/");
        }

        return mv;
    }

}
