package com.luxshare.demo.proxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloImpl implements Hello {
    @Override
    public void say() {
      log.info("这前说hello");
    }
}
