package com.jgmt.blog.service.impl;

import com.jgmt.blog.service.MarkdownService;
import org.pegdown.PegDownProcessor;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    /**
     * 获取markdown文件转化成HTML
     * @param mdFile
     * @return
     * @throws IOException
     */
    public String generateHtml(File mdFile) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));
        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(mdContent);

        return htmlContent;
    }
}
