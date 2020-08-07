package com.jgmt.blog.service.impl;

import com.jgmt.blog.service.MarkdownService;
import org.pegdown.PegDownProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    /**
     * 获取markdown文件转化成HTML
     * @param is
     * @return
     * @throws IOException
     */
    public String generateHtml(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = null;
        String mdContent = "";
        while ((line = br.readLine()) != null) {
            mdContent += line + "\r\n";
        }
        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);
        String htmlContent = pdp.markdownToHtml(mdContent);

        return htmlContent;
    }

    /**
     * 查询本地css风格文件列表
     * @return
     */
    @Override
    public List<String> getCssFileList () {
        List<String> cssList = new ArrayList<>();

        try {
            File css = new ClassPathResource("/static/css/markdown").getFile();

            if(css.isDirectory()){
                File[] listFile = css.listFiles();

                assert listFile != null;
                for (File f: listFile) {
                    cssList.add(f.getName());
                }
            }
        } catch (IOException e) {

        }

        return cssList;
    }
}
