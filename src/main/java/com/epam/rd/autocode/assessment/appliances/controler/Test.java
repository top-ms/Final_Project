package com.epam.rd.autocode.assessment.appliances.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class Test {
    @GetMapping
    public String test() {
        return "index";
    }
}
