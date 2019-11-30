package com.luxshare.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test controller
 *
 * @author lion hua
 * @since 2019-11-29
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping("")
    public void test() {
        log.info("test api");
    }
}
