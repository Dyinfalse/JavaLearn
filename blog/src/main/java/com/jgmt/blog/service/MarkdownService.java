package com.jgmt.blog.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public interface MarkdownService {
    public String generateHtml(InputStream is) throws IOException;
}
