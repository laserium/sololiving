package com.sololiving.global.restdocs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs")
public class RestDocsController {

    @GetMapping
    public String docs() {
        return "index.html";
    }

}