package com.jgmt.blog.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface MarkdownService {
    String generateHtml(InputStream is) throws IOException;

    List<String> getCssFileList();
}
