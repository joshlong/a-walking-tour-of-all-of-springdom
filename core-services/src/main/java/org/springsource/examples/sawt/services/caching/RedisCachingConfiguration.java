package org.springsource.examples.sawt.services.caching;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springsource.examples.sawt.services.*;

@Configuration
@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
@EnableCaching
public class RedisCachingConfiguration {

    // todo import the JpaConfiguration

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) throws Exception {
        RedisTemplate<String, Object> ro = new RedisTemplate<String, Object>();
        ro.setConnectionFactory(connectionFactory);
        return ro;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) throws Exception {
        return new RedisCacheManager(redisTemplate);
    }

}
