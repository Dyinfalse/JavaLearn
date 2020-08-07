package com.jgmt.blog.controller;

import com.jgmt.blog.service.MarkdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MarkdownController {

    @Autowired
    MarkdownService markdownService;

    @RequestMapping("getCssFileList")
    public Map getCssFileList () {

        Map<String, Object> data = new HashMap<>();

        List<String> cssList = markdownService.getCssFileList();

        data.put("error", "0");
        data.put("list", cssList);

        return data;
    }
}
