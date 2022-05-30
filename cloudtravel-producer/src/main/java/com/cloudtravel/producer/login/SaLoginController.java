package com.cloudtravel.producer.login;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("saLogin")
@Controller
public class SaLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaLoginController.class);

    // 测试登录，浏览器访问： http://localhost:8081/user/doLogin?username=zhang&password=123456
    @RequestMapping("doLogin")
    public String doLogin(String username, String password) {
        Boolean loginStatus = StpUtil.isLogin();
        LOGGER.info("当前用户={} , password={}登录状态为{}" , username , password , loginStatus);
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            LOGGER.info("当前token信息为: " + StpUtil.getTokenInfo());
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    // 测试注销  ---- http://localhost:8081/acc/logout
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    public static void main(String[] args) throws Throwable{
        System.out.println("郝".getBytes("utf-8").length);
    }

}
