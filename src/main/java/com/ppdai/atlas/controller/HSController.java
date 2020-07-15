package com.ppdai.atlas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HSController {

    @GetMapping("/hs")
    public String healthcheck() {
        return "OK";
    }

}
