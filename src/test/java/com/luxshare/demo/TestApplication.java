package com.luxshare.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * test application
 *
 * @author lion hua
 * @since 2019-11-02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class TestApplication {

    List<Integer> list = null;

    @Before
    public void before() {
        // 在运行每一个测试方法之前，我们先执行这个
        list = new ArrayList<Integer>() {
            {
                add(1);
                add(5);
                add(6);
                add(33);
                add(2);
                add(1);
            }
        };
    }

    @Test
    public void test() {
        list.stream()
                .forEach(o -> {
                    log.info(o.toString());
                });
    }

    @Test
    public void test2() {
        list.stream()
                .forEach(o -> log.info(o.toString()));
    }


    @Test
    public void test3() {
        list.stream()
                .forEach(System.out::println);
    }
}
