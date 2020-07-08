package com.jgmt.blog.service;

import org.pegdown.PegDownProcessor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class Markdown {

    public String generateHtml(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));
        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(mdContent);
        System.out.println(htmlContent);

        return htmlContent;
    }
}
