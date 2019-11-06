package com.servlet.web.controller;

//import org.springframework.stereotype.Controller;
//import org.jetbrains.annotations.NotNull;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
//import org.springframework.web.servlet.mvc.Controller;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

///**
// * 非注解形式
// * 需要javax包
// * Class对应一个页面请求
// * Class需要实现Controller接口的 handleRequest 方法
// */
//public class Home implements Controller {
//    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) {
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("serverTime", new Date().getTime());
//        mv.setViewName("home");
//        return mv;
//    }
//}
/**
 * 注解形式
 * 不需要实现 Controller 类，只需要在类上面加上 Controller 注解
 * 一个RequestMapping 对应一个请求地址
 * 加了RequestMapping的方法接受一个Model类型的参数，对应这次请求的数据模型
 * 方法可以返回String或者ModelAndView
 * 类上加@RequestMapping注解 所有请求都会映射在 /index 后面
 */
@org.springframework.stereotype.Controller
@RequestMapping({"index"})
public class Home {

    /**
     * 返回String
     * 参数会根据接受类型返回需要的实体（详见：p50-p51）
     */
    @RequestMapping({ "/home" })
    public String toHome (Model model){
        model.addAttribute("serverTime", new Date().getTime());
        System.out.println("访问home");
        return "home";
    }

    /**
     * 返回ModelAndView
     *
     *
     */
    @RequestMapping(value = {"/about"})
    public ModelAndView about (HttpServletRequest request){
//        获取重定向传来的 RedirectAttributes 对象
        Map<String, Object> map = (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);

        ModelAndView mv = new ModelAndView();

        mv.addObject("message", "This's About Page.");

        if(map != null) {
            System.out.println(map.get("flashName"));
            mv.addObject("flashName", map.get("flashName"));
        }

        mv.setViewName("about");

        return mv;
    }

    /**
     * Flash 属性转发
     * 访问 /index/redirect 会被重定向到 /index/about
     * 并且 传递一个 redirectAttributes flashName
     * 使用 RequestContextUtils.getInputFlashMap 接受传递的整个对象
     */
    @RequestMapping(value = {"/redirect"})
    public String redirect (RedirectAttributes redirectAttributes){
        System.out.println("请求会被重定向");

        redirectAttributes.addFlashAttribute("flashName", new Date().getTime());
        return "redirect:/index/about";
    }

    /**
     * 接收参数的方法 支持GET和POST
     * 主要使用
     *     @RequestParam 获取请求参数
     *     @PathVariable 获取路径参数
     */
    @RequestMapping(value = {"/getParams/{pathParam}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getParams (@PathVariable String pathParam, @RequestParam(defaultValue = "default HttpParam", required = false) String httpParam){
//        接受两个参数，一个通过URL传参，一个通过请求题传参
        ModelAndView mv = new ModelAndView();

        mv.addObject("pathParam", pathParam);

        mv.addObject("httpParam", httpParam);

        mv.setViewName("getParams");

        return mv;
    }
}
