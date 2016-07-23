package com.y3r9.c47.easy.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public String indexPage(Model model) {
        return "index";
    }

    @GetMapping("/data")
    public ResponseEntity getData() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "apple");
        return ResponseEntity.ok(result);
    }

}
