package com.jgmt.blog.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface BackgroundService {
    public JSONObject getBackground (String width, String height, Integer max);
}
