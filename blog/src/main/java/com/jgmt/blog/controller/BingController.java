package com.jgmt.blog.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jgmt.blog.service.BackgroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端页面的数据接口
 */
@RestController
@RequestMapping("api")
public class BingController {

    @Autowired
    private BackgroundService backgroundService;

    /**
     * bing获取的图片个数
     * 最多一次获取8个
     */
    private Integer GET_IMAGES_COUNT = 8;

    @RequestMapping(value = "getBingImages", method = RequestMethod.GET)
    @ResponseBody
    public HashMap getBingImages (
            @RequestParam(defaultValue = "3840") String width,
            @RequestParam(defaultValue = "2160") String height,
            @RequestParam(defaultValue = "7") Integer max){

        HashMap<String, Object> data = new HashMap<String, Object>();

        try {

            /**
             * 从bing 获取最近20个图片
             */
            JSONObject json = backgroundService.getBackground(width, height, GET_IMAGES_COUNT);

            JSONArray jsonList = json.getJSONArray("images");

            List<Map<String, String>> imageList = new ArrayList<>();

            Integer indexMax = GET_IMAGES_COUNT;

            /**
             * GET_IMAGES_COUNT 个里面随机挑取 max 个
             */
            for (Integer i = 0; i < max; i++){

                double random = Math.random();

                HashMap<String, String> imagesItem = new HashMap<>();

                JSONObject randomImage = (JSONObject) jsonList.get((int) (random * indexMax));

                imagesItem.put("title", randomImage.getString("copyright"));
                imagesItem.put("url", "https://cn.bing.com/" + randomImage.getString("url"));

                imageList.add(imagesItem);
                /**
                 * 添加之后删除改项
                 */
                jsonList.remove(randomImage);
                indexMax -= 1;
            }

            data.put("list", imageList);

            data.put("error", 0);

        } catch (Exception e){
            data.put("error", "server error: " + e);
            System.out.println(e);
        }

        return data;
    }

}
