package com.jgmt.blog.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface MarkdownService {
    public String generateHtml(File mdFile) throws IOException;
}
