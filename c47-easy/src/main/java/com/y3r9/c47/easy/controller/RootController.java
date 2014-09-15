package com.y3r9.c47.easy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * ------------------------------------------------
 * <h4>Copyright (C) All rights reserved by BetaSoft</h4>
 * <p/>
 * Developer: zyq
 * Date: 13-11-29 上午11:03
 * <p/>
 * Change Description
 * <p/>
 * ------------------------------------------------
 */
@Controller
@RequestMapping("/")
public class RootController {
	
    @RequestMapping
    public String index(Model model) {
    	System.out.println("feage");
    	System.out.println("egege");
        return "index";
    }

    @RequestMapping(value="canvas")
    public String canvas() {
        return "canvas";
    }
}
