package com.example.SpringSecurityPractice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping(path="login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping(path="courses")
    public String getCourses(){
        return "courses";
    }
}
