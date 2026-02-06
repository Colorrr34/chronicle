package com.ricky.chronicle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BaseController {
    @GetMapping({"/","/api"})
    public String index(){
        return "forward:/resources/index.html";
    }
}
