package com.y3r9.c47.easy.main.controller;

import javax.ws.rs.GET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Root controller.
 */
@Controller
@RequestMapping("/")
public class MainController {
	
    @GetMapping
    public String indexPage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value="canvas")
    public String canvas() {
        return "canvas";
    }
}
