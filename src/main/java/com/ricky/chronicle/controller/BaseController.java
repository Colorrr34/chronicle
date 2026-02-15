package com.ricky.chronicle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class BaseController {
    @GetMapping(value = { 
        "/", 
        "/{path:[^\\.]*}", 
        "/**/{path:[^\\.]*}",
        "/**/{path:(?!api)[^\\.]*}"
    })
    public String redirect(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.equals("/api") || path.equals("/api/")) {
        return "forward:/index.html";
        }
        if (path.startsWith("/api/") && path.length() > 5) {
        return null; 
        }
        if (path.contains(".")) {
        return null;
        }
        
        return "forward:/index.html";
    }
}
