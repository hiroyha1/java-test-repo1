package com.example.springwebdemo1.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class PingController {
    @RequestMapping(value="/ping", method=RequestMethod.GET)
    public String requestMethodName() {
        return "pong";
    }
}
