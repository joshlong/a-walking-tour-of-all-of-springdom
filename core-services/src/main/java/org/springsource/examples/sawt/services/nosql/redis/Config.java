package org.springsource.examples.sawt.services.nosql.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.data.keyvalue.redis.serializer.JacksonJsonRedisSerializer;
import org.springsource.examples.sawt.services.model.Customer;

@Configuration
public class Config {


    private String host = "127.0.0.1";


    @Bean
    public JacksonJsonRedisSerializer<Customer> jacksonJsonRedisSerializer() {
        return new JacksonJsonRedisSerializer<Customer>(Customer.class);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(this.jedisConnectionFactory());
        /*  RedisTemplate redisTemplate = new RedisTemplate(this.jedisConnectionFactory());
        redisTemplate.setDefaultSerializer(this.jacksonJsonRedisSerializer());
        redisTemplate.setHashKeySerializer(jacksonJsonRedisSerializer());
        redisTemplate.setKeySerializer( jacksonJsonRedisSerializer() );
        redisTemplate.setValueSerializer(jacksonJsonRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonJsonRedisSerializer());
        redisTemplate.setHashKeySerializer(jacksonJsonRedisSerializer());
        redisTemplate.setHashKeySerializer(jacksonJsonRedisSerializer());*/
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(this.host);

        return jedisConnectionFactory;
    }

}
