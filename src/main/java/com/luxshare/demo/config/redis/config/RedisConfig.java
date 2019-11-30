//package com.luxshare.demo.config.redis.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//
///**
// * redis config
// * 改变 redis 默认的序列化
// *
// * @author lion hua
// * @since 2019-11-29
// */
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        // 原有的jdk序列化会导致写进redis的数据不可读
//        // 设置自己的序列化工具
//        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
//        template.setConnectionFactory(redisConnectionFactory);
//        return template;
//    }
//}
