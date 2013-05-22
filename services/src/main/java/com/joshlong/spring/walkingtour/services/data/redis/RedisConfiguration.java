package com.joshlong.spring.walkingtour.services.data.redis;

import com.joshlong.spring.walkingtour.services.*;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Import({LocalDataSourceConfiguration.class, CloudFoundryDataSourceConfiguration.class})
@Configuration
@ComponentScan
public class RedisConfiguration {
    @Bean
    public Topic topic() {
        return new ChannelTopic("pubsub:customer");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(Topic topic, RedisConnectionFactory redisConnectionFactory, TaskExecutor taskExecutor) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setTaskExecutor(taskExecutor);
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                System.out.println("Received notification " + new String(message.getBody()) + ".");
                System.out.println(new String(pattern));
            }
        }, topic);
        return redisMessageListenerContainer;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setQueueCapacity(1);
        taskExecutor.setCorePoolSize(10);
        return taskExecutor;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) throws Exception {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
