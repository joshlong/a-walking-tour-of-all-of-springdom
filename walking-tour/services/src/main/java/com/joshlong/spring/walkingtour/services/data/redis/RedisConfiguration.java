package com.joshlong.spring.walkingtour.services.data.redis;

import com.joshlong.spring.walkingtour.services.*;
import com.joshlong.spring.walkingtour.services.model.Customer;

import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.*;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Import({ LocalDataSourceConfiguration.class,
		CloudFoundryDataSourceConfiguration.class })
@Configuration
@ComponentScan
public class RedisConfiguration {

	private ChannelTopic channelTopic = new ChannelTopic("pubsub:customer");;

	@Bean
	public ChannelTopic channelTopic() {
		return this.channelTopic;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
			final RedisTemplate rt,
			RedisConnectionFactory redisConnectionFactory,
			TaskExecutor taskExecutor) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setTaskExecutor(taskExecutor);
		redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
		redisMessageListenerContainer.addMessageListener(
				new MessageListenerAdapter(  
				new MessageListener() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				String topic = new String( message.getChannel());
				 Customer customerObject =(Customer)
						 rt.getValueSerializer().deserialize( message.getBody());
				
				System.out.println("Received notification on topic "+ topic +  " the value " + customerObject ) ;
			}
		}), channelTopic);
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
	public RedisTemplate<String, Object> redisTemplate(
			RedisConnectionFactory connectionFactory) throws Exception {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}
}
