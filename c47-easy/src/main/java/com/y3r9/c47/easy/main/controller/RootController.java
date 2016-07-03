package com.y3r9.c47.easy.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Root controller.
 */
@Controller
@RequestMapping("/")
public class RootController {
	
    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @GetMapping(value="canvas")
    public String canvas() {
        return "canvas";
    }
}
