package com.cloudtravel.consumer.controller;

import com.cloudtravel.websocket.common.service.ISysSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("webSocket")
@Controller
public class webSocketController {
    
    @Autowired
    ISysSocketService sysSocketService;

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("webSocket");
        return modelAndView;
    }

    @GetMapping("sendMessage")
    @ResponseBody
    public void sendMessageToFront(@RequestParam("msg")String msg) throws Throwable{
        sysSocketService.sendMessage("111" , msg);
    }
}
