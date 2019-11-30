package com.luxshare.demo.proxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类
 *
 * @author lion hua
 * @since 2019-11-27
 */
@Slf4j
public class ProxyTest {

    /**
     * 这个有错,不能这样写
     */
    @Test
    public void test() {


        final Object hello = Proxy.newProxyInstance(new Hello() {
            @Override
            public void say() {
                log.info("hello");
            }
        }.getClass().getClassLoader(), new Class[]{Hello.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("说hello之前!");
                return null;
            }
        });
        Hello h = (Hello) hello;
        h.say();
    }

    @Test
    public void test2() {
        Hello hello = new HelloImpl();
        final Object obj = Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), (proxy, method, args) -> {
            log.info("-------来来来---------");
            return method.invoke(hello, args);
        });

        Hello h = (Hello) obj;
        h.say();

    }
}
