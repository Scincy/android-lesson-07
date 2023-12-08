package kr.easw.lesson07.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class WebController {
    @RequestMapping("/login")
    public ModelAndView onLogin() {
        return new ModelAndView("login.html");
    }

    @RequestMapping("/register")
    public ModelAndView onRegister() {
        return new ModelAndView("register.html");
    }

    @RequestMapping("/manage")
    public ModelAndView onManagementDashboard() {
        return new ModelAndView("manage.html");
    }


    @RequestMapping("/server-error")
    public ModelAndView onError() {
        // 에러 페이지로 리다이렉트합니다.
        return new ModelAndView("error.html");
    }
}