package com.luxshare.demo.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lion hua
 * @since 2019-12-04
 */
@Slf4j
@Order(3)
public class ThirdApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("key3", "value3");
        MapPropertySource mapPropertySource = new MapPropertySource("third", map);
        environment.getPropertySources().addLast(mapPropertySource);
        log.info("------- thirdApplicationContextInitializer ------- ");
    }
}
