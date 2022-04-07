package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentationController {


    @GetMapping("/doc")
    public String documentation() {
        return "documentation";
    }

}
