package com.jgmt.blog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jgmt.blog.service.Background;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("home")
public class Home {

    @Autowired
    private Background background;

    @RequestMapping("getBingImages")
    @ResponseBody
    public ModelAndView getBingImages (
            @RequestParam(defaultValue = "3840") String width,
            @RequestParam(defaultValue = "2160") String height,
            @RequestParam(defaultValue = "7") String max){

        HashMap<String, String> images = new HashMap<String, String>();

        ModelAndView mv = new ModelAndView();

        mv.setViewName("home");

        try {

            JSONObject json = background.getBackground(width, height, max);

            JSONArray jsonList = json.getJSONArray("images");

            double random = Math.random();

            JSONObject randomImage = (JSONObject) jsonList.get((int) (random * 7));

            images.put("url", randomImage.getString("url"));
            images.put("title", randomImage.getString("copyright"));

        } catch (Exception e){
            images.put("data", "server error: " + e);
            System.out.println(e);
        }

        return mv;
    }

}
