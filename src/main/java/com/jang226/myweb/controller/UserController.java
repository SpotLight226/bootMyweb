package com.jang226.myweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    
    // 회원가입
    @GetMapping("/join")
    public String join() {
        return "user/join";
    }
    
    // 로그인
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    
    // 회원정보
    @GetMapping("/userDetail")
    public String detail() {
        return "user/userDetail";
    }
}
